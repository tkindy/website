(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.css :as css]
            [com.tylerkindy.website.paths :refer [data-dir]]
            [hiccup.page :refer [html5]]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.core.match :refer [match]]
            [markdown.core :refer [md-to-html-string]]
            [clj-yaml.core :as yaml]
            [java-time.api :as jt]))

(defn page [metadata content]
  (let [{:keys [title]} metadata]
    (html5 [:head
            [:title title]
            [:link {:rel :stylesheet
                    :href "/css/main.css"}]]
           [:body
            [:div
             [:nav
              [:a {:href "/"} "Tyler Kindy"]
              [:a {:href "/blog"} "Blog"]]
             [:main content]]])))

(defn home []
  (page {:title "Tyler Kindy"} [:p "Here's some content"]))

(defn parse-markdown [markdown]
  (let [lines (str/split-lines markdown)
        [_ _ fm-lines body-lines]
        (reduce
         (fn [[fm-started fm-finished fm-lines body-lines] line]
           (let [line (str/trim line)]
             (match [line fm-started fm-finished]
               ["---" false false] [true false fm-lines body-lines]
               ["---" true  false] [true true fm-lines body-lines]
               [_     false false] [false false fm-lines body-lines]
               [_     true  false] [true false (conj fm-lines line) body-lines]
               [_     true  true]  [true true fm-lines (conj body-lines line)])))
         [false false [] []]
         lines)]
    {:front-matter (->> fm-lines
                        (str/join "\n")
                        str/trim
                        yaml/parse-string)
     :body (->> body-lines
                (str/join "\n")
                str/trim)}))

(comment (parse-markdown (slurp "data/blog/2020-05-25-flexbox-ios.md")))

(defn extract-slug [path]
  (let [name (fs/file-name path)
        [_ slug] (re-matches #"\d{4}-\d{2}-\d{2}-([\w-]+)\.md" name)]
    slug))

(defn build-post [path]
  (let [markdown (slurp (str path))
        {:keys [front-matter body]} (parse-markdown markdown)
        {pub-datetime :pubDatetime, :keys [title]} front-matter]
    {:title title
     :body (list [:h1 title]
                 [:i (jt/format "MMM d, yyyy"
                                (-> pub-datetime
                                    .toInstant
                                    (jt/local-date "UTC")
                                    .atStartOfDay
                                    (.atZone (java.time.ZoneId/of "America/New_York"))))]
                 (md-to-html-string body
                                    :reference-links? true))}))

(comment (build-post "data/blog/2020-05-25-flexbox-ios.md"))

(defn build-posts []
  (->> (fs/list-dir (fs/path data-dir "blog"))
       (map (fn [path]
              [(str (extract-slug path) ".html")
               (fn []
                 (let [{:keys [title body]} (build-post path)]
                   (page {:title (str title " | Tyler Kindy")} body)))]))
       (into {})))

(defn blog [posts]
  (page {:title "Blog | Tyler Kindy"}
        [:div
         [:p "This is my blog"]
         [:ul
          (for [slug (keys posts)]
            [:li [:a {:href (str "/" slug)} slug]])]]))

(defn build-files []
  (let [posts (build-posts)]
    (merge {"index.html" home
            "blog.html" (fn [] (blog posts))
            "css/main.css" css/main}
           posts)))

(comment (((build-files) "flexbox-ios.html")))

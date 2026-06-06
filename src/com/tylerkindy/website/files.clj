(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.css :as css]
            [com.tylerkindy.website.paths :refer [data-dir]]
            [hiccup.page :refer [html5]]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.core.match :refer [match]]
            [markdown.core :refer [md-to-html-string]]
            [clj-yaml.core :as yaml]))

(defn page [& content]
  (html5 [:head
          [:link {:rel :stylesheet
                  :href "/css/main.css"}]]
         [:body
          [:div
           [:nav
            [:a {:href "/"} "Tyler Kindy"]
            [:a {:href "/blog"} "Blog"]]
           [:main content]]]))

(defn home []
  (page [:p "Here's some content"]))

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

(defn extract-slug [path]
  (let [name (fs/file-name path)
        [_ slug] (re-matches #"\d{4}-\d{2}-\d{2}-([\w-]+)\.md" name)]
    slug))

(defn build-post [path]
  (let [markdown (slurp (str path))
        {:keys [front-matter body]} (parse-markdown markdown)]
    {:body (list [:h1 (:title front-matter)]
                 (md-to-html-string body
                                    :reference-links? true))}))

(def posts
  (->> (fs/list-dir (fs/path data-dir "blog"))
       (map (fn [path]
              [(str (extract-slug path) ".html")
               (fn []
                 (page (:body (build-post path))))]))
       (into {})))

(defn blog []
  (page [:div
         [:p "This is my blog"]
         [:ul
          (for [slug (keys posts)]
            [:li [:a {:href (str "/" slug)} slug]])]]))

(def non-posts
  {"index.html" #'home
   "blog.html" #'blog
   "css/main.css" #'css/main})

(def files
  (merge non-posts posts))

(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.css :as css]
            [com.tylerkindy.website.paths :refer [posts-dir]]
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

(defn extract-slug [path]
  (let [name (fs/file-name path)
        [_ slug] (re-matches #"\d{4}-\d{2}-\d{2}-([\w-]+)\.md" name)]
    slug))

(defn read-post [path]
  (let [lines (fs/read-all-lines path)
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
                        str/trim)
     :body (->> body-lines
                (str/join "\n")
                str/trim)}))

(defn read-post-data [path]
  (let [{:keys [front-matter body]} (read-post path)]
    {:metadata (as-> front-matter $
                 (yaml/parse-string $)
                 (assoc $ :slug (extract-slug path))
                 (assoc $ :published (-> (:pubDatetime $)
                                         .toInstant
                                         (jt/local-date "UTC")
                                         .atStartOfDay
                                         (.atZone (java.time.ZoneId/of "America/New_York"))))
                 (dissoc $ :pubDatetime))
     :body (md-to-html-string body
                              :reference-links? true)}))

(defonce cached-post-data (atom nil))

(defn post-data []
  (let [paths (fs/list-dir posts-dir)
        all-data @cached-post-data
        all-data
        (->> paths
             (map (fn [path]
                    (let [last-modified (-> path
                                            fs/last-modified-time
                                            fs/file-time->instant)
                          data (get all-data path)
                          data (if (= last-modified (:last-modified data))
                                 data
                                 (read-post-data path))]
                      [path (assoc data :last-modified last-modified)])))
             (into {}))]
    (reset! cached-post-data all-data)
    all-data))

(comment (post-data))

(defn build-posts []
  (->> (post-data)
       vals
       (map (fn [{:keys [metadata body]}]
              (let [{:keys [slug title published]} metadata]
                [(str slug ".html")
                 (-> metadata
                     (assoc :content
                            (page {:title (str title " | Tyler Kindy")}
                                  (list [:h1 title]
                                        [:i (jt/format "MMM d, yyyy" published)]
                                        body)))
                     (dissoc :body))])))))

(comment (build-posts))

(defn blog []
  (page {:title "Blog | Tyler Kindy"}
        [:div
         [:p "This is my blog"]
         [:ul
          (->> (post-data)
               vals
               (sort-by (comp :published :metadata) #(.isAfter %1 %2))
               (map (fn [{{:keys [slug]} :metadata}]
                      [:li [:a {:href (str "/" slug)} slug]])))]]))

(defn build-files []
  (let [posts (build-posts)]
    (merge {"index.html" home
            "blog.html" blog
            "css/main.css" css/main}
           (->> posts
                (map (fn [[slug data]]
                       [slug (:content data)]))
                (into {})))))

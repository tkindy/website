(ns com.tylerkindy.website.blog
  (:require [babashka.fs :as fs]
            [clj-yaml.core :as yaml]
            [clojure.core.match :refer [match]]
            [clojure.string :as str]
            [com.tylerkindy.website.html :refer [page]]
            [com.tylerkindy.website.paths :refer [posts-dir]]
            [java-time.api :as jt]
            [markdown.core :refer [md-to-html-string]]))

(defn extract-path-metadata [path]
  (let [name (fs/file-name path)
        [_ date rank slug] (re-matches #"(\d{4}-\d{2}-\d{2})([a-z])?-([\w-]+)\.md" name)]
    {:published (jt/local-date date)
     :rank rank
     :slug slug}))

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
    {:metadata (merge (extract-path-metadata path)
                      (yaml/parse-string front-matter))
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
        (list
         [:p "Recent posts"]
         [:ul
          (->> (post-data)
               vals
               (sort-by (fn [{{:keys [published rank]} :metadata}]
                          [published rank])
                        (comp - compare))
               (map (fn [{{:keys [slug title published]} :metadata}]
                      [:li [:a {:href (str "/" slug)} title]
                       " "
                       [:i (jt/format "MMM d, yyyy" published)]])))])))

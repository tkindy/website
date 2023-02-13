(ns com.tylerkindy.website.blog
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [com.tylerkindy.website.output :refer [output page]]
            [com.tylerkindy.website.templates :as t]))

(defn extract-date [file]
  (let [name (.getName file)
        date (re-find #"\d{4}-\d{2}-\d{2}" name)]
    (java.time.LocalDate/parse date)))

(defn parse-post [file]
  (let [date (extract-date file)
        reader (java.io.PushbackReader. (io/reader file))
        attributes (edn/read reader)
        content (-> reader
                    slurp
                    str/trim)]
    (-> attributes
        (assoc :date date)
        (assoc :content content)
        (assoc :excerpt (first (str/split content #"\n\n" 2))))))

(defn read-posts []
  (let [post-files (-> (io/file "_posts")
                       .listFiles)
        post-files (sort-by #(.getName %)
                            (fn [x y] (compare y x))
                            post-files)
        posts (map parse-post post-files)]
    posts))

(defn blog-listing [posts]
  (t/default
   "Blog | Tyler Kindy"
   (list
    [:h2 "Latest Posts"]
    [:ul
     (for [{:keys [excerpt title url]} posts]
       [:li
        [:h3 [:a {:href url} title]]
        excerpt])])))

(defn build-blog-pages []
  (let [posts (read-posts)]
    (output "blog/index.html" (page (blog-listing posts)))))

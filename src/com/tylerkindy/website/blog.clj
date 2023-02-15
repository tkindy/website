(ns com.tylerkindy.website.blog
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [com.tylerkindy.website.output :refer [output page]]
            [com.tylerkindy.website.templates :as t]))

(defn parse-name [file]
  (let [name (.getName file)
        date (re-find #"\d{4}-\d{2}-\d{2}" name)]
    {:date (java.time.LocalDate/parse date)
     :slug (subs name 11 (- (count name) 3))}))

(def date-url-formatter
  (java.time.format.DateTimeFormatter/ofPattern "/YYYY/MM/dd"))
(defn build-url [date slug]
  (str (.format date date-url-formatter)
       "/"
       slug))

(defn parse-post [file]
  (let [{:keys [date slug]} (parse-name file)
        reader (java.io.PushbackReader. (io/reader file))
        attributes (edn/read reader)
        content (-> reader
                    slurp
                    str/trim)]
    (-> attributes
        (assoc :date date)
        (assoc :content content)
        (assoc :excerpt (first (str/split content #"\n\n" 2)))
        (assoc :url (build-url date slug)))))

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
    (output "blog/index.html" (page (blog-listing posts)))
    (doseq [{:keys [url] :as post} posts]
      (output (str (subs url 1) ".html")
              (page (t/post post))))))

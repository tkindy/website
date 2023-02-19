(ns com.tylerkindy.website.blog
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [com.tylerkindy.website.feed :refer [build-feed]]
            [com.tylerkindy.website.mdj :refer [mdj->hiccup]]
            [com.tylerkindy.website.output :refer [output-page]]
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

(defn last-modified [file]
  (-> file
      .lastModified
      java.time.Instant/ofEpochMilli
      (.truncatedTo java.time.temporal.ChronoUnit/SECONDS)))

(defn parse-post [file]
  (let [{:keys [date slug]} (parse-name file)
        reader (java.io.PushbackReader. (io/reader file))
        attributes (edn/read reader)
        content (-> reader
                    slurp
                    str/trim)]
    (-> attributes
        (assoc :date date)
        (assoc :content (mdj->hiccup content))
        (assoc :excerpt (first (str/split content #"\n\n" 2)))
        (assoc :url (build-url date slug))
        (assoc :last-modified (last-modified file)))))

(defn read-posts []
  (let [post-files (-> (io/file "_posts")
                       .listFiles)
        post-files (sort-by #(.getName %)
                            (fn [x y] (compare y x))
                            post-files)
        posts (map parse-post post-files)]
    posts))

(defn blog-listing [config posts]
  (t/default
   config
   "Blog | Tyler Kindy"
   (list
    [:h2 "Latest Posts"]
    [:ul
     (for [{:keys [excerpt title url]} posts]
       [:li
        [:h3 [:a {:href url} title]]
        excerpt])])))

(defn build-blog-pages [config]
  (let [posts (read-posts)]
    (output-page "blog/index.html" (blog-listing config posts))
    (doseq [{:keys [url] :as post} posts]
      (output-page (str (subs url 1) ".html")
                   (t/post config post)))
    (build-feed posts)))

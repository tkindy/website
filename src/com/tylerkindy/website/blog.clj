(ns com.tylerkindy.website.blog
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn extract-date [file]
  (let [name (.getName file)
        date (re-find #"\d{4}-\d{2}-\d{2}" name)]
    (java.time.LocalDate/parse date)))

(defn parse-post [file]
  (let [date (extract-date file)
        reader (java.io.PushbackReader. (io/reader file))
        attributes (edn/read reader)]
    (-> attributes
        (assoc :date date)
        (assoc :content (-> reader
                            slurp
                            str/trim)))))

(defn read-posts []
  (let [post-files (-> (io/file "_posts")
                       .listFiles)
        posts (map parse-post post-files)]
    posts))

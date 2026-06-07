(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.blog :refer [blog build-posts]]
            [com.tylerkindy.website.css :as css]
            [com.tylerkindy.website.html :refer [page]]))

(defn home []
  (page {:title "Tyler Kindy"} [:p "Here's some content"]))

(defn build-files []
  (merge {"index.html" home
          "blog.html" blog
          "css/main.css" css/main}
         (->> (build-posts)
              (map (fn [[slug data]]
                     [slug (:content data)]))
              (into {}))))

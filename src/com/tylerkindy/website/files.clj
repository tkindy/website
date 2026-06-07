(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.blog :refer [blog build-posts]]
            [com.tylerkindy.website.css :as css]))

(defn build-files []
  (merge {"index.html" blog
          "css/main.css" css/main}
         (->> (build-posts)
              (map (fn [[slug data]]
                     [slug (:content data)]))
              (into {}))))

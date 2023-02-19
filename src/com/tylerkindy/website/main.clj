(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [com.tylerkindy.website.blog :refer [build-blog-pages]]
            [com.tylerkindy.website.home :refer [build-home-page]]
            [com.tylerkindy.website.output :refer [output-dir]]))

(defn clean []
  (b/delete {:path output-dir}))

(defn build [config]
  (clean)
  (.mkdir (java.io.File. output-dir))
  (build-home-page)
  (build-blog-pages)
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

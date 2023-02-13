(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [com.tylerkindy.website.home :refer [build-home-page]]
            [com.tylerkindy.website.output :refer [output-dir]]))

(defn clean []
  (b/delete {:path output-dir}))

(defn build []
  (clean)
  (.mkdir (java.io.File. output-dir))
  (build-home-page)
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

(defn -main []
  (build))

(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [com.tylerkindy.website.home :refer [home-page]]))

(def output-dir "output")

(defn clean []
  (b/delete {:path output-dir}))

(defn build []
  (clean)
  (.mkdir (java.io.File. output-dir))
  (spit (str output-dir "/index.html") (home-page))
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

(defn -main []
  (build))

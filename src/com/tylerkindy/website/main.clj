(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [hiccup.page :refer [html5]]))

(def output-dir "output")

(defn clean []
  (b/delete {:path output-dir}))

(defn home []
  [:body
   [:h1 "Tyler Kindy"]])

(defn build []
  (clean)
  (.mkdir (java.io.File. output-dir))
  (spit (str output-dir "/index.html")
        (html5 {} (home)))
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

(defn -main []
  (build))

(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [hiccup.page :refer [html5]]))

(def output-dir "output")

(defn clean []
  (b/delete {:path output-dir}))

(defn home []
  [:body [:h1 "Hello, World!"]])

(defn build []
  (clean)
  (.mkdir (java.io.File. output-dir))
  (spit (str output-dir "/index.html")
        (html5 {} (home))))

(defn -main []
  (build))

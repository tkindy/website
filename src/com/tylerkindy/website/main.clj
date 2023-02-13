(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [hiccup.page :refer [html5]]))

(def output-dir "output")

(defn clean []
  (b/delete {:path output-dir}))

(defn home []
  (list
   [:head
    [:link {:rel :stylesheet, :href "/css/main.css"}]]
   [:body
    [:h1 "Tyler Kindy"]
    [:img {:src "/images/tyler.jpg"}]]))

(defn build []
  (clean)
  (.mkdir (java.io.File. output-dir))
  (spit (str output-dir "/index.html")
        (html5 {} (home)))
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

(defn -main []
  (build))

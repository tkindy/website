(ns com.tylerkindy.website.main
  (:require [babashka.fs :as fs]
            [com.tylerkindy.website.paths :refer [out-dir]]
            [hiccup.page :refer [html5]]))

(defn -main []
  (fs/create-dirs out-dir)
  (->> (fs/glob out-dir "*")
       (map fs/delete-tree)
       doall)
  (spit (fs/file (fs/path out-dir "index.html"))
        (html5 [:body
                [:h1 "Tyler Kindy"]])))

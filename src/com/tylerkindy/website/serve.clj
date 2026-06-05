(ns com.tylerkindy.website.serve
  (:require [babashka.fs :as fs]
            [clojure.string :as str]
            [com.tylerkindy.website.files :refer [files]]
            [com.tylerkindy.website.paths :refer [assets-dir]]
            [org.httpkit.server :refer [run-server server-port server-join]]))

(def ext-content-types
  {"html" "text/html"
   "css" "text/css"})

(defn pick-content-type [path]
  (let [[_ extension] (re-find #"\.(\w+)$" path)]
    (get ext-content-types extension "text/plain")))

(defn app [{:keys [uri]}]
  (println "Looking up" uri)
  (let [file-path (if (str/ends-with? uri "/")
                    (str uri "index.html")
                    uri)
        file-path (str/replace file-path #"^/" "")
        file (if (str/starts-with? file-path "assets/")
               (fs/file assets-dir (str/replace file-path #"^assets/" ""))
               (files file-path))]
    (if file
      {:status 200
       :headers {"Content-Type" (pick-content-type file-path)}
       :body (if (fn? file)
               (file)
               file)}
      {:status 404})))

(defn start-server []
  (let [server (run-server app {:legacy-return-value? false})]
    (println "Listening on port" (server-port server))
    server))

(comment (start-server))

(defn serve [_]
  (server-join (start-server)))

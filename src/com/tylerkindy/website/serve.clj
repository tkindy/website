(ns com.tylerkindy.website.serve
  (:require [babashka.fs :as fs]
            [clojure.string :as str]
            [com.tylerkindy.website.files :refer [files]]
            [com.tylerkindy.website.paths :refer [assets-dir]]
            [org.httpkit.server :refer [run-server server-port server-join]]))

(def ext-content-types
  {"html" "text/html"
   "css" "text/css"})

(defn extract-extension [path]
  (let [[_ extension] (re-find #"\.(\w+)$" path)]
    extension))

(defn pick-content-type [path]
  (get ext-content-types (extract-extension path) "text/plain"))

(defn app [{:keys [uri]}]
  (let [file-path (cond
                    (str/ends-with? uri "/") (str uri "index.html")
                    (nil? (extract-extension uri)) (str uri ".html")
                    :else uri)
        file-path (str/replace file-path #"^/" "")
        file (if (str/starts-with? file-path "assets/")
               (fs/file assets-dir (str/replace file-path #"^assets/" ""))
               (let [file (files file-path)]
                 (if (var? file)
                   (file)
                   file)))]
    (if file
      {:status 200
       :headers {"Content-Type" (pick-content-type file-path)}
       :body (if (fn? file)
               (file)
               file)}
      {:status 404})))

(defn start-server []
  (let [server (run-server #'app {:legacy-return-value? false})]
    (println "Listening on port" (server-port server))
    server))

(comment (start-server))

(defn serve [_]
  (server-join (start-server)))

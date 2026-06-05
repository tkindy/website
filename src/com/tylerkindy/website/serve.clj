(ns com.tylerkindy.website.serve
  (:require [org.httpkit.server :refer [run-server server-port server-join]]))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello, HTTP!"})

(defn serve [_]
  (let [server (run-server app {:legacy-return-value? false})]
    (println "Listening on port" (server-port server))
    (server-join server)))

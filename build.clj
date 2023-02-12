(ns build
  (:require [clojure.tools.build.api :as b]))

(def output-dir "output")

(defn clean [_]
  (b/delete {:path output-dir}))

(defn build [_]
  (clean nil)
  (b/copy-file {:src "test.html"
                :target (str output-dir "/index.html")}))

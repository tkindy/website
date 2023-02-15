(ns com.tylerkindy.website.output
  (:require [hiccup.page :refer [html5]])
  (:import [java.nio.file Path]))

(def output-dir "output")
(def output-path (Path/of output-dir (make-array String 0)))

(defn page [contents]
  (html5 {:lang :en} contents))

(defn output [path contents]
  (let [path (.resolve output-path path)]
    (-> path
        .getParent
        .toFile
        .mkdirs)
    (spit (.toFile path) contents)))

(defn output-page [path contents]
  (output path (page contents)))

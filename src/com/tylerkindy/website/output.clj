(ns com.tylerkindy.website.output
  (:require [hiccup.page :refer [html5]]))

(def output-dir "output")

(defn page [contents]
  (html5 {:lang :en} contents))

(defn output [path contents]
  (spit (str output-dir "/" path)
        contents))

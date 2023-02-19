(ns com.tylerkindy.website.mdj
  (:require [commonmark-hiccup.core :refer [markdown->hiccup]]))

(defn mdj->hiccup [mdj]
  (markdown->hiccup mdj))

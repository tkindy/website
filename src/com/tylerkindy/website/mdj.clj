(ns com.tylerkindy.website.mdj
  (:require [commonmark-hiccup.core :refer [markdown->hiccup]]))

(defn parse-markdown [markdown]
  (markdown->hiccup markdown))

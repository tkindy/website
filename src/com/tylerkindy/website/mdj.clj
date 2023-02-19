(ns com.tylerkindy.website.mdj
  (:require [clojure.string :as str]
            [commonmark-hiccup.core :refer [markdown->hiccup]]))

(defn mdj->markdown [mdj]
  (str/replace mdj
               #"%\(.*?\)"
               #(-> %1
                    (subs 1)
                    read-string
                    eval
                    str)))

(defn mdj->hiccup [mdj]
  (-> mdj
      mdj->markdown
      markdown->hiccup))

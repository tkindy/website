(ns com.tylerkindy.website.mdj
  (:require [clojure.string :as str]
            [commonmark-hiccup.core :refer [markdown->hiccup]]
            [hiccup.core :refer [html]]))

(defn image [{:keys [file alt class caption]}]
  (html
   [:figure.centered
    [:img {:src (str "/images/" file)
           :alt alt
           :class class}]
    [:figcaption caption]]))

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

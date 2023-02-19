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
               (fn [expr]
                 (let [expr (-> expr
                                (subs 1)
                                read-string)]
                   (-> (binding [*ns* (find-ns 'com.tylerkindy.website.mdj)]
                         (eval expr))
                       str)))))

(defn mdj->hiccup [mdj]
  (-> mdj
      mdj->markdown
      markdown->hiccup))

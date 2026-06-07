(ns com.tylerkindy.website.html
  (:require [hiccup.page :refer [html5]]))

(defn page [metadata content]
  (let [{:keys [title]} metadata]
    (html5 [:head
            [:title title]
            [:link {:rel :stylesheet
                    :href "/css/main.css"}]]
           [:body
            [:div
             [:nav
              [:a {:href "/"} "Tyler Kindy"]
              [:a {:href "/blog"} "Blog"]]
             [:main content]]])))

(ns com.tylerkindy.website.home
  (:require [hiccup.page :refer [html5]]))

(defn home []
  (list
   [:head
    [:link {:rel :stylesheet, :href "/css/main.css"}]]
   [:body
    [:h1 "Tyler Kindy"]
    [:img {:src "/images/tyler.jpg"}]]))

(defn home-page []
  (html5 {} (home)))

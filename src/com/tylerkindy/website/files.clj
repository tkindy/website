(ns com.tylerkindy.website.files
  (:require [com.tylerkindy.website.css :as css]
            [hiccup.page :refer [html5]]))

(defn page [content]
  (html5 [:head
          [:link {:rel :stylesheet
                  :href "/css/main.css"}]]
         [:body
          [:div
           [:nav
            [:a {:href "/"} "Tyler Kindy"]
            [:a {:href "/blog"} "Blog"]]
           [:main content]]]))

(defn home []
  (page [:p "Here's some content"]))

(defn blog []
  (page [:p "This is my blog"]))

(def files
  {"index.html" #'home
   "blog.html" #'blog
   "css/main.css" #'css/main})

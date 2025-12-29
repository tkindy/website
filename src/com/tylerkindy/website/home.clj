(ns com.tylerkindy.website.home
  (:require [com.tylerkindy.website.output :refer [output-page]]
            [com.tylerkindy.website.templates :refer [default]]))

(defn home [config]
  (default
   config
   "/"
   "Tyler Kindy"
   [:div.home-container
    [:img.me-img {:src "/assets/images/tyler.jpg", :alt "Tyler"}]
    [:div.me-description
     [:h2 "Hi! I'm Tyler."]
     [:p
      "I'm a software engineer at "
      [:a {:href "https://ambrook.com/"} "Ambrook"]
      "."]
     [:p
      "My primary work experience is in backend web development. Through side projects,
       I also have experience with frontend web development, primarily with React."]]]))

(defn build-home-page [config]
  (output-page "index.html" (home config)))

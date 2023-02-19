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
      "I'm a Boston-based software engineer working at "
      [:a {:href "https://www.hubspot.com/"} "HubSpot"]
      "."]
     [:p "I have experience in back-end and Android development."]]]))

(defn build-home-page [config]
  (output-page "index.html" (home config)))

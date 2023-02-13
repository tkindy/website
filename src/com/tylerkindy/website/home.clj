(ns com.tylerkindy.website.home
  (:require [com.tylerkindy.website.templates :refer [default]]
            [hiccup.page :refer [html5]]))

(defn home []
  (default
   [:div.home-container
    [:img.me-img {:src "/images/tyler.jpg", :alt "Tyler"}]
    [:div.me-description
     [:h2 "Hi! I'm Tyler."]
     [:p
      "I'm a Boston-based software engineer working at "
      [:a {:href "https://www.hubspot.com/"} "HubSpot"]
      "."]
     [:p "I have experience in back-end and Android development."]]]))

(defn home-page []
  (html5 {} (home)))

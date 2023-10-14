(ns com.tylerkindy.website.home
  (:require [com.tylerkindy.website.output :refer [output-page]]
            [com.tylerkindy.website.templates :refer [default]]))

(defn home [config]
  (default
   config
   "/"
   "Tyler Kindy"
   [:div.home-container
    [:picture
     [:source {:srcset "/assets/images/tyler.webp", :type "image/webp"}]
     [:img.me-img {:src "/assets/images/tyler.jpg", :alt "Tyler"}]]
    [:div.me-description
     [:h2 "Hi! I'm Tyler."]
     [:p
      "I'm a Boston-based software engineer working at "
      [:a {:href "https://www.hubspot.com/"} "HubSpot"]
      "."]
     [:p
      "My primary work experience is in backend web development. Through side projects,
       I also have experience with frontend web development, primarily with React."]]]))

(defn build-home-page [config]
  (output-page "index.html" (home config)))

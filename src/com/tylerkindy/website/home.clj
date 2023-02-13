(ns com.tylerkindy.website.home
  (:require [hiccup.page :refer [html5]]))

(def nav-items [{:name "Home", :link "/"}
                {:name "Blog", :link "/blog"}])
(def social-links [{:name "GitHub"
                    :image "github.png"
                    :link "https://github.com/tkindy"}
                   {:name "Twitter"
                    :image "twitter.svg"
                    :link "https://twitter.com/TylerKindy"}
                   {:name "Mastodon"
                    :image "mastodon.svg"
                    :link "https://mastodon.social/@TylerKindy"
                    :rel :me}])

(defn nav []
  [:nav
   [:h1.name "Tyler Kindy"]
   [:div.pages
    (for [{:keys [link name]} nav-items]
      [:a {:href link} name])]
   [:div.social-links
    (for [{:keys [rel link image name]} social-links]
      [:a {:rel rel, :href link}
       [:img {:src (str "/images/" image)
              :alt name}]])]])

(defn template [content]
  (list
   [:head
    [:meta {:charset :utf-8}]
    [:meta {:name :viewport, :content "width=device-width, initial-scale=1"}]
    [:link {:rel :stylesheet, :href "/css/main.css"}]
    [:link {:rel :icon, :href "/favicon.ico", :type "image/x-icon"}]]
   [:body
    [:div.container
     (nav)
     content]]))

(defn home []
  (template
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

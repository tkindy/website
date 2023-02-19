(ns com.tylerkindy.website.templates
  (:require [clojure.java.io :as io]
            [com.tylerkindy.website.feed :refer [feed-path]]
            [com.tylerkindy.website.url :refer [absolute-url]])
  (:import [java.time.format DateTimeFormatter FormatStyle]
           [java.util Locale]))

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
       [:img {:src (str "/assets/images/" image)
              :alt name}]])]])

(def gtag (slurp (io/resource "gtag.html")))

(defn default [{:keys [env]} path title content]
  (let [url (absolute-url path)]
    (list
     [:head
      [:title title]
      (when (= env :production) gtag)
      [:meta {:charset :utf-8}]
      [:meta {:name :viewport, :content "width=device-width, initial-scale=1"}]
      [:link {:rel :stylesheet, :href "/assets/css/main.css"}]
      [:link {:rel :icon, :href "/favicon.ico", :type "image/x-icon"}]
      [:link {:rel :alternate, :href (str "/" feed-path), :type "application/atom+xml"}]
      [:meta {:property :og:title, :content title}]
      [:meta {:property :og:locale, :content :en_US}]
      [:link {:rel :canonical, :href url}]
      [:meta {:property :og:url, :content url}]
      [:meta {:property :og:site_name, :content "Tyler Kindy"}]]
     [:body
      [:div.container
       (nav)
       content]])))

(def date-formatter (-> (DateTimeFormatter/ofLocalizedDate FormatStyle/MEDIUM)
                        (.withLocale Locale/US)))
(defn format-date [date]
  (.format date date-formatter))

(defn post [config url {:keys [title date content]}]
  (default
   config
   url
   (str title " | Tyler Kindy")
   (list
    [:h1 title]
    [:p [:i (format-date date)]]
    content)))

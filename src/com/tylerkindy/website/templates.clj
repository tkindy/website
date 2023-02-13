(ns com.tylerkindy.website.templates
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
       [:img {:src (str "/images/" image)
              :alt name}]])]])

(defn default [content]
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

(def date-formatter (-> (DateTimeFormatter/ofLocalizedDate FormatStyle/MEDIUM)
                        (.withLocale Locale/US)))
(defn format-date [date]
  (.format date date-formatter))

(defn post [{:keys [title date content]}]
  (default
   (list
    [:h1 title]
    [:p [:i (format-date date)]]
    content)))

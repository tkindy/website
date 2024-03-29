(ns com.tylerkindy.website.feed
  (:require [clojure.data.xml :as xml]
            [com.tylerkindy.website.output :refer [output]]
            [com.tylerkindy.website.url :refer [absolute-url]]
            [hiccup.core :refer [html]]))

(def feed-path "feed.xml")

(defn entry [{:keys [url title date last-modified content excerpt]}]
  (let [url (absolute-url url)]
    [:entry
     [:title {:type "html"} title]
     [:link {:href url :rel "alternate" :type "text/html" :title title}]
     [:published (.atStartOfDay date java.time.ZoneOffset/UTC)]
     [:updated last-modified]
     [:id url]
     [:content {:type "html" :xml:base url}
      (html content)]
     [:author
      [:name "Tyler Kindy"]]
     [:summary {:type "html"} excerpt]]))

(defn feed [posts]
  (xml/sexp-as-element
   [:feed {:xmlns "http://www.w3.org/2005/Atom"}
    [:link {:href (absolute-url (str "/" feed-path))
            :rel "self"
            :type "application/atom+xml"}]
    [:link {:href (absolute-url "/")
            :rel "alternate"
            :type "text/html"}]
    [:updated (-> (java.time.Instant/now)
                  (.truncatedTo java.time.temporal.ChronoUnit/SECONDS))]
    [:id (absolute-url (str "/" feed-path))]
    [:title {:type "html"} "Tyler Kindy"]

    (map entry posts)]))

(defn build-feed [posts]
  (output feed-path
          (xml/emit-str (feed posts))))

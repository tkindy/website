(ns com.tylerkindy.website.feed
  (:require [clojure.data.xml :as xml]
            [com.tylerkindy.website.output :refer [output]]
            [com.tylerkindy.website.url :refer [absolute-url]]))

(def feed-path "feed.xml")

(defn feed [posts]
  (xml/sexp-as-element
   [:feed {:xmlns "http://www.w3.org/2005/Atom"}
    [:generator {:uri "https://github.com/tkindy/website"}
     "Tyler Kindy's static site generator"]
    [:link {:href (absolute-url (str "/" feed-path))
            :rel "self"
            :type "application/atom+xml"}]
    [:link {:href (absolute-url "/")
            :rel "alternate"
            :type "text/html"}]
    [:updated (-> (java.time.Instant/now)
                  (.truncatedTo java.time.temporal.ChronoUnit/SECONDS))]
    [:id (absolute-url (str "/" feed-path))]
    [:title {:type "html"} "Tyler Kindy"]]))

(defn build-feed [posts]
  (output feed-path
          (xml/emit-str (feed posts))))

(ns com.tylerkindy.website.feed
  (:require [clojure.data.xml :as xml]
            [com.tylerkindy.website.output :refer [output]]))

(defn feed [posts]
  (xml/sexp-as-element
   [:feed {:xmlns "http://www.w3.org/2005/Atom"}]))

(defn build-feed [posts]
  (output "feed.xml"
          (xml/emit-str (feed posts))))

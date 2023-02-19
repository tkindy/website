(ns com.tylerkindy.website.sitemap
  (:require [clojure.data.xml :as xml]
            [com.tylerkindy.website.output :refer [output]]
            [com.tylerkindy.website.url :refer [absolute-url]]))

(defn url-elem [{:keys [url last-modified]}]
  [:url
   [:loc (absolute-url url)]
   [:lastmod (.truncatedTo last-modified
                           java.time.temporal.ChronoUnit/SECONDS)]])

(defn sitemap [pages]
  (xml/sexp-as-element
   [:urlset {:xmlns:xsi "http://www.w3.org/2001/XMLSchema-instance"
             :xsi:schemaLocation "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"
             :xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}
    (map url-elem pages)]))

(defn build-sitemap [pages]
  (output "sitemap.xml"
          (xml/emit-str (sitemap pages))))

(ns com.tylerkindy.website.url
  (:import [java.net URI]))

(def base-url "https://tylerkindy.com/")

(defn absolute-url [url]
  (-> base-url
      URI/create
      (.resolve url)
      str))

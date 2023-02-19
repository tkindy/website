(ns com.tylerkindy.website.main
  (:require [clojure.tools.build.api :as b]
            [com.tylerkindy.website.blog :refer [build-blog-pages]]
            [com.tylerkindy.website.home :refer [build-home-page]]
            [com.tylerkindy.website.output :refer [output-dir]]
            [com.tylerkindy.website.sitemap :refer [build-sitemap]]))

(defn clean []
  (b/delete {:path output-dir}))

(defn build [config]
  (clean)
  (.mkdir (java.io.File. output-dir))
  (build-home-page config)

  (let [blog-pages (build-blog-pages config)]
    (build-sitemap
     (concat [{:url "/", :last-modified (java.time.Instant/now)}
              {:url "/2022games"
               :last-modified (java.time.Instant/parse "2022-01-30T04:26:10Z")}]
             blog-pages)))
  (b/copy-dir {:src-dirs ["public"]
               :target-dir output-dir}))

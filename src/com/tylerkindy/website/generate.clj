(ns com.tylerkindy.website.generate
  (:require [babashka.fs :as fs]
            [com.tylerkindy.website.css :as css]
            [com.tylerkindy.website.paths :refer [assets-dir out-dir]]
            [hiccup.page :refer [html5]]))

(defn page [content]
  (html5 [:head
          [:link {:rel :stylesheet
                  :href "/css/main.css"}]]
         [:body
          [:div
           [:nav
            [:a {:href "/"} "Tyler Kindy"]
            [:a {:href "/blog"} "Blog"]]
           [:main content]]]))

(defn home []
  (page [:p "Here's some content"]))

(defn generate [_]
  (fs/create-dirs out-dir)
  (->> (fs/glob out-dir "*")
       (map fs/delete-tree)
       doall)
  (let [files {"index.html" (home)
               "css/main.css" (css/main)}]
    (doall (for [[path content] files]
             (do
               (some->> path
                        fs/parent
                        (fs/path out-dir)
                        fs/create-dirs)
               (spit (fs/file (fs/path out-dir path))
                     content)))))
  (fs/copy-tree assets-dir (fs/path out-dir "assets")))

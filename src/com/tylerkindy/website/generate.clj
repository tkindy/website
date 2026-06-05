(ns com.tylerkindy.website.generate
  (:require [babashka.fs :as fs]
            [com.tylerkindy.website.files :refer [files]]
            [com.tylerkindy.website.paths :refer [assets-dir out-dir]]))

(defn generate [_]
  (fs/create-dirs out-dir)
  (->> (fs/list-dir out-dir)
       (map fs/delete-tree)
       doall)
  (doall (for [[path content] files]
           (do
             (some->> path
                      fs/parent
                      (fs/path out-dir)
                      fs/create-dirs)
             (spit (fs/file (fs/path out-dir path))
                   (content)))))
  (fs/copy-tree assets-dir (fs/path out-dir "assets")))

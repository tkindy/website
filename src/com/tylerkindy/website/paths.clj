(ns com.tylerkindy.website.paths
  (:require [clojure.java.io :as io]
            [babashka.fs :as fs]))

(defn- find-root [path]
  (loop [path path]
    (if (nil? path)
      nil
      (let [git-dir (fs/path path ".git")]
        (if (fs/directory? git-dir)
          path
          (recur (fs/parent path)))))))

(def repo-root (find-root (or (some-> *file*
                                      io/resource
                                      .getPath)
                              *file*)))
(def assets-dir (fs/path repo-root "assets"))
(def data-dir (fs/path repo-root "data"))
(def out-dir (fs/path repo-root "out"))

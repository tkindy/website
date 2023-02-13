(ns com.tylerkindy.website.output)

(def output-dir "output")

(defn output [path contents]
  (spit (str output-dir "/" path)
        contents))

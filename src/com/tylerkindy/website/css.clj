(ns com.tylerkindy.website.css
  (:require [garden.core :refer [css]]
            [garden.stylesheet :refer [at-font-face]]))

(defn main []
  (css (at-font-face {:font-family "Fira Code"
                      :src "url(\"/assets/fonts/fira-code.ttf\") format(\"truetype\")"})
       [:body {:font-family "Fira Code"}]))

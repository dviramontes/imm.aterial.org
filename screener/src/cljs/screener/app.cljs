(ns screener.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]))

(enable-console-print!) ;; enable print at web inspector console)

(defn some-component []
  [:div.row
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "#FF306D"}} " and red"]
    " text."]])

(defn calling-component []
  [:div.container "Parent component"
   [some-component]])

(defn init []
  (reagent/render-component [calling-component]
                            (.getElementById js/document "mount")))

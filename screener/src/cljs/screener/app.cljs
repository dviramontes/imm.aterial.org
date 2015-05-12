(ns screener.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]))

(enable-console-print!) ;; enable print at web inspector console)

(defn question [text subtext type id]
  [:div.row
   [:div.col-lg-12
      [:h3 text]
      [:input.form-control {:field type :id id}]
      [:p.lead "subtext"]]])

(defn parent-component []
  [:div.container 
    [:div.row
      [:div.col-lg-12
        [:p.lead 
          [:b "> "]
          "Please tell us about your project."]]]
    [question 
      "Is this a re-design or a new project?"
    :text :one]
    [question 
      "What are the goals for this project?"
    :text :one]])

(defn init []
  (reagent/render-component [parent-component]
                            (.getElementById js/document "mount")))

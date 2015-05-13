(ns screener.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [cljsjs.moment :as moment]
            ;;[cljsjs.jquery-daterange-picker :as jquery-date-picker]
            ))

(enable-console-print!)

; (defn datepicker [title id]
;    [:div.row
;     [:div.col-lg-12
;      [:h3 title]
;      [:div [:input {:field :datepicker
;             :id id
;             :date-format "yyyy/mm/dd"
;             :inline true}]]]])

  ;;[:input#set-date {:type "text" :placeholder  "mm/dd/yyyy" :size 11 :value today}]

(defn date-picker [title subtext id]

  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
           [:div.row  
    [:div.col-lg-12
     [:h3 title]
     [:div [:label subtext
              [:input {:field :datepicker
            :id id
            :date-format "yyyy/mm/dd"
            :inline true}]]]]]))
            
(defn number [title subtext id]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:label [:em.subtext subtext]
     [:input.form-control {:field :numeric :id id}]]]])

(defn text [title subtext id]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:label [:em.subtext subtext]
     [:input.form-control {:field :text :id id}]]]])

(defn radio [title subtext id values]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:div.btn-group {:field :single-select id :unique.position}
     (for [opt (seq values)]
       [:button.btn.btn-default {:key opt :type "button"} (name opt)])]]])

(defn lista [title subtext id options]
  (print options)
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:div.form-group
     [:label subtext]
     [:select.form-control {:field :list :id :many.options}
      (for [opt options]
            [:option {:key opt} (name opt)])]]]])

(defn question [title subtext type id & options]
  (case type
    :radio (radio title subtext id options)
    :list  (lista title subtext id options)
    :text  (text  title subtext id)
    :numeric (number title subtext id)
    ;;:datepicker (datepicker title id)
    ))

(defn parent-component []
  [:div.container 
   [:div.row
    [:div.col-lg-12
     [:p.lead 
      [:b "> "]
      "Tell us about your project"]]]

   [:form.form

    ;;[date-picker]
    
    [question 
     "Is this a re-design or a new project?" 
     nil
     :radio :project-type :new-project :re-design]

    [question 
     "What are the goals for this project?" 
     nil
     :text :two]

    [question 
     "Where do you see us adding the most value/complementing your existing team?"
     nil
     :text :three]

    [question 
     "Who will be working on this project from your end?"
     "Will any additional outside partners or agencies be involved and how?"
     :text :four]
    
    [question 
     "What is motivating you or enabling you to do this project now?"
     nil
     :text :five]

    [question 
     "When does our work need to be finished?"
     "What is your target total completion date? What is driving that?"
     :text :six]

    ;; [question 
    ;;  "What is most important about this project?"
    ;;  nil
    ;;  :text :seven]

    [question
     "How did you hear about us ?"
     nil
     :list 
     :eight 
     :referral :online :other]
    
    [question
     "Who (is, are) your (audience, your target users, your customers)?"
     nil
     :text :nine]

    [question
     "What does success look like?"
     "How will you know this project has succeeded?"
     :text :ten]

    [question
     "What are you worried about?"
     "What do you imagine going wrong? Don't worry, you are in good company"
     :text :eleven]

    [question
     "What is your budget range?"
     "$$ amount , ballpark figure is fine"
     :numeric :twelve]

    [question
     "What does the selection process look like on your end?"
     "How many people are you talking to and when do you expect to be making a decision?"
     :text :thirteen]

    [:br]
  
    [:p.text-center [:button.btn.btn-default.btn-lg {:type "submit" :style 
                                     {:padding "1em 3em 1em 3em"
                                      :margin "2em auto"
                                      :background-color "#FF306D"
                                      :color "white"
                                      }} "Get in Touch"]]
    ]])

(defn init []
  (reagent/render-component [parent-component]
                            (.getElementById js/document "mount")))

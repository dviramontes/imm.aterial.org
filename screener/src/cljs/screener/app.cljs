(ns screener.app
  (:require [reagent.core :as r :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [cljsjs.moment :as moment]
            [ajax.core :refer [POST]]))

(enable-console-print!)

(defn deserialize [j] (.stringify js/JSON j))

(defn serialize [j] (.parse js/JSON j))

(defn handler [res]
  (print (str res)))

(defn error-handler [{:keys [status status-text]}]
  (print (str "oops: " status " " status-text)))

(defn submit-fn [e]
  (POST "http://localhost:4000/email-form"
       {:handler handler
        :params {:data "foo bar baz" }
        :error-handler error-handler})
  (print "clicked"))

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

(defn email [title subtext id]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:label [:em.subtext subtext]
     [:input.form-control {:field :email :id id :placeholder "me@mail.com"}]]]])

(defn selection-box [title subtext id values]
  (print values)
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:div.btn-group {:field :single-select id :unique.position}
     (for [opt values]
       [:button.btn.btn-default {:key opt :type "button"} (name opt)])]]])

(defn lista [title subtext id options]
  [:div.row
   [:div.col-lg-4.col-md-4
    [:h3 title]
    [:div.form-group
     [:label subtext]
     [:select.form-control {:field :list :id id}
      (for [opt options]
        [:option {:key opt} (name opt)])]]]])

(defn question [title subtext type id & options]
  (case type
    :selection-box (selection-box title subtext id options)
    :list  (lista title subtext id options)
    :text  (text  title subtext id)
    :numeric (number title subtext id)
    :email   (email title subtext id)
    ;;:datepicker (datepicker title id)
    ))

(def form
  [:form.form

    ;;[date-picker]

    ; (question
    ;  "Is this a re-design or a new project?"
    ;  nil
    ;  :selection-box
    ;  :project-type :new-project
    ;  :re-design)

    (question
     "What are the goals for this project?"
     nil
     :text :two)

    (question
     "Where do you see us adding the most value/complementing your existing team?"
     nil
     :text :three)

    (question
     "Who will be working on this project from your end?"
     "Will any additional outside partners or agencies be involved and how?"
     :text :four)

    (question
     "What is motivating you or enabling you to do this project now?"
     nil
     :text :five)

    (question
     "When does our work need to be finished?"
     "What is your target total completion date? What is driving that?"
     :text :six)

    ; (question
    ;  "What is most important about this project?"
    ;  nil
    ;  :text :seven)

    (question
     "How did you hear about us ?"
     nil
     :list
     :eight
     :referral :online :other)

    (question
     "Who (is, are) your (audience, your target users, your customers)?"
     nil
     :text :nine)

    (question
     "What does success look like?"
     "How will you know this project has succeeded?"
     :text :ten)

    (question
     "What are you worried about?"
     "What do you imagine going wrong? Don't worry, you're in good company"
     :text :eleven)

    (question
     "What is your budget range?"
     "$$ amount , ballpark figure is fine"
     :numeric :twelve)

    (question
     "What does the selection process look like on your end?"
     "How many people are you talking to and when do you expect
     to be making a decision?"
     :text :thirteen)

    (question "Lastly, Where can we contact you?" nil :email :email)

    [:br]

    [:p.text-center [:button.btn.btn-default.btn-lg
                     {:type "button"
                      :on-click submit-fn
                      :style
                      {:padding "1em 3em 1em 3em"
                       :margin "2em auto"
                       :background-color "#FF306D"
                       :color "white"
                       }} "Get in Touch"]]])

(defn parent-component []
  (let [doc (atom {})]
    [:div.container
     [:div.row
      [:div.col-lg-12
       [:br]
       [:p.lead "Tell us about your project"]]
      [:div.col-lg-6.col-md-12.col-sm.12
       [:div.container
          [bind-fields form doc (fn [id value doc;;{:keys [email] :as doc}
            ]
             (print doc))]]]]]))

(defn init []
  (r/render-component [parent-component]
                            (.getElementById js/document "mount")))

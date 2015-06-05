(ns screener.app
  (:require [reagent.core :as r :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [cljsjs.moment :as moment]
            ;; [cljsjs.jquery :as $]
            ;; [json-html.core :refer [edn->hiccup]]
            [ajax.core :refer [POST]]))

(enable-console-print!)

(defn deserialize [j] (.stringify js/JSON j))

(defn serialize [j] (.parse js/JSON j))

(defn date-picker [title subtext id]
  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
    [:div.row
    [:div.col-lg-12
     [:h3 title]
     [:div [:label subtext
            [:input {:field :datepicker :id id :date-format "yyyy/mm/dd" :inline true}]]]]]))

(defn number [title subtext id opts]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:label [:em.help-block subtext]
     [:input.form-control {:field :numeric :id id :placeholder opts}]]]])

(defn text [title subtext id & opts]
  [:div.row
   [:div.col-lg-8
    [:h3 title]
    [:label [:em.help-block subtext]]
    [:textarea.form-control {:rows 3 :field :text :id id :placeholder opts}]]])

(defn email [title subtext id & options]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    [:label [:em.help-block.help-block subtext]
     [:input.form-control {:field :email  :type "email" :id id :placeholder "me@mail.com" :required true}]]]])

(defn selection-box [title subtext id & values]
  [:div.row
   [:div.col-lg-12
    [:h3 title]
    (print values)
    [:div.btn-group {:field :single-select :id id}
     (for [opt values]
          ^{:key opt} [:button.btn.btn-default {:key opt :type "button"} "test"])]]])

(defn lista [title subtext id & options]
  [:div.row
   [:div.col-lg-4.col-md-4
    [:h3 title]
    [:div.form-group
     [:label subtext]
     [:select.form-control {:field :list :id id}
      (for [opt options]
        [:option {:key opt} (name opt)])]]]])

(def form
  [:form.form

    ;; [date-picker "hi" nil :someday]

    ;(selection-box
    ;"Is this a re-design or a new project?"
    ;nil
    ;:project-type
    ;:new-project :re-design)

    (text
     "What are the goals for this project?"
     nil
     :goals)

    (text
     "Where do you see us adding the most value/complementing your existing team?"
     nil
     :value)

    (text
     "Who will be working on this project from your end?"
     "Will any additional outside partners or agencies be involved and how?"
     :collaborators)

    (text
     "What is motivating you or enabling you to do this project now?"
     nil
     :motive)

    (text
     "When does our work need to be finished?"
     "What is your target total completion date? What is driving that?"
     :deadline
     "Q1, Q3, 2015, 2016")

    ; (text
    ;  "What is most important about this project?"
    ;  nil
    ;  :text :imperative)

    (lista
     "How did you hear about us ?"
     nil
     :referal
     :referral :online :other)

    (text
     "Who (is, are) your (audience, your target users, your customers)?"
     nil
     :audience)

    (text
     "What does success look like?"
     "How will you know this project has succeeded?"
     :success)

    (text
     "What are you worried about?"
     "What do you imagine going wrong? Don't worry, you are in good company."
     :worries-concerns)

    (number
     "What is your budget range?"
     "ballpark figure is fine"
     :budget
     "$")

    (text
     "What does the selection process look like on your end?"
     "How many people are you talking to and when do you expect
     to be making a decision?"
     :selection-process)

    (email
      "Lastly, how do we get in touch with you?"
      nil
      :email)])

;(defn editor [doc & body]
;  [:div.row
;   body
;   [:div.col-md-7
;    [:h2 "Document State"]
;    [edn->hiccup doc]]])
;; <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
;; <h4 class="modal-title" id="mySmallModalLabel">Small modal<a class="anchorjs-link" href="#mySmallModalLabel"><span class="anchorjs-icon"></span></a></h4>

(defn modal []
  [:div.modal.fade.sent-email-modal
   {:tab-index "-1" :role "dialog" :aria-labelledby "dialog" :aria-hidden "true"}
   [:div.modal-dialog.modal-sm
    [:div.modal-content
     [:div.modal-header
      [:button.close {:type "button" :data-dismiss "modal" :aria-label "Close"}
       [:span {:aria-hidden true} "x"]]
      [:h4.modal-title "Thank you, we'll be in touch shortly."]]
     ]]])

(defn parent-component []
  (let [doc (atom {})

        success-handler (fn [res]

          (print (str res)))

        error-handler (fn [{:keys [status status-text]}]
          (print (str "oops: " status " " status-text)))

        submit-fn (fn []
          (POST "http://localhost:4000/email-form"
                {:handler success-handler
                 :params {:data @doc}
                 :error-handler error-handler})
          (print "clicked"))]

    [:div.container
     (modal)
     [:div.row
      [:div.col-lg-12
       [:br]
       [:p.lead {:style {:font-size 30}}
        [:b "> "]
        "Tell us about your project"]]
      [:div.col-lg-6.col-md-12.col-sm.12
       [:div.container
        [bind-fields form doc (fn [id value _doc]
                                (reset! doc _doc)
                                (print (str id "::" value))
                                #_(swap! assoc id doc))]
        [:p.text-center [:button.btn.btn-default.btn-lg
                         {:type "button"
                          :data-toggle "modal"
                          :data-target ".sent-email-modal"
                          :on-click submit-fn
                          :style
                          {:padding "1em 3em 1em 3em"
                           :margin "2em auto"
                           :background-color "#FF306D"
                           :color "white"}} "Get in Touch"]]]]]]))


(defn init []
  (r/render-component [parent-component]
                            (.getElementById js/document "mount")))
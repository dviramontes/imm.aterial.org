(ns mailer.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            ;; [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [postal.core :refer [send-message]]
            [environ.core :refer [env]])) ;; .lein-env

(def email (env :email))
(def pass  (env :email-password))

(def conn {:host "smtp.gmail.com"
           :ssl true
           :user email
           :pass pass})


(defn send-email [body]
  (send-message conn {:from email
                      :to email
                      :subject "Message from imm.aterial.org/screener"
                      :body body}));; -> {:error :SUCCESS, :code 0, :message "messages sent"}

(defroutes app-routes
  (POST "/email-form" []
        (fn [req]
          (send-email (slurp (:body req)))
         "Email Sent!"))
  (route/not-found "Not Found"))

(def app
  ;;(wrap-defaults app-routes site-defaults)
  (wrap-cors app-routes #".*"))

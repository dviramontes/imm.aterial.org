(defproject mailer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [com.draines/postal "1.11.3"]
                 [environ "1.0.0"]]

  :plugins [[lein-ring "0.8.13"]]

  :ring {:handler mailer.handler/app
    :auto-reload? true
    :auto-refresh true}

  :profiles
    {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [ring-refresh "0.1.1"]]}})

(set-env!
 :source-paths    #{"src/cljs" "less"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "0.0-2814-4" :scope "test"]
                 [adzerk/boot-cljs-repl "0.1.9"      :scope "test"]
                 [adzerk/boot-reload    "0.2.4"      :scope "test"]
                 [pandeiro/boot-http    "0.6.1"      :scope "test"]
                 [reagent "0.5.0"]
                 [reagent-forms "0.5.1"]
                 [cljsjs/moment "2.9.0-0"]
                 [cljs-ajax "0.3.11"]
                 [json-html "0.2.8"]
                 ;; [cljsjs/jquery "1.9.1-0"]
                 ;; [cljsjs/jquery-daterange-picker "0.0.5-2"]
                 [deraen/boot-less "0.2.1" :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[deraen.boot-less    :refer [less]])

(deftask build []
  (comp (speak)
        (cljs)
        (less)
        (sift   :move {#"less.css" "css/less.css" #"less.main.css.map" "css/less.main.css.map"})))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced
                       ;; pseudo-names true is currently required
                       ;; https://github.com/martinklepsch/pseudo-names-error
                       ;; hopefully fixed soon
                       :pseudo-names true}
                      less   {:compression true})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none
                       :unified-mode true
                       :source-map true}
                 reload {:on-jsload 'screener.app/init}
                      less   {:source-map  true})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

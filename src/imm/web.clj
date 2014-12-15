(ns imm.web
  (:require
  	[imm.highlight :refer [highlight-code-blocks]]
  	[clojure.java.io :as io]
  	[hiccup.page :refer [html5]]
  	[stasis.core :as stasis]
  	[clojure.string :as str]
  	[me.raynes.cegdown :as md]))

(defn layout-page [page]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initiadl-scale=1.0"}]
    [:title "Tech blog"]
    [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"}]
    [:link {:rel "stylesheet" :href "/styles/main.css"}]
    [:link {:rel "stylesheet" :href "/styles/zenburn.css"}]]
   [:body
    [:div.logo "imm.aterial"]
    [:div.body page]]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout-page (md/to-html page pegdown-options)))

(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (map render-markdown-page (vals pages))))

(defn partial-pages [pages]
	(zipmap (keys pages)
		(map layout-page (vals pages))))

(defn prepare-pages [pages]
	(zipmap (keys pages) (map #(highlight-code-blocks %) (vals pages))))

(defn get-raw-pages []
  (stasis/merge-page-sources
   {:public
    	(stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
    :partials
	    (partial-pages (stasis/slurp-directory "resources/partials" #".*\.html$"))
    :markdown
    	(markdown-pages (stasis/slurp-directory "resources/md" #"\.md$"))}))

(defn get-pages []
	(prepare-pages (get-raw-pages)))

(def app (stasis/serve-pages get-pages))
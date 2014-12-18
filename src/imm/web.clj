(ns imm.web
  (:require [optimus.assets :as assets]
	     	[optimus.export] 		
            [optimus.link :as link]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [imm.highlight :refer [highlight-code-blocks]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.page :refer [html5]]
            [me.raynes.cegdown :as md]
            [stasis.core :as stasis]))

(defn layout-page [request page]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title "Tech blog"]
    [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"}]
    ; [:link {:rel "stylesheet" :href "/styles/main.css"}]]
	[:link {:rel "stylesheet" :href (link/file-path request "/styles/main.css")}]]    
   [:body
    [:div.logo "imm.aterial"]
    [:div.body page]]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout-page (md/to-html page pegdown-options)))

(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (map #(fn [req] (layout-page req (md/to-html % pegdown-options)))
               (vals pages))))

(defn prepare-page [page req]
  (-> (if (string? page) page (page req))
      highlight-code-blocks))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn partial-pages [pages]
	(zipmap (keys pages)
		(map #(fn [req] (layout-page req %)) (vals pages))))

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

(defn get-assets []
	(assets/load-assets "public" [#".*"]))

(def app
	(optimus/wrap (stasis/serve-pages get-pages)
		get-assets
		optimizations/all;; Minify JavaScript
						 ;; Minify CSS
						 ;; Inline CSS imports
						 ;; Concatenate bundles
						 ;; Add cache-bust expires headers (replace URL references with generated unique ones)
						 ;; Add last-modified headers


		;; serve-frozen-assets in prod
		serve-live-assets))

(def export-dir "dist")

(defn export []
  (let [assets (optimizations/all (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))


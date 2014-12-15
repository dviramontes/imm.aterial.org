(ns imm.web
  (:require [stasis.core :as stasis]))

(defn get-pages []
	(stasis/slurp-directory "resouces/public" #".*\.(html|css|js)$"))

(def app (stasis/serve-pages get-pages))
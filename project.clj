(defproject imm.aterial "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
				 [stasis "2.2.2"]
				 [ring "1.3.2"]
				 [hiccup "1.0.5"]
				 [me.raynes/cegdown "0.1.1"]
				 [enlive "1.1.5"]
				 [clygments "0.1.1"]]

  :ring {:handler imm.web/app}
  :profiles {:dev {:plugins [[lein-ring "0.8.13"]]}})

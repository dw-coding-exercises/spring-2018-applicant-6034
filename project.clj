(defproject my-exercise "0.1.0-SNAPSHOT"
  :description "An anonymous Democracy Works coding exercise"
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [clj-http "3.9.0"]
                 [com.taoensso/timbre "4.10.0"]]
  :plugins [[lein-ring "0.12.1"]]
  :profiles {:dev  [:project/dev  :profiles/dev]
             :test [:project/test :profiles/test]
             :profiles/dev  {}
             :profiles/test {:dependencies []}
             :project/dev   {:dependencies [[orchestra "2017.11.12-1"]
                                            [expectations "2.2.0-rc3"]]
                             :ring {:nrepl {:start? true
                                            :port 7000}}}
             :project/test  {}}
  :ring {:handler my-exercise.core/handler}
  :aliases {"submit" ["run" "-m" "my-exercise.submit"]})

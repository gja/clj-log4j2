(defproject clj-log4j2 "0.2.0"
  :description "Easily add log4j2 into your clojure project"
  :url "http://github.com/gja/clj-log4j2"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :profiles {:dev {:dependencies [[org.apache.logging.log4j/log4j-core "2.6.2"]
                                  [org.apache.logging.log4j/log4j-api "2.6.2"]]}})

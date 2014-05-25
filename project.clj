(defproject gun-moll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/pomegranate "0.2.0"]
                 [http-kit "2.1.14"]
                 [compojure "1.1.6"]
                 [cheshire "5.2.0"]
                 [clj-time "0.6.0"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [com.cemerick/url "0.1.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [slingshot "0.10.3"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [org.clojure/data.xml "0.0.7"]
                 [hiccup "1.0.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.16"]
                 [log4j/apache-log4j-extras "1.1"]
                 [org.slf4j/slf4j-api "1.6.2"]
                 [org.slf4j/slf4j-log4j12 "1.6.2"]
                 [tentacles "0.2.5"]
                 [inflections "0.8.2"]]
  :min-lein-version "2.0.0"
  :main gun-moll.init)

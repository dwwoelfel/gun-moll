(ns gun-moll.server
  (:require [clj-time.core :as time]
            [clj-time.format :as format-time]
            [clj-time.coerce :refer (from-long)]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.data.xml :as xml]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [compojure.handler :refer (site)]
            [compojure.core :refer (POST GET defroutes)]
            [inflections.core :refer (pluralize)]
            [org.httpkit.server :refer (run-server)]
            [slingshot.slingshot :refer (try+)]
            [tentacles.issues :as gh-issues]
            [tentacles.repos :as gh-repo]))

(defn validate-login [login]
  (assert (not (string/blank? login)))
  (assert (re-matches #"[\d\w]+" login)))

(defn get-threads [login]
  (-> (http/get (format "http://hn.algolia.com/api/v1/search_by_date?tags=author_%s,(comment)" login))
      :body
      (cheshire.core/decode true)
      :hits))

(defn thread->rss [thread]
  (let [link (format "https://news.ycombinator.com/item?id=%s" (:objectID thread))]
    [:item
     [:title (format "HN comment on %s" (:story_title thread))]
     [:link link]
     [:description (string/replace (:comment_text thread) "\\n" "\n")]
     [:author (:author thread)]
     [:pubDate (->> thread
                    :created_at
                    format-time/parse
                    (format-time/unparse (format-time/formatters :rfc822)))]
     [:guid {:isPermaLink "true"} link]]))

(defn generate-rss [login]
  (let [threads (get-threads login)]
    (-> [:rss {:version "2.0"
               :xmlns:dc "http://purl.org/dc/elements/1.1/"
               :xmlns:sy "http://purl.org/rss/1.0/modules/syndication/"}
         [:channel
          [:title (format "%s's comments on Hacker News" login)]
          [:link (format "http://gun-moll.herokuapp.com/threads?id=%s" login)]
          [:description (format "Hacker News comments by %s" login)]
          (for [thread threads]
            (thread->rss thread))]]
        xml/sexp-as-element
        xml/emit-str)))

(defroutes all-routes
  (GET "/" [] (fn [req] {:status 200 :body "<html><head><title>Gun Moll -- RSS Feeds for your Favorite Hacker News Commenters</title></head><body><p>Gun Moll gives you an RSS feed of Hacker News comments for your favorite HN commenters.</p> <p>Point your rss reader at http://gun-moll.herokuapp.com/threads?id=HN-USERNAME</p> <p>For example, to get an rss feed for patio11: <a href='http://gun-moll.herokuapp.com/threads?id=patio11'>http://gun-moll.herokuapp.com/threads?id=patio11</a></p><p>If you want to run your own sever, the <a href='https://github.com/dwwoelfel/gun-moll'>code is on GitHub</a>. Just fork it, change the app name in the circle.yml, and follow your fork on CircleCI</p></body></html>"}))
  (GET "/ping" [] (fn [req] {:status 200 :body "pong"}))
  (GET "/threads" [id] [] (fn [req]
                            (log/infof "Fetching threads for %s" id)
                            (validate-login id)
                            {:status 200 :body (generate-rss id)
                             :headers {"Content-Type" "application/xml"}})))

(defn port []
  (cond (System/getenv "HTTP_PORT") (Integer/parseInt (System/getenv "HTTP_PORT"))
        :else 8090))

(defn start []
  (def server (run-server (site #'all-routes) {:port (port)})))

(defn stop []
  (server))

(defn restart []
  (stop)
  (start))

(defn init []
  (start))

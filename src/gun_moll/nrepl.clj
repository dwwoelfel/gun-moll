(ns gun-moll.nrepl
  (:require [clojure.tools.nrepl.server :refer (start-server stop-server default-handler)]))

(defn nrepl-port []
  (when (System/getenv "NREPL_PORT") (Integer/parseInt (System/getenv "NREPL_PORT"))))

(defn init []
  (when (nrepl-port)
    (defonce server (start-server :port (nrepl-port)))))

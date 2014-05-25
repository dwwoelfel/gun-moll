(ns gun-moll.init
  (:require gun-moll.server
            gun-moll.logging
            gun-moll.nrepl))

(def init-fns [#'gun-moll.logging/init
               #'gun-moll.nrepl/init
               #'gun-moll.server/init])

(defn pretty-now []
  (.toLocaleString (java.util.Date.)))

(defn init []
  (doseq [f init-fns]
    (println (pretty-now) f)
    (f)))

(defn -main []
  (init)
  (println (pretty-now) "done"))

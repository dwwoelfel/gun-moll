(ns gun-moll.logging
  (:import org.apache.commons.logging.LogFactory
           (org.apache.log4j Logger
                             BasicConfigurator
                             EnhancedPatternLayout
                             Level
                             ConsoleAppender
                             FileAppender
                             SimpleLayout)
           org.apache.log4j.spi.RootLogger
           (org.apache.log4j.rolling TimeBasedRollingPolicy
                                     RollingFileAppender)))

(def layout (EnhancedPatternLayout. "%d{MMM d HH:mm:ssZ} %p %c %m%n"))

(defn init []
  (let [rolling-policy (doto (TimeBasedRollingPolicy.)
                         (.setActiveFileName  "hook-handler.log" )
                         (.setFileNamePattern "hook-handler-%d{yyyy-MM-dd}.log.gz")
                         (.activateOptions))
        log-appender (doto (RollingFileAppender.)
                       (.setRollingPolicy rolling-policy)
                       (.setLayout layout)
                       (.activateOptions))
        root-logger (Logger/getRootLogger)]

    (.removeAllAppenders root-logger)
    (.addAppender root-logger log-appender)
    (.addAppender root-logger (ConsoleAppender. layout))
    (.setLevel root-logger Level/INFO)))

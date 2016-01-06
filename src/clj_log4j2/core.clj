(ns clj-log4j2.core
  (:import [org.apache.logging.log4j LogManager Logger Level]
           [org.apache.logging.log4j.message Message]))

(set! *warn-on-reflection* true)

(defn log [^Logger logger ^Level level {:keys [^Message clj-message
                                               ^Throwable throwable
                                               message
                                               params]}]
  (if clj-message
    (if throwable
      (.log logger level clj-message throwable)
      (.log logger level clj-message))
    (if throwable
      (.log logger level ^Object message throwable)
      (if params
        (.log logger level ^String message ^objects (into-array Object params))
        (.log logger level ^Object message)))))

(defprotocol LogObject
  (log-object [this] "Convert to string"))

(extend-protocol LogObject
  nil
  (log-object [this] "nil")

  String
  (log-object [this] this)

  Object
  (log-object [this] (.toString this)))

(defn message [message throwable]
  (reify Message
    (getFormat [this]
      "")
    (getFormattedMessage [this]
      (log-object message))
    (getParameters [this]
      (into-array Object []))
    (getThrowable [this]
      throwable)))

(defn convert-to-log-args [args]
  (let [parse-other-args (fn [throwable args]
                           (if (= 1 (count args))
                             {:clj-message (message (first args) throwable)}
                             {:message (first args)
                              :params (rest args)}))]
    (if-let [throwable (when (instance? Throwable (first args))
                         (first args))]
      (assoc (parse-other-args throwable (rest args))
             :throwable throwable)
      (parse-other-args nil args))))

(defmacro log-at-level
  ([level args]
   `(log-at-level ~(str *ns*) ~level ~args))
  ([ns level [message & more :as args]]
   `(let [logger# (LogManager/getLogger ~ns)]
      (if (.isEnabled logger# ~level)
        (log logger# ~level
             ;; common case to optimize for, avoid expensive reflection
             ~(if (instance? String message)
                `{:message ~message
                  :params ~(when (seq more)
                             (into [] more))}
                `(convert-to-log-args [~@args])))))))

(defmacro debug [& args]
  `(log-at-level Level/DEBUG [~@args]))

(defmacro error [& args]
  `(log-at-level Level/ERROR [~@args]))

(defmacro fatal [& args]
  `(log-at-level Level/FATAL [~@args]))

(defmacro info [& args]
  `(log-at-level Level/INFO [~@args]))

(defmacro off [& args]
  `(log-at-level Level/OFF [~@args]))

(defmacro trace [& args]
  `(log-at-level Level/TRACE [~@args]))

(defmacro warn [& args]
  `(log-at-level Level/WARN [~@args]))

(defmacro spy
  ([exp]
   `(spy ~exp Level/INFO))
  ([exp level]
   `(let [result# ~exp]
      (log-at-level ~level [result#])
      result#)))

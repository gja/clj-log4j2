(ns clj-log4j2.core-test
  (:require [clojure.test :refer :all]
            [clj-log4j2.core :refer :all]))

(deftest message-of-a-string-formats-as-the-string
  (is
   (-> "foobar"
       (message nil)
       (.getFormattedMessage)
       (= "foobar"))))

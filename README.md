# clj-log4j2

A Clojure library designed to let you add log4j2 into your project. It's partly inspired by the clojure.tools/logging library, but it allows you to customize how different types get logged (for example, allowing you to automatically convert maps to json)

## Usage

The following short example should give you what you need to get started:
```clojure
(ns example
  (:require [clj-log4j2.core :as log]))

(defn hello-world []
  (log/info "Hello, {}!" "World"))

(extend-protocol log/LogObject clojure.lang.IPersistentMap
  (log-object [m]
    (cheshire.core/encode m)))

(defn hello-json []
  (log/info {:hello "world!"}))

(defn error-logging []
  (log/error (new Exception "foobar") "Something Crashed"))

(defn spy-on-object []
  (-> {}
      (log/spy)
      (assoc :foo :bar)))
```


## Installation

Logging is available in Maven central. Add this to your leiningen project.clj:

[clj-log4j2 "0.1.1"]

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

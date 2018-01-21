(ns muggle.ext.media
  "Protocols for reading and writing different data formats"
  (:refer-clojure :exclude [read]))

;; TODO: Consider channels over streams?

(defprotocol MediaReader
  (read [reader uri offset len]
    "Return an InputStream to read data from given URI, at offset")
  (size [reader uri]
    "Return a long containing the size of the object at given URI"))

(defprotocol MediaWriter
  (write [writer uri]
    "Open and return an OutputStream to write at given location"))

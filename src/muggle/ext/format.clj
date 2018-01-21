(ns muggle.ext.format
  "Protocols for working with data at rest"
  (:refer-clojure :exclude [transduce]))

(defprotocol FormatReader
  (transduce [fmt xform f in]
    "Apply the transducer to output reducing function f with data from
    InputStream, after decoding. Must respect transducer application contract.")
  (splits [fmt reader uri]
    "Return a sequence of serializable objects (e.g. maps, records),
    representing splits in the data at given URI. These will later be passed
    the open function, most likely with a new reader instance,
    to extract data."))

(defprotocol FormatWriter
  (reducer [fmt out]
    "Return a reducing function, with all *three* arity overloads, for encoding
    records according to the represented format and writing to the output stream.
    The finalize arity call is expected to close the output stream. The init
    arity can be used to prepare buffers, etc."))

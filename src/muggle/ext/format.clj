(ns muggle.ext.format
  "Protocols for working with data at rest"
  (:refer-clojure :exclude [transduce]))

(defprotocol Shard
  "The unit of data processed by a single Task. A Shard implementation
  is required to a means to apply a transducer to its data."
  (transduce
    [shard xform f]
    [shard xform f init]
    "Apply the transducer to output reducing function f with data from
    given URI, after decoding. Must respect transducer application contract."))

(defprotocol FormatReader
  (shards [fmt uri]
    "Return a sequence of serializable Shard implementations for all of the
    data located at the given UR."))

(defprotocol FormatWriter
  (reducer [fmt uri]
    "Return a reducing function, with all *three* arity overloads, for encoding
    records according to the represented format and writing to the URI.
    The initializer arity is expected to open and resources needed for writing,
    and the finalize (single arg) arity is expected to close and cleanup."))

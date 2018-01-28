(ns muggle.impl.format.text
  "Prototype format implementation for text files, meant for
  testing abstractions only for now."
  (:require [clojure.java.io :as io]
            [muggle.ext
             [format :as fmt]
             [media :as med]])
  (:refer-clojure :exclude [transduce]))

(defrecord TextShard
    [media-reader uri]

  fmt/Shard
  (transduce [this xf f]
    (fmt/transduce this xf f (f)))
  (transduce [_ xf f init]
    (let [size (med/size media-reader uri)]
      (with-open [lines (io/reader (med/read media-reader uri 0 size))]
        (clojure.core/transduce xf f init (line-seq lines))))))

(defn text-reader
  "Format reader implementation for text data."
  [media-reader]
  (reify fmt/FormatReader
    (shards [_ uri] [(->TextShard media-reader uri)])))

(defn text-writer
  "Encoder for text data"
  [media-writer]
  (reify fmt/FormatWriter
    (reducer [_ uri]
      (fn
        ([] (->> uri (med/write media-writer) io/writer))
        ([wr]
         (doto wr .close))
        ([wr x]
         (doto wr (.write (str x \newline))))))))

(ns muggle.impl.format.text
  (:require [clojure.java.io :as io]
            [muggle.ext.format  :as fmt]))

(defn text-reader
  "Extractor for text data"
  []
  (reify fmt/FormatReader
    (splits [_ rdr uri] uri)
    (transduce [_ xf f in]
      (with-open [lines (io/reader in)]
        (clojure.core/transduce xf f (line-seq lines))))))

(defn text-writer
  "Encoder for text data"
  []
  (reify fmt/FormatWriter
    (reducer [_ out]
      (fn
        ([] (io/writer out))
        ([wr]
         (doto wr .close))
        ([wr x]
         (doto wr (.write (str x \newline))))))))

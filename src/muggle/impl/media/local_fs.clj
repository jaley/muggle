(ns muggle.impl.media.local-fs
  (:require
   [clojure.java.io :as io]
   [muggle.ext.media :as med]))

(defn local-fs-reader
  "Media reader implementation for local FS"
  []
  (reify med/MediaReader
    (read [_ uri offset len]
      ;; TODO respect offset and len
      (-> uri io/input-stream))
    (size [_ uri]
      (-> uri io/file .getLength))))

(defn local-fs-writer
  "Media writer for local files"
  []
  (reify med/MediaWriter
    (write [_ uri]
      (io/output-stream uri))))

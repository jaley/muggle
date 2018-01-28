(ns muggle.impl.media.local-fs
  "Local filesystem implementation of media protocols.

  Note that this is not useful in real applications, because
  media implementations should work from any machine, and this
  can only work from a single machine. It's intended for testing
  only."
  (:require
   [clojure.java.io :as io]
   [muggle.ext.media :as med]))

(defn local-fs-reader
  "Media reader implementation for local FS"
  []
  (reify med/MediaReader
    (read [_ uri offset len]
      ;; TODO encode offset and len in URI and respect them?
      (-> uri io/input-stream))
    (size [_ uri]
      (-> uri io/file .length))))

(defn local-fs-writer
  "Media writer for local files"
  []
  (reify med/MediaWriter
    (write [_ uri]
      (io/output-stream uri))))

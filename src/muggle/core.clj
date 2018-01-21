(ns muggle.core
  (:require [muggle.ext
             [catalog :as clg]
             [media   :as med]
             [format  :as fmt]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn local-fs-catalog
  "Prototype local filesystem catalog."
  [file-ext]
  (reify clg/Catalog
    (listing [_ uri]
      (let [f (io/file uri)
            match? (fn [f]
                     (and (.isFile f)
                          (.endsWith (.getName f) file-ext)))]
        (cond
          (match? f)       (.getAbsolutePath f)
          (.isDirectory f) (->> (.listFiles f)
                                (filter match?)
                                (map (memfn getAbsolutePath))))))))

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

(let [local-cat (local-fs-catalog ".txt")
      fs-rdr  (local-fs-reader)
      fs-wri  (local-fs-writer)
      txt-rdr (text-reader)
      txt-wri (text-writer)
      files   (clg/listing local-cat "data/")
      in      (med/read fs-rdr (first files) 0 nil)
      out     (med/write fs-wri "data/out.txt")
      wf      (fmt/reducer txt-wri out)
      pipe    (comp
               (remove (memfn isEmpty))
               (mapcat (fn [s](str/split s #" ")))
               (distinct))]
  (->> in
       (fmt/transduce txt-rdr pipe wf)))

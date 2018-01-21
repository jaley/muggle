(ns muggle.impl.catalog.local-fs
  (:require [clojure.java.io :as io]
            [muggle.ext.catalog :as clg]))

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

(ns muggle.core
  (:require  [clojure.string :as str]))

(comment
  ;; TODO: needs impl exposed via api
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
         (fmt/transduce txt-rdr pipe wf))))

(ns muggle.core
  (:require  [clojure.string :as str]
             [muggle.api.core :as m]))

(comment
  (let [txt-r (m/text-reader (m/local-fs-reader))
        txt-w (m/text-writer (m/local-fs-writer))]
    (->> "data/in/ulyses.txt"
         (m/load txt-r)
         (m/eduction (comp
                      (remove (memfn isEmpty))
                      (mapcat (fn [s] (str/split s #" ")))
                      (distinct)))
         (m/save txt-w "data/out/words.txt")
         (m/run-local!)))

  )

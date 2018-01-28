(ns muggle.api.core
  "Prototype API"
  (:require [muggle.impl
             [plan :as plan]
             [task :as task]]
            [muggle.impl.media.local-fs :as fs]
            [muggle.impl.format.text :as txt]
            [muggle.ext
             [format :as fmt]
             [media :as med]])
  (:refer-clojure :exclude [load eduction]))

;; TODO: Expose readers/writers some better way

(def local-fs-reader fs/local-fs-reader)
(def local-fs-writer fs/local-fs-writer)

(def text-reader txt/text-reader)
(def text-writer txt/text-writer)

;; TODO: Consider arity overload for configurable catalog
(defn load
  "Construct a step with given input URIs as data."
  [format & uris]
  (plan/->TransductionStep
   (mapcat (partial fmt/shards format) uris)))

(defn eduction
  "Like clojure.core/eduction, but for steps. Used to attach
  an xform (transducer) to a step, which will be applied in the
  reduction for that step."
  {:arglists '([xform* step])}
  [& args]
  (let [[xforms step] [(butlast args) (last args)]]
    (reduce plan/with-xform step xforms)))

(defn save
  "Reduction to write data to an output URI using the provided
  format writer. Transducers will be applied during reduction."
  [format uri step]
  (-> step
      (plan/with-reducer (fmt/reducer format uri))))

;; TODO: Remove, it won't work this way.
(defn run-local!
  "Hack to test plan execution locally."
  [step]
  (doseq [task (plan/tasks step)]
    (task/run task)))

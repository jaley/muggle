(ns muggle.impl.task
  "Abstraction for units of work running on worker machines."
  (:require [muggle.ext
             [format :as fmt]
             [media  :as med]])
  (:refer-clojure :exclude [format]))

(defprotocol Task
  "A single unit of work to be carried out by a worker machine"
  ;; TODO: Require abstraction for results
  (run [task] "Execute the task and return results."))

(defrecord TransduceTask
    [shard xform reducer]
  Task
  (run [t]
    (fmt/transduce shard xform reducer)))

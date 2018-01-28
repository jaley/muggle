(ns muggle.impl.plan
  "Functions for defining a job plan incrementally through
  API calls."
  (:require [muggle.impl.task :as task]))

(defprotocol Step
  "A phase in the plan representing a set of tasks that can be run in
  parallel, independently."
  (with-xform [step xform]
    "Attach a transducer to this step, for application on reduction.")
  (with-reducer [step f]
    "Attach the reducing function for this step.")
  (tasks [step]
    "Compile a set of tasks to execute this step in parallel."))

;; A simple Step implementation to apply a transducer.
(defrecord TransductionStep
    [shards]
  Step
  (with-xform [s xform]
    (if (:xform s)
      (update s :xform comp xform)
      (assoc s :xform xform)))
  (with-reducer [s f]
    (assoc s :reducer f))
  (tasks [s]
    (assert (:reducer s) "Step definition incomplete.")
    (map (fn [shard]
           (task/->TransduceTask shard (:xform s) (:reducer s)))
         shards)))

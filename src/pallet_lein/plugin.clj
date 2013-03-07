(ns pallet-lein.plugin
  "Plugin to hook get-classpath, and ensure vbox xpcom and ws libs are
exclusive"
  (:require
   [clojure.pprint :refer [pprint]]
   [robert.hooke :refer [add-hook]]
   [leiningen.core.classpath :refer [resolve-dependencies]]
   [leiningen.core.main :refer [debug]]))

(defn xpcom? [[artifact _]]
  (= "vboxjxpcom" (name artifact)))

(defn ws? [[artifact _]]
  (= "vboxjws" (name artifact)))

(defn clojure? [[artifact _]]
  (and (= "org.clojure" (namespace artifact))
       (= "clojure" (name artifact))))

(def displace? #'leiningen.core.project/displace?)
(def replace? #'leiningen.core.project/replace?)

(defn- remove-xpcom-ws-conflicts
  "Remove conflicts between virtualbox xpcom and ws libs.
Assumes that these libs are only every present as direct dependencies."
  [dependencies-key project]
  (if (= dependencies-key :dependencies)
    (do
      (debug "Dependencies before cleanup" \newline
             (with-out-str (pprint (get project dependencies-key))))
      (let [remove-dep (fn [dep s] (vec (remove #{dep} s)))
            p (update-in
               project [dependencies-key]
               (fn check-dependencies [deps]
                 (->
                  (reduce
                   (fn check-conflict [[deps seen] dependency]
                     (if (or (xpcom? dependency) (ws? dependency))
                       (if seen
                         (cond
                          (displace? seen)
                          (do
                            (debug seen "displaced by " dependency)
                            [(conj (remove-dep seen deps) dependency) dependency])

                          (replace? dependency)
                          (do
                            (debug "Replacing" seen "with" dependency)
                            [(conj (remove-dep seen deps) dependency) dependency])

                          :else (do
                                  (debug "Ignoring" dependency "as" seen "seen")
                                  [deps seen]))
                         [(conj deps dependency) dependency])
                       [(conj deps dependency) nil]))
                   [[] nil]
                   deps)
                  first)))]
        (debug "Dependencies after cleanup" \newline
               (with-out-str (pprint (get p dependencies-key))))
        p))
    project))

(defn- ensure-minimal-clojure-version
  "Assure that a minimum version of clojure is in the dependencies"
  [dependencies-key project]
  (if (= dependencies-key :dependencies)
    (let [p (update-in
             project [dependencies-key]
             #(->>
               %
               (map
                (fn [[artifact version :as dependency]]
                  (if (clojure? dependency)
                    (let [[_ major minor]
                          (re-matches #"([0-9]+)\.([0-9]+).*" version)]
                      (if (and (= "1" major) (< (Integer/parseInt minor) 4))
                        [artifact "1.4.0"]
                        dependency))
                    dependency)))
               vec))]
      (debug "Dependencies after minimal version check" \newline
             (with-out-str (pprint (get p dependencies-key))))
      p)
    project))

(defn remove-xpcom-ws-conflicts-wrapper
  [f dependencies-key project & rest]
  (apply f dependencies-key (remove-xpcom-ws-conflicts dependencies-key project) rest))

(defn ensure-minimal-clojure-version-wrapper
  [f dependencies-key project & rest]
  (apply f dependencies-key
         (ensure-minimal-clojure-version dependencies-key project)
         rest))

(defn hooks []
  (add-hook #'resolve-dependencies remove-xpcom-ws-conflicts-wrapper)
  (add-hook #'resolve-dependencies ensure-minimal-clojure-version-wrapper))

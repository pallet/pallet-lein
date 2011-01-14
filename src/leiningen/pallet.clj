(ns leiningen.pallet
  "Pallet command line."
  (:require leiningen.compile))

(defn pallet
  "Launch pallet tasks from the command line.

   For a list of tasks
     lein pallet help"
  ([project & args]
     (if (and project (map? project))
       (leiningen.compile/eval-in-project
        project
        `(do
           (try
             (require 'pallet.main)
             (catch java.io.FileNotFoundException e#
               (println "Error loading pallet: " (.getMessage e#))
               (println "You need to have pallet as a project dependency")
               (println "or installed in ~/.lein/plugins")
               (System/exit 1)))
           (if-let [m# (ns-resolve 'pallet.main (symbol "-main"))]
             (m# ~@args "-project-options" ~(pr-str project))
             (do
               (println "failed to resolve " 'pallet.main (symbol "-main"))
               (System/exit 1)))))
       (try
         (require 'pallet.main)
         ((ns-resolve (the-ns 'pallet.main) 'pallet-task) args)
         (catch java.io.FileNotFoundException e#
           (println "Error loading pallet: " (.getMessage e#))
           (println "You need to install pallet and it's dependencies in")
           (println "~/.lein/plugins in order to use the lein-pallet plugin")
           (println "outside of a project.")
           1))))
  ([arg] (pallet nil arg))
  ([] (pallet nil)))

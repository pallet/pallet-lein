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

           (if-let [rv# (try
                          (require '~'pallet.main)
                          (catch java.io.FileNotFoundException e#
                            (binding [*out* *err*]
                              (println "Error loading pallet: " (.getMessage e#))
                              (println "You need to have pallet as a project")
                              (println "dependency or installed in ~/.lein/plugins"))
                            1))]
             rv#
             (if-let [m# (ns-resolve
                          (the-ns '~'pallet.main)
                          '~'pallet-task)]
               (let [env# (pallet-lein.configleaf/get-environment '~project)]
                 (m# (concat ["-project-options" ~(pr-str project)] [~@args])
                     :environment env#))
               (do
                 (binding [*out* *err*]
                   (println "failed to resolve pallet.main/pallet-task"))
                 1))))
        nil
        nil
        '(require 'pallet-lein.configleaf)
        )
       (try
         (require 'pallet.main)
         ((ns-resolve (the-ns 'pallet.main) 'pallet-task) args)
         (catch java.io.FileNotFoundException e
           (println "Error loading pallet: " (.getMessage e))
           (println "You need to install pallet and it's dependencies in")
           (println "~/.lein/plugins in order to use the lein-pallet plugin")
           (println "outside of a project.")
           1))))
  ([arg] (pallet nil arg))
  ([] (pallet nil)))

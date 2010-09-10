(ns leiningen.pallet
  "Pallet command line."
  (:require leiningen.compile))

(defn pallet
  "Launch pallet tasks from the command line.

   For a list of tasks
     lein pallet help"
  [project & args]
  (leiningen.compile/eval-in-project
   project
   `(do
      (require 'pallet.main)
      (pallet.main/-main ~@args))))

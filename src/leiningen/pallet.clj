(ns leiningen.pallet
  "Pallet command line."
 (:require
  [leiningen.core.main :refer [debug]]
  [leiningen.core.eval :refer [eval-in-project]]
  [leiningen.core.project :refer [make merge-profiles set-profiles]]
  leiningen.test))

(defn
  ^{:help-arglists []              ; suppress arguments in the default lein help
    :no-project-needed true}
  pallet
  "Launch pallet tasks from the command line.
   `lein pallet help` for more details."
  [project & args]
  (let [project-str (pr-str project)
        main-form
        `(do
           (if-let [rv# (try
                          (require '~'pallet.main)
                          (catch java.io.FileNotFoundException e#
                            (binding [*out* *err*]
                              (println
                               "Error loading pallet: " (.getMessage e#))
                              (println
                               (str "You need to have pallet as a project"
                                    " dependency.")))
                            (throw (ex-info "Pallet not found as a dependency"
                                            {:exit-code 1}) e#)))]
             ;; If there was an error, then the return value is 1 and that is
             ;; returned.
             rv#
             ;; If the require worked, the return value was nil and the else
             ;; clause happens.
             (if-let [m# (ns-resolve (the-ns '~'pallet.main) '~'pallet-task)]
               (try
                 (let [env# (:pallet/environment (read-string ~project-str))
                       s# (ns-resolve
                           (the-ns '~'pallet.main) '~'add-service)]
                   (when s#
                     ;; create a default vmfest provider
                     (s# :vbox {:provider :vmfest})
                     (s# :localhost {:provider :localhost}))
                   (m# (concat ["--project-options" ~project-str]
                               [~@args])
                       :environment env#))
                 (finally
                   ~(when (and (bound? #'leiningen.test/*exit-after-tests*)
                               leiningen.test/*exit-after-tests*)
                      `(shutdown-agents))))
               (do
                 (binding [*out* *err*]
                   (println
                    "failed to resolve pallet.main/pallet-task"))))))]
    (debug "Dependencies" (:dependencies project))
    (debug "Source Paths" (:source-paths project))
    (debug "Resource Paths" (:resource-paths project))
    (eval-in-project project main-form nil)))

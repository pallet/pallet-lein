(ns leiningen.pallet
  "Pallet command line."
 (:require
  [leiningen.core.eval :refer [eval-in-project]]
  [leiningen.core.project :refer [merge-profiles]]
  leiningen.test))

(defmacro maybe-shutdown-agents
  "Shutdown agents if required after body"
  [& body]
  `(try
     ~@body
     (finally
      (when (and (bound? #'leiningen.test/*exit-after-tests*)
                 leiningen.test/*exit-after-tests*)
        (shutdown-agents)))))

;; This profile will be added if there is no explicit :pallet profile.
(defn pallet-profile [{:keys [pallet] :as project}]
  {:source-paths (:source-paths pallet ["pallet/src"])
   :resource-paths (:resource-paths pallet ["pallet/resources"])
   :dependencies '[^:displace [org.cloudhoist/pallet "0.8.0-SNAPSHOT"]
                   ^:displace [org.cloudhoist/pallet-vmfest "0.3.0-SNAPSHOT"]
                   ^:displace [vmfest "0.3.0-SNAPSHOT"
                               :exclusions [commons-logging]]
                   ^:displace [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]
                   ;; [org.clojars.tbatchelli/vboxjws "4.2.4"]

                   ;; we do this to get a logging configuration
                   ;; this needs some thinking about
                   ^:displace [org.cloudhoist/pallet-lein "0.6.0-SNAPSHOT"]
                   ^:displace [ch.qos.logback/logback-classic "1.0.9"]]
   :repositories
   {"sonatype"
    {:url "https://oss.sonatype.org/content/repositories/releases/"
     :snapshots false}}})

(defn has-profile? [project profile-kw]
  (let [{:keys [included-profiles excluded-profiles]} (meta project)]
    ((set (concat included-profiles excluded-profiles)) profile-kw)))

(defn
  ^{:help-arglists []}             ; suppress arguments in the default lein help
  pallet
  "Launch pallet tasks from the command line.
   `lein pallet help` for more details."
  ([project & args]
     (let [project (merge-profiles
                    project
                    (or
                     (seq (filter (partial has-profile? pallet) [:pallet]))
                     [(pallet-profile project)]))
           [project args] (if (and (map? project)
                                   (every?
                                    identity
                                    (map project [:name :group :version])))
                            [project args]
                            [nil [project]])
           project-str (pr-str (dissoc project :repositories))
           main-form
           `(do
              (if-let [rv# (try
                             (require '~'pallet.main)
                             (catch java.io.FileNotFoundException e#
                               (binding [*out* *err*]
                                 (println
                                  "Error loading pallet: " (.getMessage e#))
                                 (println
                                  "You need to have pallet as a project")
                                 (println
                                  "dependency or installed in ~/.lein/plugins"))
                               1))]
                ;; If there was an error, then the return value is 1 and that is
                ;; returned.
                rv#
                ;; If the require worked, the return value was nil and the else
                ;; clause happens.
                (if-let [m# (ns-resolve (the-ns '~'pallet.main) '~'pallet-task)]
                  (try
                    (let [env# (:pallet/environment (read-string ~project-str))]
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
       (if (and project (map? project))
         (eval-in-project project main-form nil)
         (maybe-shutdown-agents
          (require 'pallet.main)
          ((ns-resolve (the-ns 'pallet.main) 'pallet-task) args)
          (catch java.io.FileNotFoundException e
            (println "Error loading pallet: " (.getMessage e))
            (println "You need to install pallet and it's dependencies in")
            (println "~/.lein/plugins in order to use the lein-pallet plugin")
            (println "outside of a project.")
            (throw (ex-info "Error loading pallet" {} e)))))))
  ([] (pallet nil)))

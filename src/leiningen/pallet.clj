(ns leiningen.pallet
  "Pallet command line."
 (:require
  [leiningen.core.main :refer [debug]]
  [leiningen.core.eval :refer [eval-in-project]]
  [leiningen.core.project :refer [make merge-profiles set-profiles]]
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

;; This profile will be added
(defn pallet-profile [{:keys [pallet] :as project}]
  {:source-paths (:source-paths pallet ["pallet/src"])
   :resource-paths (:resource-paths pallet ["pallet/resources"])
   :dependencies '[^:displace [com.palletops/pallet "0.8.0-beta.6"]
                   ^:displace [org.cloudhoist/pallet-vmfest "0.3.0-alpha.2"]
                   ^:displace [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]
                   ;; [org.clojars.tbatchelli/vboxjws "4.2.4"]

                   ;; Add an explicit version of clojure.  A project
                   ;; can override this, but the hooks ensure a minimal
                   ;; version
                   ^:displace [org.clojure/clojure "1.4.0"]

                   ;; we do this to get a logging configuration
                   ;; this needs some thinking about
                   ^:displace [com.palletops/pallet-lein "0.6.0-beta.8"]
                   ^:displace [ch.qos.logback/logback-classic "1.0.9"]]
   :jvm-opts ["-XX:+TieredCompilation"]
   :repositories
   {"sonatype"
    {:url "https://oss.sonatype.org/content/repositories/releases/"
     :snapshots false}}})

(defn has-profile?
  "Predicate to check if project has the specified profile."
  [{:keys [profiles] :as project} profile-kw]
  (let [{:keys [included-profiles excluded-profiles active-profiles]}
        (meta project)

        all-profiles (set
                      (concat
                       included-profiles excluded-profiles active-profiles
                       (keys profiles)))]
    (debug "Available profiles" all-profiles)
    (all-profiles profile-kw)))

(defn
  ^{:help-arglists []              ; suppress arguments in the default lein help
    :no-project-needed true}
  pallet
  "Launch pallet tasks from the command line.
   `lein pallet help` for more details."
  [project & args]
  (let [base (vary-meta
              (merge
               (make {:name "pallet-lein" :group "pallet" :version "0.1.0"})
               (select-keys project [:root]))
              merge (meta project))
        ;; add in any :pallet profile in the original project
        base (update-in base [:profiles]
                        merge (select-keys (:profiles project) [:base :pallet]))
        _ (debug "project is" project)
        _ (debug "base is" base)
        ;; now apply :pallet profile, with :pallet and any other included
        ;; profiles, with input from user and project level profiles.clj
        project (set-profiles
                 base
                 (concat
                  (:included-profiles (meta project))
                  [(pallet-profile project) :pallet]))
        _ (debug "project with profiles is" project)
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

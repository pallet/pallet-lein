(ns leiningen.pallet-test
  (:require
   [leiningen.pallet :as pallet]
   [leiningen.core.project :refer [make]]
   [clojure.test :refer :all]))

(def min-project
  (make {:dependencies '[[org.clojure/clojure "1.4.0"]
                         [com.palletops/pallet "0.8.0-RC.1"]
                         [ch.qos.logback/logback-classic "1.0.9"]]
         :prep-tasks []}))

(deftest pallet-test
  (is (nil? (pallet/pallet min-project))))

(deftest pallet-with-cmd-test
  (is (nil? (pallet/pallet min-project "help"))))

(deftest pallet-with-project-and-cmd-test
  (is (nil? (pallet/pallet
             (make
              {:source-paths ["src"]
               :resource-paths ["resources"]
               :test-paths ["test"]
               :native-path "target/native"
               :compile-path "target/classes"
               :target-path "target"
               :group "group"
               :name "name"
               :version "0.1.0"
               :eval-in :leiningen
               ;; lein1
               :dependencies [['org.clojure/clojure "1.4.0"]]
               :eval-in-leiningen true
               :source-path "src"
               :resource-path "resources"
               :test-path "test"
               :local-repo-classpath true
               :root (System/getProperty "user.home")})
             "help"))))

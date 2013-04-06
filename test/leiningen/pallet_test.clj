(ns leiningen.pallet-test
  (:require
   [leiningen.pallet :as pallet]
   [leiningen.core.project :refer [make]]
   [clojure.test :refer :all]))

(deftest pallet-test
  (is (nil? (pallet/pallet nil))))

(deftest pallet-with-cmd-test
  (is (nil? (pallet/pallet nil "help"))))

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

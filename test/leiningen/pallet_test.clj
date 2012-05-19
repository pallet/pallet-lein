(ns leiningen.pallet-test
  (:require
   [leiningen.pallet :as pallet])
  (:use
   clojure.test))

(deftest pallet-test
  (is (= 0 (pallet/pallet))))

(deftest pallet-with-cmd-test
  (is (= 0 (pallet/pallet "help"))))

(deftest pallet-with-project-and-cmd-test
  (is (= 0 (pallet/pallet
            {:source-paths ["src"]
             :resource-paths ["resources"]
             :test-paths ["test"]
             :native-path "target/native"
             :compile-path "target/classes"
             :target-path "target"
             :group "group"
             :name "name"
             :version "0.1.0"
             :eval-in :leiningen}
            "help"))))

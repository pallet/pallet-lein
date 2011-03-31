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
            {:compile-path "classes"
             :library-path "lib"
             :group "group"
             :name "name"
             :version "0.1.0"}
            "help"))))

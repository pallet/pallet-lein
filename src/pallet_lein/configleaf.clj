(ns pallet-lein.configleaf)

(defn get-environment
  [project]
  (if (try (require 'configleaf.core)
           true
           (catch java.io.FileNotFoundException e
             false))
    ;; First we have to dynamically resolve the functions we need.
    (let [get-current-profile-fn (ns-resolve (the-ns 'configleaf.core)
                                             'get-current-profile)
          cl-config       (:configleaf project)
          curr-profile    (get-current-profile-fn cl-config)]
      (get-in cl-config [:profiles curr-profile :pallet/environment]))))

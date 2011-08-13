(ns pallet-lein.configleaf)

(defn get-environment
  [project]
  (if (try (require 'configleaf.core)
           true
           (catch java.io.FileNotFoundException e
             false))
    ;; First we have to dynamically resolve the functions we need.
    (let [get-current-config-fn (ns-resolve (the-ns 'configleaf.core)
                                            'get-current-config)
          config-namespace-fn   (ns-resolve (the-ns 'configleaf.core)
                                            'config-namespace)
          configleaf-data       (:configleaf project)
          config-ns             (config-namespace-fn configleaf-data)
          curr-cfg              (get-current-config-fn configleaf-data)
          curr-cfg-data         (get-in configleaf-data
                                        [:configurations
                                         curr-cfg])]
      (:pallet/environment curr-cfg-data))))

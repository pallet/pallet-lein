{:dev {:dependencies [[org.cloudhoist/pallet "0.8.0-SNAPSHOT"]]}
 :release
 {:plugins [[lein-set-version "0.2.1"]]
  :set-version
  {:updates [{:path "README.md" :no-snapshot true}
             {:path "src/leiningen/pallet.clj"
              :search-regex
              #"\"pallet-lein\" \"\d+\.\d+\.\d+(-SNAPSHOT)?\""}]}}}

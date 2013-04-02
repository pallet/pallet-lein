{:dev {:dependencies [[com.palletops/pallet "0.8.0-SNAPSHOT"]]
       :plugins [[lein-set-version "0.3.0"]]}
 :release
 {:set-version
  {:updates [{:path "README.md" :no-snapshot true}
             {:path "src/leiningen/pallet.clj"
              :search-regex
              #"/pallet-lein \"\d+\.\d+\.\d+[a-zA-Z0-9-.]*\""}]}}}

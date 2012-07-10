(defproject org.cloudhoist/pallet-lein "0.5.1-SNAPSHOT"
  :description "Leiningen plugin for running Pallet tasks"
  :dev-dependencies [[org.clojure/clojure "1.2.1"]
                     [org.cloudhoist/pallet "0.7.0"]]
  :profiles {:dev {:dependencies [[org.cloudhoist/pallet "0.7.0"]]}}
  :eval-in-leiningen true
  :repositories
  {"sonatype" "https://oss.sonatype.org/content/repositories/releases/"})

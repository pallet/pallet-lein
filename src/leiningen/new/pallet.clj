(ns leiningen.new.pallet
  "Pallet project template for use with lein newnew"
  (:use leiningen.new.templates))

(def render (renderer "pallet"))

(def default-features
  {:version "0.6.5"
   :project-version "0.1.0-SNAPSHOT"
   :with-vmfest nil
   :vmfest-version "0.2.3"
   :with-growl nil
   :pallet-growl-version "0.1.0-SNAPSHOT"})

;; The features supplied as arguments to the template should be name value
;; pairs.  These are converted into a map, and the first of each pair is made
;; a keyword.
(defn feature-map
  [features]
  (into {}
        (map
         #(vector (keyword (first %)) (second %))
         (partition 2 features))))

;; Once all features are specified, we infer some values for the templates.
(defn with-automated-admin-user-dependency
  [data]
  (if (nil? (:with-automated-admin-user-dependency data))
    (if (re-matches #"0.[4-6]\..*" (:version data))
      (assoc data :with-automated-admin-user-dependency true))
    data))

(defn infer-features
  [data]
  (->
   data
   with-automated-admin-user-dependency))

(defn pallet
  "A Pallet project template. The template requires a project name, and an
   optional list of feature/value pairs."
  [name & features]
  (let [data (merge
              default-features
              (feature-map features)
              {:name name
               :sanitized (sanitize name)})
        data (infer-features data)]
    (->files
     data
     ["src/{{sanitized}}/groups/{{sanitized}}.clj" (render "group.clj" data)]
     ["project.clj" (render "project.clj" data)])))

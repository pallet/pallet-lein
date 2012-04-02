# Release Notes

## 0.4.2

- remove lein-newnew templates
  These were causing an issue. See http://dev.clojure.org/jira/browse/TNS-1

- Update test to use :evel-in-leiningen

- Add logic to shutdown agents correctly

- Update dev dependencies

- Update to support Configleaf.
  Now supports Configleaf, it is present. Takes the environment from the
  :pallet/environment key of the config, if present, and passes it to
  pallet-task. Requires the changes to pallet.

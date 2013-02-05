# pallet-lein

A [leiningen](http://github.com/technomancy/leiningen) plugin for running
[pallet](http://github.com/hugoduncan/pallet) tasks for a pallet deployment
project.

## Usage

A pallet deployment project needs to have pallet as a dependency.

For a list of pallet tasks,

    lein pallet help

The task has a default set of pallet dependencies.  To override these, you can
declare a `:pallet profile`, which will be preferred over the built in
dependencies.

The plugin adds `pallet/src` and `pallet/resources` to the project's classpath.
You can override this by specifying `:source-paths` and `:resource-paths` in a
project `:pallet` key.  For example:

```clj
  :pallet {:source-paths ["src-pallet"] :resource-paths []}
```

## Installation

Add the plugin to your `:plugins`, either in the `:dev` profile of your
`project.clj` file, or in the `:user` profile of your `~/.lein/profiles.clj`
file.

    :plugins [[org.cloudhoist/pallet-lein "0.5.2"]]

Requires lein 2.0.0 or higher.

## License

Copyright (C) 2010, 2011, 2012, 2013 Hugo Duncan

Distributed under the Eclipse Public License.

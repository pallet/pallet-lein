# pallet-lein

A [leiningen](http://github.com/technomancy/leiningen) plugin for running
[pallet](http://github.com/hugoduncan/pallet) tasks for a pallet deployment
project.

Compatible with lein 1 and 2.

## Usage

A pallet deployment project needs to have pallet as a dependency.

For a list of pallet tasks,

    lein pallet help


## Installation (lein 1.x)

Either install with `lein plugin`:

    lein plugin install org.cloudhoist/pallet-lein "0.5.0"

or, add the plugin to your `project.clj` file.

    :dev-dependencies [[org.cloudhoist/pallet-lein "0.5.0"]]

## License

Copyright (C) 2010, 2011, 2012 Hugo Duncan

Distributed under the Eclipse Public License.

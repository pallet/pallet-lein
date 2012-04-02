# pallet-lein

A [leiningen](http://github.com/technomancy/leiningen) plugin for running
[pallet](http://github.com/hugoduncan/pallet) tasks for a pallet deployment
project.

Not yet lein2 compatible.

## Usage

A pallet deployment project needs to have pallet as a dependency.

For a list of pallet tasks,

    lein pallet help


## Installation

Either install with `lein plugin`:

    lein plugin install org.cloudhoist/pallet-lein "0.4.2"

or, add the plugin to your `project.clj` file.

    :dev-dependencies [[org.cloudhoist/pallet-lein "0.4.2"]]

## License

Copyright (C) 2010, 2010, 2012 Hugo Duncan

Distributed under the Eclipse Public License.

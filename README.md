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

The plugin adds `pallet/src` to the leiningen `:source-paths` and
`pallet/resources` to the project's `:resource-paths` (it preserves the paths
you already have configured).  You can override this by specifying
`:source-paths` and `:resource-paths` in a project `:pallet` key.  For example:

```clj
:pallet {:source-paths ["src-pallet"] :resource-paths []}
```

### Default Pallet Dependencies

The plugin uses a default set of dependencies that includes
[vmfest](https://github.com/tbatchelli/vmfest/tree/develop).  This should enable
use of VirtualBox with no configuration.  Note that this support is broken on
linux, due to a bug in VirtualBox, and you will have to run the `vboxwebsrv`,
and use the `vboxjws` dependency.  To use `vboxjws` add the following profile to
your `project.clj` file.

```clj
:pallet {:dependencies [[org.virtualbox/vboxjws "4.2.6"]]}
```

### Adjusting Pallet Dependencies

The plugin's pallet dependencies can be adjusted using the leiningen `:pallet`
profile, either in your `project.clj` or `profiles.clj` files.  See the
[leiningen documentation](https://github.com/technomancy/leiningen/blob/master/doc/PROFILES.md)
for details on using profiles.

Using this mechanism, you can add pallet crate dependencies, and providers for
clouds such as EC2.

## Installation

### For pallet 0.8.0 and higher

Add the plugin to your `:plugins`, either in the `:dev` profile of your
`project.clj` file, or in the `:user` profile of your `~/.lein/profiles.clj`
file.

    :plugins [[com.palletops/pallet-lein "0.6.0-beta.9"]]

Requires lein 2.0.0 or higher.

### For pallet 0.7.x

Add the plugin to your `:plugins`, either in the `:dev` profile of your
`project.clj` file, or in the `:user` profile of your `~/.lein/profiles.clj`
file.

    :plugins [[org.cloudhoist/pallet-lein "0.5.2"]]

## License

Copyright (C) 2010, 2011, 2012, 2013 Hugo Duncan

Distributed under the Eclipse Public License.

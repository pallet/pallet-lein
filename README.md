# pallet-lein

A [leiningen](http://github.com/technomancy/leiningen) plugin for running
[pallet](http://github.com/hugoduncan/pallet) tasks for a pallet deployment
project.

## Usage

To uses the pallet-lein plugin you will need to have pallet as a
project dependency.  The simplest way to achieve that is to add pallet
to your `:dependencies` in your `project.clj` file.

For a list of pallet tasks,

    $ lein pallet help

Apart from pallet itself, you will need to add pallet crate
dependencies, and pallet providers for clouds such as EC2, to your
dependencies.

## Profiles

Adding pallet to your project's dependencies is intrusive, so you probably
want to add pallet via a profile in `project.clj`.

```clj
:profiles {:pallet {:dependencies [[com.palletops/pallet "0.8.0-RC.1"]]}}
```

You can then invoke lein using `with-profile`:

    $ lein with-profile pallet pallet help

If that gets to unwieldy, just define an alias, again in `project.clj`:

```clj
:aliases {"pallet" ["with-profile" "+pallet" "pallet"]}
```

    $ lein pallet help

Using `with-profile +pallet` adds the dependencies from the `pallet`
profile to your normal project dependencies.  To use only the pallet
profile, without the default project dependencies, use `with-profile
pallet`, without the `+`.

Using an independent profile is also useful to separate out your
pallet code from your application code.  For example, you can have
separate source and resource directories:

```clj
:profiles {:pallet {:dependencies [[com.palletops/pallet "0.8.0-RC.1"]]
                    :source-paths ["pallet/src"]
                    :resource-paths ["pallet/resources"]}}
```

See the
[leiningen documentation](https://github.com/technomancy/leiningen/blob/master/doc/PROFILES.md)
for details on using profiles.

## Installation

### For pallet 0.8.0 and higher

Add the plugin to your `:plugins`, either in the `:dev` profile of your
`project.clj` file, or in the `:user` profile of your `~/.lein/profiles.clj`
file.

    :plugins [[com.palletops/pallet-lein "0.8.0-alpha.1"]]

Requires lein 2.0.0 or higher.

### For pallet 0.7.x

Add the plugin to your `:plugins`, either in the `:dev` profile of your
`project.clj` file, or in the `:user` profile of your `~/.lein/profiles.clj`
file.

    :plugins [[org.cloudhoist/pallet-lein "0.5.2"]]

## License

Copyright (C) 2010, 2011, 2012, 2013 Hugo Duncan

Distributed under the Eclipse Public License.

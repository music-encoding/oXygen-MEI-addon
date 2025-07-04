# Build Instructions

## Prerequisites

Before you start the build process make sure all submodules are initialized and checked out.

Either clone the repository recursively

```shell
git clone https://github.com/music-encoding/oXygen-MEI-addon.git --recurse-submodules
```

or run the following to initialize the submodules

```shell
git submodule update --init
```

Moreover make sure, the following prerequisites are met by your system:

* Apache Ant installed
* Maven installed

## The Build Process

Building the oXygen-MEI-Addon is based on Apache Maven. In order to build, run the following command:

```shell
mvn compile
```

All build artifacts will be created in the `target` directory. The following steps will be executed:

1. copy-resources
  * *copy-verovio-submodule* will copies `submodules/verovio` to `target/generated-sources/verovio`
  * *copy-verovio-src* copies `src/verovio` to `target/generated-sources/verovio/bindings/java/example-mei`
  * *copy-schema* copies `submodules/schema` to `target/extra-resources/schema`
  * *compile-verovio*

This tasks launches several subtasks:

1. Download MEI Schemata and documentation
2. Build and copy verovio files
3. Copy mec-proceedings
4. copy encoding tools  

## Additional Ant Build Targets
Moreover some additional build targets are defined:

1. ant reset: purges `build`, `dist`, and `assets` directories
1. ant clean: purges `build` directory
1. ant clean.assets: purges `assets` directory
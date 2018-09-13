# Build Instructions

## The Build Process
Building the oXygen-MEI-Addon is based on Apache Ant. the straightforward command for building is:

```shell
ant
```

This tasks launches several subtasks:

1. Download MEI Schemata and documentation

## Additional Ant Build Targets
Moreover some additional build targets are defined:

1. ant reset: purges `build`, `dist`, and `assets` directories
1. ant clean: purges `build` directory
1. ant clean.assets: purges `assets` directory
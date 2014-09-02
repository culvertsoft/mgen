---
layout: default
link-title: Dependencies
---

## MGen's external dependencies

We try to keep external dependencies as low as possible, but do use some external libries.


### Java runtime library

External java dependencies are automatically downloaded when using MGen's maven repository for dependency management. Telling maven/gradle/sbt/etc to get mgen-javalib should download any dependencies you need to build an application with MGen. The MGen java runtime library depends on:

 * json-simple


### C++ runtime library

Our philosophy for the MGen C++ runtime library is to keep it small and header-only. This way we make installation very simple and don't let users keep their build setups untouched, by just adding an include directory.

By going header-only, we can also bundle any requirements with MGen. The MGen c++ runtime library depends on (and bundles):

 * RapidJSON



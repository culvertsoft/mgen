---
layout: default
link-title: Home
---

# Introduction
---
MGen is a toolkit for designing cross-langauge data models to which functionality such as serialization and type introspection can easily be attached. MGen models are defined in an [Interface Definition Language (IDL)](http://en.wikipedia.org/wiki/Interface_description_language "IDL on Wikipedia"), from which source code (classes) in multiple programming languages may be generated using the MGen Compiler.

The goal of MGen is to simplify type-safe sharing of state between applications, while permitting significant data model changes without requiring all participants to rebuild their software. We seek to be highly customizable by having plug-in based architecture for extending the MGen toolkit without having to recompile the MGen tools and libraries.

Check out [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).

Why is MGen interesting? - MGen can help you with the following, cross-language and cross-platform:

 * Save/Load the state of an application (objects) to memory, files, strings, etc
 * Read/write config files directly to objects
 * Reduce the amount of work required to build and maintain data structures in multiple languages
 * Simplify the process of sharing the state of your application over network
 * Make sure the data model in software you build today is as accessible as possible for future projects
 * Build scalable, accessible, compatible data models that you come back to later and understand.
 * Use native objects with external services and interfaces requiring text formats such as JSON.
 * Combine definitions in multiple IDLs for your data models, and have dependencies between them.
 * Much much more..

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding). 

---
layout: default
link-title: Home
---

## MGen

MGen is a softare development toolkit for designing cross-langauge data models to which functionality such as serialization and type introspection can easily be attached. MGen models are defined in one or more [Interface Definition Languages (IDLs)](http://en.wikipedia.org/wiki/Interface_description_language "IDL on Wikipedia"), from which source code (classes) in multiple programming languages may be generated using the MGen Compiler.

The goal of MGen is to simplify type-safe sharing and persistence of state between applications, while permitting significant data model changes without requiring a global rebuild. We seek to make MGen highly extensible by having plug-in based architecture for extending the MGen toolkit.

MGen consists of a compiler for source code generation and language specific runtime libraries.

Check out [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).


MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding). 

---
layout: default
link-title: Home
---

## MGen

Welcome to the MGen project page!

MGen is a software development toolkit for reducing duplicate work when building cross-language applications. It lets you define your data model in one place, and generate source code for the languages you choose. The generated sources provide interfaces that can be used for object serialization and type introspection, to which MGen also provides runtime libraries with ready-to-use serializers.

The goal of MGen is to reduce manual boilerplate code, maintanance efforts and simplify type-safe sharing and persistence of state between applications, while permitting significant data model changes without requiring a global rebuild. 

We seek to make MGen highly extendable by having plug-in based architecture for extending the MGen toolkit.

MGen consists of:

 * A compiler for source code generation
 * Language specific runtime libraries


Check out [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding). 

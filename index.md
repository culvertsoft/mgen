---
layout: default
link-title: Home
---

## MGen

Welcome to the MGen project page!

MGen is a toolkit for serializing, deserializing, analyzing and modifying objects in a generic way. It lets you snapshot polymorphic objects for both read and write using generated visitor methods, in a way that is type safe, storage format agnostic and backwards compatible.

This is achieved using:

 * Source code generation
 * Language specific support libraries
 
The MGen compiler generates source code for the data structures to read/write, and libraries are used to facilitate read/write functionality (among other things). The compiler is plug-in based and allows you to easily add support for your own code generation, new output languages, and new input IDLs.

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding),
[Djinni](https://github.com/dropbox/djinni).

Check out [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).
---
layout: default
link-title: Home
---

MGen is a toolkit for cross language object serialization. It consists of:

 * Tools for source code generation (The MGen compiler)
 * Language specific support libraries.

Think Google's Protocol Buffers on steroids - with support for native polymorphism, heterogenous containers, maps, lists, arrays and custom serialization formats.

Generated classes accept generic visitor objects, while the MGen libraries provide built-in visitor classes that serialize to JSON and binary formats. You can also roll your own custom object visitors to implement whatever functionality you like.

The MGen compiler is plug-in based, with code generation decoupled from IDL parsing - so if you're unhappy with either you can add your own IDL parser and/or code generator plug-in.

As a cherry on the cake MGen lets you modify generated code - modifications that will be identified and preserved the next time you generate.

Supported languages (so far):

 * C++
 * Java
 * JavaScript
 

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding),
[Djinni](https://github.com/dropbox/djinni).

Check out [our original technical whitepaper (note: slightly outdated)](http://culvertsoft.se/docs/WhitePaper.pdf).

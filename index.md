---
layout: default
link-title: Home
---

MGen is a toolkit for cross language object serialization. It consists of:

 * Tools for source code generation (The MGen compiler)
 * Language specific support libraries.

Think Google's Protocol Buffers - but with support for language native inheritance and polymorphism, heterogenous containers, maps, lists, arrays and custom serialization formats.

Generated classes accept generic visitors, while the MGen libraries provide visitor classes that serialize to JSON and binary formats. You can also roll your own visitors to implement whatever functionality you like.

The MGen compiler is plug-in based, with code generation decoupled from IDL parsing - so if you're unhappy with either you can add your own IDL parser and/or code generator plug-in.

As a cherry on the cake MGen lets you modify generated code - modifications that will be identified and preserved the next time you generate.

Supported languages (so far):

 * C++
 * Java
 * JavaScript
 

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [Bond](https://microsoft.github.io/bond/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding),
[Djinni](https://github.com/dropbox/djinni).

Check out [our original technical whitepaper (note: slightly outdated)](http://culvertsoft.se/docs/WhitePaper.pdf).

If you'd like to make a contribution, check [our discussion group](https://groups.google.com/forum/?hl=en#!forum/mgen-mailing-list) and [our github repo](https://github.com/culvertsoft/mgen).

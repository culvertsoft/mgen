# MGen

You've ended up at the MGen github repository!
This is where you can download the MGen source and learn how to build it. 

**If you want to know what MGen is**
 head over to our [preliminary project pages](http://culvertsoft.github.io/mgen/) instead

## Table of Contents

* [Download links](#download-links)
  * [Stable](#stable)
  * [Nightly](#nightly)
  * [Snapshot](#snapshot)
  * [Maven](#maven)
  * [Sample Projects](#sample-projects)
* [Installation](#installation)
  * [System Requirements](#system-requirements)
  * [Installing the compiler](#installing-the-compiler)
  * [Installing the Java runtime libraries](#installing-the-java-runtime-libraries)
  * [Installing the C++ runtime libraries](#installing-the-c++-runtime-libraries)
  * [Installing the JavaScript runtime libraries](#installing-the-javascript-libraries)
  * [Installing the Visual Designer](#installing-the-visual-designer)
* [Building MGen](#building-mgen)
* [State of Development](#state-of-development)
* [Feature road map](#feature-road-map)
* [License](#license)
* [Words regarding object references](#Words-regarding-object-references)


## Download links

The following are links where you can download MGen pre-built. NOTE:
 * JAR files marked _assembly_ are standalone executables, packaged with all dependencies included. 
 * JAR files intended as programming libraries are NOT marked _assembly_.

### Stable

Represents the latest non-beta release. As we're currently in alpha phase, there are yet no entries here.

### Nightly 

Represents the latest built development version, built once per night. You can download it from http://nightly.culvertsoft.se

### Snapshot

Represents the latest built development version, built after each git push. You can download it from http://snapshot.culvertsoft.se

### Maven

We plan on making the MGen tools and libraries available through Maven. However we haven't reached any formal version numbers yet, so at this point your option is to grab the snapshot, see https://oss.sonatype.org/#nexus-search;quick~mgen for details.


### Sample Projects

Currently we haven't had time to produce any dedicated sample projects. But some models of interest might be:
 * [One of the data model used by our tests](https://github.com/culvertsoft/mgen/tree/master/mgen-compiler/src/test/resources)
 * [One of the data model used by our tests](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/depend)
 * [One of the data model used by our tests](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/read)
 * [One of the data model used by our tests](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/write)
 * [The data model of the MGen Visual Designer](https://github.com/culvertsoft/mgen/tree/master/mgen-visualdesigner/model)


## Installation

At this early stage installation is manual (there is currently no installer available). Below you can find installation instructions for each language we currently support. Read the later chapters if you would like to try to add support for more languages.

### System Requirements

The MGen standalone applications (MGen Compiler and MGen Visual Designer) require Java 7 or later to be installed on your computer, such as OpenJRE/OpenJDK or the [Oracle JRE](http://java.com/en/download/index.jsp). We are currently not planning to support any earlier versions than java7.


### Installing the compiler

The MGen Compiler is just an executable java JAR file. Check the [downloads section](#download-links) and download a release of the version you want. You could for example try out [the latest build compiler](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-compiler/mgen-compiler-assembly-SNAPSHOT.jar).

There is no installation required. Just put it where you like and follow the instructions in the sections above on how to use it. Rename it as you see fit.

See [Generating source code](#generating-source-code) for how to use it.


### Installing the Java runtime libraries

The java libraries are compiled to java jar files, and currently have one external dependency: json-simple. Add the following jar files to your build:
 - [mgen-api](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-api/)
 - [mgen-javalib](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-javalib/)
 - [json-simple](https://code.google.com/p/json-simple/)

Check the [downloads section](#download-links) or try the snapshots above.

If you want to include the runtime libries by source instead or build them yourself, see [Building MGen](#building-mgen).


### Installing the C++ runtime libraries

The C++ runtime libraries are header-only (and should work with any c++98 compiler) - there is no installation required. Just download and add to your include path. All dependencies are included (currently just [rapipdjson](https://code.google.com/p/rapidjson/)). Just download the nightly or snapshot package and look in the mgen-cpplib/ folder.


### Installing the JavaScript runtime libraries

- Work in progress -


### Installing the Visual Designer

The Visual Designer is an executable java JAR file. Check the [downloads section](#download-links) to try out the latest version.
[Here](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-visualdesigner/mgen-visualdesigner-assembly-SNAPSHOT.jar) a direct link for convenience.

Place and rename the jar file where you like. It should be possible to launch by double clicking if you have a Java JRE correctly installed.


## Under the Hood

### Components

MGen's core components consist of:
 * The MGen API
 * The MGen Compiler
 * The MGen Code Generators
 * The MGen Runtime libraries
 * The MGen Visual Designer

#### The MGen API

The MGen API, as well as the interfaces to the compiler, are written entirely in Java, although under the hood much of the compiler is written in Scala.

The API defines the standard data types supported by the compiler and runtime libraries. The API also defines the Parser and Generator interfaces used by the compiler.

#### The MGen Compiler

The Compiler is a command line executable which parses IDL code and produces classes in your programming language of choice. It is capable of dynamically loading new parsers and code generators on startup, either the default MGen implementations or your own custom supplied libraries (just place a JAR file containing Java classes implementing the Generator or Parser interfaces on the Compiler's plugin search path, and they will become available to use through the compiler).

Key features for the compiler, standard parser and standard generators are:
 * Support for generic types 
 * Heterogeneous containers
 * Support for polymorphic types (maps directly to c++/java class hierarchies)
 * Plug-in architecture for loading custom parsers and generators
   * Ability to extend to new generated languages
   * Ability to extend to new idl languages

#### The MGen Code Generators

MGen currently provides code generators for the following languages: 
 * Java
 * C++
 * JavaScript

These produce:
 * Classes (or class-like structures in JavaScript)
 * ClassRegistries (registry for all generated classes)
 * Utility classes (such as dispatchers and handlers)

By default generated classes are java-beans and C++ data containers with getters and setters or class-like structures in the case of JavaScript. Some of the functionality generated for classes is:
* Flag members indicating whether a member has been set or not
* Generic visitor methods that will visit all set fields
* Read methods
* Field identification methods
* Field metadata accessible while visiting an object and through method calls
* Type metadata accessible through both virtual and static methods
* Getters and Setters
* Query methods for asking if a member is set
* Type IDs
* Methods for testing equality (c++: == operator, java: equals(..))
* Stringification and object Hash Code methods (Java Only)

ClassRegistries are primarily used to identify and instantiate data types over data streams during deserialization.


#### The MGen Runtime libraries

MGen currently provides runtime libraries for the following languages: 
 * Java
 * C++
 * JavaScript

The runtime libraries work together with generated classes to provide:
 * Serialization
 * Deep Copying
 * Equality Testing
 * Automatic type identification and instantiation
 * Type metadata
 * Stringification
 * Hashing

The runtime libraries provide two default serializers:
 * The MGen Binary format Serializer
 * The MGen JSON format Serializer

Generated classes are serializer agnostic and supply interfaces through which you can connect your own writers and readers.


#### The MGen Visual Designer

We are also working on a visual data model editor.


### Performance

So far we've only run some basic performance tests, but the results are promising. Single threaded binary serialization speed of objects is about 1-2 GBit/s (Java,C++), depending on compiler and platform. JSON serialization performance is about half of that.


## Building MGen

If you're not satisfied with downloading pre-built libraries (see [the downloads section](#download-links)), this section will explain how you build MGen from source.

Build Requirements (Build):
  * Java JDK >= 1.7
  * Python 2.x>=2.7 or 3
  * SBT >= 1.3.5 (Use the installer from http://www.scala-sbt.org/download.html)

Build Requirements (Test):
  * same as above +
  * CMAKE >= 2.8
  * MSVC>=2005 or g++>=4 or Clang. (should work with any c++98 compiler)
  * msbuild (if using MSVC)

Build Instructions:
  * clone the repo
  * python build.py --help [example: "python build.py -b" for just building]

Output will be placed inside each mgen-component's target/ directory (e.g. mgen-api/target/).
 

## State of Development

Development is a about a month from an initial beta release. 

Most of the core functionality is implemented and we've built about half the cross-language integration tests we want, but there are currently a few limitations:
 - Documentation is lacking.
 - We've not yet configured test boxes for all the platforms we want.
 - We're hoping to add parsers for xml schema, json schema and protocol buffers IDLs, but those are currently on the "nice-to-have-list"


## Feature road map


## License

Dual-Licensed under:
 * GPL v2
 * Commercial license (will be available after initial release)



## Words regarding object references

MGen serializers and utilities are designed to be state-less. There is no common object graph preservation or node-to-node implementation with synchronization. This is a conscious design choice and also implies that the standard implementation of serializers do not support circular references. MGen serializers consider all data to be just that - data. MGen serializers have no concept of references or object identities (Although generated polymorphic code and data types in most languages are of reference types - during serialization they are treated as nothing more than data containers).

However, what this does is it gives us the advantage of supporting lossy and reordering protocols, priority based messaging etc without worrying about objects having all the necessary information to be reconstructed on receiving side. If you wish to send one message over http, another over a UDP socket and a third with smoke signals, in the opposite order, MGen won't really care.

We believe supporting identities and true object graphs should be the responsibility of the layer above, the layer syncronizing applications with with each other - not the data layer. We do have ideas to build such systems (ESBs, ORMs and common object graphs among application nodes and the like) on that support the MGen data model - but not as a part of MGen.

In short - we chose to separate the concerns of data representation from data identity and transport method - the two latter not being anything MGen is concerned with at this point, however not anything MGen will prevent either - You could implement your own object identity and graph system on top of types generated by MGen (Something we already did for our visual data model editor).

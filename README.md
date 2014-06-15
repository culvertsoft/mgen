# MGen

-- HEADS UP: This README is Work-In-Progress. It's about 20% finished! -- 

MGen is a toolkit for source code generation and creating data models that are easy to communicate across language and version barriers.

Inspired by [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), MGen aims to reduce the work required to build, maintain and extend cross-language data models. MGen models are defined in an extendable [IDL](http://en.wikipedia.org/wiki/Interface_description_language "IDL on Wikipedia") and the MGen compiler produces code that is simple and fast enough to use as the internal model for many applications.

The MGen tools and libraries are designed to simplify collaboration between software teams and companies that wish to communicate data and share common software components, allowing each team to work on and their parts separately without forcing other teams to rebuild their software.

MGen's structure is modular and customizable: IDL parsers, code generators, runtime libraries can be extended and/or replaced without rebuilding MGen. There is nothing stopping you from plugging in a protobuf IDL parser, thrift wire protocol serializer, and adding custom functionality to any of the supplied code generators, or attach a code generator for your own proprietary system.


## Table of Contents

* [Basic Usage](#basic-usage)
  * [Defining a data model](#defining-a-data-model)
  * [Generating source code](#generating-source-code)
  * [Serializing and deserializing](#serializing-and-deserializing)
* [Download links](#download-links)
  * [Stable](#stable)
  * [Nightly](#nightly)
  * [Snapshot](#snapshot)
* [Installation](#installation)
  * [Java](#java)
  * [CPP](#cpp)
  * [JavaScript](#javascript)
  * [Other Languages](#other-languages)
* [Under the Hood](#under-the-hood)
  * [Components](#components)
    * [The MGen API](#the-mgen-api)
    * [The MGen compiler](#the-mgen-compiler)
      * [The MGen code generators](#the-mgen-code-generators)
    * [The MGen runtime libraries](#the-mgen-runtime-libraries)
    * [The MGen Visual Designer](#the-mgen-visual-designer)
  * [Wire formats](#Wire-formats)
    * [The MGen binary format](#the-mgen-api)
    * [The MGen json format](#the-mgen-compiler)
* [Advanced Usage](#advanced-usage)
  * [Adjusting the built-in serializers](#adjusting-the-built-in-serializers)
  * [Adding code generators](#adding-code-generators)
  * [Adding IDL parsers](#adding-idl-parsers)
  * [Adding wire formats and serializers](#add-wire-formats-and-serializers)
  * [Communication with the non-MGen world](#communication-with-the-non-mgen-world)
  * [Ideas for adding new data model mappings](#ideas-for-adding-new-data-model-mappings)
    * [C Structs](#c-structs)
    * [Memory mapped data types](#memory-mapped-data-types)
    * [GPU data types](#gpu-data-types)
    * [Database tables](#database-tables)
* [Building MGen](#building-mgen)
  * [Build Time Tools](#build-build-time-tools)
  * [Runtime Libraries](#build-runtime-libraries)
  * [Tests](#tests)
* [Version History](#version-history)
* [Future Plants](#future-plans)
  * [RPC Interfaces](#rpc-interfaces)
  * [Transport Layers](#transport-layers)

## Basic Usage

MGen's common use case is defining a data model, generating source code and providing serializers/deserializers.

### Defining a data model

The standard way to write a data model is to use the MGen IDL. You can also use the MGen Visual Designer application to build them without having to write the IDL yourself.

Our data models consist of modules, types and fields. Modules define what c++ namespace or java package a type will be generated to. Types specify the classes you want to generate, and fields the data members of your classes.

Here is an example of two types defined in MGen's IDL:
    
    <MyType1>
      <myField1 type="int32"/>
      <myField2 type="string"/>
    </MyType1>
        
    <MyType2>
      <myField1 type="float64"/>
      <myField2 type="map[string, list[MyType1]]"/>
    </MyType2>

There is a little more work required before these can be passed to the MGen Compiler for source code generation. 

1. The module above must be defined in a module file. The name of the module file defines the namespace/package of the types defined within.
2. A Project file must be created. It defines which module files to load, which code generators to use and other settings.

Module and project files are normal text files with xml contents.

In this example we create a module file named "se.culvertsoft.mymodule.xml", containing:

    <Module>
      <Types>
        <MyType1>
          <myField1 type="int32"/>
          <myField2 type="string" flags="required"/>
        </MyType1>
        <MyType2>
          <myField1 type="float64"/>
          <myField2 type="map[string, list[MyType1]]"/>
          <myField3 type="MyType1" flags="polymorphic"/>
        </MyType2>
      </Types>
    </Module>

We create a project file named "MyProject.xml", containing:
    
    <Project>
      <Generator name="Java">
        <generator_class_path>se.culvertsoft.mgen.javapack.generator.JavaGenerator</generator_class_path>
        <output_path>src_generated/main/java</output_path>
        <classregistry_path>se.culvertsoft.mymodule</classregistry_path>
      </Generator>
      <Module>se.culvertsoft.mymodule.xml</Module>
    </Project>
    
Some explanation of the above project file and other contents may be required.
Keywords in the project file are:
- name: Just there to make it easier to deal with debugging. You can call it whatever you like.
- generator_class_path: The classpath to the source code generator to be used.
- output_path: The folder where generator output will be placed.
- classregistry_path: The namespace/package of the generated class registry (we will get to this later)

In the next section we will explain how to run time MGen compiler.

### Generating source code

In the previous section, we explained how to define a data model. This section will explain how to generate code from it.
Code generation is performed by executing the MGen compiler. The mgen compiler is a java JAR file which can be executed from the command line by typing.

java -jar MGenCompiler.jar -project="MyProject.xml" -plugin_paths="generators/"

Here we run the compiler with two arguments, project and plugin_paths. 
 - project: which project file to load (see the previous section to learn more about project files).
 - plugin_paths: where the compiler should search for java JAR files containing IDL parsers and code generators.

## Under the Hood

### Components
----

MGen's core components consist of:
 * The MGen API
 * The MGen Compiler
 * The MGen Runtime libraries

The MGen API, as well as the interfaces to the compiler, are written entirely in Java, although under the hood much of the compiler is written in Scala.

The API defines the standard data types supported by the compiler and runtime libraries. The API also defines the Parser and Generator interfaces used by the compiler.

The Compiler is a command line executable which parses IDL code and produces classes in your programming language of choice. It is capable of dynamically loading new parsers and code generators on startup, either the default MGen implementations or your own custom supplied libraries (just place a JAR file containing Java classes implementing the Generator or Parser interfaces on the Compiler's plugin search path, and they will become available to use through the compiler).

MGen currently provides code generators and runtime libraries for the following languages: 
 * Java
 * C++
 * JavaScript

Our next step is to add support for LUA and Python.

If you prefer graphical tools instead of command line, we are also working on a visual data model editor.


MGen Compiler
----

The compiler is divided into two parts, the parser and the code generators.

Parsers and code generators are what we call plug-ins, that is, they are loaded dynamically on compiler startup. If the user wants to extend or replace parsers or code generators, he/she provides command line arguments to the compiler for where to search for JAR files containing his own parser and/or generator classes. The only requirement is that the custom classes implement the Parser and/or Generator interfaces specified in the MGen Java API.

Key features for the compiler, standard parser and standard generators are:
 * Support for generic types 
 * Heterogeneous containers
 * Support for polymorphic types (maps directly to c++/java class hierarchies)
 * Customizable generators (generated code contents and functionality)
 * Customizable parsers
 * Plug-in architecture for loading custom parsers and generators
 * Ability to extend to new languages

MGen Runtime Libraries
----
Out of the box, MGen supplies standard code generators for C++, Java and JavaScript. These produce two things:
 * Classes (or class-like structures in JavaScript)
 * ClassRegistries (registry for all generated classes)

By default classes are java-beans and C++ data containers with getters and setters or class-like structures in the case of JavaScript. Generated classes work together with the runtime libraries to provide utilities for:
 * Serialization
 * Deep Copying
 * Equality Testing
 * Automatic type instantiation
 * Type metadata
 * Stringification
 * Hashing

The runtime libraries provide two default serializers:
 * The MGen Binary format Serializer
 * The MGen JSON format Serializer

You can of course modify, extend and/or replace these by extending or replacing the runtime library classes.

ClassRegistries are used to dynamically instantiate and identify data types over data streams during deserialization.
In some cases they may also be used during serialization. Some language implementations rely on ClassRegistries more than others. One could argue that Java for example  does not really need one (because of reflection), however for performance reasons and API uniformity, they are created for Java as well.

One feature that ClassRegistries provide in C++ is the ability to call template methods with dynamic dispatch. This means you for example can use the ClassRegistry to access a derived visitor method through a call on the base type with a templated argument, such as a custom serializer, and the method will be called with full type preservation on the most derived class. What this type preservation also means is that during (de)serialization, once the serializer has been dispatched to the visitor method of the object it should serialize (which isn't much more than a switch-table) there's not much need for virtual calls - which can substantially improve performance.

MGen does not rely on Java reflection or C++ RTTI.

We have measured binary serialization performances of some polymorphic objects in C++ on a single ivy bridge i7 core to more than 1,5 Gbit/s (g++ 4.8.1 o3). Performance in the other direction was roughly 30% less. Performance is important to us, however it should be made clear that performance is NOT the primary focus of MGen.


MGen Standard IDL
----
MGen currently uses xml for its standard IDL. We say this because in the future (before official release) we may switch or support multiple IDLs out-of-the-box. 

There are two types of IDL files required by MGen compiler to run:
 * Project files
 * Module files

Project files are our variant of makefiles. They define what module files to include but also what code generators to use, and settings for these. Below is the project file used in defining the data model used by our visual data model editor:

    <Project>

     <Generator name="Java">
      <generator_class_path>se.culvertsoft.mgen.javapack.generator.JavaGenerator</generator_class_path>
      <output_path>src_generated/main/java</output_path>
      <classregistry_path>se.culvertsoft.mgen.visualdesigner</classregistry_path>
     </Generator>

     <Module>se.culvertsoft.mgen.visualdesigner.model.xml</Module>

    </Project>

Module files are where the actual data models are defined. The format we've come up with for doing this is very much work-in-progress, but it should be noted that it is intended to be easy to read and edit for humans - not necessarily conform to a particular markup standard. 

As you may have guessed from the project file above, the name of the module file defines what java package and c++ namespace the generated code will be placed in.

Below you see the first part of the data model definition for the visual editor which was referenced in the project file above (to view the full source [here](mgen-visualdesigner/model/se.culvertsoft.mgen.visualdesigner.model.xml) ):

    <Module>

     <Types>

      <!-- = = = = = = = -->
      <!-- = = = = = = = -->
      <!-- Utility Types -->
      <!-- = = = = = = = -->

      <EntityIdBase />

      <EntityId extends="EntityIdBase">
       <lsb type="int64" flags="required" />
       <msb type="int64" flags="required" />
      </EntityId>

      <ClassPathEntityId extends="EntityIdBase">
       <path type="string" flags="required" />
      </ClassPathEntityId>

      <Placement>
       <x type="int32" />
       <y type="int32" />
       <width type="int32" />
       <height type="int32" />
      </Placement>

      <Generator>
       <name type="string" flags="required" />
       <generatorClassName type="string" flags="required" />
       <generatorJarFileFolder type="string" flags="required" />
       <classRegistryPath type="string" flags="required" />
       <outputFolder type="string" flags="required" />
       <settings type="map[string, string]" />
      </Generator>


      <!-- = = = = = = -->
      <!-- = = = = = = -->
      <!-- Base Types -->
      <!-- = = = = = = -->

      <Entity>
       <id type="EntityIdBase" />
       <name type="string" />
       <parent type="EntityIdBase" />
      </Entity>

      <PlacedEntity extends="Entity">
       <placement type="Placement" flags="required" />
      </PlacedEntity>



What MGen is and is not
----
MGen serializers and utilities are designed to be state-less. There is no common object graph preservation or node-to-node implementation with synchronization. This is a conscious design choice and also implies that the standard implementation of serializers do not support circular references. MGen serializers consider all data to be just that - data. MGen serializers have no concept of references or object identities (Although generated polymorphic code and data types in most languages are of reference types - during serialization they are treated as nothing more than data containers).

However, what this does is it gives us the advantage of supporting lossy and reordering protocols, priority based messaging etc without worrying about objects having all the necessary information to be reconstructed on receiving side. If you wish to send one message over http, another over a UDP socket and a third with smoke signals, in the opposite order, MGen won't really care.

We believe supporting identities and true object graphs should be the responsibility of the layer above, the layer syncronizing applications with with each other - not the data layer. We do have ideas to build such systems (ESBs, ORMs and common object graphs among application nodes and the like) on that support the MGen data model - but not as a part of MGen.

In short - we chose to separate the concerns of data representation from data identity and transport method - the two latter not being anything MGen is concerned with at this point, however not anything MGen will prevent either - You could implement your own object identity and graph system on top of types generated by MGen (Something we already did for our visual data model editor).


License
----
Dual-Licensed under:
 * GPL v2
 * Commercial license (will be available after initial release)

Version
----

Development is slightly passed half-way to the first beta release, but at this point we're not putting any version numbers on it yet. So you could call it Alpha v0.x (pre-release experimental code).


Help
----
Build Requirements:
  * Java >= 1.6
  * CMAKE >= 2.10
  * g++/MinGW >= 4. Visual studio support coming! The library already works with VS, but currently not building the tests
  * make (use gnuwin32 on windows)
  * SBT >= 1.3.5 (Use the installer from http://www.scala-sbt.org/download.html)

Build Instructions:
  * clone the repo: git clone git@github.com:/culvertsoft/mgen mgen
  * cd mgen
  * make all

Target dirs:
  * mgen/mgen-...../target
 

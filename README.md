# MGen

-- HEADS UP: This README is Work-In-Progress. It's about 45% finished! -- 

MGen is a toolkit for designing cross-langauge data models to which functionality such as serialization and type introspection can easily be attached. MGen models are defined in an [Interface Definition Language (IDL)](http://en.wikipedia.org/wiki/Interface_description_language "IDL on Wikipedia"), from which source code (classes) in multiple programming languages may be generated using the MGen Compiler.

The goal of MGen is to simplify type-safe sharing of state between applications, while permitting significant data model changes without requiring all participants to rebuild their software. We seek to be highly customizable by having plug-in based architecture for extending the MGen toolkit without having to recompile the MGen tools and libraries.

MGen is inspired by tools such as [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/), [Flat Buffers](http://google.github.io/flatbuffers/), [Cap'n Proto](http://kentonv.github.io/capnproto/), [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding). 


Check out [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).

## Table of Contents

* [Basic Usage](#basic-usage)
  * [Defining a data model](#defining-a-data-model)
  * [Generating source code](#generating-source-code)
  * [Using generated code](#using-generated-code)
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
* [Under the Hood](#under-the-hood)
  * [Components](#components)
    * [The MGen API](#the-mgen-api)
    * [The MGen Compiler](#the-mgen-compiler)
    * [The MGen Code Generators](#the-mgen-code-generators)
    * [The MGen Runtime Libraries](#the-mgen-runtime-libraries)
    * [The MGen Visual Designer](#the-mgen-visual-designer)
  * [Wire Formats](#Wire-formats)
    * [Type IDs](#type-ids)
    * [The MGen binary format](#the-mgen-api)
    * [The MGen json format](#the-mgen-compiler)
  * [Performance](#performance)
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
* [Future Plans](#future-plans)
  * [RPC Interfaces](#rpc-interfaces)
  * [Transport Layers](#transport-layers)
* [License](#license)
* [Final Words](#final-words)

## Basic Usage

MGen's basic use case is defining a data model, generating source code and providing serializers and deserializers.


### Defining a data model

Data models are defined using the MGen IDL. You can write them in a text editor or use the MGen Visual Designer. Below is an example of how a type definition may look:
    
    <Apple>
      <size type="int32"/>
      <brand type="string"/>
    </Apple>

Compiling the type above with the MGen compiler will produce source code for a class called Apple with two member variables: _size_ (a 32 bit signed integer) and _brand_ (a string). To do this we must save the above type definition in a module file. A module file is simply an xml file with a sequence of type definitions. For example, we could create a file called _se.culvertsoft.mymodule.xml_ with the following contents:

    <Module>
      <Types>
        
        <Apple>
          <size type="int32"/>
          <brand type="string"/>
        </Apple>
        
        <Store>
          <stock type="list[Apple]"/>
          <price type="int32"/>
          <name type="string"/>
        </Store>
        
      </Types>
    </Module>

Before passing this file to the MGen compiler, we probably want to specify which code generators to run, and if there is more than one module to generate code for. We do this by creating a project file. Here is an example of what a project file may look like:
    
    <Project>
    
      <Generator name="Java">
        <generator_class_path>se.culvertsoft.mgen.javapack.generator.JavaGenerator</generator_class_path>
        <output_path>src_generated/main/java</output_path>
        <classregistry_path>se.culvertsoft.mymodule</classregistry_path>
      </Generator>
      
      <Depend>../models/libX/libX.xml</Depend>
      
      <Module>se.culvertsoft.mymodule.xml</Module>
      <Module>se.culvertsoft.mymodule2.xml</Module>
      
    </Project>
    
Here we have specified one code generator, one project dependency and two modules:
* Generator
  * generator_class_path
    * The class path of the source code generator (Here the default MGen java generator).
  * output_path
    * specifies the output folder where generated code will be placed
  * classregistry_path
    * The namespace/package where the genrated class registry will be placed
  * name
    * Simply a name identifier for debugging purposes. Can be anything.
* Depend
  * Specifies another project file to depend on
* Module
  * Specifies a module file to include in this project

The file structure of the above project will look something like:
* ..../se.culvertsoft.mymodule.xml (the module file for se.culvertsoft.mymodule)
* ..../se.culvertsoft.mymodule2.xml (the module file for se.culvertsoft.mymodule2)
* ..../myproject.xml (the project file)
* ..../../models/libX/libX.xml (The other project we depend on)

In the next section we will explain how to run the MGen compiler.

### Generating source code

The MGen Compiler is an executable java JAR. You run it by typing:

_java -jar MGenCompiler.jar -project="MyProject.xml" -plugin_paths="generators/"_

Here we run the compiler with two arguments, project and plugin_paths. 
 - project: which project file to load (see the previous section to learn more about project files).
 - plugin_paths: where the compiler should search for java JAR files containing IDL parsers and code generators.

Example: Here is how we generate [one of the data models for testing MGen](https://github.com/culvertsoft/mgen/tree/master/mgen-compiler/src/test/resources):

    java -jar ../mgen-compiler/target/mgen-compiler-assembly-SNAPSHOT.jar -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-cppgenerator/target/"
                                           
    ***************************************
    **                                   **
    **                                   **
    **        MGen Compiler v0.x         **
    **                                   **
    ***************************************
                                           
    Parsing command line args...ok
      project: ../mgen-compiler/src/test/resources/project.xml
      plugin_paths: ../mgen-cppgenerator/target/

    Detecting available plugins
      --> detected available parsers: se.culvertsoft.mgen.compiler.defaultparser.DefaultParser
      --> detected available generators: se.culvertsoft.mgen.cpppack.generator.CppGenerator

    INFO: Using default parser 'se.culvertsoft.mgen.compiler.defaultparser.DefaultParser' (No -parser specified)
    
    Instantiating parser...ok

    Executing parser...
    parsing project: /home/johan/git/mgen/mgen-compiler/src/test/resources/project.xml
    parsing project: /home/johan/git/mgen/mgen-compiler/src/test/resources/dependencies/project.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule1.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule2.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule3.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/gameworld.types.basemodule1.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/gameworld.types.basemodule2.xml
    parsing module: /home/johan/git/mgen/mgen-compiler/src/test/resources/gameworld.types.basemodule3.xml
    ok

    WARNING: Could not find specified generator 'se.culvertsoft.mgen.javapack.generator.JavaGenerator', skipping
    Created generator: se.culvertsoft.mgen.cpppack.generator.CppGenerator

    Generating code...ok

    Writing files to disk:
      writing: src_generated/test/cpp/gameworld/types/basemodule1/VectorR3.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/VectorR3.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Matrix4x4d.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Matrix4x4d.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Positioning.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Positioning.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Entity.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Entity.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Creature.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Creature.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Vehicle.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Vehicle.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Car.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Car.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Item.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/Item.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/EntityHolder.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/EntityHolder.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/GarageViewer.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/GarageViewer.cpp
      writing: src_generated/test/cpp/gameworld/types/basemodule1/World.h
      writing: src_generated/test/cpp/gameworld/types/basemodule1/World.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepVectorR3.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepVectorR3.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepPositioning.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepPositioning.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepEntity.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepEntity.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepCreature.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepCreature.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepVehicle.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepVehicle.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepCar.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepCar.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepItem.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepItem.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepEntityHolder.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepEntityHolder.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepGarageViewer.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepGarageViewer.cpp
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepWorld.h
      writing: src_generated/test/cpp/gameworld/dependency/depmodule1/DepWorld.cpp
      writing: src_generated/test/cpp/gameworld/types/ClassRegistry.h
      writing: src_generated/test/cpp/gameworld/types/ClassRegistry.cpp
      writing: src_generated/test/cpp/gameworld/types/Dispatcher.h
      writing: src_generated/test/cpp/gameworld/types/Dispatcher.cpp
      writing: src_generated/test/cpp/gameworld/types/Handler.h
      writing: src_generated/test/cpp/gameworld/types/Handler.cpp
      writing: src_generated/test/cpp/gameworld/types/ForwardDeclare.h


### Using generated code

Read on below, or check some of our tests at:
 - C++: https://github.com/culvertsoft/mgen/tree/master/mgen-cpplib/src/test/cpp/src/tests
 - Scala: https://github.com/culvertsoft/mgen/tree/master/mgen-javalib/src/test/scala/se/culvertsoft/mgen/javapack/test
 - Java: See scala examples (the generated code is pure java, but we wrote our tests in scala as it was easier)
 - Javascript: coming soon!
 - For advanced tests (with automatic data generation), [click here](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests).

To access the generated types we include the headers of the types we need, or the generated ClassRegistry.h header file which will let us access all the types that were just generated. We will also include some mgen headers for serialization.

    #include "mgen/serialization/VectorInputStream.h"
    #include "mgen/serialization/VectorOutputStream.h"
    #include "mgen/serialization/JsonWriter.h"
    #include "mgen/serialization/JsonReader.h"
    
    #include "gameworld/types/ClassRegistry.h"
    
    using mgen::VectorOutputStream;
    using mgen::VectorInputStream;
    using mgen::JsonWriter;
    using mgen::JsonReader;
    using mgen::VectorOutputStream;
    using gameworld::types::ClassRegistry;
    using namespace gameworld::types::basemodule1;
    
Let us then create some objects from the generated classes and set some properties on these.

    int main() {
      Car car1, car2, car3;
      car1.setBrand("Ford");
      car2.setTopSpeed(123);
      
Now let us try to serialize these cars to JSON. This is how we do it:

      // First we create a class registry.
      ClassRegistry classRegistry;
      
      // We will serialize our objects to this std::vector of bytes
      std::vector<char> buffer;
      
      // Create an OutputStream object around the vector (MGen readers and writers accept only streams)
      VectorOutputStream out(buffer);
      
      // Now create our serializer
      JsonWriter<VectorOutputStream, ClassRegistry> writer(out, classRegistry);
      
      // Write the objects
      writer.writeObject(car1);
      writer.writeObject(car2);
      writer.writeObject(car3);
      
Now we can read these objects back from the buffer in the following manner:

      // Create an InputStream object from the data source
      VectorInputStream in(buffer);
      
      // Create our deserializer
      JsonReader<VectorInputStream, ClassRegistry> reader(in, classRegistry);
     
      // Read back the objects. 
      // Note that here the root objects read back are placed on the free store, 
      // so they eventually need to be manually deleted. 
      MGenBase * obj1 = reader.readObject();
      MGenBase * obj2 = reader.readObject();
      MGenBase * obj3 = reader.readObject();
      
      std::vector<MGenBase*> objects;
      objects.push_back(obj1);
      objects.push_back(obj2);
      objects.push_back(obj3);
      
      // What we could do now is dynamic_cast to identify the types, or use one 
      // of the type ids supplied for generated classes:
      // Here using Car::_type_id, which is a 64bit hash code calculated from 'name.space.Type'.
      for (MGenBase obj * : objects) {
        switch(obj->_typeId()) {
          case Car::_type_id:
            Car * car = static_cast<Car*>(obj);
            std::cout << "Yay we got a car!" << std::endl;
            break;
          default:
            std::cout << "Oops, how did this happen?" << std::endl;
            break;
        }
      }
      
      // Other ways of reading objects when the type is known are:
      // Car * car1 = reader.readObject<Car>(); // Polymorphic read (-> heap)
      // Car car2 = reader.readStatic<Car>(); // Nonpolymorphic read (-> stack)
      
      // Ideally we'd wrap it all in a try-catch block to avoid leaking any exceptions unless we want to
      // try {
      //   reader->readObject();
      // } catch (const mgen::Exception& exception) {
      // // do something. mgen::Exception extends std::runtime_error
      // }

Additionally, MGen supports reading and writing data types from external non-MGen sources, such as json APIs to existing services, and can easily be further extended to custom binary and text formats by implementing new or extending existing Reader and Writer classes - Entirely separate of generated source code through a simple visitor interface (for example, see our [C++ JsonWriter](https://github.com/culvertsoft/mgen/blob/master/mgen-cpplib/src/main/cpp/mgen/serialization/JsonWriterBase.h)).


## Download links

The following are links where you can download MGen pre-built. You will notice that the compiler and visual designer also have variants named _assembly_.

 * JAR files marked _assembly_ are standalone executables. They are packaged with all library dependencies included. 
 * JAR files intended as programming libraries are NOT marked _assembly_.

### Stable

Represents the latest non-beta release. As we're currently in alpha phase, there are yet no entries here.

### Nightly 

Represents the latest built development version, built once per night. You can download it from http://nightly.culvertsoft.se

### Snapshot

Represents the latest built development version, built after each git push. You can download it from http://snapshot.culvertsoft.se

### Maven

We plan on making the MGen tools and libraries available on Maven Central or a similar repository. However at this early stage you will need to download jar files manually or clone this repository and build from source.

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

If you want to build MGen from source, see [Building MGen](#building-mgen) for build requirements.


### Installing the compiler

The MGen Compiler is just an executable java JAR file. Check the [downloads section](#download-links) and download a release of the version you want. You could for example try out [the latest build compiler](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-compiler/mgen-compiler-assembly-SNAPSHOT.jar).

There is no installation required. Just put it where you like and follow the instructions in the sections above on how to use it. Rename it as you see fit.

See [Generating source code](#generating-source-code) for how to use it.


### Installing the Java runtime libraries

The [java runtime libraries](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-javalib/) are compiled to a java jar file. This file depends on [the mgen API](http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-api/) (also a jar file). Both can be downloaded from the [downloads section](#download-links). You will also need [json-simple](https://code.google.com/p/json-simple/).

Add the jar files to your build and you should be good to go. If you want to include the runtime libries by source instead or build them yourself, see [Building MGen](#building-mgen).


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
 * Customizable generators (generated code contents and functionality)
 * Customizable parsers
 * Plug-in architecture for loading custom parsers and generators
 * Ability to extend to new languages

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

So far we've only performed some very basic performance tests, but results are promising. We have measured binary serialization performance of polymorphic objects in C++ on a single ivy bridge i7 core to more than 1,5 Gbit/s (g++ 4.8.1 o3). Performance in the other direction was roughly 30% less. Performance is important to us, however it should be made clear that performance is NOT the primary focus of MGen.


## Building MGen

If you're not satisfied with downloading pre-built libraries (see [the downloads section](#download-links)), this section will explain how you build MGen from source.

Build Requirements:
  * Java JDK >= 1.7
  * CMAKE >= 2.8
  * MSVC>=2005/g++,mingw>=4/Clang. (But building the tests is currently not possible on MSVC)
  * make (on windows: use gnuwin32 or cygwin)
  * SBT >= 1.3.5 (Use the installer from http://www.scala-sbt.org/download.html)

Build Instructions:
  * clone the repo
  * make

Output will be placed inside each mgen-component's target/ directory (e.g. mgen-api/target/).
 

## Version History

Development is slightly passed half-way to the first beta release, but at this point we're not putting any version numbers on it yet. So you could call it Alpha v0.x (pre-release experimental code).


## License

Dual-Licensed under:
 * GPL v2
 * Commercial license (will be available after initial release)



## Final Words

MGen serializers and utilities are designed to be state-less. There is no common object graph preservation or node-to-node implementation with synchronization. This is a conscious design choice and also implies that the standard implementation of serializers do not support circular references. MGen serializers consider all data to be just that - data. MGen serializers have no concept of references or object identities (Although generated polymorphic code and data types in most languages are of reference types - during serialization they are treated as nothing more than data containers).

However, what this does is it gives us the advantage of supporting lossy and reordering protocols, priority based messaging etc without worrying about objects having all the necessary information to be reconstructed on receiving side. If you wish to send one message over http, another over a UDP socket and a third with smoke signals, in the opposite order, MGen won't really care.

We believe supporting identities and true object graphs should be the responsibility of the layer above, the layer syncronizing applications with with each other - not the data layer. We do have ideas to build such systems (ESBs, ORMs and common object graphs among application nodes and the like) on that support the MGen data model - but not as a part of MGen.

In short - we chose to separate the concerns of data representation from data identity and transport method - the two latter not being anything MGen is concerned with at this point, however not anything MGen will prevent either - You could implement your own object identity and graph system on top of types generated by MGen (Something we already did for our visual data model editor).

# MGen

-- HEADS UP: This README is Work-In-Progress. It's about 35% finished! -- 

MGen is a toolkit for designing cross-language data models, data mappings and serialization. MGen models are defined in a flexible [Interface Definition Language (IDL)](http://en.wikipedia.org/wiki/Interface_description_language "IDL on Wikipedia"), from which source code in multiple languages may be generated using the MGen Compiler. The MGen Runtime Libraries then provide functions such as serialization, deep copying, equality testing and type identification. 

The goal of MGen is to to simplify cross-language collaboration between software components and software teams that wish to share common data structures, allowing data model modifications without forcing everyone to adapt or rebuild their software.

MGen aims to be as flexible as possible, and provide ways for adding new code generation and library features to fit different applications. These additions can be made without recompiling the MGen tools or libraries through a plug-in based architecture. Modular components include IDL parsers, source code generators and runtime libraries.

MGen is inspired by several existing tools, such as [Protocol Buffers](https://code.google.com/p/protobuf/ "sometimes called protobuf"), [Thrift](http://thrift.apache.org/), [Avro](http://avro.apache.org/), [ICE](http://www.zeroc.com/ice.html "Internet Communications Engine"), [HLA](http://en.wikipedia.org/wiki/High-level_architecture_(simulation) "High level architecture"), [WtDbo](http://www.webtoolkit.eu/wt/). 

## Table of Contents

* [Basic Usage](#basic-usage)
  * [Defining a data model](#defining-a-data-model)
  * [Generating source code](#generating-source-code)
  * [Using generated code](#using-generated-code)
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
    * [The MGen Compiler](#the-mgen-compiler)
    * [The MGen Code Generators](#the-mgen-code-generators)
    * [The MGen Runtime Libraries](#the-mgen-runtime-libraries)
    * [The MGen Visual Designer](#the-mgen-visual-designer)
  * [Wire Formats](#Wire-formats)
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

Compiling the type above with the MGen compiler will produce source code for a class called Apple with two member variables: _size_ (a 32 bit signed integer) and _brand_ (a string). To do this we must save it in a module file. A module file is simply an xml file with a sequence of type definitions. For example, we could create a file called _se.culvertsoft.mymodule.xml_ with the following contents:

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

We probably want to specify which code generators to run, and if there is more than one module to generate code for. We do this by creating a project file. Here is an example of what a project file may look like:
    
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
    
Here we have specified:
* Generator
  * Defines the settings to use for source code generation.
    * generator_class_path
      * The class path of the source code generator (In this case the default MGen java generator).
    * output_path
      * specifies the output folder where generated code will be placed
    * classregistry_path
      * The namespace/package where the genrated class registry will be placed (see the next section for information about class registries)
    * name
      * Simply a name identifier for debugging purposes. Can be anything.
* Depend
  * Specifies another project file to depend on
* Module
  * Specifies a module file to include in this project

To summarize:
 * Model definition
   * Types -> classes
   * Fields -> members
   * Modules -> namespaces/packages
 * Source code generation requires
   * Projects files -> specify which modules to generate code for
   * Module files -> define the data model

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

Example: Here is how we generate a data model for testing the MGen Compiler:

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
    parsing project: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/project.xml
    parsing project: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/dependencies/project.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule1.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule2.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/dependencies/gameworld.dependency.depmodule3.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/gameworld.types.basemodule1.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/gameworld.types.basemodule2.xml
    parsing module: /var/lib/jenkins/jobs/MGen-snapshot/workspace/mgen-compiler/src/test/resources/gameworld.types.basemodule3.xml
    ok

    WARNING: Could not find specified generator 'se.culvertsoft.mgen.javapack.generator.JavaGenerator', skipping
    Created generator: se.culvertsoft.mgen.cpppack.generator.CppGenerator

    Generating code...ok

    Writing files to disk:
      target/src_generated/gameworld/types/basemodule1/VectorR3.h
      target/src_generated/gameworld/types/basemodule1/VectorR3.cpp
      target/src_generated/gameworld/types/basemodule1/Matrix4x4d.h
      target/src_generated/gameworld/types/basemodule1/Matrix4x4d.cpp
      target/src_generated/gameworld/types/basemodule1/Positioning.h
      target/src_generated/gameworld/types/basemodule1/Positioning.cpp
      target/src_generated/gameworld/types/basemodule1/Entity.h
      target/src_generated/gameworld/types/basemodule1/Entity.cpp
      target/src_generated/gameworld/types/basemodule1/Creature.h
      target/src_generated/gameworld/types/basemodule1/Creature.cpp
      target/src_generated/gameworld/types/basemodule1/Vehicle.h
      target/src_generated/gameworld/types/basemodule1/Vehicle.cpp
      target/src_generated/gameworld/types/basemodule1/Car.h
      target/src_generated/gameworld/types/basemodule1/Car.cpp
      target/src_generated/gameworld/types/basemodule1/Item.h
      target/src_generated/gameworld/types/basemodule1/Item.cpp
      target/src_generated/gameworld/types/basemodule1/EntityHolder.h
      target/src_generated/gameworld/types/basemodule1/EntityHolder.cpp
      target/src_generated/gameworld/types/basemodule1/GarageViewer.h
      target/src_generated/gameworld/types/basemodule1/GarageViewer.cpp
      target/src_generated/gameworld/types/basemodule1/World.h
      target/src_generated/gameworld/types/basemodule1/World.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepVectorR3.h
      target/src_generated/gameworld/dependency/depmodule1/DepVectorR3.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepPositioning.h
      target/src_generated/gameworld/dependency/depmodule1/DepPositioning.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepEntity.h
      target/src_generated/gameworld/dependency/depmodule1/DepEntity.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepCreature.h
      target/src_generated/gameworld/dependency/depmodule1/DepCreature.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepVehicle.h
      target/src_generated/gameworld/dependency/depmodule1/DepVehicle.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepCar.h
      target/src_generated/gameworld/dependency/depmodule1/DepCar.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepItem.h
      target/src_generated/gameworld/dependency/depmodule1/DepItem.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepEntityHolder.h
      target/src_generated/gameworld/dependency/depmodule1/DepEntityHolder.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepGarageViewer.h
      target/src_generated/gameworld/dependency/depmodule1/DepGarageViewer.cpp
      target/src_generated/gameworld/dependency/depmodule1/DepWorld.h
      target/src_generated/gameworld/dependency/depmodule1/DepWorld.cpp
      target/src_generated/gameworld/types/ClassRegistry.h
      target/src_generated/gameworld/types/ClassRegistry.cpp


### Using generated code

To access the generated types we include the headers of the types we need, or the generated ClassRegistry.h header file which will let us access all the types that were just generated. We will also include some mgen headers for serialization.

    #include "mgen/serialization/VectorInputStream.h"
    #include "mgen/serialization/VectorOutputStream.h"
    #include "mgen/serialization/JSONWriter.h"
    #include "mgen/serialization/JSONReader.h"
    
    #include "gameworld/types/ClassRegistry.h"
    
    using mgen::VectorOutputStream;
    using mgen::VectorInputStream;
    using mgen::JSONWriter;
    using mgen::JSONReader;
    using mgen::VectorOutputStream;
    using gameworld::types::ClassRegistry;
    using namespace gameworld::types::basemodule1;
    
Let us then create some objects from the generated classes and set some properties on these.

    int main() {
      Car car1, car2, car3;
      car1.setBrand("Ford");
      car2.setTopSpeed(123);
      car3.getPositioningMutable().setPosition(VectorR3(3,2,1));
      
Now let us try to serialize these cars to JSON. This is how we do it:

      // First we create the class registry. For the curious, it handles dynamic method dispatch to visitor methods with template arguments (the serializer being the template argument).
      ClassRegistry classRegistry;
      
      // We will serialize our objects to this std::vector of bytes
      std::vector<char> buffer;
      
      // Create an OutputStream object around the vector (MGen readers and writers accept only streams)
      VectorOutputStream out(buffer);
      
      // Now create our serializer
      JSONWriter<VectorOutputStream, ClassRegistry> * writer = mgen::new_JSONWriter(out, classRegistry);
      
      // Write the objects
      writer->writeMgenObject(car1);
      writer->writeMgenObject(car2);
      writer->writeMgenObject(car3);
      
Now we can read these objects back from the stream in the following manner:

      // Create an InputStream object from the data source
      VectorInputStream in(buffer);
      
      // Create our deserializer
      JSONReader<VectorInputStream, ClassRegistry> * reader = mgen::new_JSONReader(in, classRegistry);
     
      // Read back the objects. 
      // Note that here the root objects read back are placed on the free store, 
      // so they eventually need to be manually deleted. 
      MGenBase * obj1 = reader->readMgenObject();
      MGenBase * obj2 = reader->readMgenObject();
      MGenBase * obj3 = reader->readMgenObject();
      
      std::vector<MGenBase*> objects;
      objects.push_back(obj1);
      objects.push_back(obj2);
      objects.push_back(obj3);
      
      // What we could do now is dynamic_cast to identify the types, or use one of the type id methods 
      // that are supplied for all generated classes:
      // Using a bit of c++ 11 here
      for (MGenBase obj * : objects) {
        switch(obj->_typeHash32bit()) {
          case Car::_TYPE_HASH_32BIT:
            Car * car = reinterpret_cast<Car*>(obj);
            std::cout << "Yay we got a car!" << std::endl;
            break;
          default:
            std::cout << "Oops, how did this happen?" << std::endl;
            break;
        }
      }
      
      // Or we could just c style cast it for simplicity
      Car * car1 = (Car*)obj1;
      Car * car2 = (Car*)obj2;
      Car * car3 = (Car*)obj3;
      
      // Ideally we'd wrap it all in a try-catch block to avoid leaking any exceptions unless we want to
      // try {
      //   reader->readMgenObject();
      // } catch (const mgen::Exception& exception) {
      // // do something. mgen::Exception extends std::runtime_error
      // }

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

You can of course modify, extend and/or replace these yourself.

By default generated classes are java-beans and C++ data containers with getters and setters or class-like structures in the case of JavaScript. 
The runtime libraries provide two default serializers:
 * The MGen Binary format Serializer
 * The MGen JSON format Serializer

These produce two things:
 * Classes (or class-like structures in JavaScript)
 * ClassRegistries (registry for all generated classes)

ClassRegistries are used to dynamically instantiate and identify data types over data streams during deserialization.
In some cases they may also be used during serialization. Some language implementations rely on ClassRegistries more than others. One could argue that Java for example  does not really need one (because of reflection), however for performance reasons and API uniformity, they are created for Java as well.

One feature that ClassRegistries provide in C++ is the ability to call template methods with dynamic dispatch. This means you for example can use the ClassRegistry to access a derived visitor method through a call on the base type with a templated argument, such as a custom serializer, and the method will be called with full type preservation on the most derived class. What this type preservation also means is that during (de)serialization, once the serializer has been dispatched to the visitor method of the object it should serialize (which isn't much more than a switch-table) there's not much need for virtual calls - which can substantially improve performance.

MGen does not rely on Java reflection or C++ RTTI.

We plan to add support for LUA and Python.


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

You can of course modify, extend and/or replace these yourself.


#### The MGen Visual Designer

If you prefer graphical tools instead of command line, we are also working on a visual data model editor.


### Performance

We have measured binary serialization performances of some polymorphic objects in C++ on a single ivy bridge i7 core to more than 1,5 Gbit/s (g++ 4.8.1 o3). Performance in the other direction was roughly 30% less. Performance is important to us, however it should be made clear that performance is NOT the primary focus of MGen.



## Building MGen

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

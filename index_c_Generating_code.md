---
layout: default
link-title: Generating code
submenu:
  - { anchor: "a", title: "generating code" }
  - { anchor: "b", title: "command line parameters" }
---

## Generating source code (classes) <a name="a">&nbsp;</a>

In order to generate source code/classes for [the data model you defined in the MGen IDL](index_b_Basic_model.html), we use the MGen compiler. Before we run it, it needs to know:

 * Which module files to use
 * What code generators to use
 * Where to place the output

We need an MGen **Project** file. This is an ordinary xml file, so let's create a file called project.xml (the name does not matter) with the following contents:


    <Project>

      <Sources>
        <Source>com.fruitcompany.fruits.xml</Source>
      </Sources>

      <Generator name="Java">
        <output_path>src/main/java</output_path>
        <classregistry_path>com.fruitcompany</classregistry_path>
        <class_path>se.culvertsoft.mgen.javapack.generator.JavaGenerator</class_path>
      </Generator>

      <Generator name="C++">
        <output_path>src/main/cpp</output_path>
        <classregistry_path>com.fruitcompany</classregistry_path>
        <generate_unity_build>true</generate_unity_build>
        <class_path>se.culvertsoft.mgen.cpppack.generator.CppGenerator</class_path>
      </Generator>

    </Project>


Lastly we call the MGen compiler:

    $..> mgen project.xml


        *********************************************************************
        **                                                                 **
        **                                                                 **
        **        MGen Compiler (SNAPSHOT 2014-08-31 16:58:56 +0200)       **
        **                                                                 **
        *********************************************************************

    Parsing command line args...ok

    Parsing project...
      parsing project: C:\Users\GiGurra\Dropbox\CulvertSoft\exampleModels\gh-pages-example1\project.xml
      parsing module: C:\Users\GiGurra\Dropbox\CulvertSoft\exampleModels\gh-pages-example1\com.fruitcompany.fruits.xml
    ok

    Linking types...ok

    Checking for type conflicts...ok

    Instantiating generators...
    Created generator: se.culvertsoft.mgen.javapack.generator.JavaGenerator
    Created generator: se.culvertsoft.mgen.cpppack.generator.CppGenerator

    Generating code...ok

    Writing files to disk:
      writing: src/main/java/com/fruitcompany/fruits\Brand.java
      writing: src/main/java/com/fruitcompany/fruits\Fruit.java
      writing: src/main/java/com/fruitcompany/fruits\Apple.java
      writing: src/main/java/com/fruitcompany/fruits\Banana.java
      writing: src/main/java/com/fruitcompany\ClassRegistry.java
      writing: src/main/java/com/fruitcompany\Dispatcher.java
      writing: src/main/java/com/fruitcompany\Handler.java
      writing: src/main/cpp/com/fruitcompany/fruits\Brand.h
      writing: src/main/cpp/com/fruitcompany/fruits\Brand.cpp
      writing: src/main/cpp/com/fruitcompany/fruits\Fruit.h
      writing: src/main/cpp/com/fruitcompany/fruits\Fruit.cpp
      writing: src/main/cpp/com/fruitcompany/fruits\Apple.h
      writing: src/main/cpp/com/fruitcompany/fruits\Apple.cpp
      writing: src/main/cpp/com/fruitcompany/fruits\Banana.h
      writing: src/main/cpp/com/fruitcompany/fruits\Banana.cpp
      writing: src/main/cpp/com/fruitcompany\ClassRegistry.h
      writing: src/main/cpp/com/fruitcompany\ClassRegistry.cpp
      writing: src/main/cpp/com/fruitcompany\Dispatcher.h
      writing: src/main/cpp/com/fruitcompany\Dispatcher.cpp
      writing: src/main/cpp/com/fruitcompany\Handler.h
      writing: src/main/cpp/com/fruitcompany\Handler.cpp
      writing: src/main/cpp/com/fruitcompany\ForwardDeclare.h

    *** COMPILATION SUCCESS ***


### Supported command line parameters <a name="b">&nbsp;</a>

Calling the compiler without arguments will show you the command line options it supports:

    $..> mgen

        *********************************************************************
        **                                                                 **
        **                                                                 **
        **        MGen Compiler (SNAPSHOT 2014-08-31 16:58:56 +0200)       **
        **                                                                 **
        *********************************************************************

    Valid MGen compiler arguments are:
      -help: displays this help
      -project="myProjectFile.xml": specify project file (Required)
      -plugin_paths="my/external/path1, my/external/path2": specify additional plugin paths (Optional)
      -output_path="specify output path (Optional)
      -fail_on_missing_generator=true/false: Default false (Optional)
      -check_conflicts="true/false" (default=true): If false: the compiler will ignore any type name/id/hash conflicts (Opti
onal). Useful for IDL<->IDL translation
      -include_paths="/home/me,/home/you": The paths to search for files

    Environmental variables can also be used:
      MGEN_PLUGIN_PATHS: See command line argument 'plugin_paths'
      MGEN_INCLUDE_PATHS: See command line argument 'include_paths'
      MGEN_INSTALL_PATH: if set, ${MGEN_INSTALL_PATH}/jars will be searched for plugins


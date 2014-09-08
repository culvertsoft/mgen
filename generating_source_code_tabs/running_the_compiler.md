---
---

To run the MGen compiler, we simply call 'mgen' from a shell and pass the project file name as the first parameter:

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



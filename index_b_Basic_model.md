---
layout: default
link-title: Defining a model
---

## Defining a data model

The simplest use case for MGen is defining a data model, generating source code and providing cross-language serialization functionality. The data model in this example is defined in the MGen IDL - MGen can be configured to use other IDLs, or combine multiple IDLs, but we consider those to be advanced subjects.

Types/classes are defined within modules. A module corresponds to a c++ namespace or java package. A module is defined in a module file, where the name of the module file determines the **module path** (java package name, or c++ namespace).

A file named **com.fruitcompany.fruits.xml**, with the contents:

    <Module>

      <Enums>
        <Brand><A/></B/><C/></Brand>
      </Enums>

      <Types>

        <Fruit>
          <brand type="Brand"/>
        </Fruit>

        <Apple extends="Fruit">
          <radius type="float64"/>
        </Apple>

        <Banana extends="Fruit">
          <length type="float64"/>
        </Banana>

      </Types>

    </Module>

Defines a module with:

 * Brand - an enum
 * Fruit - a class
 * Apple - a sub class of Fruit
 * Banana - a sub class of Fruit

This is all we need to define a complete data model in the c++ namespace "com::fruitcompany::fruits" and/or java package com.fruitcompany.fruits. The order of the definitions does not matter.

---
---

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




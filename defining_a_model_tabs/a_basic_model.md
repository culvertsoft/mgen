---
---

The MGen compiler expects you to define your classes in what we call modules. A module corresponds to a c++ namespace or java package, and is defined within a module file. The name of the module file determines the **module path** (i.e. the java package or c++ namespace).

A file named **com.fruitcompany.fruits.xml**, with the contents:

{% highlight xml %}

<Module>

  <Enums>
    <Brand><A/><B/><C/></Brand>
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

{% endhighlight %}

Defines a module with:

 * Brand - an enum
 * Fruit - a class
 * Apple - a sub class of Fruit
 * Banana - a sub class of Fruit

This is all we need to define a complete data model in the c++ namespace "com::fruitcompany::fruits" and/or java package com.fruitcompany.fruits. The order of the definitions does not matter.




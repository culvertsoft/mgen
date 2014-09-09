---
---

As mentioned in <a target-tab="custom-wire-formats" class="active" href="index_l_Advanced_use.html#a">the section on custom serializers</a>, all generated classes get generic visitor methods. You can use any visitor type that adheres to the visitor api of MGen classes. In java this is just a normal java interface, while in c++ any duck typed template type will work (Side note: it turns out that double dynamic dispatch of template parameters can be implemented in c++ if you generate code for your own method dispatch tables - which the MGen compiler does).

As we've seen before this can be used to serialize and deserialize objects, but it can also be used for other purposes. We can use it for: 

 * Generic object analysis
 * Generic object stringification
 * Type meta data generation
 * Generic object randomization
 * Generic mapping of objects to UI elements
 * and more ..

In fact for our cross-language integration tests, we make extensive use of this functionality to simplify creating test data. Using the generic visitor methods, we can very easily (roughly 150 lines of code, including empty lines) write a class that will generically visit and randomize objects of any set of classes ([source link](https://github.com/culvertsoft/mgen/blob/master/mgen-integrationtests/build/common/MGenObjectRandomizer.h)). Therefore it's very easy for us to generate test data to for our tests.

We are certain that there are lots of other applications for this API that we haven't thought about yet.


---
---

The MGen IDL is the language we created for specifying data models in. It's written in XML, and supports:

 * Classes with single inheritance
 * Fixed point numeric types: int8, int16, int32, int64
 * Floating point numeric types: float32, float64
 * Strings
 * maps: [*numeric/string* -> any]
 * lists: [any]
 * arrays: [any]
 * enumerations
 * Class members of any type of the types above (and classes)
 * Class members that are polymorphic
 * Class member default values
 * Class member flags (e.g. required, polymorphic, transient etc.)
 * Class constants
 * Heterogenous containers (through polymorphism)

For more information of what features are supported in the MGen IDL, see [our preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).

Also have a look at some of the test models for MGen we have over at github:

 * [https://github.com/culvertsoft/mgen/tree/master/mgen-compiler/src/test/resources](https://github.com/culvertsoft/mgen/tree/master/mgen-compiler/src/test/resources)
 * [https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/depends](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/depends)
 * [https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/read](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/read)
 * [https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/write](https://github.com/culvertsoft/mgen/tree/master/mgen-integrationtests/models/write)



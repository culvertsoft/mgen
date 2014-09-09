---
---

The MGen compiler was built with extendability in mind. The compiler itself is completely agnostic to the IDL source code it uses as input and the actual programming language source code/classes it produces as output. What connects the IDL source code to the generated programming language code is MGen's internal model, which is part of what we call the MGen API. The MGen API defines how data models may be constructed using MGen (for details see the mgen-api-javadoc.jar or [the source code at github](https://github.com/culvertsoft/mgen)).

There are two Interface classes in the MGen API that facilitate this architecture. 

 * [The Parser class](https://github.com/culvertsoft/mgen/blob/master/mgen-api/src/main/java/se/culvertsoft/mgen/api/plugins/Parser.java)
 * [The Generator class](https://github.com/culvertsoft/mgen/blob/master/mgen-api/src/main/java/se/culvertsoft/mgen/api/plugins/Generator.java)

When [writing an MGen project file](index_c_Generating_code.html), you are telling the compiler what Parser and Generator classes to load. There is a default Parser which is used if you don't specify one (the MGen IDL parser), but you always specify which Generator classes to load by fully qualified java class paths.

When you specify Parser and Generator classes to the compiler in your project file, they are searched on your java class path. If they are not found there, the compiler searches for java jar files containing classes with the correct name and Interface in your plug-in directories. This way you can easily extend existing parsers, generators and add completely new ones without having to recompile any part of the MGen compiler.

The following plug-ins are bundled with MGen:

 * The MGen IDL parser
 * The MGen java generator
 * The MGen c++ generator
 * The MGen javascript generator
 * The MGen IDL generator (yes we can generate our own IDL)


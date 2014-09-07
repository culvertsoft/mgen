---
layout: default
link-title: Advanced use
submenu:
  - { anchor: "a", title: "config files" }
  - { anchor: "b", title: "type identification" }
  - { anchor: "c", title: "editing generated code" }
  - { anchor: "c2", title: "compiler plug-ins" }
  - { anchor: "d", title: "non-mgen ifcs" }
  - { anchor: "e", title: "custom generators" }
  - { anchor: "f", title: "custom idl parsers" }
  - { anchor: "g", title: "custom wire formats" }
  - { anchor: "h", title: "generic visitors" }
  - { anchor: "i", title: "cmd line arg parser" }
---

## Advanced use <a name="defining_a_model">&nbsp;</a>

This page explains some of MGen's advanced use cases.


### Config files <a name="a">&nbsp;</a>

Mapping objects to configuration files is no different from mapping objects to any other data source. MGen supports mapping objects to JSON (and the MGen binary format) out-of-the-box, so JSON is probably the easiest format to use for configuration files. If you have existing configuration files in other formats (e.g. YAML, XML, ..) you can use generic libraries to convert it to JSON, and then pass that to the MGen readers and writers.

In either case, we need a data model that maps the configuration file contents to an MGen object class. Example - Consider a JSON configuration file with the following contents:

    {
      "hostName": "localhost",
      "port": 12345,
      "numThreads": 3,
      "logFile": "/home/logger/app.log"
    }

We could define a class called for example 'AppConfigarion' that maps c++ and java objects to this file using the following MGen IDL code (see the section on [defining a data model](index_b_Basic_model.html)):

    <AppConfigarion>
      <hostName type="string" />
      <port type="int" />
      <numThreads type="int" />
      <logFile type="string" />
    </AppConfigarion>

We can now read and write these files to and from our statically typed generated classes, as shown in the section on [using generated code](index_c_using_gen_code.html). To recap, here's how you would use it in c++:

    // Read the config file to memory (assuming you have such a utility function)
    const std::string cfgFileData = readFile("/home/logger/cfg.json");

    // Recap: The types required for reading an mgen object
    ClassRegistry registry;
    StringInputStream stream(cfgFileData);
    JsonReader<StringInputStream, ClassRegistry> reader(stream, classRegistry);

    // Now map the configuration
    AppConfigarion cfg = reader.readStatic<AppConfigarion>();
    
    // Now we can check what parameters are set
    cfg.hasHostName()
    cfg.hasPort()
    cfg.hasNumThreads()
    cfg.hasLogFile()
    
    // And then get them
    std::string hostName = cfg.getHostName();
    int port = cfg.getPort();
    int numThreads = cfg.getNumThreads();
    std::string logFile = cfg.getLogFile();
    
    // Change the configuration
    cfg.setHostName("remote_host_X");
    
    // And serialize it again to write the changes back to disk
    StringOutputStream outStream;
    JsonPrettyWriter<StringOutputStream, ClassRegistry> writer(outStream, classRegistry);
    writer.writeObject(cfg);
    
    // Get the serialized string
    std::string newCfgFileContents = outStream.str();
    
    // Write it back to disk (assuming you have such a utility function)
    writeToDisk("/home/logger/cfg.json", newCfgFileContents);


Instead of checking if fields are set when reading and writing objects you can flag them as 'required'. If those fields are not set when reading or writing an object an exception will be thrown. Be very careful though with required fields, as they may cause serious incompatibility issues when you want to be backwards compatible with older data models and clients.

There may also be configuration parameters you may only have during runtime of your application, but don't want to store to the configuration files. You can flag those fields as 'transient'. For more information on field flags, again, see the section on [defining a data model](index_b_Basic_model.html).



### Identifying object types  <a name="b">&nbsp;</a>

In all the previous examples, we have always known what class to read. That is often not the case in real applications. In most of our internal use cases we rarely know what kind of object is being read back or sent over the wire, so we need to be able to:

 * Write objects to data streams with *just enough* type metadata
 * Read the object genericly to the correct type without losing any information
 * Identify the type of object we just read back
 * Pass it on to the right handlers

Consider our initial example ([the fruit model](index_b_Basic_model.html)). It has three classes:

 * Fruit (the base class)
 * Apple (a sub class)
 * Banana (another sub class)

Suppose we serialize a couple of objects of randomly chosen classes (Apples or Bananas) to a file. Now we want to read them back and see what it was we wrote down. First of all, let's read the objects back not assuming anything about their types.

In Java (With the reader from [the previous java example](http://culvertsoft.github.io/mgen/index_c_using_gen_code.html#b)):

    MGenBase object = reader.readObject();

And in c++:

    MGenBase * object = reader.readObject(); // read to heap

In Java we can use the following methods to identify the type of the object:

 * Java's built in instanceof operator
 * Java's object.getClass() and map that to generic handlers
 * MGen's 64bit type id by object._typeId() (generated for all MGen classes)
 * MGen's generated Dispatch and Handler types

In c++ we can use:

 * RTTI: dynamic_cast
 * RTTI: typeId(object)
 * MGen's 64bit type id by object->_typeId() (generated for all MGen classes)
 * MGen's generated Dispatch and Handler types

Perhaps the most interesting of these are the Dispatch and Handler types generated by MGen. Handlers are classes with a handle(object) method for every class that you generated code for. Dispatch(ers) are classes or functions that take any MGenBase object reference and a handler, and pass it to the handler's correct handle(object)-method. 

Handlers' handle(object)-methods have default implementations for all types. The default implementations pass along the object to its base class handler, until the MGenBase class is reached. If no handle(object)-method is overloaded there, the object is ignored.

Here's a c++ example of using dispatch and a Handler:

    // First we define a custom handler class
    // that extends the generated Handler class
    class MyHandler: public Handler {
    public:
      void handle(Apple& apple) {
        std::cout << "That was an apple!" << std::endl;
      }
      void handle(Banana& banana) {
        std::cout << "That was a banana!" << std::endl;
      }
    };
    
    MyHandler handler;
    
    MGenBase * object = reader.readObject();
    
    if (object) {
      dispatch(*object, handler);
    }

The java way is almost identical. For more information about type identification, dispatch and handlers you can also look in our [preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).


### Editing generated code directly <a name="c">&nbsp;</a>

Using generated code tends to bring some disadvantages, being locked into a specific development environment, development rules and assumptions for how an application should be designed - not to mention being locked out from the strengths of a specific a programming languge. Some tools might force you to write pseudo code in a specific integrated environment/modeling tool which then generates the actual code to be compiled.

MGen's philosophy is a bit different - Use your own code where you want to, and generated code where it fits - And feel free to manually edit generated code. What normally happens to manual edits if you regenerate the code? They're lost - NOT with MGen. Keep coding your applications in eclipse, emacs, visual studio - whatever you prefer.

Code generated with MGen contain what we call 'custom code sections'. These are blocks in generated code intended specificly to be edited manually. When you re-generate the source code for your model (e.g. add some fields, rename some members, etc) your custom code sections will be picked up and moved to the newly generated source code file. 

Of course, we can't support any arbitrary custom code edit, but you can do most things like add methods to classes, add inheritance of more super types and interfaces (multiple inheritance), and add some fields that you don't want to be included in the MGen system (e.g. runtime information, debug information etc).

This functionality is entirely language agnostic and works on string identification level when the MGen compiler is writing generated code to disk - so if you decide to use this feature just make sure to keep generating code to the same place - The MGen compiler will handle the rest.


### MGen compiler plug-ins <a name="c2">&nbsp;</a>

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


### Reading and writing objects from non-mgen sources <a name="d">&nbsp;</a>

In the same way we saw that [config files can be mapped directly to objects](index_l_Advanced_use.html#a), similar principles can be applied to other data sources that provide data in well defined formats, such as REST APIs, C structs, lua tables and other custom formats. It is simply a matter of providing a generalized mapping between the type of data source you're working with and the MGen data model.

Ideas for how this can be done can be seen in the sections on [adding new wire formats](index_l_Advanced_use.html#g) and [writing custom object visitors](index_l_Advanced_use.html#g). If you're more interested in solving a specific problem, less general solutions may be easier to implement.

One way to approach the problem is to write your own [MGen compiler plug-in](index_l_Advanced_use.html#c2). For example many APIs are defined using xml schemas, json schemas or similar. The MGen compiler is IDL agnostic and only requires a parser plug-in to work. If an xml schema plug-in or json schema plug-in was to be written for the MGen compiler, you could add those schemas to your model and get generated source code that directly mapped to those APIs.



### Adding custom code generators to the MGen compiler <a name="e">&nbsp;</a>

This section will show you an example on how to create your own code generator (=[a compiler plug-in](index_l_Advanced_use.html#c2)) and load it into the MGen compiler.


### Adding custom IDL parsers to the MGen compiler <a name="f">&nbsp;</a>


### Adding new wire formats/writing custom serializers <a name="g">&nbsp;</a>


### Writing custom MGen object visitors <a name="h">&nbsp;</a>


### Parsing command line arguments <a name="i">&nbsp;</a>

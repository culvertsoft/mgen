---
layout: default
link-title: Advanced use
submenu:
  - { anchor: "a", title: "config files" }
  - { anchor: "b", title: "obj. identification" }
  - { anchor: "c", title: "custom code" }
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

Perhaps the most interesting of these are the Dispatch and Handler types generated by MGen. Handlers are classes with a handle(object) method for every class that you generated code for. Dispatch(ers) are classes or functions that take any MGenBase object reference and a handler, and pass it to the handler's correct handle(object)-method. Handlers handle(object)-methods with default implementations for all types that pass along the object to its base handler, until the MGenBase class is reached. If no handle(object)-method is overloaded there, the object is ignored.

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


### Reading and writing objects from non-mgen sources <a name="d">&nbsp;</a>


### Adding custom code generators to the MGen compiler <a name="e">&nbsp;</a>


### Adding custom IDL parsers to the MGen compiler <a name="f">&nbsp;</a>


### Adding new wire formats/writing custom serializers <a name="g">&nbsp;</a>


### Writing custom MGen object visitors <a name="h">&nbsp;</a>


### Parsing command line arguments <a name="i">&nbsp;</a>

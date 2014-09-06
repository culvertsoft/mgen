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

Mapping objects to configuration files is no different from mapping objects to any other data source. MGen supports mapping objects to JSON (and the MGen binary format) out-of-the-box, so JSON is probably the easiest format to use with for configuration files. 

If you have existing configuration files in other formats (e.g. YAML, XML, ..) you can use generic libraries to convert to and from JSON, and then pass that to the MGen readers and writers.

In any case you need to create a data model that maps the configuration files to an MGen object class. See the section on [Defining data models](index_b_Basic_model.html) for how to do this.

Example - Consider a JSON configuration file with the following contents:

    {
      "hostName": "localhost",
      "port": 12345,
      "numThreads": 3,
      "logFile": "/home/logger/app.log"
    }

Which we map to a class called 'AppConfigarion' using the following MGen IDL code (see the section on [defining a data model](index_b_Basic_model.html)):

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


### Editing generated code directly <a name="c">&nbsp;</a>


### Reading and writing objects from non-mgen sources <a name="d">&nbsp;</a>


### Adding custom code generators to the MGen compiler <a name="e">&nbsp;</a>


### Adding custom IDL parsers to the MGen compiler <a name="f">&nbsp;</a>


### Adding new wire formats/writing custom serializers <a name="g">&nbsp;</a>


### Writing custom MGen object visitors <a name="h">&nbsp;</a>


### Parsing command line arguments <a name="i">&nbsp;</a>

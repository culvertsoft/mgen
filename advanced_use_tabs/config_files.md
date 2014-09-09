---
---

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



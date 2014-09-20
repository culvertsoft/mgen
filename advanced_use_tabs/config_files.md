---
---

Mapping objects to configuration files is no different from mapping objects to any other data source. MGen supports mapping objects to JSON (and the MGen binary format) out-of-the-box, so JSON is probably the easiest format to use for configuration files. If you have existing configuration files in other formats (e.g. YAML, XML, ..) you can use generic libraries to convert it to JSON, and then pass that to the MGen readers and writers.

In either case, we need a data model that maps the configuration file contents to an MGen object class. Example - Consider a JSON configuration file with the following contents:

{% highlight json %}

{
  "hostName": "localhost",
  "port": 12345,
  "numThreads": 3,
  "logFile": "/home/logger/app.log"
}

{% endhighlight %}

We could define a class called for example 'AppConfigarion' that maps c++ and java objects to this file using the following MGen IDL code (see the section on [defining a data model](index_b_Basic_model.html)):

{% highlight xml %}

<AppConfigarion>
  <hostName type="string"/>
  <port type="int"/>
  <numThreads type="int"/>
  <logFile type="string"/>
</AppConfigarion>

{% endhighlight %}

We can now read and write these files to and from our statically typed generated classes, as shown in the section on [using generated code](index_c_using_gen_code.html). To recap, here's how you would use it in c++:

{% highlight c++ %}

// Recap: The types required for reading an mgen object
ClassRegistry registry;
std::fstream iStream("/home/logger/cfg.json");
JsonReader<std::fstream, ClassRegistry> reader(iStream, registry);

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
    
// Serialize and write it back to disk
std::fstream oStream("/home/logger/cfg.json");
JsonPrettyWriter<std::fstream, ClassRegistry> writer(oStream, registry);
writer.writeObject(cfg);

{% endhighlight %}

Instead of checking if fields are set when reading and writing objects you can flag them as 'required'. If those fields are not set when reading or writing an object an exception will be thrown. Be very careful though with required fields, as they may cause serious incompatibility issues when you want to be backwards compatible with older data models and clients.

There may also be configuration parameters you may only have during runtime of your application, but don't want to store to the configuration files. You can flag those fields as 'transient'. For more information on field flags, again, see the section on [defining a data model](index_b_Basic_model.html).



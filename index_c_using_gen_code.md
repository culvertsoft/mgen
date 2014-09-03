---
layout: default
link-title: Using generated code
submenu:
  - { anchor: "a", title: "c++ example" }
  - { anchor: "b", title: "java example" }
  - { anchor: "c", title: "javascript example" }
  - { anchor: "d", title: "other wire formats" }
---

## Using generated code

This page shows how to serialize objects of previously generated example classes. 

### A c++ example <a name="a">&nbsp;</a>

This example shows how to serialize generic objects to JSON and back. For simplicity we will serialize to std::strings, but we could just as well serialiize to generic data sink - just a class having a write(void*, int) method.

In this example we use the following includes and namespace directives, and create a global class registry for our following functions to use:

    #include <iostream>

    #include <com/fruitcompany/ClassRegistry.h>
    #include <mgen/serialization/MemInputStream.h>
    #include <mgen/serialization/StringOutputStream.h>
    #include <mgen/serialization/JsonPrettyWriter.h>
    #include <mgen/serialization/JsonReader.h>

    using com::fruitcompany::ClassRegistry;
    using namespace com::fruitcompany::fruits;
    using namespace mgen;

    // A class registry for type identification
    const ClassRegistry registry;


Then we define our serialization functions:

    std::string toJSON(const MGenBase& object) {

        // Create a target to stream the object to
        StringOutputStream stream;

        // Create a writer object
        JsonPrettyWriter<StringOutputStream, ClassRegistry> writer(stream, registry);

        // Write the object
        writer.writeObject(object);

        // Return the written string
        return stream.str();
    }

    template <typename T>
    T fromJSON(const std::string& json) {

        // Create a data source to stream objects from
        MemInputStream stream(json);

        // Create a reader object
        JsonReader<MemInputStream, ClassRegistry> reader(stream, registry);

        // Read object. You can read T* polymorphicly with reader.readObject<T>()
        return reader.readStatic<T>();
    }


Lastly comes the main function which uses the above:

    int main() {

        // Create some objects
        const Apple apple(Brand_A, 4);
        const Banana banana = Banana().setLength(5).setBrand(Brand_B);

        // Serialize them to JSON and print them
        std::cout << toJSON(banana) << std::endl;
        std::cout << toJSON(apple) << std::endl;

        // Read the objects back from their serialized form
        const Apple appleBack = fromJSON<Apple>(toJSON(apple));
        const Banana bananaBack = fromJSON<Banana>(toJSON(banana));

        // Check that they are still the same
        std::cout << (apple == appleBack) << std::endl;
        std::cout << (banana == bananaBack) << std::endl;

        return 0;
    }


### A java example <a name="b">&nbsp;</a>


### A javascript example <a name="c">&nbsp;</a>


### Other wire formats <a name="d">&nbsp;</a>

MGen supports both binary and JSON serialization formats out-of-the-box. You can also use generic converters between other formats (e.g. XML, YAML) and JSON to map data written in those formats directly to MGen objects.

If you wish to go further you can also add completely custom formats by creating your own reader and writer classes, which are just generic object visitors.





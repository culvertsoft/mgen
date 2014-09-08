---
---

This example shows how to serialize objects to JSON and back. For simplicity we will serialize to std::strings, but we could just as well serialiize to generic data sink - just a class having a write(void*, int) method.

In this example we use the following includes and namespace directives, and create a global class registry for our following functions to use:

{% highlight c++ %}

#include <com/fruitcompany/ClassRegistry.h>
#include <mgen/serialization/StringInputStream.h>
#include <mgen/serialization/StringOutputStream.h>
#include <mgen/serialization/JsonPrettyWriter.h>
#include <mgen/serialization/JsonReader.h>

using namespace mgen;
using namespace com::fruitcompany;
using namespace com::fruitcompany::fruits;

// A class registry for type identification
const ClassRegistry registry;

{% endhighlight %}

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
        StringInputStream stream(json);

        // Create a reader object
        JsonReader<StringInputStream, ClassRegistry> reader(stream, registry);

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

---
---

Classes generated with the MGen compiler do not contain any specific code for serialization. Instead they are generated with generic object visitor methods. For ideas on what other features besides serialization you could implement with these methods, see the section on [writing MGen object visitors](index_l_Advanced_use.html#h).

MGen readers and writers (serializers) are hand-written classes able to exploit the generic visitor methods mentioned above. While these look slightly different java vs c++, the idea is the same. You dispatch your serializer to the object you wish to serialize, and you will receive a callback for every field.

What about reading data streams with objects where certain fields are missing or received in a different order? - MGen allows you to "visit streams" as well. To implement a custom reader you read field ids from the stream and then dispatch a callback to visit specific object field corresponding to that id - Essentially you have the ability to visit an object's fields from both directions - both from an object to be written to a stream, and from a stream to be written to an object.

With this information, implementing new readers and writers becomes nothing more than supporting the object visitor API, for the languages you want to support. See our [preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf) for more information. Also check our current readers and writers in the MGen source code  - [mgen-cpplib:serialization](https://github.com/culvertsoft/mgen/tree/master/mgen-cpplib/src/main/cpp/mgen/serialization) and [mgen-javalib:serialization](https://github.com/culvertsoft/mgen/tree/master/mgen-javalib/src/main/java/se/culvertsoft/mgen/javapack/serialization).


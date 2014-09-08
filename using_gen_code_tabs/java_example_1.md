---
---

Just as in the c++ example, this examples shows how to serialize objects to JSON and back. We start by using the following imports, and similar to the c++ example we also create a class registry for type identification:

    import se.culvertsoft.mgen.javapack.classes.MGenBase;
    import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter;
    import se.culvertsoft.mgen.javapack.serialization.JsonReader;

    import com.fruitcompany.ClassRegistry;
    import com.fruitcompany.fruits.Apple;
    import com.fruitcompany.fruits.Banana;
    import com.fruitcompany.fruits.Brand;

    public class Application {

        static Charset charset = Charset.forName("UTF-8");
        static ClassRegistry classRegistry = new ClassRegistry();


We define our serialization functions:

        static String toJSON(final MGenBase object) 
                throws IOException {

            // Create an output to stream the object to
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // Create a writer object
            JsonPrettyWriter writer = new JsonPrettyWriter(bos, classRegistry);

            // Write the object
            writer.writeObject(object);

            // Return the written string
            return new String(bos.toByteArray(), charset);
        }

        static <T extends MGenBase> T fromJSON(String json, Class<T> cls)
                throws IOException {

            // Create a data source to stream objects from
            // Standard Java InputStream objects can also be used
            StringReader stream = new StringReader(json);

            // Create a reader object
            JsonReader reader = new JsonReader(stream, classRegistry);

            // Read the object (the read is polymorphic)
            return reader.readObject(cls);
        }


Lastly comes the main function which uses the above:

        public static void main(final String[] params) 
                throws IOException {

            // Create some objects
            Apple apple = new Apple(Brand.A, 4);
            Banana banana = new Banana().setLength(5).setBrand(Brand.B);

            // Serialize them to JSON and print them
            System.out.println(toJSON(banana));
            System.out.println(toJSON(apple));

            // Read the objects back from their serialized form
            Apple appleBack = fromJSON(toJSON(apple), Apple.class);
            Banana bananaBack = fromJSON(toJSON(banana), Banana.class);

            // Check that they are still the same
            System.out.println(apple.equals(appleBack));
            System.out.println(banana.equals(bananaBack));

        }
    }

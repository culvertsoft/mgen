---
---

This section will show you how to create your own IDL parser plug-in ([a compiler plug-in](index_l_Advanced_use.html#c2)) and use it with the MGen compiler. To do this, we:

 * Create a class implementing the Parser interface
 * Build and package the class into a java jar file
 * Place the jar file on the MGen compiler's plug-in path
 * Specify the parser in your MGen [project file](index_c_Generating_code.html)

The Parser interface looks like this (comments removed):

    public interface Parser {
      void parse(final List<File> sources, 
                 final Map<String, String> settings, 
                 final Project parent);
    }

The input we have to work with are the source files specified for us to read, and some settings - And we should add our output (the modules and classes we parse) into the provided project parameter. Handling the settings parameter is optional - it is just a map containing the settings you provided in your IDL project file, module files and command line arguments.

In this example we will pretend we just parsed a module with a single class, and add it to the project ([full source code](https://github.com/culvertsoft/mgen/blob/master/mgen-api/src/test/java/se/culvertsoft/mgen/api/test/exampleparser/ExampleParser.java)).

    public class ExampleParser implements Parser {

      @Override
      public void parse(List<File> sources, 
                        Map<String, String> settings, 
                        Project parent) {
		
        // This parser doesn't parse any code.
        // It just pretends to already have done so 
        // and adds a mock Module with a mock Class for
        // example and tutorial purposes.
      
        String modulePath = "com.fruitcompany";
        String idlRelFilePath = "com.fruitcompany.txt";
        String idlAbsFilePath = "c:\\myMgenProject\\com.fruitcompant.txt";
      
        // Get or create the module we want to add a class to
        Module module = 
            parent.getOrCreateModule(modulePath,
                                     idlRelFilePath,
                                     idlAbsFilePath,
                                     settings);
		
        // Create a class called "FruitBowl"
        ClassType bowl = 
            new ClassType("FruitBowl", // Class name 
                          module, // Parent module
                          null); // Super type
		
        // Add a field named "capacity" to class "FruitBowl"
        bowl.addField(new Field(bowl.fullName(), // parent class name
                                "capacity", // field name
                                Int32Type.INSTANCE, // field type
                                null)); // field flags

        // Finally, add the new class to the module
        module.addClass(bowl);
      }
    }

Then just build and package that into a standard java jar file with your build system of choice. 

Now we need to tell the MGen compiler where to find the jar file. We do this with the command line parameter [plugin_paths](index_c_Generating_code.html#b). Another way is to simply drop it in your MGEN_INSTALL_PATH/jars (see [Installation](index_e1_Installation.html)).

Lastly, we add a 'Sources' section to our [MGen project file](index_c_Generating_code.html), like this:

    <Sources parser="com.fruitcompany.ExampleParser">
      <Source>my_source_file1.xyz</Source>
      <Source>my_source_file2.xyz</Source>
      <Source>my_source_file3.xyz</Source>
    </Sources>

Next time you run the MGen compiler, it will try to load the source files specified above and parse them to your new parser class, which we defined above.


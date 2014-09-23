---
---

This section will show you how to create your own code generator plug-in (<a target-tab="compiler-plug-ins" class="active" href="index_l0_Advanced_use.html#a">a compiler plug-in</a>) and use it with the MGen compiler. To do this, we:

 * Create a class implementing the Generator interface
 * Build and package the class into a java jar file
 * Place the jar file on the MGen compiler's plug-in path
 * Specify the generator in your MGen [project file](index_c_Generating_code.html)

The Generator interface looks like this (comments removed):

{% highlight java %}

public interface Generator {
  List<GeneratedSourceFile> generate(Project project, 
                                     Map<String, String> settings);
}

{% endhighlight %}

The input we have to work with is a Project model and some settings - And we should produce a list of GeneredSourceFile items. Handling the settings parameter is optional - it is just a map containing the settings you provided in your IDL project file, module files and command line arguments.

The Project parameter is where the interesting content exists. A Project is an MGen API class that describes your model (see the MGen API javadoc documentation or [the source](https://github.com/culvertsoft/mgen/blob/master/mgen-api/src/main/java/se/culvertsoft/mgen/api/model/Project.java)). You can query it for what modules a project contains (project.modules()) and what dependencies (project.dependencies()) it has. 

In this example we'll create a simple Generator class that just logs the names of all classes to be generated to a file. This is what it looks like ([source code](https://github.com/culvertsoft/mgen/blob/master/mgen-api/src/test/java/se/culvertsoft/mgen/api/test/examplegenerator/ExampleGenerator.java)):

{% highlight java %}

public class ExampleGenerator implements Generator {

  @Override
  public List<GeneratedSourceFile> generate(Project project, 
                                            Map<String, String> settings) {

    StringBuilder sb = new StringBuilder();
		
    sb.append("Generator log for: " + project.name()).append("\n");
		
    // Print all the modules and their contents
    for (Module module : project.modules()) {
        		
      // Print the module path
      sb.append(module.path()).append("\n");
        		
      // Print enums
      sb.append("  enums:").append("\n");
      for (EnumType enumT : module.enums()) {
        sb.append("    ").append(enumT.shortName()).append("\n");
      }
        		
      // Print classes
      sb.append("  classes:").append("\n");
      for (ClassType classT : module.classes()) {
        sb.append("    ").append(classT.shortName()).append("\n");
      }
			
    }
		
    String fileName = "generated_files.log";
    String sourceCode = sb.toString();

    return Arrays.asList(new GeneratedSourceFile(fileName, sourceCode));
  }
}

{% endhighlight %}

Then just build and package that into a standard java jar file with your build system of choice. 

Now we need to tell the MGen compiler what folder the jar file is in. We do this with the command line parameter [plugin_paths](index_c_Generating_code.html#b). Another way is to simply drop it in your MGEN_INSTALL_PATH/jars (see [Installation](index_e1_Installation.html)).

Lastly, we add a 'Generator' directive to our [MGen project file](index_c_Generating_code.html), like this:

{% highlight xml %}

<Generator name="MyExampleGenerator">
  <class_path>com.fruitcompany.ExampleGenerator</class_path>
</Generator>

{% endhighlight %}

Next time you run the MGen compiler, it will generate the log file using the class we defined above.



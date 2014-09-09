---
---

Note: This is an experimental feature currently only available in the MGen java runtime library. 

We have added a simple command line argument parser, which lets you take a set of application command line arguments and create a java object from them. It works by analyzing an MGen class through the generated class visitor interface ([explained earlier](index_l_Advanced_use.html#h)), and produce a command line argument parser for that class with corresponding help string.

Below is an example on how to use this functionality:

    // Just like previous examples we need a class registry
    ClassRegistry registry = new ClassRegistry();

    // Select the class you wish to map the command line arguments to
    // - We use one of the classes from the previous fruit example
    Class<Apple> cls = Apple.class;

    // Then we create the parser
    CommandLineArgParser parser = new CommandLineArgParser(cls, registry);
    
    // And the corresponding help string
    String helpString = new CommandLineArgHelp(cls).toString();
    
    // Now we can print the help string
    System.out.println(helpString);

    // And map command line args directly to an object
    // The "-" signs are not required.
    String[] applicationArgs = {"-brand", "A", "-radius", "3" };
    Apple apply = parser.parse(applicationArgs);


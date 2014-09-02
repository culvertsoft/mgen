---
layout: default
link-title: Functionality
---

## Summary of functionality

MGen can help you manage your multi-language application environment, by letting you:

 * Define your applications' data model in one place
 * Generate classes for multiple languages
 * Map binary data, JSON, XML, tables etc directly to objects
 * Map config files directly to objects
 * Map command line arguments directly to objects
 * Simplify sharing the state of your application over network
 * Stay backwards compatible with old data and clients
 * Customize serialization through generated visitor methods
 * Combine data model definitions from multiple IDLs
 * Add your own IDL parsers through MGen's plug-in architecture
 * Add custom code generators through MGen's plug-in architecture
 * Write code directly in generated source files, and re-generate!

Bottom line, MGen let's you build:

 * Accessible, comprehensible, cross-language data models that you come back to later and understand and attach to new (external or your own) interfaces.


### Language support 

The currently supported languages are:

 * C++
 * Java
 * JavaScript

We plan to support further languages, and prioritize by demand. The MGen toolkit is designed to be easily extendable to further languages by just adding custom code generator plug-ins to the MGen compiler.


### Development information 

 * Java 1.7 or newer. External deps through maven (currently just json-simple)
 * C++98 or newer. External deps included. Libraries are header-only. Generated code is .cpp+.h
 * JavaScript: [info pending]




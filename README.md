MGen
====

Flexible cross-language data modeling tools for programmers:

 * Pre-Build Model Design 	(pre-build model definitons in the mgen IDLs, or customizable others such as protobuf, thrift, avro etc)
 * Pre-Build Model Generation	(pre-build code generation through the mgen-compiler)
 * Runtime Module Utilities 	(support libraries with functionality such as serialization, data mapping etc)
 
IDL 	--> 	Compiler [ Parser + Code Generators ] 	--> 	Generated Code + Support Libraries	-->	Application/Library


Each component customizable and extendable plugin system:
 * Subclassing parsers (to support custom IDLs, or many simultaneously defined models in different IDLs)
 * Subclassing generators (to generate custom functionality, or add/remove generated functionality)
 * Extending support libraries to  through subclassing or the mgen-compiler's plugin system (loads jar files with parser or generator subclasses). 


====

Key features:
 * Design, maintain, modify your model in one place and generate native representations to your language of choice, that make sense to work with, not just as middleware for storage/messaging/rpc.
 * Support for generic and heterogenous containers
 * Support for polymorphic types (maps directly to c++/java class hierarchies)
 * Model versioning and backwards compatibility. In many cases you can add, remove, change order among and even move fields among sub/super types and still preserve backwards compatibility.
 * Customizable generators (generated code contents and functionality)
 * Customizable parsers
 * Customizable serializers
 * Ability to extend to new languages


====

Dual-Licensed under:
 * GPL v2
 * Commercial License (future)

====

 - Alpha v0.x -
(pre-release experimental code)

MGen
====

Flexible cross-language modeling tools for developers:

 * Data model design
 * Class/Code generation
 * Runtime utilities (serializers, mappers etc)
 
IDL(s) 	--> 	Compiler [Parser + Code Generators] 	--> 	Generated Code (to be used with support libraries)


Each component customizable/extendable:

 * IDLs can be customized/extended by adding new parsers or extending existing ones, through the Compiler's plugin architecture.

 * Generated code can be customized/extended/stripped by adding new generators or subclassing existing ones, also through the Compiler's plugin architecture.

 * The supplied support libraries can also be extended or replaced (e.g. adding new serializers)


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

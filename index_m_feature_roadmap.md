---
layout: default
link-title: Feature road map
---

## Feature road map and future plans

We have several features that we want to implement for MGen in the future. 
Below are some of these:

 * Add support for Python (generator + runtime library)
 * Add support for C# (generator + runtime library)
 * Finish building our visual gui application for data model design
 * Add support for custom allocators (c++)
 * Add support for std:: and boost:: smart pointers (c++)
 * Add generated operators "<<", "<" and ">" (c++)
 * Add support for C++11 features
 * Add support for mixins
 * Add support for ordered maps
 * Add support for sets
 * Add support for read-only fields (only setable in constructors)
 * Better naming of boolean getters (getSet() -> isSet())
 * Add support for IDL: Xml Schema
 * Add support for IDL: JSON Schema
 * Add support for further languages

We'd also like to start work on follow-up products to mgen. Our first follow-up product will be something we call [MNet](https://github.com/culvertsoft/mnet/) (working name), which is intended to be a language agnostic software network infrastructure / service bus provider.

The order in the list above does not necessarily reflect the order the features will be implemented in. The order will primarily be decided by demand and, especially in the case of new languages, what level of experience we can find.

At this point we're focusing on consolidating MGen's current features and getting ready for our first release. Once that is done, we'll start looking at the features above. There are some milestones available over at the [MGen github page](https://github.com/culvertsoft/mgen) if you're interested in dates.

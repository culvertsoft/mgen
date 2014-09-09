---
---

In the same way we saw that [config files can be mapped directly to objects](index_l_Advanced_use.html#a), similar principles can be applied to other data sources that provide data in well defined formats, such as REST APIs, C structs, lua tables and other custom formats. It is simply a matter of providing a generalized mapping between the type of data source you're working with and the MGen data model.

Ideas for how this can be done can be seen in the sections on [adding new wire formats](index_l_Advanced_use.html#g) and [writing custom object visitors](index_l_Advanced_use.html#g). If you're more interested in solving a specific problem, less general solutions may be easier to implement.

One way to approach the problem is to write your own [MGen compiler plug-in](index_l_Advanced_use.html#c2). For example many APIs are defined using xml schemas, json schemas or similar. The MGen compiler is IDL agnostic and only requires a parser plug-in to work. If an xml schema plug-in or json schema plug-in was to be written for the MGen compiler, you could add those schemas to your model and get generated source code that directly mapped to those APIs. If you want to add support for an IDL, see the section on [adding custom IDL parsers](index_l_Advanced_use.html#f).


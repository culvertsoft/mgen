---
---

In the same way we saw that <a target-tab="config-files" class="active" href="index_l0_Advanced_use.html#a">config files can be mapped directly to objects</a>, similar principles can be applied to other data sources that provide data in well defined formats, such as REST APIs, C structs, lua tables and other custom formats. It is simply a matter of providing a generalized mapping between the type of data source you're working with and the MGen data model.

Ideas for how this can be done can be seen in the sections on <a target-tab="custom-wire-formats" class="active" href="index_l0_Advanced_use.html#a">adding new wire formats</a> and <a target-tab="generic-visitors" class="active" href="index_l0_Advanced_use.html#a">writing custom object visitors</a>. If you're more interested in solving a specific problem, less general solutions may be easier to implement.

One way to approach the problem is to write your own <a target-tab="compiler-plug-ins" class="active" href="index_l0_Advanced_use.html#a">MGen compiler plug-in</a>. For example many APIs are defined using xml schemas, json schemas or similar. The MGen compiler is IDL agnostic and only requires a parser plug-in to work. If an xml schema plug-in or json schema plug-in was to be written for the MGen compiler, you could add those schemas to your model and get generated source code that directly mapped to those APIs. If you want to add support for an IDL, see the section on <a target-tab="custom-idl-parsers" class="active" href="index_l0_Advanced_use.html#a">adding custom IDL parsers</a>.


---
layout: default
link-title: Advanced use
submenu:
  - { anchor: "a", title: "config files" }
  - { anchor: "b", title: "obj. identification" }
  - { anchor: "c", title: "custom code" }
  - { anchor: "d", title: "non-mgen ifcs" }
  - { anchor: "e", title: "custom generators" }
  - { anchor: "f", title: "custom idl parsers" }
  - { anchor: "g", title: "custom wire formats" }
  - { anchor: "h", title: "generic visitors" }
  - { anchor: "i", title: "cmd line arg parser" }
---

## Advanced use <a name="defining_a_model">&nbsp;</a>

This page explains some of MGen's advanced use cases more in detail.


### Config files <a name="a">&nbsp;</a>

Reading or writing objects from configuration files is no different from reading it from any other source. MGen supports mapping objects to JSON out-of-the-box, so JSON is probably the easiest format to read configuration files from. Should you wish to read configuration files in other formats (YAML, XML, etc) the easieast way is probably to convert it to JSON with a generic xml/yaml -> json library.

There are two situations to consider:

 * Defining the contents of new configuration files
 * Mapping the contents of existing configuration file

In both cases you need to define a data model that maps the configuration file contents to an MGen object class. See the section on [Defining data models](index_b_Basic_model.html) for how to do this.





### Identifying object types  <a name="b">&nbsp;</a>


### Editing generated code directly <a name="c">&nbsp;</a>


### Reading and writing objects from non-mgen sources <a name="d">&nbsp;</a>


### Adding custom code generators to the MGen compiler <a name="e">&nbsp;</a>


### Adding custom IDL parsers to the MGen compiler <a name="f">&nbsp;</a>


### Adding new wire formats/writing custom serializers <a name="g">&nbsp;</a>


### Writing custom MGen object visitors <a name="h">&nbsp;</a>


### Parsing command line arguments <a name="i">&nbsp;</a>

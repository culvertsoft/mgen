---
---

One of the most important features of MGen is backwards compatibility. Consider the following scenarios: 

 * Your application stores data using an old data model, then loads with an uddated one
 * You build two applications that communicate with each other but will be updated at different times, so they might have different models when they talk to each other.

MGen serializers handle both of these situations, if the changes to the model are reasonable. Below are the rules you need to follow when modifying a model that needs to stay backwards compatible.

#### The following changes are backwards compatible:

 * Adding modules
 * Adding classes
 * Adding fields to a class 
 * Reordering fields in a class
 * Adding subclasses to a class
 * Moving fields from sub class to super class
 * Removing fields (with restrictions, see below)

Adding modules to a data model will not affect any of the previous classes. 

Adding classes to a data model will not affect any of the previous classes. If an older application reads an object of an unknown class, it will simply return a super class that it knows and omit the unknown fields, or simply skip past the object entirely if it doesn't have any known super classes (serializers will return a NULL object, that you can simply ignore).

Added fields to a class will simply be skipped by an older application version. In the newer application the new fields will be unset (unless you specified a default value for that field in your model).

Reordering fields simply affects the order they written to generated code in and the order of serialization. However MGen serializers are agnostic to that order. However if you care a lot about performance, you might not want reorder fields, as it causes increased number of CPU cache misses when deserializing objects.

Adding subclasses to a class is perfectly valid. If a new subclass is sent to an older application, that application will simply think it received an object of the old super class, and skip past the newer parts of the object.

Moving fields from a sub class to a super class is also valid (but not the other way). When MGen serializers read and write objects, they just write each field that has been set until no more fields remain - Which class they belong to is not relevant and not part of the MGen serialization formats.

Removing fields from generated code is valid, however, not recommended - because its field ID might get reused if you later introduce a new field with the same field ID - and then type conflicts could arise. It is better to keep the field and simply not use it (unset fields will not be serialized or take space on the wire). Another option is to flag the field as *parked*, which will cause it to still occupy a field ID, but not generate any source code.


#### The following changes are usually not backwards compatible:

 * Adding, removing or modifying *required* fields
 * Moving fields from super class to sub class
 * Renaming fields
 * Renaming classes
 * Changing the data type of a field
 * Changing the id of a field
 * Changing the id of a class

Adding or modifying required fields is dangerous. If you add a required field, all objects received from older applications will fail object validation (throwing exceptions on serialization and deserializationo), as they will lack the data you require from them. Removing the field or the required flag will cause the reverse - older applications will no longer be able to validate objects from your applications (throwing exceptions on serialization and deserializationo). Many consider *required* fields to be dangerous, and best not used at all.

Moving fields from super class to sub class causes older applications to lose access to those fields, so unless the older application never cared about the field it's not valid to do this.

Renaming fields changes their IDs if they're automatically generated, and even if you specified the ID manually, it's STILL NOT valid because many serialization formats use field names instead of field IDs (e.g. JSON, XML ..). It CAN be done for the MGen binary format, but it's definitely NOT recommended!

Renaming classes changes their IDs if they're automatically generated, and even if you specified the ID manually, it's STILL NOT valid because external serialization formats may use class names instead of IDs. It CAN be done for the MGen formats, but it's definitely NOT recommended!

Changing the data type of a field is not backwards compatible. MGen serializers always validate the data types on data being read so that it matches the data type of the field being read - If they don't match an exception is thrown.

Changing the ID of a field is not backwards compatible, since it could no longer be read by older application versions.

Changing the ID of a class is not backwards compatible, since it could no longer be read by older application versions.


#### How the ID system works

In simple terms, classes and their fields all have ID numbers assigned to them by the MGen compiler. These IDs are deterministicly determined and not dependent on any particular order or number of types and fields defined in the model. 

IDs can either be assigned manually by you in the IDL (similar to protocol buffers or thrift), or generated for you as a hash of the qualified class name or field name. How this is done and how conflicts are avoided is explained in the [preliminary technical whitepaper](http://culvertsoft.se/docs/WhitePaper.pdf).






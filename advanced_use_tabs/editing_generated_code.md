---
---

Using generated code tends to bring some disadvantages, being locked into a specific development environment, development rules and assumptions for how an application should be designed - not to mention being locked out from the strengths of a specific a programming languge. Some tools might force you to write pseudo code in a specific integrated environment/modeling tool which then generates the actual code to be compiled.

MGen's philosophy is a bit different - Use your own code where you want to, and generated code where it fits - And feel free to manually edit generated code. What normally happens to manual edits if you regenerate the code? They're lost - NOT with MGen. Keep coding your applications in eclipse, emacs, visual studio - whatever you prefer.

Code generated with MGen contain what we call 'custom code sections'. These are blocks in generated code intended specificly to be edited manually. When you re-generate the source code for your model (e.g. add some fields, rename some members, etc) your custom code sections will be picked up and moved to the newly generated source code file. 

Of course, we can't support any arbitrary custom code edit, but you can do most things like add methods to classes, add inheritance of more super types and interfaces (multiple inheritance), and add some fields that you don't want to be included in the MGen system (e.g. runtime information, debug information etc).

This functionality is entirely language agnostic and works on string identification level when the MGen compiler is writing generated code to disk - so if you decide to use this feature just make sure to keep generating code to the same place - The MGen compiler will handle the rest.


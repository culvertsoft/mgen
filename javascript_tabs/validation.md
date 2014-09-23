---
---

Because of the fact that javascript is weakly typed; we need some way to validate that data conforms to a class.

Please note that because JavaScript uses a float 64 (IEEE 754) for all number representation Int64 cannot be accuratly represented and some problems may arise on bigger numbers.

With the default settings validation occurs when the object is created and on serialization. Or if checked with the validate method described below.

{% highlight java %}
window.onload = function(){

    var settings = {}

    //true for type-validation of data on creation. Default true
    settings.validate = true;        

    var registry = mGen.generate(blueprint, settings);
    var fruit = new registry.Fruit();
    var banana = new registry.Banana();

    // A simple way to validate that an object fits the specified 
    // blueprint is to use registry.validate(). If the setting 
    // never_catch_error is false the function will return true
    // if it validated correctly or false if not.

    // If instead never_catch_error is set to true it will 
    // throw an error if the object did not validate.
    // You can for example use this as you would use asserts
    // as sanity checks on objects.

    banana.brand = "Wrongfully enum";

    registry.validate(banana, {
        strict_enums: true,
        never_catch_error: false,
        warn: true
    });

    // Because of warn: true, the console printed:

    /*
    Validation failed. Reason: 
        Error: Could not create object model.Banana Reason: 
        Error: Tried to create enum "Wrongfully enum" but only A, B, C are allowed.
        You can bypass this with setting strict_enums to false 
    */

}
{% endhighlight %}

Validating is done on all supported types including int, floats, strings enum and of course classes.
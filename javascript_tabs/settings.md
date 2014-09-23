---
---

The following example shows the different settings avalible.

{% highlight javascript %}

window.onload = function(){

    var settings = {}

    // true if warnings should be printed in the console, default is true
    settings.warn = true;

    // true if mgen never internally should catch error, 
    // this is preferable when you want to find the origin of
    // a error instead of a pritty error. Default false
    settings.never_catch_error = false;

    // true for type-validation of data on creation. Default true
    settings.validate = true;        

    // true if transient fields should be removed on serialization. 
    // Default true   
    settings.verify_transient = true;

    // true if transient fields should be removed on object creation. 
    // Default false
    settings.remove_transient = false;  

    // true if js should throw a error when encountering a unknown 
    // enum value. Default false. If set to false, it will convert 
    // the unknown value to the string "UNKNOWN".
    settings.strict_enums = true;

    var registry = mGen.generate(blueprint, settings);
    var fruit = new registry.Fruit();
    var banana_one = new registry.Banana();

    // brand is a enum field. Lets assign a brand that was not declared as 
    // a ok value for this enum.
    banana_one.brand = "Faulty brand";

    // The below outcommented code would have thrown: "Tried to create 
    // enum "Unknown brand" but only A, B, C are allowed.You can bypass
    // this with setting strict_enums to false "
    // This is becouse we set the settings strict_enums = true.

    // var banana_two = new registry.Banana(banana_one);


    // It is possible to override settings for single use by providing
    // the constructor with a second settings argument.
    // All settings can be overridden like this.
    var banana_two = new registry.Banana(banana_one, {strict_enums: false});


    // The console.log below will print UNKNOWN. While we might have been
    // able to save the enum value in js, it would be problematic for
    // other strict languages. We are striving for a consistent behavior
    // when possible, therefore we chose this behavior.
    console.log(banana_two.brand);
}
{% endhighlight %}

The transient flag is to mark a field that should not be serialized/mapped. There are two settings that are important for this functionallity.

{% highlight javascript %}

    //true if transient fields should be removed on serialization. Default true   
    settings.verify_transient = true;

    //true if transient fields should be removed on object creation. Default false
    settings.remove_transient = false;  

{% endhighlight %}

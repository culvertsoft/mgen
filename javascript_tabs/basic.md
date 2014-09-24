---
---

This page shows a simple example on how to use MGen's javascript runtime library.

{% highlight javascript %}

//Assuming that you have the mGen library 
//and the blueprint in you javascript scope

//create a registry over the classes.
var registry = mGen.generate(blueprint);

// Create some objects
var apple = new registry.Apple({
	brand: "A",
	radius: 4
});

var banana = new registry.Banana({
	length: 5,
	brand: "B"
});

// banana can now be accessed as simple js objects.

console.log(banana.length);

// would print 5 in the console.

// Also note that banana is a instanceof Fruit! Yes, we have inheritance in js!
console.assert(banana instanceof registry.Fruit);

//The jsonHandler is used for json mapping
var jh = mGen.jsonHandler(registry);

// Serialize them to a JSON string
var appleAsString = jh.objectToString(apple);
var bananaAsString = jh.objectToString(banana);

// Read the objects back from their serialized form
var appleBack = jh.stringToObject(appleAsString);
var bananaBack = jh.stringToObject(bananaAsString);

// Or if you know what kind of object you have.
// You can also create the object directly:
appleBack = new registry.Apple(appleAsString);

{% endhighlight  %}

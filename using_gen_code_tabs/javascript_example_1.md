---
---
Just as in the c++ example, this examples shows how to serialize objects to JSON and back.

The code generated in previous examples creates what we call a blueprint. The blueprint is used by the javascript library to create a class registry. Both the blueprint and the mgen javascript library can be used in an oldschool manner (that pollutes the window object) or by using the AMD approach (tested with require.js).

The registry is then responsible of creating stuff like validation and inheritance. We highly suggest that you also check out the JavaScript <a href="{{ site.baseurl }}/index_l1_JavaScript.html"> use cases </a> after you have read this page.

This is how the blueprint will look like when generated from the previous project example:

{% highlight javascript %}

/* Autogenerated code by MGen for JavaScript */

(function(){
	"use strict";
	var blueprint = {};
	
	blueprint.classes = {};
	blueprint.classes["model.Fruit"] =  {
		"__t": "Njk",
		"brand": {
			"flags": [],
			"type": "enum:A, B, C",
			"hash": "SxI"
		}
	};

	... More classes ...

	blueprint.lookup = function( typeId )  {
		var t = typeId.match(/(.{1,3})/g);
		switch( t[0] )  {
			case "Njk":
				switch( t[1] )  {
					case "qpA":
						return "model.Apple";
					case "v5o":
						return "model.Banana";
				}
				return "model.Fruit";
		}
	};

	// Support for requirejs etc.
	if (typeof define === "function" && define.amd) {
		define("fruits_blueprint", [], function(){
			return blueprint;
		});
	} else {
		// expose blueprint the old fashioned way.
		window.fruits_blueprint = blueprint;
	}
})();

{% endhighlight %}

This is a simple example on how to use this library.

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
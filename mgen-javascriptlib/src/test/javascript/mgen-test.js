/** JSLINT CONFIG */
/*global mgen_classreg: false, mGenLibrary: false, console: false
// QUnit vars: QUnit: false, test: false, module: false, ok: false, strictEqual: false, throws: false */

(function() {
	"use strict";

	 /* ***********************************************************\
	|*         TEST DATA                                           *|
	 \*************************************************************/

	var car_as_string = "{\"__t\":[\"Ntk\",\"Ksk\",\"L20\"],\"positioning\":{\"__t\":[\"NHU\"],\"position\":{\"__t\":[\"J8g\"]}},\"topSpeed\":100,\"brand\":\"Bananananana\"}";

	
	 /* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/

	module("Setup");

	test( "Testing QUnit framework is set up.", function(){
		ok( 1 == "1", "Passed!" );
	});

	test( "Testing that all functions and tables needed are in place.", function(){
		ok( mgen_classreg, " Class register Passed!" );
		ok( mGenLibrary, " mGenLibrary Passed!" );
	});

	var mGen = mGenLibrary(mgen_classreg);

	test( "Has mGen been created.", function(){
		ok( mGen, "Passed!" );
	});


	 /* ***********************************************************\
	|*         CREATION -- MAKE SURE WE CAN CREATE SOM CLASSES     *|
	 \*************************************************************/

	module("Simple Class Creation");

	test( "Can create simple base Class.", function(){
		var c = new mGen.VectorR3();
		ok( c, "Class created!" );
		ok( c instanceof mGen, "Class is a instance of mGen");
	});

	test( "Defult field.", function(){
		var c = new mGen.VectorR3("DEFAULT");
		ok( c, "Class created!" );
		strictEqual( c.x, 0, "vector x initilized to 0 by default" );
	});

	test( "Basic inheritance.", function(){
		var c = new mGen.VectorR3();
		ok( c, "Class created!" );
		ok( c instanceof mGen, "Class is a instance of mGen");
	});

	test( "Class with data.", function(){
		var c = new mGen.Positioning({
			position: new mGen.VectorR3({
				x: 2,
				y: 3,
				z: -1
			})
		});

		ok( c, "Class created!" );
		strictEqual(c.position.x, 2, "critical field set correctly");
	});


	test( "Class with nested data.", function(){
		var c = new mGen.Car({
			positioning: {
				position : { x: 1, y: 2, z: 3 }
			},
			topSpeed: 100,
			brand: "banananaa"
		});

		ok( c, "Class created!" );
		strictEqual(c.positioning.position.x, 1, " positioning.position.x Field set correctly");
		strictEqual(c.topSpeed, 100, " topSpeed Field set correctly");
	});

	test( "Test default field.", function(){

		var c = new mGen.VectorR3({
			x: 0,
			y: 0,
			z: 0
		});

		ok( c, "Class created!" );
		strictEqual(c.x, 0, "x field set correctly");
		strictEqual(c.y, 0, "y field set correctly");
		strictEqual(c.z, 0, "z field set correctly");
	});


	test( "Test required field.", function(){
		throws(function(){
			new mGen.Positioning({});
		}, "throw error on missing required field");
		
		var c = new mGen.Positioning({
			position: new mGen.VectorR3({
				x: 2,
				y: 3,
				z: -1
			}),
			velocity: new mGen.VectorR3({
				x: 0,
				y: 0,
				z: 0
			})
		});

		ok( c, "Class created!" );
		strictEqual(c.position.x, 2, "position field set correctly");
		strictEqual(c.velocity.x, 0, "velocity field set correctly");
	});

	test( "Multiple levels of inheritance.", function(){
		var data = {
			positioning: new mGen.Positioning({
				position : new mGen.VectorR3()
			}),
			topSpeed: 100,
			brand: "Banana"
		};

		var settings = {
			never_catch_error: false,
			validate: false,
			warn: false
		};

		var a = new mGen.Car(data, settings);
		var b = new mGen.Vehicle(data, settings);
		var c = new mGen.Entity(data, settings);

		ok( a, "Class car created!" );
		ok( b, "Class Vehicle created!" );
		ok( c, "Class Entity created!" );
		
		ok( a instanceof mGen.Car, "Car instanceof Car" );
		ok( a instanceof mGen.Vehicle, "Car instanceof Vehicle" );
		ok( a instanceof mGen.Entity, "Car instanceof Entity" );
		
		ok( !(b instanceof mGen.Car), "Vehicle not instanceof Car" );
		ok( b instanceof mGen.Vehicle, "Vehicle instanceof Vehicle" );
		ok( b instanceof mGen.Entity, "Vehicle instanceof Entity" );

		ok( !(c instanceof mGen.Car), "Entity instanceof Car" );
		ok( !(c instanceof mGen.Vehicle), "Entity instanceof Vehicle" );
		ok( c instanceof mGen.Entity, "Entity instanceof Entity" );
	});


	 /* ***********************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE OBJECTS   *|
	 \*************************************************************/

	module("Serialization");

	test( "Simple toJSONString serialization.", function(){
		var a = new mGen.Car({
			positioning: new mGen.Positioning({
				position : new mGen.VectorR3()
			}),
			topSpeed: 100,
			brand: "Bananananana"
		});
		strictEqual(a.toJSONString(), car_as_string, "Car toJSONString works correctly");

		var b = new mGen.Car({
			positioning: {
				position : new mGen.VectorR3()
			},
			topSpeed: 100,
			brand: "Bananananana"
		});
		strictEqual(b.toJSONString(), car_as_string, "Car toJSONString works correctly");

	});

	 /* ***********************************************************\
	|*  DESERIALIZATION -- MAKE SURE THAT WE CAN DESERIALIZE       *|
	 \*************************************************************/

	module("Deserialization");

	test( "Simple toJSONString serialization.", function(){
		var a = new mGen.gameworld.types.basemodule1.Car({
			positioning: new mGen.Positioning({
				position : new mGen.VectorR3()
			}),
			topSpeed: 100,
			brand: "Bananananana"
		});

		var b = new mGen.createFromJsonString(a.toJSONString());

		strictEqual(a.toJSONString(), b.toJSONString(), "Car deserialization works correctly");
	});



	 /* ***********************************************************\
	|*  ERRORS -- MAKE SURE WE THROW ERRORS AT THE CORRECT PLACE   *|
	 \*************************************************************/
	
	module("Error handling");

	test( "Create object with missing critical field.", function(){
		throws(function(){
			new mGen.Positioning({});
		}, "throw error on missing critical field");
		
		throws(function(){
			new mGen.Car({});
		}, "throw error on missing critical field in sub class");
	});

	test( "Create object with wrong data.", function(){
		throws(function(){
			new mGen.Positioning({
				position: new mGen.VectorR3(),
				velocity: new mGen.Car()
			});
		}, "throw error on wrong data in");
	});

	test( "Create object with wrong fields.", function(){
		throws(function(){
			new mGen.Positioning({
				position: new mGen.VectorR3(),
				this_field_does_not_exist: new mGen.Car()
			});
		}, "throw error on wrong data in");
	});
	
	test( "Create object with wrong type data.", function(){
		throws(function(){
			new mGen.Car({
				positioning: new mGen.Positioning({
					position : new mGen.VectorR3({
					 x: "string",
					 y: 0.43,
					 z: []
					})
				})
			});
		}, "throw error on wrong type of simple data");
	});

	test( "Create object with wrong type data.", function(){
		throws(function(){
			new mGen.Car({
				positioning: new mGen.Positioning({
					position : new mGen.VectorR3({
					 x: 0.32,
					 y: 0,
					 z: 0,
					})
				})
			});
		}, "throw error on wrong type of float/int");
	});

	test( "Create new object With no validation and validate after.", function(){
		var settings = {validate: false, never_catch_error: true};
		var c = new mGen.Car({
			positioning: new mGen.Positioning({
				position : new mGen.VectorR3({
				 x: 0.32,
				 y: 0,
				 z: 0,
				}, settings)
			}, settings)
		}, settings);

		ok(!c.validate(), "Car is not valid");

		var d = new mGen.Car({
			positioning: {
				position : { x: 13, y: 0, z: 0 }
			},
			topSpeed: 100,
			brand: "banananaa"
		}, settings);

		ok(d.validate(), "Car is valid");

	});


	 /* ***********************************************************\
	|*  HANDLER -- TEST THE SPECIALIZED HANDLER SYSTEM             *|
	 \*************************************************************/

	module("Handler");


	test( "Test simple handler.", function(){
		var handler = new mGen.handler();

		var handler_vehicle_called = false;
		var handler_car_called = false;


		//set up handler listeners:
		handler
		.on(mGen.Vehicle, function(){
			handler_vehicle_called = true;
		})
		.on(mGen.Car, function(){
			handler_car_called = true;
		});
		

		// send the handler some messages:

		handler.handle(car_as_string);

		handler.handle(new mGen.Vehicle({
			positioning: {
				position : { x: 13, y: 0, z: 0 }
			},
			topSpeed: 100})
		);

		ok(handler_car_called, "Car message has run");
		ok(handler_vehicle_called, "Vehicle message has run");
	});







	 /* ***********************************************************\
	|*  OPTIMIZATION -- LETS MAKE THE BROWSER SWEAT A BIT          *|
	 \*************************************************************/

	module("Optimization");

	// TODO!


	// TODO 
	/*

	*/
}) ();
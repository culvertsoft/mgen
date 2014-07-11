/** JSLINT CONFIG */
/*global mgen_classreg: false, mGenGenerate: false, it: false, describe: false, expect: false, xit: false, throws: false */

(function(){
	"use strict";

	 /* ***********************************************************\
	|*         TEST DATA                                           *|
	 \*************************************************************/

	var car_as_string = "{\"__t\":[\"Ntk\",\"Ksk\",\"L20\"],\"positioning\":{\"__t\":[\"NHU\"],\"position\":{\"__t\":[\"J8g\"]}},\"topSpeed\":100,\"brand\":\"IAmRolling\"}";

	 /* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/

	 if(!mgen_classreg){
	 	throw "mgen_classreg missing";
	 }

	 if(!mGenGenerate){
	 	throw "mgen_classreg missing";
	 }

	var Registry = mGenGenerate(mgen_classreg);

	 /* ***********************************************************\
	|*         CREATION -- MAKE SURE WE CAN CREATE SOM CLASSES     *|
	 \*************************************************************/

	describe("Simple Class Creation", function(){

		it( "Has mGen been created.", function(){
			expect( Registry ).toBeDefined();
		});

		it( "Can create simple base Class.", function(){
			var c = new Registry.VectorR3();
			expect( c instanceof Registry ).toBe( true );
		});

		it( "Defult field.", function(){
			var c = new Registry.VectorR3( "DEFAULT" );
			expect( c.x ).toBe( 0 );
		});

		it( "Basic inheritance.", function(){
			var c = new Registry.VectorR3();
			expect( c instanceof Registry ).toBe( true );
		});

		it( "Class with data.", function(){
			var c = new Registry.Positioning({
				position: new Registry.VectorR3({
					x: 2,
					y: 3,
					z: -1
				})
			});
			expect( c.position.x ).toBe( 2 );
		});

		it( "Class with nested data.", function(){
			var c = new Registry.Car({
				positioning: {
					position : { x: 1, y: 2, z: 3 }
				},
				topSpeed: 100,
				brand: "IAmRolling"
			});

			expect( c.positioning.position.x ).toBe( 1 );
			expect( c.topSpeed ).toBe( 100 );
		});

		it( "Test default field.", function(){

			var c = new Registry.VectorR3({
				x: 0,
				y: 0,
				z: 0
			});

			expect(c.x).toBe( 0 );
			expect(c.y).toBe( 0 );
			expect(c.z).toBe( 0 );
		});

		it( "Test required field.", function(){
			expect( new Registry.Positioning({}) ).toThrow(); // missing required field
			
			var c = new Registry.Positioning({
				position: new Registry.VectorR3({
					x: 2,
					y: 3,
					z: -1
				}),
				velocity: new Registry.VectorR3({
					x: 0,
					y: 0,
					z: 0
				})
			});

			expect( c.position.x ).toBe( 2 );
			expect( c.velocity.x ).toBe( 0 );
		});

		it( "Multiple levels of inheritance.", function(){
			var data = {
				positioning: new Registry.Positioning({
					position : new Registry.VectorR3()
				}),
				topSpeed: 100,
				brand: "Banana"
			};

			var settings = {
				never_catch_error: false,
				validate: false,
				warn: false
			};

			var a = new Registry.Car(data, settings);
			var b = new Registry.Vehicle(data, settings);
			var c = new Registry.Entity(data, settings);

			expect( a instanceof Registry.Car     ).toBe( true  );
			expect( a instanceof Registry.Vehicle ).toBe( true  );
			expect( a instanceof Registry.Entity  ).toBe( true  );
			
			expect( b instanceof Registry.Car     ).toBe( false );
			expect( b instanceof Registry.Vehicle ).toBe( true  );
			expect( b instanceof Registry.Entity  ).toBe( true  );

			expect( c instanceof Registry.Car     ).toBe( false );
			expect( c instanceof Registry.Vehicle ).toBe( false );
			expect( c instanceof Registry.Entity  ).toBe( true  );
		});

	});

	 /* ***********************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE OBJECTS   *|
	 \*************************************************************/

	describe("Serialization", function(){

		it( "Simple toJSONString serialization.", function(){
			var a = new Registry.Car({
				positioning: new Registry.Positioning({
					position : new Registry.VectorR3()
				}),
				topSpeed: 100,
				brand: "IAmRolling"
			});
			expect( a.toJSONString() ).toBe( car_as_string );

			var b = new Registry.Car({
				positioning: {
					position : { x: 0, y: 0, z: 0 }
				},
				topSpeed: 100,
				brand: "IAmRolling"
			});
			expect( b.toJSONString() ).toBe( car_as_string );
		});

	});

	 /* ***********************************************************\
	|*  DESERIALIZATION -- MAKE SURE THAT WE CAN DESERIALIZE       *|
	 \*************************************************************/

	describe("Deserialization", function(){

		it( "Simple toJSONString serialization.", function(){
			var a = new Registry.gameworld.types.basemodule1.Car({
				positioning: new Registry.Positioning({
					position : new Registry.VectorR3()
				}),
				topSpeed: 100,
				brand: "IAmRolling"
			});

			var b = new Registry.createFromJsonString(a.toJSONString());

			expect(a.toJSONString(), b.toJSONString(), "Car deserialization works correctly");
		});

	});

	 /* ***********************************************************\
	|*  ERRORS -- MAKE SURE WE THROW ERRORS AT THE CORRECT PLACE   *|
	 \*************************************************************/

	describe("Error handling", function(){

		it( "Create object with missing critical field.", function(){
			throws(function(){
				new Registry.Positioning({});
			}, "throw error on missing critical field");
			
			throws(function(){
				new Registry.Car({});
			}, "throw error on missing critical field in sub class");
		});

		it( "Create object with wrong data.", function(){
			throws(function(){
				new Registry.Positioning({
					position: new Registry.VectorR3(),
					velocity: new Registry.Car()
				});
			}, "throw error on wrong data in");
		});

		it( "Create object with wrong fields.", function(){
			throws(function(){
				new Registry.Positioning({
					position: new Registry.VectorR3(),
					this_field_does_not_exist: new Registry.Car()
				});
			}, "throw error on wrong data in");
		});
		
		it( "Create object with wrong type data.", function(){
			throws(function(){
				new Registry.Car({
					positioning: new Registry.Positioning({
						position : new Registry.VectorR3({
						 x: "string",
						 y: 0.43,
						 z: []
						})
					})
				});
			}, "throw error on wrong type of simple data");
		});

		it( "Create object with wrong type data.", function(){
			throws(function(){
				new Registry.Car({
					positioning: new Registry.Positioning({
						position : new Registry.VectorR3({
						 x: 0.32,
						 y: 0,
						 z: 0,
						})
					})
				});
			}, "throw error on wrong type of float/int");
		});

		it( "Create new object With no validation and validate after.", function(){
			var settings = {validate: false, never_catch_error: true};
			var c = new Registry.Car({
				positioning: new Registry.Positioning({
					position : new Registry.VectorR3({
					 x: 0.32,
					 y: 0,
					 z: 0,
					}, settings)
				}, settings)
			}, settings);

			expect(!c.validate(), "Car is not valid");

			var d = new Registry.Car({
				positioning: {
					position : { x: 13, y: 0, z: 0 }
				},
				topSpeed: 100,
				brand: "IAmRolling"
			}, settings);

			expect(d.validate(), "Car is valid");
		});

	});

	 /* ***********************************************************\
	|*  HANDLER -- TEST THE SPECIALIZED HANDLER SYSTEM             *|
	 \*************************************************************/

	describe("Handler", function(){

		xit( "Test simple handler.", function(){
			var handler = new Registry.handler();

			var handler_vehicle_called = false;
			var handler_car_called = false;


			//set up handler listeners:
			handler
			.on(Registry.Vehicle, function(){
				handler_vehicle_called = true;
			})
			.on(Registry.Car, function(){
				handler_car_called = true;
			});
			

			// send the handler some messages:

			handler.handle(car_as_string);

			handler.handle(new Registry.Vehicle({
				positioning: {
					position : { x: 13, y: 0, z: 0 }
				},
				topSpeed: 100})
			);

			expect(handler_car_called, "Car message has run");
			expect(handler_vehicle_called, "Vehicle message has run");
		});

	});
})();
/** JSLINT CONFIG */
/*global mgen_classreg: false, mGenGenerate: false, it: false, describe: false, expect: false, xit: false, throws: false */

requirejs(['mGen'], function (mGen) {
	"use strict";

	/* ***********************************************************\
	|*         TEST DATA                                           *|
	 \*************************************************************/
	// read this from file
	var car_as_string = "{\"__t\":\"AAQKskL20\",\"positioning\":{\"__t\":\"AAM\",\"position\":{\"__t\":\"AAE\",\"x\":0,\"y\":0,\"z\":0}},\"topSpeed\":100,\"brand\":\"IAmRolling\"}";

	/* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/

	if (!se_culvertsoft) { // se_culvertsoft is the class registry blueprint.
		throw "se_culvertsoft missing";
	}

	if (!mGen.generate) {
		throw "mgen_classreg missing";
	}

	var registry = mGen.generate(mgen_classreg);

	/* ***********************************************************\
	|*         CREATION -- MAKE SURE WE CAN CREATE SOM CLASSES     *|
	 \*************************************************************/

	describe("Simple Class Creation", function() {

		it("Has mGen been created.", function() {
			expect(registry).toBeDefined();
		});

		it("Can create simple base Class.", function() {
			var c = new registry.VectorR3();
			expect(c instanceof registry).toBe(true);
		});

		it("Defult field.", function() {
			var c = new registry.VectorR3("DEFAULT", {
				strict_enums: false
			});
			expect(c.x).toBe(0);
		});

		it("Basic inheritance.", function() {
			var c = new registry.VectorR3();
			expect(c instanceof registry).toBe(true);
		});

		it("Class with data.", function() {
			var c = new registry.Positioning({
				position: new registry.VectorR3({
					x: 2,
					y: 3,
					z: -1
				})
			});
			expect(c.position.x).toBe(2);
		});

		it("Class with nested data.", function() {
			var c = new registry.Car({
				positioning: {
					position: {
						x: 1,
						y: 2,
						z: 3
					}
				},
				topSpeed: 100,
				brand: "IAmRolling"
			});

			expect(c.positioning.position.x).toBe(1);
			expect(c.topSpeed).toBe(100);
		});

		it("Test default field.", function() {

			var c = new registry.VectorR3({
				x: 0,
				y: 0,
				z: 0
			});

			expect(c.x).toBe(0);
			expect(c.y).toBe(0);
			expect(c.z).toBe(0);
		});

		it("Test required field.", function() {
			expect(function() {
				new registry.Positioning()
			}).toThrow();

			var c = new registry.Positioning({
				position: new registry.VectorR3({
					x: 2,
					y: 3,
					z: -1
				}),
				velocity: new registry.VectorR3({
					x: 0,
					y: 0,
					z: 0
				})
			});

			expect(c.position.x).toBe(2);
			expect(c.velocity.x).toBe(0);
		});

		it("Multiple levels of inheritance.", function() {
			var data = {
				positioning: new registry.Positioning({
					position: new registry.VectorR3()
				}),
				topSpeed: 100,
				brand: "Banana"
			};

			var settings = {
				never_catch_error: false,
				validate: false,
				warn: false
			};

			var a = new registry.Car(data, settings);
			var b = new registry.Vehicle(data, settings);
			var c = new registry.Entity(data, settings);

			expect(a instanceof registry.Car).toBe(true);
			expect(a instanceof registry.Vehicle).toBe(true);
			expect(a instanceof registry.Entity).toBe(true);

			expect(b instanceof registry.Car).toBe(false);
			expect(b instanceof registry.Vehicle).toBe(true);
			expect(b instanceof registry.Entity).toBe(true);

			expect(c instanceof registry.Car).toBe(false);
			expect(c instanceof registry.Vehicle).toBe(false);
			expect(c instanceof registry.Entity).toBe(true);
		});

	});

	/* ***********************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE OBJECTS   *|
	 \*************************************************************/

	describe("Serialization", function() {

		it("Simple toJSONString serialization.", function() {
			var a = new registry.Car({
				positioning: new registry.Positioning({
					position: new registry.VectorR3({
						x: 0,
						y: 0,
						z: 0
					})
				}),
				topSpeed: 100,
				brand: "IAmRolling"
			});

			var jsonHandler = mGen.jsonHandler(registry);

			expect( jsonHandler.objectToString(a) ).toBe( car_as_string );

			var b = new registry.Car({
				positioning: {
					position: {
						x: 0,
						y: 0,
						z: 0
					}
				},
				topSpeed: 100,
				brand: "IAmRolling"
			});
			expect( jsonHandler.objectToString(b) ).toBe(car_as_string);
		});

	});

	/* ***********************************************************\
	|*  DESERIALIZATION -- MAKE SURE THAT WE CAN DESERIALIZE       *|
	 \*************************************************************/

	describe("Deserialization", function() {

		it("Simple toJSONString serialization.", function() {
			var a = new registry.gameworld.types.basemodule1.Car({
				positioning: new registry.Positioning({
					position: new registry.VectorR3()
				}),
				topSpeed: 100,
				brand: "IAmRolling"
			});

			var jsonHandler = mGen.jsonHandler(registry);
			var b = jsonHandler.stringToObject(jsonHandler.objectToString(a))

			expect( jsonHandler.objectToString(a) ).toBe( jsonHandler.objectToString(b) );
		});

	});

	/* ***********************************************************\
	|*  ERRORS -- MAKE SURE WE THROW ERRORS AT THE CORRECT PLACE   *|
	 \*************************************************************/

	describe("Error handling", function() {

		it("Create object with missing critical field.", function() {
			expect(function() {
				new registry.Positioning({});
			}).toThrow();

			expect(function() {
				new registry.Car({});
			}).toThrow();
		});

		it("Create object with wrong data.", function() {
			expect(function() {
				new registry.Positioning({
					position: new registry.VectorR3(),
					velocity: new registry.Car()
				});
			}).toThrow();
		});

		it("Create object with wrong fields.", function() {
			expect(function() {
				new registry.Positioning({
					position: new registry.VectorR3(),
					this_field_does_not_exist: new registry.Car()
				});
			}).toThrow();
		});

		it("Create object with wrong type data.", function() {
			expect(function() {
				new registry.Car({
					positioning: new registry.Positioning({
						position: new registry.VectorR3({
							x: "string",
							y: 0.43,
							z: []
						})
					})
				});
			}).toThrow();
		});

		it("Create object with wrong type data.", function() {
			expect(function() {
				new registry.Car({
					positioning: new registry.Positioning({
						position: new registry.VectorR3({
							x: 0.32,
							y: 0,
							z: 0,
						})
					})
				});
			}).toThrow();
		});

		it("Create new object With no validation and validate after.", function() {
			var settings = {
				validate: false,
				never_catch_error: true
			};
			var c = new registry.Car({
				positioning: new registry.Positioning({
					position: new registry.VectorR3({
						x: 0.32,
						y: 0,
						z: 0,
					}, settings)
				}, settings)
			}, settings);

			 expect(registry.validate(c)).toBe(false);

			var d = new registry.Car({
				positioning: {
					position: {
						x: 13,
						y: 0,
						z: 0
					}
				},
				topSpeed: 100,
				brand: "IAmRolling"
			}, settings);

			 expect(registry.validate(d)).toBe(true);
		});

	});

	/* ***********************************************************\
	|*  HANDLER -- TEST THE SPECIALIZED HANDLER SYSTEM             *|
	 \*************************************************************/

	describe("Handler", function() {

		xit("Test simple handler.", function() {
			var handler = new registry.handler();

			var handler_vehicle_called = false;
			var handler_car_called = false;


			//set up handler listeners:
			handler
				.on(registry.Vehicle, function() {
					handler_vehicle_called = true;
				})
				.on(registry.Car, function() {
					handler_car_called = true;
				});


			// send the handler some messages:

			handler.handle(car_as_string);

			handler.handle(new registry.Vehicle({
				positioning: {
					position: {
						x: 13,
						y: 0,
						z: 0
					}
				},
				topSpeed: 100
			}));

			expect(handler_car_called, "Car message has run");
			expect(handler_vehicle_called, "Vehicle message has run");
		});

	});
});
/** JSLINT CONFIG */
/*global requirejs: false, describe: false, it: false, expect: false */

requirejs(["mGen", "default_blueprint"], function (mGen, default_blueprint) {
	"use strict";

	/* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/

	if (!default_blueprint) {
		throw "mgen_blueprint missing";
	}

	if (!mGen.generate) {
		throw "mGen.Generate missing";
	}

	var registry = mGen.generate(default_blueprint);

	/* ***********************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE OBJECTS   *|
	 \*************************************************************/

	describe("Serialization", function() {

		it("Simple toJSONString serialization.", function() {
			var a = new registry.TypeWithDefValues();

			var jsonHandler = mGen.jsonHandler(registry);
			var def = {"__t":"HAssew","bc":-1,"bd":-2,"be":-3,"bf":-4,"a":true,"b":"b","c":-1,"e":-3,"f":-4,"g":3.5,"i":"-6.5","j":[1,2,3,4],"k":[[0,0],[0,0]],"l":[1,2,3,4],"m":[[0,0],[0,0]],"n":{"a":1},"o":{"1":1},"p":{"1":{}},"b_obj":{"__t":"tgo44Q","ba":-11,"bb":2,"bc":3,"a":-1,"b":2,"c":3},"c_obj":{"__t":"tgo44Q","ba":-11,"bb":2,"bc":3,"a":-1,"b":2,"c":3}};

			expect( jsonHandler.objectToString(a) ).toBe( JSON.stringify(def));

		});

	});

});
/** JSLINT CONFIG */
/*global requirejs: false, describe: false, it: false, expect: false */

requirejs(["mGen", "transient_blueprint"], function (mGen, transient_blueprint) {
	"use strict";

	/* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/

	if (!transient_blueprint) {
		throw "mgen_blueprint missing";
	}

	if (!mGen.generate) {
		throw "mGen.Generate missing";
	}

	var registry = mGen.generate(transient_blueprint);

	/* **********************************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE TRANSIENT OBJECTS   *|
	 \***********************************************************************/

	describe("Transient Serialization", function() {

		it("Simple toJSONString serialization.", function() {
			var a = new registry.MyType({
				a: 12,
				b: 3.5,
				c: 123.354,
				d: "stroing",
				e: [12, 13],
				f: [[12.12, 12.13], [12.12, 12.13]],
				g: {"argh": [3, 4, 7, 1]},
				h: {"__t": "TDI"},
				ta: 12,
				tb: 3.5,
				tc: 123.354,
				td: "stroing",
				te: [12, 13],
				tf: [[12.12, 12.13], [12.12, 12.13]],
				tg: {"argh": [3, 4, 7, 1]},
				th: {"__t": "TDI"},
			});

			var jsonHandler = mGen.jsonHandler(registry);
			var def = {
				__t: "888",
				a: 12,
				b: 3.5,
				c: 123.354,
				d: "stroing",
				e: [12, 13],
				f: [[12.12, 12.13], [12.12, 12.13]],
				g: {"argh": [3, 4, 7, 1]},
				h: {"__t": "TDI"}
			};

			expect( jsonHandler.objectToString(a) ).toBe( JSON.stringify(def));

		});

	});

});
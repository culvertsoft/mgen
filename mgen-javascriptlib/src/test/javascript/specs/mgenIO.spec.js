/** JSLINT CONFIG */
/*global mgen_classreg: false, mGenGenerate: false, it: false, describe: false, expect: false, xit: false, throws: false */

(function() {
	"use strict";

	function javaToJavaScript(str)
	{
	    len = str.length();
	    tmp = "";
	    for (var i=0; i<len; i++)
	        tmp += String.fromCharCode(str.charAt(i));
	    return tmp;
	}

	/* ***********************************************************\
	|*         TEST DATA                                           *|
	 \*************************************************************/
	var filepath = "src/test/car.json";

	var jPath = java.nio.file.Paths.get(filepath);
	var fileBytes = java.nio.file.Files.readAllBytes(jPath)
	var testdata = javaToJavaScript(new java.lang.String(fileBytes));

	/* ***********************************************************\
	|*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	 \*************************************************************/



	if (!mgen_classreg) {
		throw "mgen_classreg missing";
	}

	if (!mGen.generate) {
		throw "mgen generate missing";
	}

	var registry = mGen.generate(mgen_classreg);

	/* ***********************************************************\
	|*  SERIALIZATION -- MAKE SURE THAT WE CAN SERIALIZE OBJECTS   *|
	 \*************************************************************/

	describe("Serialization", function() {

		it(".", function() {

			var j = mGen.jsonHandler(registry);
			var car = j.stringToObject(testdata);
			var carString = j.objectToString(car);
			expect( carString ).toBe( testdata );
		});
	});
})();
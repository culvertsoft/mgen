requirejs(['mGen', 'se_culvertsoft'], function(mGen, se_culvertsoft) {
	"use strict";

	function javaToJavaScript(str) {
		var len = str.length();
		var tmp = "";
		for (var i = 0; i < len; i++) {
			tmp += String.fromCharCode(str.charAt(i));
		}
		return tmp;
	}

	function splitToStringPerObject(str) {
		var ret = [];
		var start = 0;
		var lvl = 0;
		var len = str.length;
		for (var i = 0; i < len; i++) {
			if (str[i] == "{") {
				lvl++;
			} else if (str[i] == "}") {
				lvl--;
			}
			if (lvl === 0) {
				ret.push(str.substring(start, i + 1))
				start = i + 1;
			}
		}
		if (start !== i) {
			ret.push(str.substring(start))
		}
		return ret;
	}

	// /* ***********************************************************\
	// |*         SETUP -- MAKE SURE ALL FILES ARE IN PLACE           *|
	//  \*************************************************************/

	if (!se_culvertsoft) {
		throw "se_culvertsoft missing";
	}

	if (!mGen.generate) {
		throw "mgen generate missing";
	}

	var registry = mGen.generate(se_culvertsoft, {
		never_catch_error: true
	});


	var j = mGen.jsonHandler(registry);

	/* ***********************************************************\
	|*         TEST EMPTY OBJECTS                                *|
	\*************************************************************/
	describe("Empty Objects", function() {
		var filepath = "../../generated/write/data_generated/emptyObjects_json.data";
		var jPath = java.nio.file.Paths.get(filepath);
		var fileBytes = java.nio.file.Files.readAllBytes(jPath)
		var testdata = javaToJavaScript(new java.lang.String(fileBytes));
		var arrStringObjects = splitToStringPerObject(testdata);

		it("", function() {
			j = mGen.jsonHandler(registry);
			for (var i = 0; i < arrStringObjects.length; i++) {
				var o = j.stringToObject(arrStringObjects[i]);
				var res = j.objectToString(o);
				expect(res).toBe(arrStringObjects[i]);
			}
		});
	});


	/**********************************************************\
	|*         TEST RANDOM OBJECTS                                *|
	\***********************************************************/
	describe("Random Objects", function() {
		var filepath = "../../generated/write/data_generated/randomizedObjects_json.data";
		var jPath = java.nio.file.Paths.get(filepath);
		var fileBytes = java.nio.file.Files.readAllBytes(jPath)
		var testdata = javaToJavaScript(new java.lang.String(fileBytes));
		var arrStringObjects = splitToStringPerObject(testdata);

		it("", function() {
			j = mGen.jsonHandler(registry);
			for (var i = 0; i < arrStringObjects.length; i++) {
				var o = j.stringToObject(arrStringObjects[i]);
				var res = j.objectToString(o);
				expect(res).toBe(arrStringObjects[i]);
			}
		});
	});
});
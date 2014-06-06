/*exported mGenLibrary, refreshErrorMessages */

/*****************************************************

This is a helper library for the MGenSuite. What it does is that it parses 
a class register and creates "classes" out of them. Please look at the 
unit tests to get a better understanding on how to use this library.

*****************************************************/


function mGenLibrary(registry, settings) {
    "use strict";

    settings = settings || {};
    settings = {
        warn: (typeof settings.warn === "undefined") ? true : settings.warn,
        use_hash_based_type: (typeof settings.use_hash_based_type === "undefined") ? false : settings.use_hash_based_type,
        never_catch_error: (typeof settings.never_catch_error === "undefined") ? false : settings.never_catch_error,
        validate: (typeof settings.validate === "undefined") ? true : settings.validate,
    };

    var MGen = function() {};

    MGen.prototype.validate = function(options) {
        options = options || {};
        var def = {
            warn: false,
            validate: true
        };
        extend(options, def);
        extend(options, settings);

        try {
            new(treeIndex(MGen, registry.hashRegistry[getLastHash(this.__t)]))(this, options);
        } catch (e) {
            if (options.warn) {
                window.console.warn("Validation failed. Reason: \n" + e);
            }
            if (options.never_catch_error) {
                throw e;
            }
            return false;
        }
        return true;
    };

    MGen.createFromJsonString = function(data) {
        if (typeof data !== "string") throw "Create from string needs a string argument";
        var d = JSON.parse(data);
        return new(treeIndex(MGen, registry.hashRegistry[getLastHash(d.__t)]))(d);
    };

    MGen.createFromJson = function(data) {
        return new (MGen.getClass(registry.hashRegistry[getLastHash(data.__t)]))(data);
    };

    MGen.hasClass = function(path) {
        return registry.classRegistry[path] || typeof MGen[path] !== "undefined";
    };

    MGen.getClass = function(path) {
        return treeIndex(MGen, path);
    };

    MGen.handler = function() {
        this.eventList = {};
    };

    MGen.prototype.toJSONString = function() {
        return JSON.stringify(this);
    };

    MGen.handler.prototype.on = function(C, func) {
        var _c = new C("NO_CONSTRUCT");
        this.eventList[getTypePath(_c)] = {
            def: C,
            func: func
        };
        return this;
    };

    MGen.handler.prototype.handle = function(msg, path) {
        if (path) {
            if (this.eventList[path]) {
                return this.eventList[path].func(msg);
            } else {

            }
        } else {
            if (typeof msg === "string") {
                msg = MGen.createFromJsonString(msg);
            }

            if (this.eventList[getTypePath(msg)]) {
                this.eventList[getTypePath(msg)].func(msg);
            } else {
                if (!path) {
                    path = getTypePath(msg);
                }
                var parent = getParentTypePathFromPath(path);
                if (!parent) {
                    throw "Could not handle message: " + msg.__t;
                } else {
                    this.hanle(msg, parent);
                }
            }
        }
    };


    /* CLASS GENERATION FROM registry.classRegistry */

    // create the classes in root and at absolue path.
    for (var obj_key in registry.classRegistry) {
        if (registry.classRegistry.hasOwnProperty(obj_key)) {
            setClass(registry.classRegistry[obj_key], obj_key);
        }
    }

    function setParent(obj) {
        if (registry.classRegistry.hasOwnProperty(obj)) {
            var C = treeIndex(MGen, getTypePathFromHashArray(registry.classRegistry[obj].mGenTypeHash));
            if (C.prototype instanceof MGen) {
                return;
            }
            var super_path = getParentTypePath(registry.classRegistry[obj]);
            if (!super_path) {
                C.prototype = new MGen();
            } else {
                setParent(super_path);
                C.prototype = new(treeIndex(MGen, super_path))("NO_CONSTRUCT");
            }
            C.prototype.constructor = C;
        }
    }

    //lets find each super and create inheritance
    for (var obj in registry.classRegistry) {
        setParent(obj);
    }

    return MGen;

    /* CREATION HELPER FUNCTIONS */

    function getShortName(path) {
        var a = path.split(".");
        return a[a.length - 1];
    }

    function treeIndex(obj, path, value, orig_path) {
        if (!orig_path) {
            orig_path = path;
        }
        if (typeof path == "string") {
            return treeIndex(obj, path.split("."), value, orig_path);
        } else if (path.length == 1 && value !== undefined) {
            obj[path[0]] = value;
            return obj[path[0]];
        } else if (path.length === 0) {
            if (typeof obj !== "function") {
                throw "Class " + orig_path + " not found in registry.";
            }
            return obj;
        } else {
            if (path.length > 1) {
                obj[path[0]] = obj[path[0]] || {};
            }
            return treeIndex(obj[path[0]], path.slice(1), value, orig_path);
        }
    }

    function setClass(obj, path) {
        var C = createClass(obj);
        treeIndex(MGen, path, C);
        if (!MGen[getShortName(path)]) {
            MGen[getShortName(path)] = C;
        } else {
            var C_ = new C("NO_CONSTRUCT");
            //TODO: we have a collision. Lets create a list of all the collisions and then later turn it into a warning for the user.
            if (typeof MGen[getShortName(path)] == "function") {
                var C1 = new MGen[getShortName(path)]("NO_CONSTRUCT");
                MGen[getShortName(path)] = [getTypePath(C1), getTypePath(C_)];
            } else {
                MGen[getShortName(path)].push(getTypePath(new C_("NO_CONSTRUCT")));
            }

        }
    }

    function createClass(obj) {
        var c = {};
        c[obj] = function(data, options) {
            if (data == "NO_CONSTRUCT") {
                this.__t = obj.mGenTypeHash;
                return;
            }

            var default_construction = false;
            if (data == "DEFAULT") {
                default_construction = true;
                data = null;
            }

            /* Options are local settings. Inherits from the settings done when created the registry */
            options = options || {};
            extend(options, settings);

            if (data) {
                if (data.__t) {
                    var objTypeHash = getLastHash(obj.mGenTypeHash);
                    var dataTypeHash = getLastHash(data.__t);
                    //check if they data corresponds to created object.
                    if (objTypeHash != dataTypeHash) {
                        // check if data is a child to type.
                        if (isHashInList(data.__t, objTypeHash)) {
                            return new(treeIndex(MGen, registry.hashRegistry[dataTypeHash]))(data);
                        } else {
                            throw " Tried to create " + registry.hashRegistry[objTypeHash] + " but got " + registry.hashRegistry[dataTypeHash];
                        }
                    }
                }
                if (options.validate) {
                    //check for faulty indata.
                    for (var _key in data) {

                        if (!data.hasOwnProperty(_key)) continue;

                        if (!obj[_key] && _key != "__t") {
                            var possible = "";
                            for (var pos in obj) {
                                if (pos != "__t") possible += " \t " + pos + " \n ";
                            }
                            throw getTypePathFromHashArray(obj.mGenTypeHash) + " does not have field " + _key +
                                " \n Possible options are: \n" + possible + " \n ";
                        }
                    }
                }
            }

            //populate fields with data.
            for (var field in obj) {

                if (!obj.hasOwnProperty(field)) continue;

                if (field == "mGenTypeHash") {
                    this.__t = obj[field];
                } else {
                    try {
                        if (options.validate && !default_construction) {
                            this[field] = createField(obj[field], field, data, options);
                        } else if (default_construction) {
                            this[field] = createDefaultField(obj[field], field, options);
                        } else {
                            this[field] = data ? data[field] : null;
                        }
                    } catch (err) {
                        if (options.never_catch_error) {
                            throw err;
                        } else {
                            var object_path = registry.hashRegistry[getLastHash(obj.mGenTypeHash)];
                            throw "Could not create object " + object_path + " Reason: \n" + err;
                        }
                    }

                    if (this[field] === null) {
                        delete(this[field]);
                    }
                }
            }
        };
        return c[obj];
    }

    function createField(field, key, data, options) {
        if (hasFlag(field.flags, "required")) {
            if (!data || !data[key]) {
                throw "Missing REQUIRED value: \"" + key + "\" of type " + field.type;
            }
            return createFieldOfType(field.type, data[key], options);
        } else {
            if (data && typeof data[key] !== "undefined") {
                return createFieldOfType(field.type, data && data[key] || null, options);
            } else {
                return null;
            }
        }
    }

    function createDefaultField(field, key, options) {
        if (options.validate && hasFlag(field.flags, "required")) {
            throw "Missing REQUIRED value: \"" + key + "\" of type " + field.type;
        } else {
            return createFieldOfType(field.type, null, options);
        }
    }

    function createFieldOfType(type, value, options) {
        var t = type.split(":");

        if (options.use_hash_based_type) {
            if (registry.hashRegistry[t[0]]) {
                t[1] = t[0];
                t[0] = "object";
            }
        } else {
            if (registry.classRegistry[t[0]]) {
                t[1] = getLastHash(registry.classRegistry[t[0]].mGenTypeHash);
                t[0] = "object";
            }
        }

        var C, ret;
        switch (t[0]) {
            case "object":
                var typePath = registry.hashRegistry[t[1]];
                if (value && typeof value !== "object") {
                    throw "Tried to create " + type + " but had no object data.";
                }
                value = value || {};
                ret = new(treeIndex(MGen, typePath))(value);
                return ret;
            case "list":
                if (value && !Array.isArray(value)) {
                    throw "Tried to create " + type + " but had no array data.";
                }
                value = value || [];
                ret = [];
                C = treeIndex(MGen, t[1]);
                value.forEach(function(entry) {
                    ret.push(new C(entry));
                });
                return ret;
            case "map":
                if (value && typeof value !== "object") {
                    throw "Tried to create " + type + " but had no object data.";
                }
                value = value || {};
                ret = {};
                for (var key in value) {
                    if(value.hasOwnProperty(key)){
                        ret[key] = createFieldOfType(t[2], value[key], options);
                    }
                }
                return ret;
            case "int8":
                checkInt(value, 8, type, options);
                ret = value || 0;
                return ret;
            case "int16":
                checkInt(value, 16, type, options);
                ret = value || 0;
                return ret;
            case "int32":
                checkInt(value, 32, type, options);
                ret = value || 0;
                return ret;
            case "int64":
                checkInt(value, 64, type, options);
                ret = value || 0;
                return ret;
            case "float32":
                checkFloat(value, 32, type, options);
                ret = value || 0;
                return ret;
            case "float64":
                checkFloat(value, 64, type, options);
                ret = value || 0;
                return ret;
            case "string":
                if (value && typeof value !== "string") {
                    throw "Tried to create " + type + " but had no string data.";
                }
                ret = value || "";
                return ret;
        }
    }

    function checkInt(value, size, type, options) {
        if (size > 32) {
            if (options.warn) {
                window.console.warn("mgen_js cannot handle 64 bit integers well due to javascript limitations. See https://developer.mozilla.org/en-US/docs/Mozilla/js-ctypes/js-ctypes_reference/Int64");
            }
        }

        if (typeof value === "undefined" || value === null) {
            return;
        }

        if (typeof value !== "number") {
            throw "Tried to create " + type + " but data was of type: " + (typeof value);
        }

        if (isNumeric(parseInt(value, 10)) && (parseFloat(value, 10) == parseInt(value, 10))) {
            var lim = Math.pow(2, size - 1);
            if (value > lim - 1 || value < -lim) {
                throw value + " is out of range for int" + size;
            }
            return true;
        } else {
            throw "Tried to create " + type + " but data ( " + value + " ) was not a valid integer (floating point?).";
        }

    }

    function checkFloat(value, size, type, options) {
        if (typeof value === "undefined" || value === null) {
            return;
        }
        if (size < 64) {
            if (options.warn) {
                window.console.warn("Float32 is not easy to represent in javascript. Expect rounding at serialization/deserialization.");
            }
        }
        if (!isNumeric(value)) {
            throw "Tried to create " + type + " but data ( " + value + " ) was not a valid float.";
        }
    }

    function hasFlag(fields, flag) {
        return (fields.indexOf(flag) > -1);
    }

    function isNumeric(number) {
        // Borrowed from jQuery with comment:
        // parseFloat NaNs numeric-cast false positives (null|true|false|"")
        // ...but misinterprets leading-number strings, particularly hex literals ("0x...")
        // subtraction forces infinities to NaN
        return number - parseFloat(number) >= 0;
    }

    function getTypePath(obj) {
        return getTypePathFromHashArray(obj.__t);
    }

    function getLastHash(hashArray) {
        return hashArray[hashArray.length - 1];
    }

    function getTypePathFromHashArray(hashArray) {
        return registry.hashRegistry[getLastHash(hashArray)];
    }

    function getParentTypePath(obj) {
        var hashArray = obj.mGenTypeHash;
        if (hashArray.length > 1) {
            return registry.hashRegistry[hashArray[hashArray.length - 2]];
        } else {
            return null;
        }
    }

    function getParentTypePathFromPath(path) {
        return getParentTypePath(registry.classRegistry[path]);
    }

    function isHashInList(hashArray, hash) {
        return hashArray.indexOf(hash) !== -1;
    }

    function extend(options, settings) {
        for (var field in settings) {
            if (!settings.hasOwnProperty(field)) continue;
            options[field] = (typeof options[field] === "undefined") ? settings[field] : options[field];
        }
    }
}

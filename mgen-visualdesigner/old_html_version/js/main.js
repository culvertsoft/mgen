/* global $: false, mgen_classreg: false, mGenLibrary: false, handleToolbar: false, handleClassWindow: false, handleMap: false*/

$(window).load(function() {
    "use strict";

    $.initWindowMsg();

    var mgen = mGenLibrary(mgen_classreg, {
        validate: false
    });

    var dataStructure = new mgen.Project("default");

    var $element = {
        content: $("#content"),
        toolbar: $("<div>", {
            id: "toolbar",
            class: "ui-widget-content"
        }),
        classWindow: $("<div>", {
            id: "class-window",
            class: "ui-widget-content"
        }),
        map: $("<div>", {
            id: "map",
            class: "ui-widget-content"
        })
    };

    //ugly observer//observee pattern on the dataStructure change. 
    var dataChangeListeners = [];

    var dataChangeFunc = function(data, caller) {
        dataStructure = data;
        $.each(dataChangeListeners, function(index, listObj) {
            if (!caller || listObj.caller !== caller) {
                listObj.func(dataStructure);
            }
        });
    };

    dataChangeListeners.push({
        name: "toolbar",
        func: handleToolbar($element.toolbar, dataChangeFunc, "toolbar", mgen)
    });
    dataChangeListeners.push({
        name: "classWindow",
        func: handleClassWindow($element.classWindow, dataChangeFunc, "classWindow", mgen)
    });
    dataChangeListeners.push({
        name: "map",
        func: handleMap($element.map, dataChangeFunc, "map", mgen)
    });

    $element.content.empty().append($element.toolbar, $element.classWindow);
    dataChangeFunc(dataStructure);
});

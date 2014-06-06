/*global $: false, console: false */
/*exported handleToolbar */

function handleToolbar($toolbar, dataChangeFunc, callName, mgen) {
    'use strict';

    var childWin, _data;
    var $elements = {};
    var base_elements = [
        'codeViewButton',
        'saveButton',
        'editToolButton',
        'makeModuleButton',
        'settingsField',
        'showFieldCheckbox',
        'showInheritedFieldsCheckbox',
        'showFullNameCheckbox',
        'baseModuleNameField'
    ];

    $toolbar.empty();
    $.each(base_elements, function(index, name) {
        $elements[name] = $('<div>', {
            id: name
        });
        $toolbar.append($elements[name]);
    });

    $elements.codeViewButton.button({
        label: 'CodeView'
    }).click(function() {
        childWin = window.open(
            'popup.html',
            'child',
            'width=500, height=500, location=no, menubar=no, scrollbars=no, status=no, toolbar=no');
        if (window.focus) {
            childWin.focus();
        }
    });

    $elements.saveButton.button({
        label: 'Save'
    }).button('disable');

    $elements.editToolButton.button({
        label: 'Edit'
    }).click(function() {
        //TODO: edit
    });

    $elements.makeModuleButton.button({
        label: 'Module'
    }).click(function() {
        //TODO: edit
    });

    // get messages from child window
    window.addEventListener('message', function(e) {
        if (e.data === 'getData') {
            childWin.postMessage(_data, '*');
        } else {
            dataChangeFunc(e.data, 'toolbar');
        }
    }, false);

    $elements.settingsField.empty().mgenField({
        mgen: mgen,
        type: 'map:string:string',
        dataChange: function(event, data) {
            _data.settings = data;
            dataChangeFunc(_data, 'toolbar');
            if(childWin){
                childWin.postMessage(_data, '*');
            }
        }
    });

    return function(data) {
        $elements.settingsField.mgenField({
            data: data.settings
        });
        if(childWin) {
            childWin.postMessage(_data, '*');
        }
        console.log(data);
        _data = data;
    };
}

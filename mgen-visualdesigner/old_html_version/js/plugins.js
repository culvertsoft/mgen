/*global jQuery: false */
// Avoid `console` errors in browsers that lack a console.
(function() {
    'use strict';
    var method;
    var noop = function() {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

/* windowMsgHandler */
(function($) {
    'use strict';
    $.windowMsgHandlers = [];
    $.initWindowMsg = function() {
        $('body').append($('<form name="windowComm"></form>')
            .append($('<input type="hidden" name="windowCommEvent">'))
            .append($('<input type="hidden" name="windowCommData">'))
            .append($('<input id="myinput" type="button" name="windowCommButton" value="" style="display:none">')));
        $('#myinput').click(function() {
            var eventType = $('[name=windowCommEvent]').val();
            var data = $('[name=windowCommData]').val();
            for (var i = 0; i < $.windowMsgHandlers.length; i++) {
                var h = $.windowMsgHandlers[i];
                if (h.event == eventType) {
                    h.callback.call(null, data);
                    break;
                }
            }
        });
    };
    $.triggerParentEvent = function(event, msg) {
        return $.triggerWindowEvent(window.opener, event, msg);
    };
    $.triggerWindowEvent = function(otherWindow, event, msg) {
        if (typeof otherWindow == 'object') {
            var form = otherWindow.document.forms.windowComm;
            if (form) {
                form.windowCommEvent.value = event;
                form.windowCommData.value = msg;
                form.windowCommButton.click();
                return true;
            }
        }
        return false;
    };
    $.windowMsg = function(event, callback) {
        $.windowMsgHandlers.push({
            event: event,
            callback: callback
        });
    };
})(jQuery);

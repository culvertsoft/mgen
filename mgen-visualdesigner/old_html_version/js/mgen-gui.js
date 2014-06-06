/*!
 * jQuery plugin to create editable fields from mgen generated classes.
 * Original author: @Phrozen
 */
 /* global jQuery: false */

(function($, window, document, undefined) {
    'use strict';
    var pluginName = 'mGenField',
        defaults = {
            data: null,
            type: null,
            onDataChange: function() {}
        };

    // The actual plugin constructor

    function Plugin(element, options) {
        this.element = element;

        this.options = $.extend({}, defaults, options);

        this._defaults = defaults;
        this._name = pluginName;

        this.init();
    }

    Plugin.prototype.init = function() {
        this.element.empty();
    };

    $.fn[pluginName] = function(options) {
        return this.each(function() {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName, new Plugin(this, options));
            }
        });
    };

})(jQuery, window, document);

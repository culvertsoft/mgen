/*!
 * jQuery plugin to create editable fields from mgen generated classes.
 * Original author: @Phrozen
 */

/* global jQuery: false */
(function($) {
	'use strict';
	$.widget('mgen.mgenField', {

		// Default options.
		options: {
			mgen: null,
			data: {},
			type: 'string',
			child: null
		},

		_create: function() {
			var that = this;
			var type = this.options.type;
			var t = type.split(':');
			if(this.options.mgen.hasClass(t[0])){
				t[1] = t[0];
				t[0] = 'object';
			}

			//$(this.element)['mgenField' + t[0]];

			switch (t[0]) {
				case 'object':
					this.options.childFunc = $.fn.mgenFieldObject;
					break;
				case 'list':
					this.options.childFunc = $.fn.mgenFieldList;
					break;
				case 'map':
					this.options.childFunc = $.fn.mgenFieldMap;
					break;
				case 'int8':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'int16':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'int32':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'int64':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'float32':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'float64':
					this.options.childFunc = $.fn.mgenFieldNumber;
					break;
				case 'string':
					this.options.childFunc = $.fn.mgenFieldString;
					break;
				default:
					throw 'What is a ' + t[0] + '?';
			}

			this.options.childFunc.apply(this.element, [{
				mgen: this.options.mgen,
				type: this.options.type,
				data: this.options.data,
				dataChange: function(event, data){
					that._trigger('dataChange', null, data);
				}
			}]);
		}
	});

	

	$.widget('mgen.mgenFieldMap', {

		// Default options.
		options: {
			mgen: null,
			data: {},
			type: 'map:string:string',
			child: null,
			rows: $('<div>')
		},

		_create: function() {
			var that = this;
			var t = this.options.type.split(':');
			if(!t[0] || !t[1] || !t[2]){
				throw 'Missing type from ' + this.options.type;
			}

			function rowCreate(key, value){
				var $row = $('<div>');
				var $key_value = $('<div>').mgenFieldKeyValuePair(
					{
						key: {
							type: t[1],
							data: key
						},
						value: {
							type: t[2],
							data: value
						},
						dataChange: function(event, data){
							console.log(data);
							that.options.data[data.key] = data.value;
							that._trigger('dataChange', null, that.options.data);
						}
					}
				);
				
				var $delete_key = $('<div>', {text: "X"}).click(function(){
					var key = $key_value.mgenFieldKeyValuePair('get_key')
					delete that.options.data[key];
					that._trigger('dataChange', null, that.options.data);
					$row.remove();
				});

				$row.append($key_value, $delete_key);
				return $row;
			}


			$.each(this.options.data, function(key, value){
				that.options.rows.append(rowCreate(key, value));
			});

			var $create_new = $('<div>', {text: "+"}).click(function(){
				that.options.rows.append(rowCreate());
			});
		
			this.element.append(this.options.rows, $create_new);
		}
	});


	$.widget('mgen.mgenFieldKeyValuePair', {

		// Default options.
		options: {
			key: {
				type: 'string',
				data: ''
			},
			value: {
				type: 'string',
				data: ''
			},
			key_element: null,
			value_element: null,
		},

		_create: function() {
			var that = this;
			this.options.key_element = $('<input>', { type: 'text'});
			this.options.value_element = $('<input>', { type: 'text'});
			var onChange = function(){
				that.options.key.data = $(that.options.key_element).val();
				that.options.value.data = $(that.options.value_element).val();
				that._trigger('dataChange', null, {
					key: that.options.key.data,
					value: that.options.value.data
				});
			};

			this.options.key_element.val(this.options.key.data).change(onChange);
			this.options.value_element.val(this.options.value.data).change(onChange);
			this.element.append(this.options.key_element, this.options.value_element);
		},

		get_key: function(){
			var that = this;
			return that.options.key.data;
		}
	});


})(jQuery);

$(function(){
	"use strict";

	var all_tabs = {};

	function fix_tab_links($element){
		$('a[tab-id]').each(function(){
			all_tabs[$(this).attr('tab-id')] = this;
		});

		$('a[target-tab]').each(function(){
			var $link = $(this);
			$link.off();
			$link.click(function(){
				var target = $link.attr('target-tab');
				if(!all_tabs[target]){
					console.error(target + " is a target-tab but found no tab-id");
				}
				$(all_tabs[target]).trigger("click");
			});
		});
	}

	$('.tabs').tabs({
		load: function(event, ui){
			fix_tab_links(ui.panel);
		}
	});

	fix_tab_links($("body"));
});
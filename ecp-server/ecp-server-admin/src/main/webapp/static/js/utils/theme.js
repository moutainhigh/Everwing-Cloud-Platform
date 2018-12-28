/** * 更换主题 */
changeTheme = function(type) {
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('jqueryEasyUI')) + 'jqueryEasyUI/themes/' + type
			+ '/easyui.css';
	$easyuiTheme.attr('href', href); 
	var $iframe = $('iframe');
	if($iframe.length >0){
		for(var i=0; i<$iframe.length; i++){ 
			var ifr = $iframe[i];
			$(ifr).contents().find('#easyuiTheme').attr('href',href);
		}
	}
	$.cookie('easyuiThemeName', type, {
				expires : 7
	});
};
(function ($,undefined){
	$.formatDate = function(date,pattern){
		var month = format(date.getMonth()+1);
		var day =  format(date.getDate());
		var hours = format(date.getHours());
		var minutes = format(date.getMinutes());
		var seconds = format(date.getSeconds());
		if(pattern=="yyyy"){
			return date.getFullYear();
		}else if(pattern=="yyyy-MM"){
			return date.getFullYear()+"-"+month;
		}else if(pattern=="yyyy-MM-dd"){
			return date.getFullYear()+"-"+month+"-"+day;
		}else{
			return date.getFullYear()+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
		}
	};
	
	function format(s){
		if(s<10){
			return "0"+s;
		}else{
			return s;
		}
	}
})(jQuery);
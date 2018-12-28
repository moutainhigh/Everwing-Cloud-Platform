$(function(){
	$('body').css('visibility','hidden');
});
//easyloader.theme="metro-blue"; // 设置主题
easyloader.css=false;
easyloader.locale="zh_CN"; // 本地化设置
easyloader.onLoad = function(name){
	if(name=='parser'){
		$.parser.onComplete = function(){
			setTimeout(function(){$('body').css('visibility','visible');},80);
		}
	}
}
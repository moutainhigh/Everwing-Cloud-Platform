$.ajaxSetup({
	cache: false,
	complete:function(xhr,status){
		if(xhr.responseText){
			if(xhr.responseText.substring(4,7)=="500"){
				$.messager.alert('提示信息', "请求数据出错,请稍后再试", 'info');
	  			//alert("请求数据出错,请稍后再试");
	  		}else if(xhr.responseText.substring(4,7)=="989"){
	  			$.messager.alert('提示信息',"登录超时,为了保护您的数据,请重新登录",'info',function(){
                   window.top.location.href="/SaaS";
               	});
//	  			alert("为了保护您的数据,请重新登录");
//	  			window.top.location.href="/eamoms";
	  		}else if(xhr.responseText.substring(4,7)=="988"){
	  			$.messager.alert('提示信息',"您的账号在另一个地点登录，您被迫离线，为了保障您的账号安全，建议尽快更换密码！",'info',function(){
                   window.top.location.href="/SaaS";
               	});
	  		}
		}
	}
});
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="/WEB-INF/shiro.tld"%>
<script type="text/javascript" src="static/js/jqueryEasyUI/tabs-iframe.js "></script>
<script type="text/javascript" src="static/js/jqueryEasyUI/constrain-window.js"></script>
<style type="text/css">
ul{margin: 0;padding: 0}
.index_zt { float: right; margin-right: 20px;}
.index_zt li{ float: left;padding-left:20px;margin-left:10px; line-height:50px; height:50px;  color: #fff;font-size: 12px;}
.index_zt li a{text-decoration:none;color:white;}
.index_zt a:hover{background:#D9D9D9;}
.icon-cubes{
	background:url(static/images/index/cubes_all.png) no-repeat center center;
}
.icon-online{
	background:url(static/images/index/online.png) no-repeat center left;
}
.icon-person-info{
	background:url(static/images/index/person_info.png) no-repeat center left;
}
.icon-logout{
	background:url(static/images/index/logout.png) no-repeat center left;
}
.icon-user-help{
	background:url(static/images/index/user_help.png) no-repeat center left;
}
.centerHeader{
	text-align: center;
}
.layout-button-left{background:url('static/images/common/left.png') no-repeat 0 0;}
.layout-button-right{background:url('static/images/common/right.png') no-repeat 0 0;}
.bangzhu{ padding: 5px;}
.bangzhu b{line-height: 30px;height: 30px;}
.bangzhu a{ color: #333; text-decoration: none; line-height: 30px; display: block;}
.bangzhu a:hover{ text-decoration:underline; color: blue;}
</style>
<script type="text/javascript">
var isLoad0 = false;//是否加载第一个面板
var isLoad1 = false;//是否加载第二个面板
$(function(){
	$.extend($.fn.validatebox.defaults.rules, {
	    equals: {
	        validator: function(value,param){
	            return value == $(param[0]).val();
	        },
	        message: '两次密码输入不匹配.'
	    },
	    phone:{
	    	validator:function(value,param){
	    		var pattern = /(^1\d{10}$|^\d{7,8}$|^0\d{2,3}-\d{7,8}$)/;
	    		return value.match(pattern);
	    	},
	    	message: '请输入正确的联系电话'
	    }
	});
	$('#mainTabs').tabs({
		//加载右侧页面
		onContextMenu:function(e,title,index){
			e.preventDefault();
			if(index>0){
				$('#tabsMenu').menu('show',{left:e.pageX,top:e.pageY});
			}
		},
		/*onSelect:function(title,index){
			if(index==0){
				$('#mainTabs').tabs('updateIframeTab',{
	            	which:index,
	            	iframe: {src:"static/admin_welcome"}
			    });
			}
		}*/
	});
	//加载主页面
	$('#mainTabs').tabs('updateIframeTab',{
		which:0,
		iframe: {src:"static/admin_welcome"}
	});
	//显示帮助页面
	$('#eamomsHelp').tooltip({
        content: $('<div class="bangzhu"></div>'),
        showEvent: 'click',
        hideDelay:300,
        onUpdate: function(content){
        	var ts = "<b>请下载对应帮助文档</b><br/>";
        	var xty = "<a href='login_downLoadHelp?docType=0'>-  环境自动监测（监控）运营管理系统操作手册(协调员)</a>";
        	var ywgcs = "<a href='login_downLoadHelp?docType=1'>-  环境自动监测（监控）运营管理系统操作手册(运维工程师)</a>";
        	var ywzr = "<a href='login_downLoadHelp?docType=2'>-  环境自动监测（监控）运营管理系统操作手册(运维主任)</a>";
            content.panel({
                width: 400,
                height: 150,
                border: false,
                content: ts+xty+ywgcs+ywzr
            });
        },
        onShow: function(){
            var t = $(this);
            t.tooltip('tip').unbind().bind('mouseenter', function(){
                t.tooltip('show');
            }).bind('mouseleave', function(){
                t.tooltip('hide');
            });
        }
    });
});
	function openTabs(name,url,iconCls){
		var tabs = $('#mainTabs').tabs('getTab',name);
		if(tabs){
			var index = $('#mainTabs').tabs('getTabIndex',tabs);
			$('#mainTabs').tabs('select',index);
		}else{
			 $('#mainTabs').tabs('addIframeTab',{
				 tab:{title: name,iconCls:iconCls, closable: true},
	             iframe: {src:url}
	         });
		}
	}
	
	function closeTab(action){
	    var alltabs = $('#mainTabs').tabs('tabs');
	    var currentTab =$('#mainTabs').tabs('getSelected');
	    var allTabtitle = [];
	    $.each(alltabs,function(i,n){
	        allTabtitle.push($(n).panel('options').title);
	    });
	    switch (action) {
	        case "refresh":
	            var title = currentTab.panel('options').title;
	            $('#mainTabs').tabs('updateIframeTab',{
	            	which:title
		         });
	            break;
	        case "close":
	            var currtab_title = currentTab.panel('options').title;
	            $('#mainTabs').tabs('close', currtab_title);
	            break;
	        case "closeall":
	            $.each(allTabtitle, function (i, n) {
	            	if (n != '个人面板'){
	                    $('#mainTabs').tabs('close', n);
	                }
	            });
	            break;
	        case "closeother":
	            var currtab_title = currentTab.panel('options').title;
	            $.each(allTabtitle, function (i, n) {
	            	if (n != currtab_title && n != '个人面板')
	                {
	                    $('#mainTabs').tabs('close', n);
	                }
	            });
	            $('#mainTabs').tabs('select',currtab_title);
	            break;
	    }
	}

	/** 
	* 销毁iframe，释放iframe所占用的内存。 
	* @param iframe 需要销毁的iframe对象 
	*/
	function destroyIframe(iframe){
		//把iframe指向空白页面，这样可以释放大部分内存。 
		iframe.src = 'about:blank'; 
		try{ 
			iframe.contentWindow.document.write(''); 
			iframe.contentWindow.document.clear(); 
		}catch(e){} 
		//把iframe从页面移除 
		//iframe.parentNode.removeChild(iframe); 
	}
	
	function closeSvgWin(){
		destroyIframe($("#svgIframe")[0]);
	}
</script>
<title>云平台管理系统</title>
<body class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height: 50px;overflow: hidden; background: url(static/images/index/main_top.png) repeat-x;">
			<div style="width: 580px; height: 50px; float: left;line-height:50px; padding-left:55px;color:#fff; margin-left: 10px; font-size:20px; font-family:'微软雅黑','黑体'; background: url(static/images/index/jzlogo.png) no-repeat left center; ">云平台管理系统</div>
		<div class="index_zt">
			<ul>
				<li class="icon-online"> <shiro:principal/> ,欢迎您访问！</li>
				<%-- <li class="icon-person-info">
					<!-- <a href="javascript:void(0);" onclick="$('#dlg').dialog('open');">用户设置</a> -->
					<a href="javascript:void(0);">用户设置</a>
				</li>
				<li class="icon-user-help"><a id="eamomsHelp" href="#">帮助</a></li> --%>
				<li class="icon-logout"><a
					href="logout">注销退出</a></li>
			</ul>
		</div>
	</div>
		<div data-options="region:'center',border:false" style="overflow: hidden;">
			<div id="mainTabs" data-options="fit:true">
    			<!-- <div title="主页" iconCls="icon-large-clipart" data-options="tools:[{
                        iconCls:'icon-mini-refresh',
                        handler:function(){
                            var title = $(this).parent().parent().text();
                            $('#mainTabs').tabs('updateIframeTab',{'which':title});
                        }
                    }]">
                </div> -->
			</div>
		</div>
		<div data-options="region:'south'" border="false" style="background-color:#2476B2;height: 20px;text-align: center;padding-top: 2px;">
			<span style="font-family: Arial, Verdana, sans-serif;color: #FFF;font-size: 12px;">粤ICP备16083573号-2 Copyright © 2013 everwing All rights reserved</span>
		</div>
		<div data-options="region:'west',split:false,collapsible:true,headerCls:'centerHeader',title:'导航菜单',hideCollapsedContent:false" border="false" style="overflow: hidden;width:235px;">
			<div class="easyui-accordion" data-options="fit:true">
				<div title="云平台管理" style="overflow:auto;" class="index-menu">
					<span >
                       <span class="mymenu" id="PkgUpgrade">
	                       	<a id="pkg" class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('升级包管理','appPkg/main','icon-soft')">
	                       		<span>升级包管理</span>
	                       	</a></br>
                       </span>
                       <span class="mymenu" id="CompanyAudit">
	                       <a class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('租户(公司)审核','companyApproval/main','icon-soft')">
	                       		<span>租户(公司)审核</span>
	                       	</a></br>
                       	</span>
                       	<span class="mymenu" id="Announcement">
	                       	<a class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('系统公告管理','announcement/main','icon-soft')">
	                       		<span>系统公告管理</span>
	                       	</a></br>
                       	</span>
                       	<span class="mymenu" id="AdminLog">
	                       	<a class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('后台日志管理','adminLog/main','icon-soft')">
	                       		<span>后台日志管理</span>
	                       	</a></br>
                       	</span>
                     </span>
        		</div>
				<div title="系统管理" style="overflow:auto;" class="index-menu">
					<span>
                       	<span class="mymenu" id="Account">
	                       	<a class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('账号管理','account/main','icon-soft')">
	                       		<span>账号管理</span>
	                       	</a></br>
                       	</span>
                       	<span class="mymenu" id="Role">
	                       	<a class="easyui-linkbutton" style="width:100%" href="javascript:void(0)" iconCls="icon-soft" plain="true"
	                       		onclick="openTabs('角色管理','role/main','icon-soft')">
	                       		<span>角色管理</span>
	                       	</a></br>
                       	</span>
                     </span>
        		</div>
        	</div>	
		</div>
		<!-- <div data-options="region:'east',split:true,collapsed:true,headerCls:'centerHeader',title:'即时聊天',hideCollapsedContent:false" border="false" style="width:225px;">
			<div class="easyui-accordion" data-options="fit:true"></div>	
		</div> -->
	<div id="tabsMenu" class="easyui-menu" data-options="onClick:function(item){closeTab(item.id);}" style="width: 150px;">
		<div id="refresh">重新加载</div>
		<div class="menu-sep"></div>
		<div id="close">关闭标签页</div>
		<div id="closeother">关闭其他标签页</div>
		<div id="closeall">关闭所有标签页</div>
	</div>
	
</body>
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}
.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
	text-align: right;
}

.fitem input {
	width: 220px;
}	
.fitemFile{
	width: 224px!important;
}
</style>
<script type="text/javascript">
var isLoadForm = false;
function showPhotoImg(data){
	isLoadForm = true;
	var photoName = data['user.photo'];
	if(photoName){
		$('#photoImg').attr("src","system/user_loadPhoto?user.id="+data['user.id']+"&user.photo="+photoName+"&_="+new Date().getTime());
	}else{
		$('#photoImg').attr("src","static/images/system/zwtx.jpg");
	}
}
function validFile(){
	var filename=$('#photo').val();
	var suffix = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
	if(!/(jpg|jpeg|png)$/.test(suffix)){
		layer.msg('图片类型必须是jpeg,jpg,png中的一种', {icon: 2,time: 5000});
		$('#photo').val('');
	}
}
function saveUser(){
	 $('#fm').form('submit',{
        url: "system/user_updateUserInfo",
        onSubmit: function(param){
       		var isValid = $(this).form('validate');
			 if(isValid){
				 $.messager.progress({
		                title:'请稍后',
		                msg:'数据保存中...'
		            });
				 return true;
			 }        		
            return false;
        },
        success: function(result){
	       	 $.messager.progress('close');
	         if (result=='success'){
	         	isLoadForm = false;
	        	$('#dlg').dialog('close');
	         }else if(result=='error'){
	       		layer.msg('保存个人信息失败', {icon: 2,time: 5000});
	         }else{
	         	layer.msg(result, {icon: 2,time: 5000});
	         }
        }
    });
}

$(function(){
	$('.mymenu').hide()
	$.ajax({
		  type: "GET",
		  url: "permission/currUserPermissioin",
		  dataType: "json",
		  success: function(result){
			  $(result).each(function(){//显示有权限的菜单
				  //console.info("权限：",this.permissionId)
				  $('#'+this.permissionId).show();
			  })
			  
			  var visibleMenus=$('.mymenu a:visible');
			  if(visibleMenus.length>0){//展示第一个可见菜单的页面
				  visibleMenus[0].click();
			  }
		  }
	});
})
</script>
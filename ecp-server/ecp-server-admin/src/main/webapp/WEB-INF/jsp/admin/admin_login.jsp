<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="${pageContext.request.contextPath}/"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>云平台管理系统</title>
<script type="text/javascript">
	var topHref = window.top.location.href 
	var currHref = window.location.href 
	if(topHref != currHref){
		window.top.location.href = window.location.href
	}
</script>
<script type="text/javascript" src="static/js/jqueryEasyUI/jquery.min.js"></script>
<script type="text/javascript" src="static/js/layer/layer.js"></script>
<style type="text/css">
* {
	margin: 0px;
	padding: 0px;
}

html {
	font-family: 'Microsoft YaHei', 'SimHei', serif;
	background: #fff url(static/images/login/bg.jpg) repeat-x top;
}

body {
	font-family: 'Microsoft YaHei', 'SimHei', serif;
	font-size: 12px;
	overflow: hidden
}

.logo {
	padding-left: 68px;
	width: 500px;
	background: url(static/images/login/logo.png)
		no-repeat;
	position: relative;
	top: 26px;
	left: 30px;
	height: 59px;
}

.logo span {
	font-size: 22px;
	font-family: 'Microsoft YaHei', 'SimHei';
	color: #1777b1;
	line-height: 35px;
}

.logo b {
	font-size: 13px;
	line-height: 20px;
	font-weight: normal;
	color: #1777b1;
}

.login_main {
	background:
		url(static/images/login/login_bg.png)
		no-repeat top;
	width: 985px;
	height: 514px;
	/*以下为IE6设置PNG透明代码
                      _background:none;
                      _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="static/img/login/login_bg.png");*/
	margin-left: -493px;
	position: absolute;
	left: 50%;
	top: 50%;
	margin-top: -257px;
}

.login_img {
	width: 537px;
	height: 363px;
	background: url(static/images/login/yunyin.jpg)
		no-repeat;
	position: relative;
	top: 38px;
	left: 41px;
}

.login_logo {
	height: 43px;
	line-height: 43px;
	color: #fff;
	font-size: 18px;
	text-align: center;
}

.login_cpright {
	float: left;
	width: 560px;
	margin: 256px 0 0 80px;
	font-size: 12px;
	text-align: center;
	color: #000;
	font-family: Verdana, Geneva, sans-serif;
}

.login_form {
	position: relative;
	left: 620px;
	width: 325px;
	height: 354px;
	top: -324px;
	background:
		url(static/images/login/from_bg.jpg)
		no-repeat top;
}

.login_left {
	float: left;
	width: 680px;
}

.login_dl {
	font-size: 14px;
	color: #000;
	width: 85px;
	height: 55px;
	line-height: 55px;
	padding-right: 5px;
}

.login_input {
	height: 31px;
	width: 185px;
	text-indent: 3px;
	padding-left: 22px;
	border: 1px solid #d2dbe3;
	box-shadow: inset 0 3px 3px rgba(0, 0, 0, 0.1), 0 1px 0
		rgba(255, 255, 255, 0.2);
}

.login_Verify {
	height: 21px;
	line-height: 21px;
	padding: 5px 0;
	width: 70px;
	text-indent: 3px;
	border: 1px solid #d2dbe3;
	box-shadow: inset 0 3px 3px rgba(0, 0, 0, 0.1), 0 1px 0
		rgba(255, 255, 255, 0.2);
}

.login_jzmm {
	height: 24px;
	line-height: 24px;
	color: #000;
}

.login_btnbg {
	height: 36px;
	width: 118px;
	border: 0px;
	cursor: pointer;
	background:
		url(static/images/login/login_but1.png)
		no-repeat;
	color: #fff;
	font-size: 18px;
	margin-top: 20px;
	font-family: 'Microsoft YaHei', 'SimHei';
}

.login_btnbg:hover {
	background:
		url(static/images/login/login_but2.png)
		no-repeat;
}

.username_bg {
	background:
		url(static/images/login/username_bg.png)
		no-repeat 5px center; background-color: #fff;
}

.password_bg {
	background:
		url(static/images/login/password_bg.png)
		no-repeat 5px center;background-color: #fff;
}

.app_down {
	position: absolute;
	right: 50px;
	top: 40px;
	z-index: 999;
	width: 160px;
}

.app_down .pc_down {
	padding: 5px 18px 5px 30px;
	background: url(static/images/login/app.png)
		no-repeat 4px center #f4f5f9;
	display: inline-block;
	float: left;
	border: 1px solid #d0dbe1;
	cursor: pointer;
	border-radius: 3px; /* 所有角都使用半径为5px的圆角，此属性为CSS3标准属性 */
	-moz-border-radius: 3px; /* Mozilla浏览器的私有属性 */
	-webkit-border-radius: 3px; /* Webkit浏览器的私有属性 */
}

.app_down a:hover {
	color: orange;
}

.app_down .ma_down {
	display: block;
	border: 0px solid #d0dbe1;
	height: 28px;
	cursor: pointer;
}

.app_down a {
	color: #606060;
	text-decoration: none;
}

.app_down .ma_down img {
	display: block;
	height: 26px;
}

#verifyInfo {
	padding-left: 5px;
	font-size: 12px;
}

.domw_app {
	width: 230px;
	height: 320px;
	border: 1px solid #e7e7e7;
	background: #FFF;
	-webkit-box-shadow: 3px 3px 10px #ccc;
	-moz-box-shadow: 3px 3px 10px #ccc;
	box-shadow: 3px 3px 10px #ccc;
	right: 0px;
	top: 37px;
	position: absolute;
	float: right;
	z-index: 10000;
	display: none;
}

.domw_app .point {
	display: block;
	height: 10px;
	width: 16px;
	margin-top: -9px;
	background:
		url(static/images/login/down_point.png) no-repeat;
	position: absolute;
	right:5px;
/* 	margin-left: 210px;
	*_margin-left: 120px; */
	
}

.domw_app .app_button {
	display: block;
	margin-left:44px;
	position:relative; top:40px;
	margin-bottom: 10px;
	width: 144px;
	height: 33px;
	line-height: 33px;
	background:
		url(static/images/login/down_app2.png)
}

.domw_app a:hover {
	background:
		url(static/images/login/down_app1.png)
}

.domw_app .down_qr {
	margin-left:25px;
	width: 180px;
	height: 180px;
	position:relative; top:60px;
}

.domw_app .down_qr img {
	width: 180px;
	height: 180px;
}
</style>
<!--[if IE]>
<style type="text/css">
 .login_input{line-height:31px;} 
 .point{margin-left:100px;}
</style>
<![end if]-->
<script type="text/javascript" src="static/js/jqueryEasyUI/jquery.min.js"></script>
<script type="text/javascript">
var topHref = window.top.location.href 
var currHref = window.location.href 
if(topHref != currHref){
	window.top.location.href = window.location.href
}
	$(function() {
		$("#username").focus();
	});
	function login() {
		var name = trim(document.getElementById("username").value);
		var password = trim(document.getElementById("password").value);
		if (name == "") {
			layer.msg('请输入用户名', {icon: 2,time: 5000});
		} else if (password == "") {
			layer.msg('请输入密码', {icon: 2,time: 5000});
		} else {
			document.getElementById("form").submit();
		}
	}
	function checkedUname_len(){
		var username = trim(document.getElementById("username").value);
		if (username.length>16) {
				layer.msg('用户名不能超过30位', {icon: 2,time: 5000});
		}
	}
	function checkedPwd_len(){
		var password = trim(document.getElementById("password").value);
		if (password.length>16) {
				layer.msg('密码长度不能超过16位', {icon: 2,time: 5000});
		}
	}
	function trim(str) {
		return str.replace(/(^\s+)|(\s+$)/g, "");
	}
</script>
</head>
<body>
	<div class="login_main">
		<div class="app_down">
			
			<div class="ma_down">
				<span style=" font-size: 12px;float: left;line-height: 28px;">手机客户端下载&nbsp;&nbsp;&nbsp;</span>
				<img id="appleDownLoad"
					src="static/images/login/iphone_icon.png"
					width="26" height="26"
					style="margin-bottom: -10px; cursor: pointer; float: left" /> 
				<img id="androidDownLoad"
					src="static/images/login/android_icon.png"
					width="26" height="26"
					style="margin-bottom: -10px; cursor: pointer; float: right" />

				<div class="domw_app" id="qrcode_box">
					<b class="point"></b> <a href="static/system/apkVersion_downloadFrompc" target="_blank" class="app_button"></a>
					<div class="down_qr">
						<img id="downurl" src="static/images/login/qrcode.png" />
					</div>
				</div>
			</div>
		</div>

		<div class="logo">
			<span>云平台管理系统</span><br/> <b>Project management subsystem</b>
		</div>
		<div class="login_img"></div>
		<div class="login_form">
			<form id="form" action="login" method="post">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="position: relative; top: 50px;">
					<tr>
						<td align="right" class="login_dl">用户名：</td>
						<td colspan="3"><input name="username" id="username" type="text" class="login_input username_bg"  maxlength="30" /></td>
					</tr>
					<tr>
						<td align="right" class="login_dl">密 码：</td>
						<td colspan="3"><input name="password" id="password"
							type="password" onkeyup="if(event.keyCode==13){login();}"
							class="login_input password_bg" maxlength="16"/></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="left" height="26" colspan="3"><span id="errorMsg" style="color: red;">${errorMsg}</span></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="left" height="26" class="login_jzmm" colspan="3"><label><input
								type="checkbox" name="rememberMe" value="true" class="checkbox" />
								记住密码</label></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="left" colspan="3"><input type="button"
							class="login_btnbg" onclick="login();" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div style="height: 35px; text-align: center; font-size: 14px; font-family: Verdana, Geneva, sans-serif; color: #606060; position: absolute; top: 50%; margin-top: 280px; width: 100%;">深圳市翔恒科技开发有限公司
		Copyright@2014 版权所有</div>
</body>
</html>
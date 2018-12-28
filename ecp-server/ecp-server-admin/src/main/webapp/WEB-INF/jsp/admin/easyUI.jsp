<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jqueryEasyUI/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/utils/ajaxInterceptor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jqueryEasyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jqueryEasyUI/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/zyupload/zyupload-1.0.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/checkValue.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/utils/jquery.cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/utils/theme.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/layer/layer.js"></script>
<!-- MY97时间控件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
    
<%
String easyuiThemeName = "bootstrap";
Cookie cookies[] = request.getCookies();
if(cookies != null && cookies.length >0){
	for(int i=0; i<cookies.length; i++){
		if(cookies[i].getName().equals("easyuiThemeName")){
			easyuiThemeName = cookies[i].getValue();
				break ;
		}
	}
}	
%>
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/<%=easyuiThemeName %>/easyui.css">
<script type="text/javascript">
	$(function(){
		$('body').css('visibility','visible');
	})
</script>
<style>
body{visibility: hidden;}
</style>
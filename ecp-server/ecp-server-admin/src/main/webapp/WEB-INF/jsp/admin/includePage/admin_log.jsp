<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title>后台日志管理</title>
<body>
	<div id="tbar" style="height: 25px;">
		<div style="margin-left: 10px;">
			<input class="easyui-datebox" data-options="prompt:'开始时间'" id="timeStart" name="timeStart"/>至
			<input class="easyui-datebox" data-options="prompt:'结束时间'" id="timeEnd" name="timeEnd" validType="dateValid['#timeStart']"/>
			<input class="easyui-textbox" data-options="prompt:'操作账号/日志内容', width:150" id="queryContent" name="queryContent"/>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">查询</a>
			<div style="display:inline-block;text-align:right;width:49%;">
				<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'" onclick="searchData(true)">下载查询数据</a>
			</div>
		</div>
	</div>
	<!-- 
		{
		    "limitEnd": 30,
		    "limitStart": 0,
		    "rows": [
		        {
		            "operationDescription": "admin审核公司[xxx有限公司]通过",
		            "businessName": "test",
		            "createTime": 1499845050000,
		            "isSuccess": true,
		            "logSourceType": "1",
		            "projectId": "test",
		            "modelName": "test",
		            "operationType": 1,
		            "operationUserName": "admin",
		            "operationLogId": "test-1",
		            "companyId": "test",
		            "operationRoleName": "test"
		        }
		    ],
		    "total": 1
		}
	 -->
	<div class="easyui-layout" fit="true">
		<div region="center" border="false">

			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar" sortName="createTime" sortOrder="desc" 
			   url="adminLog/list" method="post" pagination="true"  pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:false,fit:true,fitColumns:true">
			    <thead>
					<tr>
				      <th data-options="field:'ck',checkbox:true" width="50"><input type="checkbox" /></th>
				      <th data-options="field:'createTime',align:'center',halign:'center'" width="30" formatter="formatDateTime">日志时间</th>
				      <th data-options="field:'operationUserName',align:'center',halign:'center'" width="30" >操作账号</th>
				      <th data-options="field:'operationDescription',align:'center',halign:'center'"  width="100" >日志内容</th>
 				      <th data-options="field:'isSuccess',align:'center',halign:'center'"  width="20" formatter="formatResult">结果</th>
				 	</tr>
			    </thead>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript" src="static/js/base.js"></script>
<script type="text/javascript">
function formatResult(data){
	return data ? "成功":"失败";
}

function searchData(isDownload){
    var dt1=$('#timeStart').datebox('getValue');
    var dt2=$('#timeEnd').datebox('getValue');
	var dt3 = new Date(dt1.replace(/-/g, "/")); //转换成日期类型
	var dt4 = new Date(dt2.replace(/-/g, "/")); //转换成日期类型
	if(dt1!=""&&dt3=='Invalid Date'){
		layer.msg('起始时间格式不对!', {icon: 2,time: 5000});
		return false;
	}
	if(dt2!=""&&dt4=='Invalid Date'){
		layer.msg('结束时间格式不对!', {icon: 2,time: 5000});
		return false;
	}
	if(dt3 >= dt4){
		layer.msg('结束时间要大于开始时间!', {icon: 2,time: 5000});
		return false;
	}
	if(isDownload){
		var queryContent = $('#queryContent').val();
		var timeStart = $('#timeStart').datebox('getValue');
		var timeEnd = $('#timeEnd').datebox('getValue');
		window.open(encodeURI("adminLog/export?queryContent="+queryContent+"&timeStart="+timeStart+"&timeEnd="+timeEnd));
	}else{
		$('#dg').datagrid('load',{
			'queryContent': $('#queryContent').val(),
			'timeStart': $('#timeStart').datebox('getValue'),
			'timeEnd': $('#timeEnd').datebox('getValue')
		});
	}
}

</script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/color.css">
<style>
.role_root{background:url('static/images/common/world.png') no-repeat center center!important;}
.role_leaf{background:url('static/images/common/user.png') no-repeat center center!important;}
</style>

</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.myTable td, .myTable th{
	padding: 3px;
}
</style>
<title>系统公告管理</title>
<body>
	<div id="tbar" style="height: 25px;">
		<div style="margin-left: 10px;">
			关键字搜索：<input class="easyui-textbox" id="query_content" data-options="prompt:'标题/内容/公司名'" />&nbsp;&nbsp;
			状态：<select name="status" id="status">
				<option value = "">全部</option>
				<option value = "0">未发布</option>
				<option value = "1">已发布</option>
			</select>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">查询</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'add'" onclick="newData()">新增</a>
			<div style="display:inline-block;text-align:right;width:49%;">
				<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'" onclick="release()">发布</a>
			</div>
		</div>
	</div>
	<!-- 
	{
    "limitEnd": 15,
    "limitStart": 0,
    "rows": [
        {
            "content": "-=-=-",
            "accountName": "admin66",
            "title": "系统升级公告",
            "status": 1,
            "announcementId": "qw",
            "companyName": "Monkong7",
            "createDate": 1483286400000,
            "createAccountId": "992",
            "targetCompanyId": "09841dc0-204a-41f2-a175-20a6dcee0187"
        }
    ],
    "total": 1
}
	 -->
	<div class="easyui-layout" fit="true">
		<div region="center" border="false">
			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar" sortName="" sortOrder="" 
			   url="${pageContext.request.contextPath}/announcement/list" method="post" pagination="true"  pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:false,fit:true,fitColumns:true">
			    <thead>
					<tr>
				      <th data-options="field:'ck',checkbox:true" width="50"><input type="checkbox" /></th>
				      <th data-options="field:'title',align:'center',halign:'center'" width="100">标题</th>
				      <th data-options="field:'companyName',align:'center',halign:'center'" width="100" formatter="formatCompanyName">发送目标公司</th>
				      <th data-options="field:'createDate',align:'center',halign:'center'"  width="50" formatter="formatDate">创建日期</th>
				      <th data-options="field:'accountName',align:'center',halign:'center'"  width="80">创建人账号</th>
				      <th data-options="field:'status',align:'center',halign:'center'"  width="80"  formatter="formatState">状态</th>
				      <th data-options="field:'everyThing',align:'center',halign:'center'"  width="80" formatter="formatOperation">操作</th>
				 	</tr>
			    </thead>
			</table>
		</div>
	</div>
	<!--  
	  -->
	<div id="newDlg" class="easyui-dialog" style="width:600px;height:400px;"
            closed="true" modal="true" buttons="#dlg-buttons" title="创建公告">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
				<form id="formSave" method="post" action="announcement/save" novalidate>
		    		<br><table class="myTable" align="center">
						<tr>
							<td>发送对象:</td>
							<td>
								<select name="targetCompanyId" id="type">
									  <option value = "all">所有</option>
									  <c:forEach items="${companyList }" var="company">
										  <option value = "${company.companyId }">${company.companyName }</option>
									  </c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>立即发布:</td>
							<td><input name="status" class="easyui-switchbutton" data-options="onText:'是',offText:'否',value:'1'" /></td><!-- 0=未发布 1=已发布 -->
						</tr>
						<tr>
							<td>公告标题:</td>
							<td><input name="title" class="easyui-textbox" required="true" missingMessage="公告标题" style="width:250px"/></td>
						</tr>
						<tr>
							<td>内容：</td>
							<td><textarea name="content" required="true"  rows="10" cols="70"></textarea></td>
						</tr>
		    		</table>
		        </form>
			</div>
    	</div>
         <div id="dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="$('#formSave').submit()" style="width:90px">保存</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#newDlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
	

	<div id="detailDlg" class="easyui-dialog" title="公告详情" data-options="iconCls:'icon-save',closed:true" style="width:500px;height:250px;padding:10px">
        The dialog content.
    </div>
</body>
<script type="text/javascript">

function formatCompanyName(value,row,index){
	return value ? value : '<所有公司>'
}

function formatState(value,row,index){
	var state = Number(row['state']); 
	return ['未发布','已发布'][value];
}
var dataListMap={};
function formatOperation(value,row,index){
	var id = row['announcementId'];
	dataListMap[id]=row;
	return '<a href="javascript:void(0)" onclick="detailInfo(\''+id+'\')">查看详情</a>';
}

function searchData(){
	$('#dg').datagrid('load',{
		query_content: $('#query_content').val(),
		status : $('#status').val()
	});
}
function formatDate(date){
	if(date){
		return moment(new Date(date)).format('YYYY-MM-DD')
	}else{
	 	return ""	
	}
}


function release(){
	var check = $('#dg').datagrid('getChecked');
	if(check.length <= 0){
		layer.msg("请选择操作数据",{icon:2,time:5000});
		return ;
	}
	var ids='';
	for(var i=0;i<check.length;i++){
		ids = ids+check[i].announcementId+','
	}
	
	$.ajax({
		   type: "get",
		   url: "announcement/release",
		   data: "announcementIds="+ids,
		   dataType: "json",
		   success: function(data){
				if(data.code === 0){
					layer.msg("发布成功",{icon:1,time:3000});
				} else {
					layer.msg("发布失败",{icon:2,time:3000});
				}
			    $('#dg').datagrid('reload');
		   },
			error: function(data){
				layer.msg("服务器错误.",{icon:1,time:3000});
			}
		});
	
}

function detailInfo(id,event){
	window.event? window.event.cancelBubble = true : event.stopPropagation();
	 var currRow = dataListMap[id]
	
	$('#detailDlg').html(currRow['content']);
	
	$('#detailDlg').dialog('open');
}

function newData(){
	$('input[name="title"]').val('')
	$('input[name="content"]').val('')
	$('#newDlg').dialog('open')
}

	$('#formSave').form({
	    url:'announcement/save',
	    onSubmit:function(param){
	    	console.info(param)
	    	param.testM='monkong'
	        return $(this).form('validate');
	    },
	    success:function(data){
			   $('#newDlg').dialog('close');
			   $('#dg').datagrid('reload');
	    }
	});
function mysubmit(){
	
	
	//$('#formSave').submit()
	/* $("#formSave").submit();
			   $('#newDlg').dialog('close');
			   $('#dg').datagrid('reload'); */
	//设置status的值
	/* console.info(formData)
	$.ajax({
		   type: "POST",
		   url: "announcement/save",
		   data: formData,
		   dataType:"json",
		   success: function(msg){
			   $('#newDlg').dialog('close');
			   $('#dg').datagrid('reload');
		   }
		});  */
}
</script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/color.css">
<style>
.role_root{background:url('static/images/common/world.png') no-repeat center center!important;}
.role_leaf{background:url('static/images/common/user.png') no-repeat center center!important;}
</style>

</html>
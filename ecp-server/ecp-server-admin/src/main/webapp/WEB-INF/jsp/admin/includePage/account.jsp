<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<title>账号管理</title>
<body>
<style type="text/css">
	.add {background-image: url("static/images/common/add.png");}
	.delete {background-image: url("static/images/common/delete.png");}
	.search {background-image: url("static/images/common/search.png");}
	.update {background-image: url("static/images/common/update.png");}
</style>
<style type="text/css">
	#fm{
	    margin:0;
	    padding:10px 60px;
	}
	
	.fitem{
	    margin-bottom:10px;
	}
	.fitem label{
	    display:inline-block;
	    width:60px;
	    text-align: right;
	}
	.fitem input{
	    width:200px;
	}
</style>

	<div id="tbar" style="height: 25px;">
		<div style="margin-left: 10px;">
			<input class="easyui-textbox" data-options="prompt:'用户账号/工号/姓名/创建人账号名', width:300" id="queryContent" />
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">搜索</a>
			
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'add'" onclick="openNew()">新增</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'update'" onclick="openUpdate();">修改</a>
		</div>
	</div>
    
		    	
	<div id="update-dlg" class="easyui-dialog" style="width:440px;height:500px;"
            closed="true" modal="true" buttons="#update-dlg-buttons" title="修改用户">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
		    	<form id="updateFm" method="post">
		    	<input type="hidden" id="updateAccountId" name="accountId">
		    	<input type="hidden" id="updateAdminInfoId" name="adminInfoId">
		    		<table align="center">
		    			<tr>
		    				<td>
					            <div class="fitem">
					                <label style="width:80px">用户账号:</label>
					                <input id="accountName" name="accountName" class="easyui-textbox" required="true" missingMessage="请输入用户账号" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">用户工号:</label>
					                <input id="workNum" name="workNum" class="easyui-textbox"  missingMessage="请输入用户工号" >
					            </div>
					            <div class="fitem">
					                <label style="width:80px">用户姓名:</label>
					                <input id="realName" name="realName" class="easyui-textbox" required="true" missingMessage="请输入用户姓名" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					        		<label style="width:80px">角色:</label>
			                		<ul id="updateRoleTree">加载中...</ul>
					   			</div>
		    				</td>
		    			</tr>
		    		</table>
		        </form>
    		</div>
    	</div>
         <div id="update-dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="updateData()" style="width:90px">修改</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#update-dlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
		    	
	<div id="dlg" class="easyui-dialog" style="width:440px;height:500px;"
            closed="true" modal="true" buttons="#dlg-buttons" title="新增用户">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
		    	<form id="fm" method="post">
		    		<table align="center">
		    			<tr>
		    				<td>
					            <div class="fitem">
					                <label style="width:80px">用户账号:</label>
					                <input name="accountName" class="easyui-textbox" required="true" missingMessage="请输入用户账号" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">用户工号:</label>
					                <input name="workNum" class="easyui-textbox"  missingMessage="请输入用户工号" >
					            </div>
					            <div class="fitem">
					                <label style="width:80px">用户姓名:</label>
					                <input name="realName" class="easyui-textbox" required="true" missingMessage="请输入用户姓名" data-options="validType:{length:[0,30]}">
					            </div>
					                <label style="width:80px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;性别: &nbsp;</label>
					                <!-- <input name="sex"  class="easyui-textbox" required="true" missingMessage="请输入用户姓名"> -->
					                男 <input name="sex" type="radio" value="1" checked/> 
					                女 <input name="sex" type="radio" value="0"/>
					            <!-- <div class="fitem">
					            </div> -->
					            <div class="fitem">
					                <label style="width:80px">密码:</label>
					                <input type="password" id="password"  name="password" class="easyui-textbox" required="true" missingMessage="请输入密码" data-options="validType:{length:[6,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">确认密码:</label>
					                <input type="password" name="repassowrd" class="easyui-textbox" required="true" missingMessage="请再次输入密码" validType="equalTo['#password']" invalidMessage="两次输入密码不匹配">
					            </div>
					            <div class="fitem">
					        		<label style="width:80px">角色:</label>
			                		<ul id="roleTree">加载中...</ul>
					   			</div>
		    				</td>
		    			</tr>
		    		</table>
		        </form>
    		</div>
    	</div>
         <div id="dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="saveData()" style="width:90px">保存</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
    <!-- 
{
    "limitEnd": 30,
    "limitStart": 0,
    "rows": [
        {
            "roleList": [
                "test角色"
            ],
            "createTime": 1501579223000,
            "accountName": "admin",
            "sex": true,
            "accountId": "991",
            "updateTime": 1501579225000,
            "createrName": "admin",
            "state": 1,
            "adminInfoId": "2321",
            "createrAccountId": "991",
            "realName": "Monkong",
            "workNum": "1001"
        }
    ],
    "total": 1
}
      -->
    
    
	<div class="easyui-layout" fit="true">
		<div region="center" border="false">
			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar"  
			   url="account/list" method="post" pagination="true"
			   pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:true,fit:true,fitColumns:true" sortName="" sortOrder="">
			    <thead>
					<tr>
				      <!-- <th data-options="field:'accountId'"  width="50">用户编号</th> -->
				      <th data-options="field:'accountName'" width="30">用户账号</th>
				      <th data-options="field:'workNum'"  width="50">用户工号</th>
				      <th data-options="field:'sex'"  width="20" formatter="formatSex">性别</th>
				      <th data-options="field:'state'"  width="30"  formatter="formatStatus">状态</th>
				      <th data-options="field:'roleList'" width="100" >用户角色</th>
				      <th data-options="field:'createrName'"  width="30" >创建人</th>
				      <th data-options="field:'createTime'"  width="50"  formatter="formatDateTime">创建时间</th>
				      <th data-options="field:'updateTime'"  width="50"  formatter="formatDateTime">更新时间</th>
				 	</tr>
			    </thead>
			</table>
		</div>
	</div>
</body>

<script type="text/javascript" src="static/js/base.js"></script>
<script type="text/javascript">
function openUpdate(){
	
	var row = $('#dg').datagrid('getSelected')
	if (row){
		$('#update-dlg').dialog('open')
		$('#updateAccountId').val(row.accountId)
		$('#updateAdminInfoId').val(row.adminInfoId)
		$('#accountName').textbox('setValue',row.accountName)
		$('#workNum').textbox('setValue',row.workNum)
		$('#realName').textbox('setValue',row.realName)
		
		$('#updateRoleTree').tree({
		    url:'role/treeView/account/'+row.accountId,
		    checkbox:true,
		    lines:true,
		    method:"get"
		});
	}else{
		layer.msg('请选择一行操作数据', {icon: 2,time: 5000});
	}
}
function openNew(){
	$('#dlg').dialog('open')
	$('#roleTree').tree({
	    url:'role/treeView/allRole',
	    checkbox:true,
	    lines:true,
	    method:"get"
	});
}

function formatSex(value,row,index){
	return row.sex?'男':'女'
}

function formatStatus(value,row,index){
	var str =  ['失效','有效'][row.state]
	var result = "<a href='javascript:void(0);'  onclick='updateForce("+"\""+ row.accountId +"\" ,"+ row.state +")'>"+str+"</a>";
	return result;
}

function openPermission(roleId){
	//根据roleid查询权限树
	$('#roleDlg').dialog('open')
	$('#roleTree2').tree({
	    url:'permission/treeView/role/' + roleId,
	    checkbox:true,
	    lines:true,
	    method:"get"
	});
}

function formatPermission(value,row,index){
	return "<a href='javascript:openPermission(\""+row.roleId+"\")'>权限详情</a>";
}

function updateForce(id,status){
	var opt = "";
	var falg="";
	if(status){
		opt = "失效";
		falg="0";
	}else{
		opt = "有效";
		falg="1";
	}
	$.messager.confirm('提示','确定要【'+opt+'】此账号吗? ',function(r){
     	if(r){
	        	$.messager.progress({
		                title:'请稍后',
		                msg:'正在'+opt+'数据...'
		            });
	            $.ajax({
	            	url:'account/updateOnlyAccount',
	            	type:"post",
	            	data:{'accountId':id,'state':falg},
	            	dataType:"json",
	        		success:function(data){
	        			$.messager.progress('close');
	        			if (data.code===0){
	                    	$('#dg').datagrid('reload');
	                    }else if(data=='error'){
	                   	    layer.msg(opt+'数据失败', {icon: 2,time: 5000});
	                    }else{
	                    	layer.msg(data, {icon: 2,time: 5000});
	                    }
	        		},
	        		error:function(request,msg){
	        			$.messager.progress('close');
	        			layer.msg(opt+'数据失败:'+msg, {icon: 2,time: 5000});
	        		}
	            });
     	}
     });
}
function updateData(){
	 $('#updateFm').form('submit',{
         url: 'account/update',
         onSubmit: function(param){
        	 var isValid = $(this).form('validate');
			 if(isValid){
				 var nodes = $('#updateRoleTree').tree('getChecked');
				 var permissionIds='';
				 for(var i=0,len=nodes.length;len>i;i++){
					 if(i==len-1){
						 permissionIds+=nodes[i].id
					 }else{
						 permissionIds+=nodes[i].id+","
					 }
				 }
				 param.roleIds = permissionIds;
				 $.messager.progress({
		                title:'请稍后',
		                msg:'数据保存中...'
				});
			 }        		
             return isValid;
         },
         success: function(result){
        	 result=eval('('+result+')');
        	 $.messager.progress('close');
             if (result.code===0){
            	 $('#update-dlg').dialog('close');
            	 location.reload();
             }else if(result=='error'){
            	 layer.msg('修改数据失败', {icon: 2,time: 5000});
             }else{
             	layer.msg(result+"", {icon: 2,time: 5000});
             }
         }
     })
}
/**
 * 保存数据
 */
function saveData(){
	 $('#fm').form('submit',{
         url: 'account/save',
         onSubmit: function(param){
        	 var isValid = $(this).form('validate');
			 if(isValid){
				 var nodes = $('#roleTree').tree('getChecked');
				 var roleIds='';
				 for(var i=0,len=nodes.length;len>i;i++){
					 if(i==len-1){
						 roleIds+=nodes[i].id
					 }else{
						 roleIds+=nodes[i].id+","
					 }
				 }
				 param.roleIds = roleIds;
				 $.messager.progress({
		                title:'请稍后',
		                msg:'数据保存中...'
				});
			 }        		
             return isValid;
         },
         success: function(result){
        	 result=eval('('+result+')');
        	 console.log('4--',result)
        	 $.messager.progress('close');
             if (result.code===0){
            	 $('#dlg').dialog('close');
            	 location.reload();
             }else if(result=='error'){
            	 layer.msg('保存数据失败', {icon: 2,time: 5000});
             }else{
             	layer.msg(result+"", {icon: 2,time: 5000});
             }
         }
     })
}
/**
 * 删除软件管理信息
 */
function destroySoft(){
    var row = $('#dg').datagrid('getSelected');
    if (row){
    	var soft_id = row['appPkgId'];
        $.messager.confirm('提示','确定要删除此软件信息?',function(r){
        	if(r){
	        	$.messager.progress({
		                title:'请稍后',
		                msg:'正在删除软件...'
		            });
	            $.ajax({
	            	url:'appPkg/delete?id='+soft_id,
	            	dataType:"json",
	        		success:function(data){
	        			$.messager.progress('close');
	        			if (data.code === 0){
	                	 	$('#dlg').dialog('close');
	                    	$('#dg').datagrid('reload');
	                    }else if(data=='error'){
	                   	    layer.msg('删除软件失败', {icon: 2,time: 5000});
	                    }else{
	                    	layer.msg(data, {icon: 2,time: 5000});
	                    }
	        		},
	        		error:function(request,msg){
	        			$.messager.progress('close');
	        			layer.msg('删除软件失败:'+msg, {icon: 2,time: 5000});
	        		}
	            });
        	}
        });
    }else{
    	layer.msg('请选择一行操作数据', {icon: 2,time: 5000});
    }
}

function searchData(){
	$('#dg').datagrid('load',{
		'queryContent': $('#queryContent').val(),
	});
}

</script>
<script type="text/javascript">
$.extend($.fn.validatebox.defaults.rules, {  
    /*必须和某个字段相等*/
    equalTo: {
        validator:function(value,param){
            return $(param[0]).val() == value;
        },
        message:'字段不匹配'
    }
           
});
</script>
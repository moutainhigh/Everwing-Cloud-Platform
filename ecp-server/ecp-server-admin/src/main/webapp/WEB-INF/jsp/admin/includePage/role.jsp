<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<title>角色管理</title>
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
	    margin-bottom:5px;
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
			<input class="easyui-textbox" data-options="prompt:'角色名称/角色描述/创建人', width:200" id="queryContent" />
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">搜索</a>
			
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'add'" onclick="openNew()">新增</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'update'" onclick="openUpdate();">修改</a>
			<!-- <a class="easyui-linkbutton" data-options="plain:true,iconCls:'delete'" onclick="openDisable();">失效</a> -->
		</div>
	</div>
	<div id="roleDlg" class="easyui-dialog" style="width:400px;height:250px;"
            closed="true" modal="true" buttons="#roleDlg-buttons" title="查看权限">
    	<div  fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
    			<ul id="roleTree2"></ul>
    		</div>
    	</div>
    </div>
    
	<div id="update-dlg" class="easyui-dialog" style="width:440px;height:370px;"
            closed="true" modal="true" buttons="#update-dlg-buttons" title="修改角色">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
		    	<form id="updateFm" method="post">
		    	<input type="hidden" id="updateRoleId" name="roleId">
		    		<table align="center">
		    			<tr>
		    				<td>
					            <div class="fitem">
					                <label style="width:80px">角色名称:</label>
					                <input id="updateRoleName" name="roleName" class="easyui-textbox" required="true" missingMessage="请输入角色名称" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">角色描述:</label>
					                <input id="updateRoleDecsription" name="roleDecsription"  data-options="height:70, multiline:true"  missingMessage="请输入角色描述" class="easyui-textbox">
					            </div>
					            <div class="fitem">
					        		<label style="width:80px">权限:</label>
			                		<ul id="updateRoleTree"></ul>
					   			</div>
		    				</td>
		    			</tr>
		    		</table>
		        </form>
    		</div>
    	</div>
         <div id="update-dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="updateData()" style="width:90px">保存</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#update-dlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
	<div id="dlg" class="easyui-dialog" style="width:440px;height:370px;"
            closed="true" modal="true" buttons="#dlg-buttons" title="新增角色">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
		    	<form id="fm" method="post">
		    		<table align="center">
		    			<tr>
		    				<td>
					            <div class="fitem">
					                <label style="width:80px">角色名称:</label>
					                <input name="roleName" class="easyui-textbox" required="true" missingMessage="请输入角色名称" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">角色描述:</label>
					                <input name="roleDecsription"  data-options="height:70, multiline:true"  missingMessage="请输入角色描述" class="easyui-textbox">
					            </div>
					            <div class="fitem">
					        		<label style="width:80px">权限:</label>
			                		<ul id="roleTree"></ul>
					   			</div>
		    				</td>
		    			</tr>
		    		</table>
		        </form>
    		</div>
    	</div>
         <div id="dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="postData()" style="width:90px">保存</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
    <!-- 
    {
    "limitEnd": 30,
    "limitStart": 0,
    "rows": [
        {
            "createTime": 1501039705000,
            "status": 1,
            "updateTime": 1501126113000,
            "createrName": "admin",
            "roleDecsription": "test描述",
            "roleName": "test角色",
            "createAccountId": "991",
            "roleId": "qweqwe"
        }
    ],
    "total": 2
}
      -->
    
    
	<div class="easyui-layout" fit="true">
		<div region="center" border="false">
			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar"  
			   url="role/list" method="post" pagination="true"
			   pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:true,fit:true,fitColumns:true" sortName="status desc,update_time" sortOrder="desc">
			    <thead>
					<tr>
				      <!-- <th data-options="field:'roleId'"  width="50">角色编号</th> -->
				      <th data-options="field:'roleName'" width="30">角色名称</th>
				      <th data-options="field:'status'"  width="20" formatter="formatStatus">状态</th>
				      <th data-options="field:'createrName'"  width="40">创建人</th>
				      <th data-options="field:'createTime'"  width="50"  formatter="formatDateTime" >创建时间</th>
				      <th data-options="field:'updateTime'" width="50"  formatter="formatDateTime" >更新时间</th>
				      <th data-options="field:'createAccountId'"  width="30" formatter="formatPermission">权限</th>
				      <th data-options="field:'roleDecsription'"  width="100">角色描述</th>
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
		$('#updateRoleId').val(row.roleId)
		$('#updateRoleName').textbox('setValue',row.roleName)
		$('#updateRoleDecsription').textbox('setValue',row.roleDecsription)
		
		$('#updateRoleTree').tree({
		    url:'permission/treeView/role/'+row.roleId,
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
	    url:'permission/treeView/allPermissions',
	    checkbox:true,
	    lines:true,
	    method:"get"
	});
}
function formatStatus(value,row,index){
	var str =  ['失效','有效'][row.status]
	var result = "<a href='javascript:void(0);'  onclick='updateForce("+"\""+ row.roleId +"\" ,"+ row.status +")'>"+str+"</a>";
	return result;
}

function openPermission(roleId){
	//根据roleid查询权限树
	$('#roleTree2').tree({
	    url:'permission/treeView/role/' + roleId,
	    checkbox:true,
	    lines:true,
	    method:"get"
	});
	
	$('#roleDlg').dialog('open')
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
	$.messager.confirm('提示','确定要【'+opt+'】此角色吗? ',function(r){
     	if(r){
	        	$.messager.progress({
		                title:'请稍后',
		                msg:'正在'+opt+'数据...'
		            });
	            $.ajax({
	            	url:'role/update',
	            	type:"post",
	            	data:{'roleId':id,'status':falg},
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
         url: 'role/update',
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
				 param.permissionIds = permissionIds;
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
            	 layer.msg('保存数据失败', {icon: 2,time: 5000});
             }else{
             	layer.msg(result+"", {icon: 2,time: 5000});
             }
         }
     })
}
/**
 * 保存数据
 */
function postData(){
	 $('#fm').form('submit',{
         url: 'role/save',
         onSubmit: function(param){
        	 var isValid = $(this).form('validate');
			 if(isValid){
				 var nodes = $('#roleTree').tree('getChecked');
				 var permissionIds='';
				 for(var i=0,len=nodes.length;len>i;i++){
					 if(i==len-1){
						 permissionIds+=nodes[i].id
					 }else{
						 permissionIds+=nodes[i].id+","
					 }
				 }
				 param.permissionIds = permissionIds;
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
            	 layer.msg('保存软件失败', {icon: 2,time: 5000});
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
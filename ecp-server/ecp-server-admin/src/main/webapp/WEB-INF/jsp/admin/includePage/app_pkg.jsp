<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<title>升级包管理</title>
<body>
<style type="text/css">
	.add {background-image: url("static/images/common/add.png");}
	.delete {background-image: url("static/images/common/delete.png");}
	.search {background-image: url("static/images/common/search.png");}
	.update {background-image: url("static/images/common/update.png");}
</style>
	<div id="tbar" style="height: 25px;">
		<div style="margin-left: 10px;">
			查询：
			      <select id="type" name="type">
			      	  <option value = "">全部终端</option>
			      	  <option value = "0">IOS邻音</option>  
					  <option value = "1">Android邻音</option>  
					  <option value = "2">门控机</option>  
					  <option value = "3">室内机</option>  
					  <option value = "5">重庆室内机</option>  
					  <option value = "4">物业APP</option>
					  <option value = "6">9寸门口机</option>
					  <option value = "7">9寸门口机3188</option>
			      </select>
			      <input class="easyui-textbox" prompt="请输入版本号" id="version" name="version"/>
			      <input class="easyui-datebox" prompt="开始时间" id="timeStart" name="timeStart"/>至
			      <input class="easyui-datebox" prompt="结束时间" id="timeEnd" name="timeEnd" validType="dateValid['#timeStart']"/>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">查询</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'add'" onclick="newSoft();">新增</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'delete'" onclick="destroySoft();">删除</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width:440px;height:370px;"
            closed="true" modal="true" buttons="#dlg-buttons">
    	<div id="tabs" fit="true" class="easyui-tabs" style="width: 100%;" border="false">
    		<div title="基本信息">
		    	<input id="softId" type="hidden">
		    	<form id="fm" method="post" enctype="multipart/form-data" novalidate>
		    		<table align="center">
		    			<tr>
		    				<td>
		    					<!-- <div class="fitem">
					                <label style="width:80px">软件名称:</label>
					                <input name="smname" id="smname" delay="60000" prompt="软件名称" class="easyui-textbox" data-options="validType:{length:[0,30]}" required="true" missingMessage="请输入软件名称.">
					            </div> -->
					            <div class="fitem">
					                <label style="width:80px">版本号:</label>
					                <input name="version" prompt="版本号" class="easyui-textbox" required="true" missingMessage="请输入版本号" data-options="validType:{length:[0,30]}">
					            </div>
					            <div class="fitem">
					                <label style="width:80px">版本描述:</label>
					                <input name="description" prompt="版本描述" data-options="validType:'length[0,999]'" required="true" missingMessage="请输入版本描述" class="easyui-textbox">
					            </div>
					            <div class="fitem">
					        		<label style="width:80px">文件上传:</label>
			                		<input id="uploadFile" name="uploadFile" type="file" onchange="validFile();" class="fitemFile"/>
					   			</div>
					            <!-- <div class="fitem" display="none">
					                <label style="width:80px">软件描述:</label>
					                <input name="softDescribe" data-options="validType:'length[0,50]'" multiline="true" class="easyui-textbox" style="height: 70px;">
					            </div> -->
					            <div class="fitem">
					                <label style="width:80px">升级终端:</label>
					                <select name="type" id="type"> 
									  <option value = "-1">－－请选择类型－－</option>
									  <option value = "0">IOS邻音</option>
									  <option value = "1">Android邻音</option>
									  <option value = "2">门控机</option>
									  <option value = "3">室内机</option>
									  <option value = "5">重庆室内机</option>
									  <option value = "4">物业APP</option>
									  <option value = "6">9寸门口机</option>
										<option value = "7">9寸门口机3188</option>
									</select>
					            </div>
					            
					            <div class="fitem">
					                <label style="width:80px">强制更新:</label>
					                <select name="isForce" id="isForce"> 
									  <option value = "0" selected="selected">否</option>  
									  <option value = "1">是</option>  
									</select>
					            </div>
		    				</td>
		    			</tr>
		    		</table>
		        </form>
    		</div>
    	</div>
         <div id="dlg-buttons">
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-ok" onclick="saveSoft()" style="width:90px">保存</a>
	        <a href="javascript:void(0);" class="easyui-linkbutton c8" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');" style="width:90px">取消</a>
	    </div>
    </div>
	<div class="easyui-layout" fit="true">
	<!-- 	<div region="west" border="true" collapsible="false" split="true" title="部门" style="width:220px;">
			<ul id="deptTree" class="easyui-tree" method="get" url="user_loadDept" data-options="onLoadSuccess:loadDeptTree, onClick:clickDeptTree"></ul>
		</div> -->
		<div region="center" border="false">
			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar"  
			   url="appPkg/list" method="get" pagination="true"
			   pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:true,fit:true,fitColumns:true" sortName="" sortOrder="">
			    <thead>
					<tr>
				      <th data-options="field:'type'"  width="50" formatter="formatType">升级终端</th>
				      <th data-options="field:'version'" width="30"  formatter="formatUrl">版本号</th>
				      <th data-options="field:'uploadTime'" formatter="formatDate" width="50">上传时间</th>
				      <th data-options="field:'uploadUserName'"  width="50">上传者</th>
				      <th data-options="field:'description'"  width="100">版本描述</th>
				      <th data-options="field:'availableTime'" formatter="formatDate" width="50">启用时间</th>
				      <th data-options="field:'enableUserName'"  width="50">启用操作者</th>
				      <th data-options="field:'isForce'" width="30" formatter="formatForce">强制升级</th>	
				      <th data-options="field:'status'" width="30" formatter="formatOper" >状态</th>	
				 	</tr>
			    </thead>
			</table>
		</div>
	</div>
</body>
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
	.fitemFile{
		width: 204px!important;
	}
</style>
<script type="text/javascript">
function formatForce(value,row,index){
	var str =  row['isForce']?'是':'否'
	var result = "<a href='javascript:void(0);' style='text-decoration:none' onclick='updateForce("+"\""+row['appPkgId']+"\" ,"+ row['isForce'] +")'>"+str+"</a>";
	return result;
}
function formatOper(value,row,index){
	var str = row['status']===1?'启用':'禁用'
	var result = "<a href='javascript:void(0);' style='text-decoration:none' onclick='updateStatus("+"\""+row['appPkgId']+"\""+","+ row['status'] +","+ row['type'] +")'>"+str+"</a>";
	return result;
}

function formatType(value,row,index){
	var result = '';
	if(value == '0'){
		result = 'IOS邻音';
	} else if(value == '1') {
		result = 'Android邻音';
	} else if(value == '2') {
		result = '门控机';
	} else if(value == '3') {
		result = '室内机';
	} else if(value == '4') {
		result = '物业APP';
	}else if(value == '5') {
		result = '重庆室内机';
	}else if(value == '6') {
		result = '9寸门口机';
	}else if(value == '7') {
        result = '9寸门口机3188';
    }
	return result;
}

function formatUrl(value,row,index){
	return "<a href='"+"${pageContext.request.contextPath }/file/"+row.pkgFileId+"' target='_blank'> "+value+" </a>";
}

function formatDate(date){
	if(date){
		return moment(new Date(date)).format('YYYY-MM-DD HH:mm:ss')
	}else{
	 return ""	
	}
}


function formatMandatoryUpgrade(value,row,index){
	var result = '';
	if(value == '0'){
		result = '否';
	} else if(value == '1') {
		result = '是';
	}
	return result;
}

function updateStatus(id,status,type){
	var opt = "";
	var falg="";
	if(status===1){
		opt = "禁用";
		falg="0";
	}else{
		opt = "启用";
		falg="1";
	}
	$.messager.confirm('提示','确定要【'+opt+'】此版本吗? \n 启用后会 禁用 同类型其他版本！',function(r){
     	if(r){
	        	$.messager.progress({
		                title:'请稍后',
		                msg:'正在'+opt+'账号...'
		            });
	            $.ajax({
	            	url:'appPkg/update?type='+type+'&status='+falg+'&appPkgId='+id,
	            	dataType:"json",
	        		success:function(data){
	        			$.messager.progress('close');
	        			if (data.code===0){
	                    	$('#dg').datagrid('reload');
	                    }else if(data=='error'){
	                   	    layer.msg(opt+'账号失败', {icon: 2,time: 5000});
	                    }else{
	                    	layer.msg(data, {icon: 2,time: 5000});
	                    }
	        		},
	        		error:function(request,msg){
	        			$.messager.progress('close');
	        			layer.msg(opt+'账号失败:'+msg, {icon: 2,time: 5000});
	        		}
	            });
     	}
     });
}
function updateForce(id,isForce){
	var opt = "";
	var falg="";
	if(isForce){
		opt = "禁用";
		falg="0";
	}else{
		opt = "启用";
		falg="1";
	}
	$.messager.confirm('提示','确定要【'+opt+'】强制升级吗? ',function(r){
     	if(r){
	        	$.messager.progress({
		                title:'请稍后',
		                msg:'正在'+opt+'账号...'
		            });
	            $.ajax({
	            	url:'appPkg/update?'+'isForce='+falg+'&appPkgId='+id,
	            	dataType:"json",
	        		success:function(data){
	        			$.messager.progress('close');
	        			if (data.code===0){
	                    	$('#dg').datagrid('reload');
	                    }else if(data=='error'){
	                   	    layer.msg(opt+'账号失败', {icon: 2,time: 5000});
	                    }else{
	                    	layer.msg(data, {icon: 2,time: 5000});
	                    }
	        		},
	        		error:function(request,msg){
	        			$.messager.progress('close');
	        			layer.msg(opt+'账号失败:'+msg, {icon: 2,time: 5000});
	        		}
	            });
     	}
     });
}


var uniqueCount = 0;
$.extend($.fn.validatebox.defaults.rules, {

});

var url;//form提交地址
var isLoadRole = false;//是否加载过角色树
function validFile(){
	var filename=$('#uploadFile').val();
	var suffix = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
	/* if(!/(apk)$/.test(suffix)){
		layer.msg('文件类型必须是apk类型文件', {icon: 2,time: 3000});
		$('#uploadFile').val('');
	} */
}

/**
 * 添加软件
 */
function newSoft(){
	$('#fm').form('clear');
	//新增时开启唯一性验证
	uniqueCount = 2;
	$('#softId').val('');	
	$('#dlg').dialog('open').dialog('setTitle','新增软件');
    url='appPkg/save';
}


/**
 * 保存软件
 */
function saveSoft(){
	if($('#type').val() == '-1' || $('#type').val() == null){
		layer.msg('请选择升级终端', {icon: 2,time: 5000});
		return;
	}
	/* if($('#mandatoryUpgrade').val() == '-1' || $('#mandatoryUpgrade').val() == null){
		layer.msg('请选择是否强制升级', {icon: 2,time: 5000});
		return;
	}  */
	console.log('1',url)
	 $('#fm').form('submit',{
         url: url,
         onSubmit: function(param){
        	 console.log('2')
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
            	 layer.msg('保存软件失败', {icon: 2,time: 5000});
             }else{
             	layer.msg(result+"", {icon: 2,time: 5000});
             }
         }
     })
         console.log('3')
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
/**
 * 查询软件
 */
function searchData(){
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

	$('#dg').datagrid('load',{
		//'smnamepara': $('#smnamepara').val(),
		'version': $('#version').val(),
		'timeStart': $('#timeStart').datebox('getValue'),
		'type': $('#type').val(),
		'timeEnd': $('#timeEnd').datebox('getValue')
	});
}
</script>
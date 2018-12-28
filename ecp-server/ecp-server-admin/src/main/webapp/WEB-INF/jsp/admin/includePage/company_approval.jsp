<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.description{
	color: gray;
    font-size: 12px;
    font-style: italic;
}
.approvalTable tr:hover {
    background-color: #f5f5f5;
}
.approvalTable th, .approvalTable td {
    border-bottom: 1px solid #ddd;
	padding: 15px;
}
</style>
<title>企业审核</title>
<body>
	<div id="tbar" style="height: 25px;">
		<div style="margin-left: 10px;">
			关键字搜索：<input class="easyui-textbox" id="query_content" data-options="prompt:'公司名/账号名/邮箱/电话/工商注册号/证件号/地址', width:300" />&nbsp;&nbsp;
			审核状态：<select name="status" id="state">
				<option value = "">全部</option>
				<option value = "2">待审核</option>
				<option value = "1">审核通过</option>
				<option value = "0">审核不通过</option>
			</select>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'search'" onclick="searchData();">查询</a>
			<!-- <div style="display:inline-block;text-align:right;width:49%;">
				<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'" onclick="changeState(2)">通过</a>
			<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'" onclick="changeState(3)">拒绝</a>
			</div>  -->
		</div>
	</div>
	<!-- 
			{
			    "limitEnd": 30,
			    "limitStart": 0,
			    "rows": [
			        {
			            "companyAddress": "哇哇哇贾近近景",
			            "accountId": "994",
			            "status": 2,
			            "companyName": "测试001",
			            "orgCodeFileId": "2ef96602-93ce-4db4-8fbc-8d35ae667b87",
			            "accountName": "admin88",
			            "bizSaleLicenseFileId": "01bb261c-86ad-4b3d-82b0-2d89b76fbac7",
			            "companyApprovalId": "eaadad85-6e06-4a5e-a98a-1c2d43af1148",
			            "logoFileId": "4721ef45-512f-4d93-9bb7-b2a1255488fe",
			            "email": "50606052@gmail.com",
			            "taxLicenseFileId": "10cd6a42-9913-4e8d-b925-f163797dca7a",
			            "bizRegistryLicenseNum": "123456789013333",
			            "companyLocation": "河北省邯郸市大名县",
			            "propertyCertFileId": "9bb8cbd2-96bf-4093-bcc3-03550090e14d",
			            "mobile": "13242446850"
			        }
			    ],
			    "total": 1
			}
	 -->
	<div class="easyui-layout" fit="true">
		<div region="center" border="false">
			<table id="dg" class="easyui-datagrid" rownumbers="true" toolbar="#tbar" sortName="" sortOrder="" 
			   url="${pageContext.request.contextPath}/companyApproval/list" method="post" pagination="true"  pageSize="30" data-options="emptyMsg:'没有查询到数据',border:true,singleSelect:false,fit:true,fitColumns:true">
			    <thead>
					<tr>
				      <!-- <th data-options="field:'ck',checkbox:true" width="50"><input type="checkbox" /></th> -->
				      <th data-options="field:'createDate',align:'center',halign:'center'" width="60" formatter="formatDate">申请日期</th>
				      <th data-options="field:'accountName',align:'center',halign:'center'" width="60">物业APP账号</th>
				      <th data-options="field:'mobile',align:'center',halign:'center'" width="60">注册人电话</th>
				      <th data-options="field:'email',align:'center',halign:'center'" width="90">注册人邮箱</th>
				      <th data-options="field:'companyName',align:'center',halign:'center'" width="120">公司名称</th>
				      <th data-options="field:'companyLocation',align:'center',halign:'center'"  width="100">省市县</th>
				      <th data-options="field:'status',align:'center',halign:'center'"  width="40" formatter="formatState">状态</th>
				      <th data-options="field:'everyThing',align:'center',halign:'center'"  width="40" formatter="format">操作</th>
				 	</tr>
			    </thead>
			</table>
		</div>
	</div>

	<div id="detailWindow" class="easyui-window" title="企业详情" style="width:600px;height:400px"
		 data-options="iconCls:'icon-save',modal:true,collapsible:false,resizable:false,closed:true">
		 
		 <form id="form1" action="">
		 <table class="approvalTable" style="width:100%">
			 <tr>
				 <th width="15%">注册APP账号：</th>
				 <td width="75%"><span id="accountName"></span><br/><span class="description"></span></td>
				 <td width="10%"></td>
			 </tr>
			 <tr>
				 <th>企业全称：</th>
				 <td><span id="companyName"></span><br/><span class="description">只支持中国大陆工商局或市场监督管理局登记的企业。请填写工商营业执照上的企业全称。</span></td>
				 <td>
				 	<select name="companyNameAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>企业地址：</th>
				 <td><span id="address"></span><br/><span class="description">与企业工商营业执照上一致。</span></td>
				 <td>
				 	<select name="companyAddressAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>工商执照注册号：</th>
				 <td><span id="bizRegistryLicenseNum">请填写工商营业执照上的注册号；或三证合一后18位的统一社会信用代码。</span><br/><span class="description"></span></td>
				 <td>
				 	<select name="bizRegistryLicenseNumAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>企业Logo：</th>
				 <td><span class="description"></span><br><a id="logoFileId_a" target="_blank"><img id="logoFileId" src="" alt="公司Logo" width="180px"/></a></td>
				 <td>
				 	<select name="logoAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>企业工商营业执照：</th>
				 <td>
					 <span class="description">只支持中国大陆工商局或市场监督管理局颁发的工商营业执照，且必须在有效期内。<br>若办理过三证合一的企业，请再次上传最新的营业执照。<br>格式要求：原件照片、扫描件或者加盖公章的复印件。</span><br>
					 <a id="bizSaleLicenseFileId_a" target="_blank"><img id="bizSaleLicenseFileId" src="" alt="公司营业执照" width="180px"/></a>
				</td>
				 <td>
				 
				 	<select name="bizSaleLicenseAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>税务登记证：</th>
				 <td>
				 	<span class="description">税务登记证必须在有效期范围内。若办理过三证合一的企业无法提供税务登记证，请上传最新的营业执照。<br>格式要求：原件照片、扫描件或加盖公章的复印件。</span><br>
				 	<a id="taxLicenseFileId_a" target="_blank"><img id="taxLicenseFileId" src="" alt="税务登记证" width="180px"/></a></td>
				 <td>
				 	<select name="taxLicenseAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>组织机构代码证：</th>
				 <td>
				 	<span class="description">组织机构代码证必须在有效期范围内。若办理过三证合一的企业无法提供组织机构代码证，请上传最新的营业执照。<br>格式要求：原件照片、扫描件或加盖公章的复印件</span><br>
				 	<a id="orgCodeFileId_a" target="_blank"><img id="orgCodeFileId" src="" alt="组织机构代码证" width="180px"/></a></td>
				 <td>
				 	<select name="orgCodeAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 <tr>
				 <th>物业资质证书：</th>
				 <td>
				 	<span class="description"></span>
				 	<a id="propertyCertFileId_a" target="_blank"><img id="propertyCertFileId" src="" alt="物业资质" width="180px"/></a></td>
				 <td>
				 	<select name="propertyCertAuditTxt">
				 		<option value="通过"> ✅ 通过</option>
				 		<option value="不清晰">不清晰</option>
				 		<option value="不正确">不正确</option>
				 	</select>
				 </td>
			 </tr>
			 
		 </table>
		 
			<!-- <div style="padding: 10px;">
				<p class="p">
					<strong class="strong">注册APP账号：</strong><span class="span" id="accountName">1234567@qq.com</span>
				</p>
				<p class="p">
					<strong class="strong">公司名称：</strong><span class="span" id="companyName">1234567@qq.com</span>&nbsp; | <i>审核反馈内容：</i><input name="companyNameAuditTxt" type="text"/>
				</p>
				<h3 id="companyName"></h3>
				<p class="p">
					<strong class="strong">地址：</strong>
					<span class="span" id="address" title="" style="white-space:nowrap;max-width:190px; overflow:hidden;text-overflow: ellipsis;display:inline-block;"> </span>&nbsp; | <i>审核反馈内容：</i><input name="companyAddressAuditTxt" type="text"/>
				</p>
				<p class="p">
					<strong class="strong">工商执照注册号：</strong><span class="span" id="bizRegistryLicenseNum"></span>&nbsp; | <i>审核反馈内容：</i><input name="bizRegistryLicenseNumAuditTxt" type="text"/>
				</p>

				<div class="clear"></div>
				<h5>公司Logo | <i>审核反馈内容：</i><input name="logoAuditTxt" type="text"/></h5>
				<a id="logoFileId_a" target="_blank"><img id="logoFileId" src="" alt="公司Logo" width="180px"/></a>
				<h5>公司营业执照 | <i>审核反馈内容：</i><input name="bizSaleLicenseAuditTxt" type="text"/></h5>
				<a id="bizSaleLicenseFileId_a" target="_blank"><img id="bizSaleLicenseFileId" src="" alt="公司营业执照" width="180px"/></a>
				<h5>税务登记证 | <i>审核反馈内容：</i><input name="taxLicenseAuditTxt" type="text"/></h5>
				<a id="taxLicenseFileId_a" target="_blank"><img id="taxLicenseFileId" src="" alt="税务登记证" width="180px"/></a>
				<h5>组织机构代码证 | <i>审核反馈内容：</i><input name="orgCodeAuditTxt" type="text"/></h5>
				<a id="orgCodeFileId_a" target="_blank"><img id="orgCodeFileId" src="" alt="组织机构代码证" width="180px"/></a>
				<h5>物业资质 | <i>审核反馈内容：</i><input name="propertyCertAuditTxt" type="text"/></h5>
				<a id="propertyCertFileId_a" target="_blank"><img id="propertyCertFileId" src="" alt="物业资质" width="180px"/></a>
			</div> -->
			<div id="submitBtn" style="text-align: center">
				<input id="companyApprovalId" name="companyApprovalId" type="hidden"/>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submit(1)">审核通过</a>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="submit(0)">审核不通过</a>
			</div>
		 </form>
	</div>
</body>
<style type="text/css">
	#fm{
	    margin:0;
	    padding:10px 10px;
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
function formatState(value,row,index){
	var state = Number(row['state']); 
	return ['审核不通过','审核通过','待审核'][value];
}
var companyApprovalMap={};
function format(value,row,index){
	var id = row['companyApprovalId'];
	companyApprovalMap[id]=row;
	return '<a href="javascript:void(0)" onclick="detailInfo(\''+id+'\')">查看详情</a>';
}
//打开审批窗口
function approvalShow(id){
	$("#pId").val(id);
	$('#dlg').dialog('open').dialog('setTitle','产品审批');
}
//审批
function approval(id,state){
	var urlP=null;
	if(id==0){
		id=$('#pId').val();
	}
	if(state==1){
		urlP=$("#urlP").val();
	}
	if(state==1&&(urlP==""||urlP==null)){
		layer.msg('请填写域名！', {icon: 2,time: 5000});
	}else{
		 $.ajax({
	         cache: false,
	         type: "POST",
	         url:"${pageContext.request.contextPath}/product/approval",
	         data:{"id":id,"state":state,"url":urlP},
	         async: false,
	         success: function(data){
	         	layer.msg('审批成功！', {icon: 1,time: 5000});
	         	$('#dlg').dialog('close');
		 		$("#urlP").val("");
	         	$('#dg').datagrid('reload');
	         },
	         error: function(request) {
	             layer.msg('审批失败，请联系管理员！', {icon: 2,time: 5000});
	         }
	     });
	}
}
/**
 * 查询用户
 */
function searchData(){
	$('#dg').datagrid('load',{
		query_content: $('#query_content').val(),
		status : $('#state').val()
	});
}
function formatDate(date){
	if(date){
		return moment(new Date(date)).format('YYYY-MM-DD')
	}else{
	 return ""	
	}
}

function detailInfo(id,event){
	window.event? window.event.cancelBubble = true : event.stopPropagation();
	var currRow = companyApprovalMap[id]
	
	if(currRow['status'] === 1){
		$('#submitBtn').hide();
	}else{
		$('#submitBtn').show();
	}
	
	$('#companyName').html(currRow['companyName']);
	$('#address').html(currRow['companyLocation']+'  '+currRow['companyAddress']);
	$('#bizRegistryLicenseNum').html(currRow['bizRegistryLicenseNum']);
	$('#accountName').html(currRow['accountName']);
	$('#companyApprovalId').val(currRow['companyApprovalId']);
	
	$("input[name='companyAddressAuditTxt'").val(currRow['companyAddressAuditTxt']);
	$("input[name='bizRegistryLicenseNumAuditTxt'").val(currRow['bizRegistryLicenseNumAuditTxt']);
	$("input[name='bizSaleLicenseAuditTxt'").val(currRow['bizSaleLicenseAuditTxt']);
	$("input[name='taxLicenseAuditTxt'").val(currRow['taxLicenseAuditTxt']);
	$("input[name='orgCodeAuditTxt'").val(currRow['orgCodeAuditTxt']);
	$("input[name='propertyCertAuditTxt'").val(currRow['propertyCertAuditTxt']);
	
	//图片src
	var imgBasePath = 'file/'
	$("#logoFileId").attr("src",imgBasePath+currRow['logoFileId']);
	$("#logoFileId_a").attr("href",imgBasePath+currRow['logoFileId']);
	$("#bizSaleLicenseFileId").attr("src",imgBasePath+currRow['bizSaleLicenseFileId']);
	$("#bizSaleLicenseFileId_a").attr("href",imgBasePath+currRow['bizSaleLicenseFileId']);
	$("#taxLicenseFileId").attr("src",imgBasePath+currRow['taxLicenseFileId']);
	$("#taxLicenseFileId_a").attr("href",imgBasePath+currRow['taxLicenseFileId']);
	$("#orgCodeFileId").attr("src",imgBasePath+currRow['orgCodeFileId']);
	$("#orgCodeFileId_a").attr("href",imgBasePath+currRow['orgCodeFileId']);
	$("#propertyCertFileId").attr("src",imgBasePath+currRow['propertyCertFileId']);
	$("#propertyCertFileId_a").attr("href",imgBasePath+currRow['propertyCertFileId']);
	
	$('#detailWindow').window('open');
}

$(function(){
	$('.panel-tool-max').click();
})

function submit(status){
	var formData = $("#form1").serialize();
	//设置status的值
	console.info(formData)
	$.ajax({
		   type: "POST",
		   url: "companyApproval/update?status="+status,
		   data: formData,
		   dataType:"json",
		   success: function(msg){
			   $('#detailWindow').window('close');
			   $('#dg').datagrid('reload');
		   }
		}); 
}
</script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/js/jqueryEasyUI/themes/color.css">
<style>
.role_root{background:url('static/images/common/world.png') no-repeat center center!important;}
.role_leaf{background:url('static/images/common/user.png') no-repeat center center!important;}
</style>

<div id="aaa" style="width:300px; height:300px;text-align: center;padding-top: 24px">操作审核中,请等待.....</div>

</html>
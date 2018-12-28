<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="easyUI.jsp" %>
<%@ taglib prefix="shiro" uri="/WEB-INF/shiro.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="${pageContext.request.contextPath}/">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- <title>xxx</title> 子页面中可以声明覆盖-->
	<script type="text/javascript" src="static/js/utils/monment.js"></script>
</head>
<style type="text/css">
	.add {background-image: url("static/images/common/add.png");}
	.delete {background-image: url("static/images/common/delete.png");}
	.search {background-image: url("static/images/common/search.png");}
	.update {background-image: url("static/images/common/update.png");}
</style>

<!-- <body> -->	<jsp:include page="includePage/${subJsp}.jsp"/><!-- </body> -->

<link rel="stylesheet" type="text/css" href="static/js/jqueryEasyUI/themes/icon.css">
<link rel="stylesheet" type="text/css" href="static/js/jqueryEasyUI/themes/color.css">
<style>
.role_root{background:url('static/images/common/world.png') no-repeat center center!important;}
.role_leaf{background:url('static/images/common/user.png') no-repeat center center!important;}
</style>
</html>
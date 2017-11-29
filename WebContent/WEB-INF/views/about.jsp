<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务路径详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="${ctx}/static/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/jquery-validationEngine/css/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/static/ligerUI/js/ligerui-1.9.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validationEngine/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/jquery-validationEngine/jquery.validationEngine.min.js" type="text/javascript" charset="utf-8"></script> 
<script src="${ctx}/js/common.js" type="text/javascript"></script>
 <script type="text/javascript">
 $(function (){
 	//$("#formSave").ligerForm({inputWidth:400});
 	var data ='${jsonStr}';
 	var value=eval('('+data+')');
 	$("#sysName").text('系统名称：'+value.sysName);
 	$("#version").text('软件版本：'+value.version);
 	$("#developer").text('开发人员：'+value.developer);
 	$("#copyright").text(value.copyright);
 	$("#licenseUser").text('授权用户：'+value.licenseUser);
 	$("#deviceNum").text('设备数量：'+value.deviceNum);
 	$("#deadline").text('有效期限：'+value.deadline);
 });
 </script>
</head>
<body>
		<table align="center" class="tb_edit2">
			<tr>
				<td><p id="sysName"></p></td>
			</tr>
			<tr>
				<td><p id="version"></p></td>
			</tr>
			<tr>
				<td><p  id="developer"></p></td>
			</tr>
			<tr>
				<td><p id="copyright"></p></td>
			</tr>
			<tr>
				<td style="height:18px"></td>
			</tr>
			<tr>
				<td><p id="licenseUser" /></td>
			</tr>
			<tr>
				<td><p id="deviceNum"></p></td>
			</tr>
			<tr>
				<td><p id="deadline"></p></td>
			</tr>
		</table>
</body>
</html>


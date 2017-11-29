<%@ page language="java" pageEncoding="UTF-8"%>
<link href="${ctx}/static/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
<link href="${ctx}/static/ligerUI/skins/Gray2014/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/jquery-validationEngine/css/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/style/css/common.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
<%-- <script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script> --%>
<script src="${ctx}/static/ligerUI/js/ligerui.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validationEngine/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/jquery-validationEngine/jquery.validationEngine.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/common.js" type="text/javascript"></script> 
<script src="${ctx}/js/edit.js" type="text/javascript"></script>
<script>
$.ajaxSetup({
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(xhr, textStatus) {
		if (xhr.status==911) {
			window.location = "${ctx}/login.jsp";
			return;
		}
	}
});
</script>
<%@ page language="java" pageEncoding="UTF-8"%>
<link href="${ctx}/static/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
<link href="${ctx}/static/ligerUI/skins/Gray2014/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/style/css/common.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/static/ligerUI/js/ligerui.min.js" type="text/javascript"></script>
<script src="${ctx}/js/common.js" type="text/javascript"></script>
<script src="${ctx}/js/main.js" type="text/javascript"></script>
<script>
//å¨å±çAJAXè®¿é®ï¼å¤çAJAXæ¸æ±æ¶SESSIONè¶æ¶
$.ajaxSetup({
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(xhr, textStatus) {
		if (xhr.status==911) {
			//è·³è½¬çç»å½é¡µé¢
			window.location = "${ctx}/login.jsp";
			return;
		}
	}
});
</script>

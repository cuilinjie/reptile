<link href="${ctx}/static/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
<%-- <link href="${ctx}/static/ligerUI/skins/Gray2014/css/all.css" rel="stylesheet" type="text/css" /> --%>
<link href="${ctx}/static/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/ligerUI-ext/css/ligerui-icons-ext.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/style/css/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/static/My97DatePicker/skin/WdatePicker.css" type="text/javascript"></script>


<script src="${ctx}/static/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/static/ligerUI/js/ligerui.min.js" type="text/javascript"></script>
<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/common.js" type="text/javascript"></script>
<script src="${ctx}/js/dateFormat.js" type="text/javascript"></script>
<script src="${ctx}/js/main.js" type="text/javascript"></script>
<script src="${ctx}/js/formatter.js" type="text/javascript"></script>
<script>
//全局的AJAX访问，处理AJAX清求时SESSION超时
$.ajaxSetup({
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(xhr, textStatus) {
		if (xhr.status==911) {
			//跳转的登录页面
			window.location = "${ctx}/login.jsp";
			return;
		}
	}
});
</script>
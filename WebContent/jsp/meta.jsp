<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	request.setAttribute("ctx", request.getContextPath());
%>
<!-- <%="http://" + request.getServerName() + ":"
					+ request.getLocalPort() + request.getRequestURI()%> -->
<link rel="stylesheet" type="text/css"
	href="${ctx}/jquery-easyui-1.4.1/themes/metro/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/jquery-easyui-1.4.1/themes/icon.css">
<script type="text/javascript"
	src="${ctx}/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${ctx}/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script>
	var ctx = '${ctx}';
	
	$.fn.datebox.defaults = $.extend({}, $.fn.datebox.defaults, {
		formatter : function(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			return y + '-' + (m < 10 ? ("0" + m) : m) + "-"
					+ (d < 10 ? ("0" + d) : d);
		}
	});

	function parseDate(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ("0" + m) : m) + "-"
				+ (d < 10 ? ("0" + d) : d);
	}
	 
	function StringToDate(str) { 
		var tempStrs = str.split(" "); 
		
		var dateStrs = tempStrs[0].split("-"); 
		var year = parseInt(dateStrs[0], 10); 
		var month = parseInt(dateStrs[1], 10) - 1 || 1; 
		var day = parseInt(dateStrs[2], 10) || 1; 
		
		var timeStrs; 
		var hour = 0; 
		var minute = 0; 
		var second = 0; 
		if(tempStrs.length > 1){
			timeStrs = tempStrs[1].split(":"); 
			hour = parseInt(timeStrs [0], 10); 
			minute = parseInt(timeStrs[1], 10) - 1; 
			second = parseInt(timeStrs[2], 10); 
		}
		var date = new Date(year, month, day, hour, minute, second); 
		return date;
	}
	
	String.prototype.DateDiff = function(strInterval, dtEnd) {   
	    var dtStart = this;  
	    if (typeof dtStart == 'string' || typeof dtStart == 'object' )//如果是字符串转换为日期型  
	    {   
	    	dtStart = StringToDate(dtStart);  
	    }  
	    if (typeof dtEnd == 'string' )//如果是字符串转换为日期型  
	    {   
	        dtEnd = StringToDate(dtEnd);  
	    }  
	    switch (strInterval) {   
	        case 's' :return parseInt((dtEnd - dtStart) / 1000);  
	        case 'n' :return parseInt((dtEnd - dtStart) / 60000);  
	        case 'h' :return parseInt((dtEnd - dtStart) / 3600000);  
	        case 'd' :return parseInt((dtEnd - dtStart) / 86400000);  
	        case 'w' :return parseInt((dtEnd - dtStart) / (86400000 * 7));  
	        case 'm' :return (dtEnd.getMonth()+1)+((dtEnd.getFullYear()-dtStart.getFullYear())*12) - (dtStart.getMonth()+1);  
	        case 'y' :return dtEnd.getFullYear() - dtStart.getFullYear();  
	   }
	}


	//日期格式化对象
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, //month
			"d+" : this.getDate(), //day
			"h+" : this.getHours(), //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
			"S" : this.getMilliseconds()
		//millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}

	$.ajaxSetup({
		//async : false,
		type : 'get',
		cache : false,
		statusCode : {
			401 : function(data) {
				data = data.responseText;
				var d = eval("(" + data + ")");
				if (d && d.message)
					parent.SHOWMESSAGE("异常提醒", d.message, "error");
			}
		}
	});

	$.messager.defaults = {
		ok : "确定",
		cancel : "取消"
	};

	$.fn.pagination.defaults = $.extend({}, $.fn.pagination.defaults, {
		beforePageText : "第",
		afterPageText : "页，共 {pages} 页",
		displayMsg : "当前显示第 {from} 条到 {to} 条，共 {total} 条"
	});

	jQuery.extend({
		convertObject2Params : function(object) {
			var tmp = "";
			for ( var i in object) {
				tmp += "&" + i + "=" + object[i]
			}
			tmp = tmp.length > 0 ? tmp.substr(1) : "";
			return tmp;
		}
	});
	
	 
</script>
<style>
html, body {
	margin: 0px;
	padding: 0px;
}

.datagrid-row-selected a {
	color: #fff;
}

.condition_filter {
	padding: 10px 0px;
	border-top: 1px solid #ccc;
}

.combobox-item{
	height:20px;
	line-height:20px;
}
</style>
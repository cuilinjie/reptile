<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${s:paramVal("SystemName")}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/index2.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/jquery-superfish/css/superfish.css" media="screen" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/mainRes.jsp" %>
<script src="${ctx}/static/jquery-superfish/js/hoverIntent.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-superfish/js/superfish.js" type="text/javascript"></script>
<script src="${ctx}/js/index.js" type="text/javascript"></script>

<script type="text/javascript" >
var tab = null;
var accordion = null;
var tree = null;
var topDivHeight = 0;
$(function(){
    //布局
	$("#layout-index").ligerLayout({leftWidth:200, bottomHeight:20, space:1,
		allowBottomResize:false});
    
	$("ul.sf-menu").superfish({disableHI:true,delay:500});
    $("#menu_bar a").attr("target","home");
    
    $("#open_show").toggle(function(){
    	alert(1);
		var h1 = $("#index-center").height();
		var h2 = $(".l-layout-bottom").position().top;
		h1 = h1 + topDivHeight;
		h2 = h2 + topDivHeight;
		$("#layout-index").height(h1);
		$(".l-layout-center").height(h1);
		$("#index-center").height(h1);
		$(".l-layout-bottom").css("top", h2);
		$("#banner").hide();
		$(this).removeClass("open").addClass("close");
	},function(){
		$("#banner").show();
		var h1 = $("#index-center").height();
		var h2 = $(".l-layout-bottom").position().top;
		h1 = h1 - topDivHeight;
		h2 = h2 - topDivHeight;
		$("#layout-index").height(h1);
		$(".l-layout-center").height(h1);
		$("#index-center").height(h1);
		$(".l-layout-bottom").css("top", h2);
		$(this).removeClass("close").addClass("open");
	});
    
	$("#index-loading").hide();
	
//	onDeadline();
	//onlineCheck();
	//setInterval(onlineCheck, 30000);
});

function doMenu(id, name, url)
{
    if (!id) return;
    if (!name) return;
    if (!url) return;

    $("#home").attr("src",url);
}

function getActWin()
{
	var ifwin = $('iframe[name="home"]')[0].contentWindow;
	return ifwin;
}
</script>

</head>
<body>
	<div id="index-loading" class="pageloading"></div> 
	<div class="top" id="banner">
		<!-- <div class="top_logo"></div> -->
    	<div class="top_sysname">${s:paramVal("SystemName")}</div>
		<div class="fR top_qbar" style="width:400px;">
			<table width="100%">
				<tr>
					<td class="hR">
			<ul class="fR">
				<li>当前用户：${LOGIN_USER.username}&nbsp;&nbsp;</li>
				<li class="home"><a href="#" onclick="doMenu('home', '系统首页', 'main.do');">首页</a>|</li>
				<li class="configure"><a href="#" onclick="doMenu('setup', '个人设置', 'system/user/setupGo.do');">设置</a>|</li>
				<li class="help_topic"><a href="#" onclick="onAbout();">关于</a>|</li>
				<li class="help_view"><a href="logout.do">退出</a></li>
			</ul>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="menu_bar" class="menu_bar">
		<div id="open_show" class="fR open"></div>
		<div style='padding-left:${s:paramVal("index2MenuLeftPos")}px;'>${menuHtmlData}</div>		
    </div>
    
	<div id="layout-index">
		<div position="center" id="index-center">
			<iframe frameborder="0" name="home" id="home" src="main.do"></iframe>
		</div>
		<div position="bottom" id="index-bottom">
			<table width="100%">
				<tr>
					<td width="15%"><p id="onlineNum"></p></td>
					<td width="75%">${s:paramVal("SystemCopyRight")}</td>
					<td width="10%"></td>
				</tr>
			</table>	
		</div>
	</div>
</body>
</html>

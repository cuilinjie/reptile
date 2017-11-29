<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${s:paramVal("SystemName")}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/index1.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/mainRes.jsp" %>
<script src="${ctx}/js/index.js" type="text/javascript"></script>

<script type="text/javascript">

var tab = null;
var accordion = null;
var tree = null;

$(function(){
    //布局
	$("#layout-index").ligerLayout({topHeight:60, leftWidth:200, bottomHeight:20, space:2,
		allowTopResize:false, allowBottomResize:false, onHeightChanged:f_heightChanged});
	
    var height = $(".l-layout-center").height();
    //Tab
    $("#index-center").ligerTab({ height: height });
        
    //面板
    $("#index-accordion").ligerAccordion({ height: height - 24, speed: null });

    //树
    var treeData = ${treeMenuData};
    $("#index-treeMenu").ligerTree({
        checkbox: false,
        slide: false,
        nodeWidth: 150,
        attribute: ['code','name','id','url'],
        textFieldName: 'name',
        data: treeData,
        onSelect: function(node)
        {
            if (!node.data.url) return;
            var tabid = $(node.target).attr("tabid");
            if (!tabid)
            {
            	tabid = node.data.id;
                $(node.target).attr("tabid", tabid);
            }
            addTab(tabid, node.data.name, node.data.url);
        }
    });


    tab = $("#index-center").ligerGetTabManager();
    accordion = $("#index-accordion").ligerGetAccordionManager();
    tree = $("#index-treeMenu").ligerGetTreeManager();

	$("#index-loading").hide();
	
	//onlineCheck();
	//setInterval(onlineCheck, 30000);
});

function f_heightChanged(options)
{
    if (tab)
        tab.addHeight(options.diff);
    if (accordion && options.middleHeight - 24 > 0)
        accordion.setHeight(options.middleHeight - 24);
}

function doMenu(id, name, url)
{
    if (!id) return;
    if (!name) return;
    if (!url) return;

    addTab(id, name, url);
}

function addTab(tabid, text, url)
{ 
    tab.addTabItem({ tabid:tabid, text:text, url:url });
} 

function getActWin()
{
	var tabid = tab.getSelectedTabItemID();
	var ifwin = $('iframe[name="'+tabid+'"]')[0].contentWindow;
	return ifwin;
}
</script>

</head>
<body >
	<div id="index-loading" class="pageloading"></div> 
    <div id="index-top">
    	<!-- <div class="top_logo"></div> -->
    	<div class="top_sysname">${s:paramVal("SystemName")}</div>
		<div class="fR top_qbar" style="width:400px;">
			<table width="100%">
				<tr>
					<td class="hR vM">
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
			<table width="100%">
				<tr>
					<td>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="layout-index">
		<div position="left" title="功能菜单" id="index-accordion" class="l-scroll">
			<ul id="index-treeMenu" ></ul>
		</div>
		<div position="center" id="index-center">
			<div tabid="home" title="系统首页" style="height:300px">
				<iframe frameborder="0" name="home" id="home" src="main.do"></iframe>
			</div>
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

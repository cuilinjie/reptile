<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="syslog:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>登录统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="${ctx}/static/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/static/ligerUI/js/ligerui-1.9.min.js" type="text/javascript"></script>
<script src="${ctx}/js/dateFormat.js" type="text/javascript"></script>
<script src="${ctx}/js/common.js" type="text/javascript"></script>
<script src="${ctx}/js/main.js" type="text/javascript"></script>

<script type="text/javascript">

objName = '登录统计';
mainUrl = '${ctx}/system/stat/searchLoginStat.do';
isPageList = true;

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	{ text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true },
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
             { display:'最后登录时间', name:'lastLoginTime', width:150, align:'center', type:'date', dateFormat:'yyyy-MM-dd',
              	render: function (row){
              		return getFormatDateByLong(row.lastLoginTime, "yyyy-MM-dd hh:mm:ss");
              	}
             },
             { display:'用户账号', name:'loginName', width:100, align:'left' },
             { display:'用户名称', name:'userName', width:100, align:'left' },
             { display:'客户端IP', name:'clientIp', width:100, align:'left' },
             { display:'登录次数', name:'totalLoginNum', width:100, align:'center' }
       		],  
       	url: timeURL(mainUrl),	
       	sortName: 'lastLoginTime', sortOrder: 'desc',
       	root: 'result', record: 'totalItems',
       	pageParmName: 'pageNo', pagesizeParmName: 'pageSize', 
       	sortnameParmName: 'orderBy', sortorderParmName: 'orderDir',
		alternatingRow: 'true',
        width: '100%', height:'100%', 
        pageSize: 20, pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
        onSelectRow: f_onSelectRow,
    	onDblClickRow: f_onDblClickRow
    });
    
    $("#bnSearch").click(f_bnSearch);
    $("#bnReset").click(f_bnReset);
});

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<table id="tb-toolbar"><tr>
	<td>${s:titleContent("登录统计", "", ctx)}</td>
	<td width="100%"><div id="toptoolbar"></div></td>
	</tr></table> 
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="" method="post">
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">起始时间:</td>
                <td><input name="filter_GED_lastLoginTime" class="ip1" type="text" ltype="date"/></td>
                <td></td>
                <td align="right">截止时间:</td>
                <td><input name="filter_LED_lastLoginTime" class="ip1" type="text" ltype="date"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right">用户账号:</td>
                <td><input name="filter_LIKES_loginName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right">用户名称:</td>
                <td><input name="filter_LIKES_userName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right">客户端IPss:</td>
                <td><input name="filter_LIKES_clientIp" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right"></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td colspan=3 align="right" style="padding:10px">
                <input id="bnSearch" type="button" value="查 询" class="l-button bn2"/> 
                </td>
                <td colspan=3 style="padding:10px">
                <input id="bnReset" type="button" value="重 置" class="l-button bn2"/>
                </td>
            </tr>   
        </table>
        </form>
    </div>
    </div>
 </div>
</body>
</html>
</shiro:hasPermission>  


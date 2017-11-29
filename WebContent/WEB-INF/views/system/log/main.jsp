<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="syslog:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日志管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '日志';
baseUrl = '${ctx}/system/log/';
isViewDlgExist = true;

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
         	{ text:'查看', click:f_onView, icon:'view' },
        	{ line:true },
        	<shiro:hasPermission name="syslog:delete"> 
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	{ text:'清除', click:f_onClear, icon:'candle' },
        	{ line:true },
        	</shiro:hasPermission>  
        	{ text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true },
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });
    dataMain.Rows = ${jsonData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'日志时间', name:'createdTime', width:150, align:'center', type:'date',
            	render: function (row){
            		return long2StrDate(row.createdTime);
            	}
            },
            { display:'日志标题', name:'title', width:150, align:'left' },
            { display:'日志内容', name:'content', width:450, align:'left' },
            { display:'操作用户', name:'operater', width:100, align:'left' },
            { display:'客户端IP', name:'clientIp', width:100, align:'left', type:'ip' },
            { display:'操作', width:70, render: function (row)
                {
                    var html = '';
                    html += '<div onclick="viewRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-view"></span></div>';
                	<shiro:hasPermission name="syslog:delete"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                	</shiro:hasPermission>  
                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                }
            }
       		],  
       		data: dataMain,
			alternatingRow: 'true',
	        width: '100%', height:'99%', 
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
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form id="formSearch" action="" method="post">
    	<table align="center" class="tb_edit Tab_box">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right"><span class="T_title">起始时间:</span></td>
                <td><input name="filter_GED_createTime" class="ip1" type="text" ltype="date"/></td>
                <td></td>
                <td align="right"><span class="T_title">截止时间:</span></td>
                <td><input name="filter_LED_createTime" class="ip1" type="text" ltype="date"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">日志标题:</span></td>
                <td><input name="filter_LIKES_logTitle" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right"><span class="T_title">日志内容:</span></td>
                <td><input name="filter_LIKES_logContent" class="ip1" type="text" ltype="text"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作用户:</span></td>
                <td><input name="filter_LIKES_userAccount" class="ip1 content3" type="text" /></td>
                <td></td>
                <td align="right"><span class="T_title">客户端IPaa:</span></td>
                <td><input name="filter_LIKES_clientAddress" class="ip1 content3" type="text" ltype="text"/></td>
                <td></td>
            </tr>
            <tr>
                <td colspan=3 align="right" style="padding:10px">
                <input id="bnSearch" type="button" value="查 询" class="buttonl1"/> 
                </td>
                <td colspan=3 style="padding:10px">
                <input id="bnReset" type="button" value="重 置" class="buttonl1"/>
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


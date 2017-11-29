<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限组管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '权限组';
baseUrl = '${ctx}/system/privilege/group';
isTreeList = true;

isDbClickEdit = true;

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	/* { text:'移动', click:f_onMove, icon:'move'},
        	{ line:true }, */
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	/* { text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true }, */
        	{ text:'权限项', click: onManageItems, icon:'config' },
        	{ line:true },
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows = ${jsonTreeData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'权限组名称', name:'name', width:180, align:'left' },
            { display:'权限组说明', name:'remark', width:180, align:'left' },
            { display:'树编码', name:'treeCode', width:150, align:'left' },
            { display:'操作', width:100, render: function (row)
                {
            		var html = '';
             		html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
             		if( html != '') html += '<div class="l-bar-separator"></div>';
             		html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
             		if( html != '') html += '<div class="l-bar-separator"></div>';
             		html += '<div onclick="manageItems(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-config"></span></div>';
                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                }
            }
       		],  
		data: dataMain,
       	tree: { columnName: 'name' },
		alternatingRow: 'true',
        width: '100%', height:'99%', 
        pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
        onSelectRow: f_onSelectRow,
		onDblClickRow: f_onDblClickRow
    });

	$("#formSearch").ajaxForm({dataType:'json', success:f_searchOK}); 
	$("#bnReset").click(f_bnReset);
});

function onManageItems()
{
	var id = getSingleSelectId('管理');
	if( id != null && id != '' ){
		manageItems(id);
	}
}

function manageItems(id)
{
	var url = timeURL('${ctx}/system/privilege/item/main.do?grpId='+id);
	gotoURL(url);
}

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="${ctx}/system/privilege/group/search.do" method="post">
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">权限组名称:</td>
                <td><input name="filter_LIKES_grpName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right"></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td colspan=3 align="right" style="padding:10px">
                <input id="bnSearch" type="submit" value="查 询" class="l-button bn2"/> 
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


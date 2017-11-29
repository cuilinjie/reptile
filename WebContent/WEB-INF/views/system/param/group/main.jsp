<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="paramGroup:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>参数组管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '参数组';
baseUrl = '${ctx}/system/param/group';

<shiro:hasPermission name="paramGroup:update"> 
isDbClickEdit = true;
</shiro:hasPermission>

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
         	<shiro:hasPermission name="paramGroup:create"> 
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="paramGroup:update"> 
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	/* { text:'上移', click:f_onMoveUp, icon:'moveUp' },
        	{ line:true },
        	{ text:'下移', click:f_onMoveDown, icon:'moveDown' },
        	{ line:true }, */
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	</shiro:hasPermission>  
        	/* { text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true }, */
        	<shiro:hasPermission name="paramItem:read"> 
        	{ text:'参数项', click:onManageItems, icon:'config' },
        	{ line:true },
        	</shiro:hasPermission>  
        	/* <shiro:hasPermission name="paramItem:update"> 
        	{ text:'参数值', click:onEditValues, icon:'record' },
        	{ line:true }, */
        	</shiro:hasPermission>  
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows = ${jsonListData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
             { display:'参数组名称', name:'name', width:200, align:'left' },
             { display:'参数组编码', name:'code', width:200, align:'left' },
             { display:'参数组类型', name:'status', width:100, align:'left' },
             { display:'描述', name:'remark', width:200, align:'left' },
             { display:'操作', width:130, render: function (row)
                 {
                 	var html = '';
                  	<shiro:hasPermission name="paramGroup:create"> 
                 	html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                 	</shiro:hasPermission>  
                 	<shiro:hasPermission name="paramGroup:delete"> 
                 	if( html != '') html += '<div class="l-bar-separator"></div>';
                 	html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                 	</shiro:hasPermission>  
                 	<shiro:hasPermission name="paramGroup:update"> 
                 	if( html != '') html += '<div class="l-bar-separator"></div>';
                 	html += '<div onclick="manageItems(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-config"></span></div>';
                 	</shiro:hasPermission>  
                     return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                 }
             }
       		],  
		data: dataMain,
		checkbox: 'true', alternatingRow: 'true',
		width: '100%', height:'99%', 
		pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
		onSelectRow: f_onSelectRow,
		onDblClickRow: f_onDblClickRow,
		isChecked: f_isChecked
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
	var url = timeURL('${ctx}/system/param/item/main.do?grpId='+id);
	gotoURL(url);
}

function onEditValues()
{
	var id = getSingleSelectId('编辑');
	if( id != null && id != '' ){
		editValues(id);
	}
}

function editValues(id)
{
	var url = timeURL(baseUrl + '/itemValue.do?id=' + id);
	window.parent.showDlg('修改参数', 720, 480, url);     
}

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="${ctx}/system/param/group/search.do" method="post">
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">参数组名称:</td>
                <td><input name="filter_LIKES_grpName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right">参数组编码:</td>
                <td><input name="filter_LIKES_grpCode" class="ip1" type="text" ltype="text"/></td>
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
</shiro:hasPermission>

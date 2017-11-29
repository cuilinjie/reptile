<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="resGroup:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>字典管理</title>
<meta http-equiv="Content-Type" content="text/html; char
set=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '字典';
baseUrl = '${ctx}/system/res/group';

<shiro:hasPermission name="resGroup:update"> 
isDbClickEdit = true;
</shiro:hasPermission>

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
         	<shiro:hasPermission name="resGroup:create"> 
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="resGroup:update"> 
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	/* { text:'上移', click:f_onMoveUp, icon:'moveUp' },
        	{ line:true },
        	{ text:'下移', click:f_onMoveDown, icon:'moveDown' },
        	{ line:true }, */
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="resGroup:delete"> 
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	</shiro:hasPermission>  
        	/* { text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true }, */
        	<shiro:hasPermission name="resItem:read"> 
        	{ text:'字典项', click: onManageItems, icon:'config' },
        	{ line:true },
        	</shiro:hasPermission>  
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows = ${jsonListData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'字典名称', name:'name', width:200, align:'left' },
            { display:'字典编码', name:'code', width:160, align:'left' },
            { display:'字典类型', name:'status', width:100 },
            { display:'字典项关系', name:'type', width:100 },
            { display:'字典项类型', name:'itemType', width:100 },
            { display:'描述', name:'remark', width:200, align:'left' },
            { display:'操作', width:100, render: function (row)
                {
                	var html = '';
                	<shiro:hasPermission name="resGroup:update"> 
                    html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="resGroup:delete"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="resItem:read"> 
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
	var url = timeURL('${ctx}/system/res/item/main.do?grpId='+id);
	gotoURL(url);
}

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="${ctx}/system/res/group/search.do" method="post">
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">字典名称:</td>
                <td><input name="filter_LIKES_grpName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right">字典编码:</td>
                <td><input name="filter_LIKES_grpCode" class="ip1" type="text" ltype="text"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right">字典类型:</td>
                <td><s:resSelect name="filter_EQI_grpType" code="resGroupType" prompt="全部" ltype="select"/></td>
                <td></td>
                <td align="right">字典项类型:</td>
                <td><s:resSelect name="filter_EQI_itemType" code="resItemType" prompt="全部" ltype="select"/></td>
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

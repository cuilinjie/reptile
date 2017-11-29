<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="mdlName" value="权限管理（${group.name}）" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '权限';
baseUrl = '${ctx}/system/privilege/item';
backUrl = '${ctx}/system/privilege/group/main.do';
filtParam = 'grpId=${group.id}'

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
        	/* { text:'上移', click:f_onMoveUp, icon:'moveUp' },
        	{ line:true },
        	{ text:'下移', click:f_onMoveDown, icon:'moveDown' },
        	{ line:true }, */
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        /* 	{ text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true }, */
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' },
         	{ line:true },
        	{ text:'返回', click:f_onBack, icon:'back' }
        	]
    });

    dataMain.Rows = ${jsonListData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'权限名称', name:'name', width:150, align:'left' },
            { display:'权限编码', name:'code', width:150, align:'left' },
            { display:'业务权限', name:'isBs', width:80, align:'left' },
            { display:'开发权限', name:'isDv', width:80, align:'left' },
            { display:'角色可见', name:'isRoleDis', width:80, align:'left' },
            { display:'状态', name:'status', width:80, align:'left' },
            { display:'描述', name:'remark', width:200, align:'left' },
            { display:'操作', width:70, render: function (row)
                {
            		var html = '';
             		html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
             		if( html != '') html += '<div class="l-bar-separator"></div>';
             		html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
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

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="${ctx}/system/privilege/item/search.do" method="post">
		<input type="hidden" name="filter_EQS_sysPrivGroup.id" value="${group.id}"/>
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">权限名称:</td>
                <td><input name="filter_LIKES_privName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right">权限编码:</td>
                <td><input name="filter_LIKES_privCode" class="ip1" type="text" ltype="text"/></td>
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


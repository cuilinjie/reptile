<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="mdlName" value="参数项管理（${group.name}）" />

<shiro:hasPermission name="user:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>参数管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '参数';
baseUrl = '${ctx}/system/param/item';
backUrl = '${ctx}/system/param/group/main.do';
filtParam = 'grpId=${group.id}'

<shiro:hasPermission name="paramItem:update"> 
isDbClickEdit = true;
</shiro:hasPermission>

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	<shiro:hasPermission name="paramItem:create"> 
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="paramItem:update"> 
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	{ text:'上移', click:f_onMoveUp, icon:'up' },
        	{ line:true },
        	{ text:'下移', click:f_onMoveDown, icon:'down' },
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="paramItem:delete"> 
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	</shiro:hasPermission>  
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' },
         	{ line:true },
        	{ text:'返回', click: f_onBack, icon:'back' }
        	]
    });

    dataMain.Rows = ${jsonListData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
             { display:'参数名称', name:'name', width:150, align:'left' },
             { display:'参数编码', name:'code', width:150, align:'left' },
             { display:'参数值类型', name:'valType', width:100, align:'left' },
             { display:'参数值', name:'valText', width:300, align:'left' },
             { display:'描述', name:'remark', width:150, align:'left' },
             { display:'操作', width:70, render: function (row)
				{
                	var html = '';
                 	<shiro:hasPermission name="paramItem:update"> 
                    html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                 	</shiro:hasPermission>  
                 	<shiro:hasPermission name="paramItem:delete"> 
                 	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
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

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form style="padding:0px;" id="formSearch" action="${ctx}/system/param/item/search.do" method="post">
		<input type="hidden" name="filter_EQS_sysParamGroup.paramGrpId" value="${group.id}"/>
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right">参数名称:</td>
                <td><input name="filter_LIKES_paramName" class="ip1" type="text" ltype="text"/></td>
                <td></td>
                <td align="right">参数编码:</td>
                <td><input name="filter_LIKES_paramCode" class="ip1" type="text" ltype="text"/></td>
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

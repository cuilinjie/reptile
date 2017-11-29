<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">
operName = '${LOGIN_USER.loginName}';
operOrgId = '${LOGIN_USER_MAIN_ORGAN.id}';
objName = '机构';
tplName='org';
baseUrl = '${ctx}/system/organ';
isTreeList = true;
expColNames = '上级机构名称,机构名称,机构编码,机构类型,机构电话,机构地址,邮政编码,机构说明';

isDbClickEdit = true;

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	{ text:'添加', click: f_onAdd, icon:'add'},
        	{ line:true },
        	{ text:'修改', click: f_onEdit, icon:'edit'},
        	{ line:true },
        	{ text:'移动', click: f_onMove, icon:'move'},
        	{ line:true }, 
        	{ text:'删除', click: onDelete, icon:'delete' },
        	{ line:true },
        	/* { text:'查询', click: f_onSearch, icon:'search' },
        	{ line:true },*/
        	{ text:'导入', click: f_onImport, icon:'import' },
        	{ line:true },
        	{ text:'导出', click: f_onExport, icon:'export' },
        	{ line:true }, 
        	{ text:'刷新', click: f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows = ${jsonTreeData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'机构名称', name:'orgName', width:200, align:'left' },
            { display:'机构编码', name:'orgCode', width:200, align:'left' },
            { display:'操作', width:100, render: function (row)
                {
                    var html = '';
                    html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="delRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                }
            }
       		],  
		data: dataMain,
       	tree: {columnName: 'orgName'},
		alternatingRow: 'true',
        width: '100%', height:'99%', 
        pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
        onSelectRow: f_onSelectRow,
		onDblClickRow: f_onDblClickRow
    });

	$("#formSearch").ajaxForm({dataType:'json', success:f_searchOK}); 
	$("#bnReset").click(f_bnReset);
});

function onDelete()
{
	var id = getAllSelectId();
	if( id != null && id != '' ){
		delRow(id);
	}
}

function delRow(id)
{
	if( operOrgId != '' && id==operOrgId ) {
    	window.parent.$.ligerDialog.error('不能删除当前登录用户所属机构！'); 
    	return; 
    }else {
    	deleteRow(id);
    }
}
function f_onExport(){
	location.href=baseUrl+"/export.do";
}
</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;"></div>
</body>
</html>


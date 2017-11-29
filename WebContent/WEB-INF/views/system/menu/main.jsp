<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>菜单管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">
objName = '菜单';
isTreeList = true;
baseUrl = '${ctx}/system/menu';

<shiro:hasPermission name="menu:update"> 
isDbClickEdit = true;
</shiro:hasPermission>

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
         	<shiro:hasPermission name="menu:create"> 
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="menu:update"> 
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	{ text:'移动', click:f_onMove, icon:'communication'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="menu:delete"> 
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="menu:mapPriv"> 
        	{ text:'关联权限', click: onMapPriv, icon:'config' },
        	{ line:true },
        	</shiro:hasPermission>  
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows = ${jsonTreeData};
    //console.log(JSON.stringify(${jsonTreeData}));
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'菜单名称', name:'name', width:200, align:'left' },
            { display:'菜单URL', name:'url', width:200, align:'left' },
            { display:'菜单图标', name:'icon', width:150, align:'left' },
            { display:'菜单说明', name:'remark', width:150, align:'left' },
            { display:'树编码', name:'treeCode', width:150, align:'left' },
            { display:'操作', width:100, render: function (row)
                {
                    var html = '';
                	<shiro:hasPermission name="menu:update"> 
                    html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="menu:delete"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="menu:mapPriv"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="mapPriv(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-config"></span></div>';
                    </shiro:hasPermission>  
                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                }
            }
       		],  
		data: dataMain,
       	tree: {columnName: 'name'},
		alternatingRow: 'true',
        width: '100%', height:'99%', 
        pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
        onSelectRow: f_onSelectRow,
		onDblClickRow: f_onDblClickRow
    });

	$("#formSearch").ajaxForm({dataType:'json', success:f_searchOK}); 
	$("#bnReset").click(f_bnReset);
});

function onMapPriv()
{
	var id = getSingleSelectId('关联');
	if( id != null && id != '' ){
		mapPriv(id);
	}
}

function mapPriv(id)
{
	var url = timeURL(baseUrl + '/mapPrivGo.do?id=' + id);
	window.parent.parent.showDlg('菜单关联权限', 800, 480, url);     
}
</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
 </div>
</body>
</html>
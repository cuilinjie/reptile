<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="user:read">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName='用户';
baseUrl='${ctx}/system/user';

<shiro:hasPermission name="user:update"> 
isDbClickEdit = true;
</shiro:hasPermission>

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	<shiro:hasPermission name="user:create"> 
        	{ text:'添加', click:f_onAdd, icon:'add'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="user:update"> 
        	{ text:'修改', click:f_onEdit, icon:'edit'},
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="user:delete"> 
        	{ text:'删除', click:f_onDelete, icon:'delete' },
        	{ line:true },
        	</shiro:hasPermission>  
        	<shiro:hasPermission name="user:assignRole"> 
        	{ text:'角色分配', click: onMapRole, icon:'config' },
        	{ line:true },
        	</shiro:hasPermission> 
        	<shiro:hasPermission name="user:viewMenu"> 
        	{ text:'菜单查看', click: onViewMenu, icon:'view' },
        	{ line:true },
        	</shiro:hasPermission>  
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });

    dataMain.Rows =${jsonListData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'用户类型', name:'type', width:100, align:'left' },
            { display:'用户账号', name:'loginName', width:150, align:'left' },
            { display:'用户名称', name:'username', width:150, align:'left' },
            { display:'所属机构', name:'orgName', width:200, align:'left' },
            { display:'操作', width:160, render: function (row)
                {
                    var html = '';
                    if ( row.id != '${LOGIN_USER.id}') {
                	<shiro:hasPermission name="user:update"> 
                    html += '<div onclick="editRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-edit"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="user:delete"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="deleteRow(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-delete"></span></div>';
                	</shiro:hasPermission>  
                	<shiro:hasPermission name="user:assignRole"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="mapRole(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-config"></span></div>';
                	</shiro:hasPermission>  
                    }
                	<shiro:hasPermission name="user:viewMenu"> 
                	if( html != '') html += '<div class="l-bar-separator"></div>';
                    html += '<div onclick="viewMenu(\''+row.id+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-view"></span></div>';
                	</shiro:hasPermission>  
                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
                }
            }
       		],  
    	data:dataMain,	
		checkbox: 'true', alternatingRow: 'true',
		width: '100%', height:'99%', 
		pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
		onSelectRow: f_onSelectRow,
		onDblClickRow: f_onDblClickRow,
		isChecked: f_isChecked
    });

	$("#orgId").ligerComboBox({
		absolute:false, resize:false, treeLeafOnly: false,
    	width: 196, selectBoxWidth: 196, selectBoxHeight: 150, 
    	textField: 'orgName', valueFieldID: 'filter_EQS_orgId',
    	initValue: '${user.orgId==null ? LOGIN_USER_MAIN_ORGAN.id:user.orgId}',
		tree: { url: timeURL('${ctx}/system/organ/getMinData.do?treeCode=${LOGIN_USER_MAIN_ORGAN.treeCode}'), 
    		textFieldName: 'orgName', checkbox: false }
    });

    $("#bnSearch").click(f_bnSearch);
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
    if ( id == '${LOGIN_USER.id}') {
    	window.parent.$.ligerDialog.error('不能操作当前登录用户！'); 
    	return; 
    }
    else{
    	var url = timeURL(baseUrl + '/mapPrivGo.do?id=' + id);
    	window.parent.showDlg('用户权限分配', 800, 480, url);     
    }
}

function onMapRole()
{
	var id = getSingleSelectId('关联');
	if( id != null && id != '' ){
		mapRole(id);
	}
}

function mapRole(id)
{
    if ( id == '${LOGIN_USER.id}') {
    	window.parent.$.ligerDialog.error('不能操作当前登录用户！'); 
    	return; 
    }
    else{
		var url = timeURL(baseUrl + '/mapRoleGo.do?id=' + id);
		window.parent.showDlg('用户角色分配', 800, 480, url);    
    }
}

function onViewMenu()
{
	var id = getSingleSelectId('查看');
	if( id != null && id != '' ){
		viewMenu(id);
	}
}

function viewMenu(id)
{
	var url = timeURL(baseUrl + '/viewMenu.do?id=' + id);
	window.parent.showDlg('用户菜单查看', 800, 480, url);     
}

</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form id="formSearch" action="" method="post">    	
    	<table align="center" class="tb_edit">
            <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
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
                <td align="right">用户类型:</td>
                <td><s:resSelect name="filter_EQI_type" code="userType" prompt="全部" ltype="select"/></td>
                <td></td>
                <td align="right">所属机构:</td>
                <td>
                <input id="orgId" name="orgId" type="text" class="ip1"/>
                </td>
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

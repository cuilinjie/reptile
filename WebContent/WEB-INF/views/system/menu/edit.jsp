<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">

editResult = '${result}';
editMessage = '${message}';
editAction = '${action}';
$(function ()
{				
	checkEditSuccess();
	if(typeof checkEditError == "function")checkEditError();
	if(typeof baseInit == "function")baseInit();
});

</script>

</head>
<body> 
	<form:form id="formSave" modelAttribute="menu" action="${ctx}/system/menu/save.do" method="post">
		<input type="hidden" name="action" value="${action}"/>
		<input type="hidden" name="parentId" value="${menu.parentId}"/>
		<input type="hidden" name="id" value="${menu.id}"/>
		<table align="center" class="tb_edit Tab_box">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right" ><span class="T_title">菜单名称:</span></td>
                <td><input id="name" name="name" value="${menu.name}" type="text" ltype="text" class="ui-textbox validate[required,maxSize[64]] content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">菜单状态:</span></td>
                <td><s:resSelect name="status" id="status" value="${menu.status}" code="menuStatus" ltype="select"  /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">菜单URL:</span></td>
                <td><input id="url" name="url" value="${menu.url}" type="text" ltype="text" class="ui-textbox validate[optional,maxSize[256]] content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">菜单图标:</span></td>
                <td><input id="icon" name="icon" value="${menu.icon}" type="text" ltype="text" class="validate[optional,maxSize[64]] ui-textbox content3" /></td>
                <td></td>
            </tr>   
            <tr>
                <td align="right"><span class="T_title">菜单说明:</span></td>
                <td colspan=4><input id="remark" name="remark" value="${menu.remark}" type="text" ltype="text" class="validate[optional,maxSize[128]] ui-textbox content3"  /></td>
                <td></td>
            </tr>
            <tr>
           		<td align="center" colspan=6 >
           		<input id="bnSave" type="submit" value="保 存" class="buttonl1" />
           		</td>
            </tr>
		</table>
    </form:form>
</body>
</html>


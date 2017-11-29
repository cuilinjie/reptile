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
<title>机构编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">

editResult = '${result}';
editMessage = '${message}';
editAction = '${action}';
editObjId = '${organ.id}';

$(function ()
{
	checkEditSuccess();
	if(typeof checkEditError == "function")checkEditError();
	if(typeof baseInit == "function")baseInit();
});

</script>

</head>
<body> 
	<div class="wFull hL TK_title">上级机构：${parentOrgan.orgName}</div>
	<form:form id="formSave" modelAttribute="org" action="${ctx}/system/organ/save.do" method="post">
		<input type="hidden" name="action" value="${action}"/>
		<input type="hidden" name="parentId" value="${organ.parentId}"/>
		<input type="hidden" name="id" value="${organ.id}"/>
		<table align="center" class="tb_edit Tab_box">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right"><span class="T_title">机构名称:</span></td>
                <td><input id="orgName" name="orgName" value="${organ.orgName}" type="text" ltype="text" class="validate[required,maxSize[64]] ui-textbox content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">机构编码:</span></td>
                <td><input id="orgCode" name="orgCode" value="${organ.orgCode}" type="text" ltype="text" class="validate[optional,maxSize[32]] ui-textbox content3" /></td>
                <td></td>
            </tr>
            <tr>
            	<td align="right"><span class="T_title">菜单状态:</span></td>
                <td><s:resSelect name="status" value="${organ.status}" code="organStatus" ltype="select" /></td>
                <td colspan="4"></td>
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


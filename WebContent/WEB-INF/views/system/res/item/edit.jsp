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
<title>字典项编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">

editResult = '${result}';
editMessage = '${message}';
editAction = '${action}';
editObjId = '${item.id}';

$(function ()
{				
	checkEditSuccess();
	if(typeof checkEditError == "function")checkEditError();
	if(typeof baseInit == "function")baseInit();
});

</script>

</head>
<body> 
	<div class="wFull hL TK_title">当前字典：${group.name}</div>
	<form:form id="formSave"  action="${ctx}/system/res/item/save.do" method="post">
		<input type="hidden" name="action" value="${action}"/>
		<input type="hidden" name="grpId" value="${group.id}"/>
		<input type="hidden" name="parentId" value="${item.parentId}"/>
		<input type="hidden" name="id" value="${item.id}"/>
		<table align="center" class="tb_edit Tab_box">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right"><span class="T_title">字典项名称:</span></td>
                <td><input id="resName" name="name" value="${item.name}" type="text" ltype="text" class="validate[required,maxSize[64]] content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">状态:</span></td>
                <td><s:resSelect name="status" value="${item.status}" code="sysStatus" ltype="select" /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">字典项编码:</span></td>
                <td colspan=4><input id="code" name="code" value="${item.code}" type="text" ltype="text" class="validate[required,maxSize[64]] content3" /></td>
                <td></td>
            </tr>   
            <tr>
                <td align="right"><span class="T_title">说明:</span></td>
                <td colspan=4><input id="remark" name="remark" value="${item.remark}" type="text" class="validate[optional,maxSize[128]] content3" ltype="text"  /></td>
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


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
<title>字典编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">

editResult = '${result}';
editMessage = '${message}';
editAction = '${action}';
editObjId = '${group.id}';

$(function ()
{				
	checkEditSuccess();
	if(typeof checkEditError == "function")checkEditError();
	if(typeof baseInit == "function")baseInit();
});

</script>

</head>
<body> 
	<form:form id="formSave" modelAttribute="group" action="${ctx}/system/res/group/save.do" method="post">
		<input type="hidden" name="action" value="${action}"/>
		<input type="hidden" name="id" value="${group.id}"/>
		<table align="center" class="tb_edit Tab_box">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right"><span class="T_title">字典名称:</span></td>
                <td><input id="name" name="name" value="${group.name}" type="text" ltype="text" class="validate[required,maxSize[64]] ui-textbox content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">字典项关系:</span></td>
                <td><s:resSelect name="type" value="${group.type}" code="resGroupType" ltype="select" /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">字典编码:</span></td>
                <td><input id="code" name="code" value="${group.code}" type="text" ltype="text" class="validate[required,maxSize[64]] ui-textbox content3" /></td>
                <td></td>
                <td align="right"><span class="T_title">字典项类型:</span></td>
                <td><s:resSelect name="itemType" value="${group.itemType}" code="resItemType" ltype="select" /></td>
                <td></td>
            </tr>   
            <tr>
                <td align="right"><span class="T_title">说明:</span></td>
                <td><input id="remark" name="remark" value="${group.remark}" type="text" class="validate[optional,maxSize[128]] ui-textbox content3" ltype="text"  /></td>
                <td></td>
                <td align="right"><span class="T_title">字典组类型:</span></td>
                <td><s:resSelect name="status" value="${group.status}" code="resGroupStatus" ltype="select" /></td>
                <td></td>
            </tr>
            <%-- <tr>
                <td align="right">用户可添加:</td>
                <td align="left" colspan=4><form:checkbox path="addEnable" value="${group.addEnable}"/></td>
                <td></td>
            </tr>   
            <tr>
                <td align="right">用户可修改:</td>
                <td align="left" colspan=4><form:checkbox path="editEnable" value="${group.editEnable}"/></td>
                <td></td>
            </tr>   
            <tr>
                <td align="right">用户可删除:</td>
                <td align="left" colspan=4><form:checkbox path="delEnable" value="${group.delEnable}"/></td>
                <td></td>
            </tr>    --%>
            <tr>
           		<td align="center" colspan=6 >
           		<input id="bnSave" type="submit" value="保 存" class="buttonl1" />
           		</td>
            </tr>
		</table>
    </form:form>
</body>
</html>


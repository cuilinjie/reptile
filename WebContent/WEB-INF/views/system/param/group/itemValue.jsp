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
<title>参数值编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">
	$(function ()
	{
		<c:if test="${result=='success'}">
		window.parent.closeDlg();
		</c:if>

		$("#formSave").ligerForm({inputWidth:200});
		$("input").filter(".ip2").ligerTextBox({ width: 553 });
		$("#formSave").validationEngine({promptPosition: "centerRight"});	

		<c:if test="${result=='error'}">
		$.ligerDialog.error('${message}');
		</c:if>
	});

</script>

</head>
<body> 
	<form id="formSave" action="${ctx}/system/param/group/saveValue.do" method="post">
		<input type="hidden" name="id" value="${group.paramGrpId}"/>
		<table align="center" class="tb_edit">
			<tr>
				<td class="td_w1"></td>
				<td class="td_w7"></td>
				<td class="td_w3"></td>
			</tr>
      <c:forEach var="item" items="${group.sysParamItems}" >
	        <tr>
	          <td align="right">${item.paramName}：</td>
	          <td>
	            <input id="${item.paramCode}" name="${item.paramCode}" value="${item.valueText}" type="text" class="validate[required]" />
	          </td>
	          <td></td>
	        </tr>
      </c:forEach>
            <tr>
           		<td align="center" colspan=6 >
           		<input id="bnSave" type="submit" value="保 存" class="l-button bn2 mg6" />
           		</td>
            </tr>
		</table>
    </form>
</body>
</html>


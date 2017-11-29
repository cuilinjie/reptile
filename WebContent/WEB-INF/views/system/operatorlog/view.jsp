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
<title>日志查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/minRes.jsp" %>
</head>
<body> 
		<table align="center" class="tb_edit Tab_box">
			<tr>
				<td class="td_w1"></td>
				<td class="td_w2"></td>
				<td class="td_w3"></td>
				
			</tr>
            <tr>
                <td align="right"><span class="T_title">业务模块:</span></td>
                <td>${log.operatorModel}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作类型:</span></td>
                <td>${log.operatorActionStr}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作用户:</span></td>
                <td>${log.operatorPersion}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">客户端IP:</span></td>
                <td>${log.clientIpaddr}</td>
                <td></td>
            </tr>   
            <tr>
                <td align="right"><span class="T_title">操作时间:</span></td>
                <td>${log.operatorTimeStr}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作内容:</span></td>
                 <td><textarea readonly="readonly" style="padding:0px;margin-left:1%;margin-top:1%;height:100px;width:100%;">${log.operatorContext}</textarea></td>
                <td></td>
            </tr>
		</table>

</body>
</html>


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
                <td align="right"><span class="T_title">日志标题:</span></td>
                <td>${log.title}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">日志内容:</span></td>
                <td>${log.content}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">日志时间:</span></td>
                <td><s:date value="${log.createdTime}"/></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作用户:</span></td>
                <td>${log.operater}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">用户名称:</span></td>
                <td>${log.username}</td>
                <td></td>
            </tr>   
            <tr>
                <td align="right"><span class="T_title">所属机构:</span></td>
                <td>${log.orgName}</td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">客户端IP:</span></td>
                <td>${log.clientIp}</td>
                <td></td>
            </tr>   
		</table>

</body>
</html>


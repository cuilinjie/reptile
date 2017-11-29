<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>超时或未登录</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />

<script type="text/javascript" >
alert('超时或未登录！请重新登录系统！');
top.window.location.href='${ctx}/login.jsp';
if( typeof(window.opener)=='object' ){
  window.opener.location.href=window.opener.location.href;
  window.opener.location.reload();
  window.close();
}
</script>
</head>
<body>
超时或未登录
</body>
</html>
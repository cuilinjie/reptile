<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>${s:paramVal("SystemName")}</title>
<meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="${ctx}/style/css/common.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/js/common.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-cookies/jquery.cookies.2.2.0.min.js" type="text/javascript"></script>

<script type="text/javascript">

var cookie_login = '${ctx}/loginName';
$(function ()
{
  if( window != top ){
    top.location.href = location.href;
  }
  
  var loginName = $.cookies.get(cookie_login);
  if(loginName != null){
    document.frmLogin.loginName.value = loginName;
    document.frmLogin.chkSave.checked = true;
    document.frmLogin.loginPasswd.focus();
  }
  else{
	document.frmLogin.loginName.focus();	  
  }
  
  $("#loginName").keypress(function(e)
  {
    return next_ctrl_onfocus($("#loginPasswd"), e);
  });
  
  $("#login").click(function()
  {
    return login();
  });

});

function login()
{
  var nameObj = document.frmLogin.loginName;
  if(nameObj.value.length == 0) {
    alert('请输入用户名！');
    nameObj.focus();
    return false;
  }
  var passwdObj = document.frmLogin.loginPasswd;
  if(passwdObj.value.length == 0) {
    alert('请输入密码！');
    passwdObj.focus();
    return false;
  }

  var chkSaveObj = document.frmLogin.chkSave;
  if(chkSaveObj.checked==true) {
	  $.cookies.set(cookie_login, nameObj.value, { hoursToLive: 180*24 }); /* 保存180天 */
  }
  else{
	  $.cookies.del(cookie_login);
  }

  document.frmLogin.submit();
  return false;
}

</script>
<style type="text/css">
body{background:url("${ctx}/style/img/login_bg1.jpg") #01518c no-repeat; background-position: right bottom;background-attachment: fixed;background-size: contain; font-family:"微软雅黑";}
.align-center{position:fixed; left:50%; top:30%; margin-left:-250px; margin-top:-100px;} 
.loginbg{ /* background:url("${ctx}/style/img/dl_bg.png") no-repeat; */ background:#0088D2; border-radius:20px;opacity:0.8;width:491px; height:295px; position:relative; text-align:left;}
.bon_text{ font-size:18px; text-align:center; color:#FFFFFF; padding-top:50px;}
.bon_text div{ margin-bottom:14px;}
.bon_text div label{  width:60px;  text-align:right; line-height:30px; }
.bon_text input{ vertical-align:middle;color: #000;}
.bon_text p{margin:10px 0;}
.bon_text #chk{ margin-bottom:2px; margin-right:4px;}
.bon_text #msg{ color:#FF3131; font-size:16px;  padding-top:0px;}
.bon_login{ margin-left: 190px;}
.bon_login input{background:url("${ctx}/style/img/aa.png") no-repeat; background-size:100%; padding:10px 30px;cursor: pointer;
font-weight: bold;color:#023773; font-size:16px;border:none;width: 150px;height: 44px; }
.ipt{height:30px;border:1px solid #0060BD; border-radius:5px; padding-left:10px;width:180px;}
.Login_title{font-size:30px; text-align:center; color:#FFF;  line-height:60px; margin-bottom:20px;font-weight:bold; text-shadow:1px 1px #000}
</style>
</head>

<body>

	




  <div class="align-center">
  <div class="Login_title">SE设备管理平台</div>
    <form name="frmLogin" action="${ctx}/login.do"  method="post">
      <div class="loginbg">
      	<div class="bon_text fl">
      		<div>
      		<label>用户名：</label><input id="loginName"  name="loginName" class="ipt" type="text"  value="" tabindex="1">
      		</div>
      		<div>
      		<label>密　码：</label><input id="loginPasswd"  name="loginPasswd" class="ipt" type="password" value="" tabindex="2">
      		</div>
      		<p><input type="checkbox" id="chk" name="chkSave" value="1" tabindex="3"><span style="font-size:14px;">保存用户名</span></p>
      		<p id="msg">${message}</p>
      	</div>
      	<div class="bon_login"><input id="login" name="imageField" type="button" value="登录" <%-- src="${ctx}/style/img/dl1.png" --%>  tabindex="4"></div>
      </div>
    </form>
  </div>
</body>
</html>
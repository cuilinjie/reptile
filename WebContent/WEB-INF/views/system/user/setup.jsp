<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户个人设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<script type="text/javascript">
	$(function ()
	{				
		$("#formSave").ligerForm({inputWidth:200});
    	<shiro:hasPermission name="userSetup:passwd"> 
		$("#chkEditPasswd").change(function()
	    {
			if( this.checked ) {
				$("#isEditPasswd").attr("value","1");
				$("#trEditPasswd").css({display:''});
			}else{
				$("#isEditPasswd").attr("value","0");
				$("#trEditPasswd").css({display:'none'});
			}
        });
    	</shiro:hasPermission>  

		<c:if test="${result=='success'}">
		window.parent.$.ligerDialog.success('保存成功'); 
		</c:if>
		<c:if test="${result=='error'}">
		window.parent.$.ligerDialog.error('${message}');
		</c:if>
		<c:if test="${MenuStyleChange=='true'}">
		window.parent.location.href='${ctx}/index.do';
		</c:if>
		
	});

</script>

</head>
<body> 
	<div class="wFull hL">当前用户：${curUser.loginName}</div>
	<form:form id="formSave" modelAttribute="user" action="${ctx}/system/user/setup.do" method="post">
		<input type="hidden" name="userId" value="${curUser.id}"/>
		<input type="hidden" id="isEditPasswd" name="isEditPasswd" value="0"/>
		<table align="center" class="tb_edit">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
			</tr>
    		<shiro:hasPermission name="userSetup:passwd"> 
            <tr>
                <td align="right">修改密码:</td>
                <td align="left"><input id="chkEditPasswd" type="checkbox" value="1"/></td>
                <td></td>
            </tr>
            <tr id="trEditPasswd" style="display:none">
                <td align="right">用户密码:</td>
                <td align="left"><input id="loginPasswd" name="loginPasswd" value="" type="password" ltype="password" class="validate[required,maxSize[32]] content3" /></td>
                <td></td>
            </tr>
            </shiro:hasPermission>  
    		<shiro:hasPermission name="userSetup:menuStyle"> 
            <tr>
                <td align="right">菜单风格:</td>
                <td align="left">
                <div class="content3">
                <s:resSelect id="menuStyle" name="menuStyle" value="${curUser.menuStyle}" code="loginMenuStyle" ltype="select" /></td>
                </div>
                <td></td>
            </tr>
            </shiro:hasPermission>  
    		<shiro:hasPermission name="userSetup:skinName"> 
            <tr>
                <td align="right">皮肤风格:</td>
                <td align="left">
                <div class="content3">
                <s:resSelect id="skinName" name="skinName" value="${curUser.skinName}" code="loginSkinName" ltype="select" /></td>
                </div>
                <td></td>
            </tr>
            </shiro:hasPermission>
    		<shiro:hasPermission name="userSetup:mobile"> 
            <tr>
                <td align="right">手机号码:</td>
                <td align="left"><input id="mobile" name="mobile" value="${curUser.mobile}" type="text" ltype="text" class="validate[optional,maxSize[32]] content3" /></td>
                <td></td>
            </tr>
            </shiro:hasPermission>  
    		<shiro:hasPermission name="userSetup:email"> 
            <tr>
                <td align="right">电子邮箱:</td>
                <td align="left"><input id="email" name="email" value="${curUser.email}" type="text" ltype="text" class="validate[optional,maxSize[128]] content3" /></td>
                <td></td>
            </tr>
            </shiro:hasPermission>  
            <tr>
           		<td align="center" colspan=3 >
           		<input id="bnSave" type="submit" value="保 存" class="l-button bn2 mg6" />
           		</td>
            </tr>
		</table>
    </form:form>
</body>
</html>


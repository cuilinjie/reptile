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
<title>用户编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<%@ include file="/jsp/editRes.jsp" %>
<style type="text/css">
.redBorder{
border-color:red;
}
</style>
<script type="text/javascript">

editResult = '${result}';
editMessage = '${message}';
editAction = '${action}';
	
$(function ()
{				
	checkEditSuccess();
	mainInit();
	if(typeof checkEditError == "function")checkEditError();
	if(typeof baseInit == "function")baseInit();
    $("#formSave").submit(function(event){
    	if(!checkValue()){
    		event.preventDefault();
    	}
    });
});

function mainInit()
{
	$("#mainOrg").ligerComboBox({
    	width: 196, selectBoxWidth: 196, selectBoxHeight: 200, 
    	textField: 'orgName', valueFieldID: 'orgId',
    	initValue: '${user.orgId==null ? LOGIN_USER_MAIN_ORGAN.id:user.orgId}',
    	treeLeafOnly: false,
		tree: { url: timeURL('${ctx}/system/organ/getMinData.do?treeCode=${LOGIN_USER_MAIN_ORGAN.treeCode}'), 
    		textFieldName: 'orgName', checkbox: false }
    });
	
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
}
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
}

function checkValue(){
	var loginName = $("#loginName").val().trim();
	if(checkStrNull(loginName,"用户账号",$("#loginName"))){		
		return false;
	}
	if(!checkStrMaxLength(loginName,20,"用户账号",$("#loginName"))){
		return false;
	}	
	if(!checkLoginName( $("#loginName").val())){
		return false;
	}
	$("#loginName").parent().removeClass("redBorder");
	var username = $("#username").val().trim();
	
	if(checkStrNull(username,"用户名称",$("#username"))){
		return false;
	}
	if(!checkStrMaxLength(username,20,"用户名称",$("#username"))){
		return false;
	}
	if(username.length != $("#username").val().length){
		$.ligerDialog.error("用户名称前后不能存在空格","错误",function(){
			$("#username").focus();
		});
		return false;
	}
	if(!checkUserName(username)){
		$("#username").focus();
		return false;
	}
	$("#username").parent().removeClass("redBorder");
	var passwd = $("#loginPwd").val();
	if(editAction=='add' || (editAction=='edit' && $("#isEditPasswd").val()==1)){
		if(checkStrWithSpace(passwd,"用户密码",$("#loginPwd"))){
			return false;
		}		
		if(!checkStrMaxLength(passwd,18,"用户密码",$("#loginPwd"))){
			return false;
		}
		if(!checkStrMinLength(passwd,6,"用户密码",$("#loginPwd"))){
			return false;
		}	
	}
	$("#loginPwd").parent().removeClass("redBorder");
	var mobile = $("#mobile").val();
	if(checkStrNull(mobile,"手机号码",$("#mobile"))){
		return false;
	}
	if(!checkPhoneNum(mobile)){
		return false;
	}
	$("#mobile").parent().removeClass("redBorder");
	var email = $("#email").val();
	if(checkStrNull(email,"电子邮箱",$("#email"))){
		return false;
	}
	if(!checkEmail(email)){
		return false;
	}
	$("#email").parent().removeClass("redBorder");
	return true;
}

function checkStrNull(str,strlabel,target){
	if(str.length==0){
		$.ligerDialog.error(strlabel + "为空",'错误',function(){
			target.focus();
			target.parent().addClass("redBorder");
		});
		return true;
	}
	return false;
}
function checkStrWithSpace(str,strlabel,target){
	var withoutSpace = str.replace(/\s/g,"");
	if(str.length != withoutSpace.length){
		$.ligerDialog.error(strlabel + "不能包含空格",'错误',function(){
			target.focus();
			target.parent().addClass("redBorder");
		});
		return true;
	}
	return false;
}
function checkLoginName(loginName){
	var regx=/^[A-Za-z0-9_-]+$/g;
	if(!regx.test(loginName)){
		$.ligerDialog.error("用户账号不能包含空格等特殊字符","错误",function(){
			$("#loginName").focus();
			$("#loginName").parent().addClass("redBorder");
		});
		
		return false;
	}
	return true;
}
function checkUserName(userName){
	var regx=/^([^\x00-\xff]|[A-Za-z0-9_-])+\s*([^\x00-\xff]|[A-Za-z0-9_-])+$/g;
	if(!regx.test(userName)){
		$.ligerDialog.error("用户名称不能包含特殊字符","错误",function(){
			$("#username").focus();
			$("#username").parent().addClass("redBorder");
		});
		return false;
	}
	return true;
}
function checkPhoneNum(phone){
	var phoneReg = /^1[3|4|5|7|8]\d{9}$/;
	if(!phoneReg.test(phone)){ 
		$.ligerDialog.error("手机号码无效","错误",function(){
			$("#mobile").focus();
			$("#mobile").parent().addClass("redBorder");
		});
        return false; 
    } 
	return true;
}
function checkEmail(email){
	 var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	 if(!myreg.test(email))
	 {
	  $.ligerDialog.error('邮箱地址无效',"错误",function(){
			$("#email").focus();
			$("#email").parent().addClass("redBorder");
		});
	  return false;
	}
	 return true;
}
function checkStrMaxLength(str,maxLen,strlabel,target){
	if(str.length>maxLen){
		$.ligerDialog.error(strlabel + "不能超过" + maxLen + "个字符","错误",function(){
			target.focus();
			target.parent().addClass("redBorder");
		});
		return false;
	}
	return true;
}

function checkStrMinLength(str,minLen,strlabel,target){
	if(str.length<minLen){
		$.ligerDialog.error(strlabel + "不能少于" + minLen + "个字符","错误",function(){
			target.focus();
			target.parent().addClass("redBorder");
		});
		return false;
	}
	return true;
}
</script>

</head>
<body> 
	<form:form id="formSave" modelAttribute="user" action="${ctx}/system/user/save.do" method="post">
		<input type="hidden" name="action" value="${action}"/>
		<input type="hidden" name="id" value="${user.id}"/>
		<input type="hidden" id="orgId" name="orgId" value="${user.orgId}"/>
		<input type="hidden" id="isEditPasswd" name="isEditPasswd" value="0"/>
		<table align="center" class="tb_edit Tab_box">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td align="right"><span style="color:red;">*</span><span class="T_title">用户类型:</span></td>
                <td><s:resSelect id="type" name="type" value="${user.type}" code="userType" filter="i.code<'${LOGIN_USER.type}'" ltype="select" /></td>
                <td></td>
                <td align="right"><span style="color:red;">*</span>用户状态:</td>
                <td><s:resSelect id="status"  name="status" value="${user.status==false?0:1}" code="userStatus" ltype="select" /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span style="color:red;">*</span><span class="T_title">用户账号:</span></td>
                <td><input autocomplete=off id="loginName" name="loginName" value="${user.loginName}" type="text" ltype="text" class="validate[required,maxSize[32]] content3" /></td>
                <td></td>
                <td align="right"><span style="color:red;">*</span><span class="T_title">用户名称:</span></td>
                <td>
                <input autocomplete="off" id="username" name="username" value="${user.username}" type="text" ltype="text" class="validate[optional,maxSize[32]] content3" /></td>
				<td style="display: none;"><input type="text" name="ignore"/><!-- 解决火狐下有默认值的bug --></td>
                <td></td>
            </tr>
            <tr>
            <c:if test="${action=='add'}">
                <td align="right"><span style="color:red;">*</span><span class="T_title">用户密码:</span></td>
                <td style="display:none;"><input type="password" name="ignore1"/></td><!-- 解决火狐下有默认值的bug -->
                <td><input autocomplete=off id="loginPwd" name="loginPwd" value="${user.loginPwd}" type="password" ltype="password" class="validate[required,maxSize[32]] content3" /></td>
                <td></td>
            </c:if>
            <c:if test="${action=='edit'}">
                <td align="right"><span class="T_title">修改密码:</span></td>
                <td align="left"><input id="chkEditPasswd" type="checkbox" value="1"/></td>
                <td></td>
            </c:if>
                <td align="right"><span style="color:red;">*</span><span class="T_title">所属机构:</span></td>
                <td><input id="mainOrg" name="mainOrg" type="text" /></td>
                <td></td>
            </tr>
            <c:if test="${action=='edit'}">
            <tr id="trEditPasswd" style="display:none">
                <td align="right"><span style="color:red;">*</span><span class="T_title">用户密码:</span></td>
                <td colspan=4><input id="loginPwd" name="loginPwd" value="" type="password" ltype="password" class="validate[required,maxSize[32]] content3" /></td>
                <td></td>
            </tr>
            </c:if>
            <tr>
                <td align="right"><span class="T_title">用户说明:</span></td>
                <td colspan=4><input id="remark" name="remark" value="${user.remark}" type="text" class="validate[optional,maxSize[128]] content3" ltype="text"  /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span style="color:red;">*</span><span class="T_title">手机号码:</span></td>
                <td><input id="mobile" name="mobile" value="${user.mobile}" type="text" ltype="text" class="validate[optional,maxSize[32]] content3" /></td>
                <td></td>
                <td align="right"><span style="color:red;">*</span><span class="T_title">电子邮箱:</span></td>
                <td><input id="email" name="email" value="${user.email}" type="text" ltype="text" class="validate[optional,maxSize[128]] content3" /></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span style="color:red;">*</span><span class="T_title">菜单风格:</span></td>
                <td><s:resSelect id="menuStyle" name="menuStyle" value="${user.menuStyle}" code="loginMenuStyle" ltype="select" /></td>
                <td></td>
                <td align="right"><span style="color:red;">*</span><span class="T_title">皮肤风格:</span></td>
                <td><s:resSelect id="skinName" name="skinName" value="${user.skinName}" code="loginSkinName" ltype="select" /></td>
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


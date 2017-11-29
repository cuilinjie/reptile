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
<title>清除日志</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">
	$(function ()
	{
		<c:if test="${result=='success'}">
			window.parent.loadData();     
			window.parent.closeDlg();
		</c:if>

		$("#formClear input[ltype='date']").ligerDateEditor({showTime:true, format:"yyyy-MM-dd HH:mm:ss", width:150}); 
		$("#formClear").ligerForm({inputWidth:150});
		
		var dtNow = new Date();
		var dtStr = getFormatDate(dtNow);
		$("#formClear input[name='clearTime']").val(dtStr);
		
		$("#bnClear").click(function(){
    		var clearTime =$("#formClear input[name='clearTime']").val();
       		var clearType =$("#formClear input[name='clearType']:checked").val();
        	if( clearType == 2 ){
        		if( clearTime == "" ){
        			$.ligerDialog.error('请输入指定的清除日志时间！');
        			return false;
        		}
        	}
        	if( clearTime == "" ){
        		$("#formClear input[name='clearTime']").val(dtStr);
        	}

			$.ligerDialog.confirm('是否确认清除数据？', function (yes)
            {
                if(yes) $("#formClear").submit();
            });
        	
        	return true;
        });

		<c:if test="${result=='error'}">
		$.ligerDialog.error('${message}');
		</c:if>
	});
	
</script>
</head>
<body> 
	<form:form id="formClear" modelAttribute="user" action="${ctx}/system/log/clear.do" method="post">
		<table align="center" class="tb_edit">
			<tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
            <tr>
                <td></td>
                <td><input type="radio" name="clearType" value="1" checked />清除所有日志</td>
                <td colspan="3"></td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="radio" name="clearType" value="2" />清除指定时间以前的所有日志</td>
                <td colspan="3"><input name="clearTime" class="" type="text" ltype="date"/></td>
                <td></td>
            </tr>
            <tr>
           		<td align="center" colspan=6 >
           		<input id="bnClear" type="button" value="清 除" class="l-button bn2 mg6" />
           		</td>
            </tr>
		</table>
    </form:form>
</body>
</html>


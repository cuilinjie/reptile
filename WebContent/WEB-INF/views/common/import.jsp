<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>导入数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

$(function ()
{            
	checkResult('${result}', '${message}');
});

function checkResult(result, message)
{
	if( result=='success' && message!='' ){
		window.parent.loadData();  
    	$.ligerDialog.success(message);
    }
    if( result=='error' && message!='' ){
    	$.ligerDialog.error(message);
    }
}

</script>
</head>
<body class="whFull"> 
	<div class="wFull hL">
  	<form id="formDownload" method="post" target="hidden_frame">
		<a href="${ctx}/file/template-${tplName}.xls" class="l-button bn4 mg6" style="width:80px">下载模板</a>
  	</form>
	</div>
  	<form id="formImport" action="${EXCEL_IMP_URL}" method="post" enctype="multipart/form-data" target="hidden_frame">
	    <table align="center" class="tb_edit">
	      <tr>                
	        <td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
	        <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
	      </tr>
	      <tr>
	        <td align="right">导入文件:</td>
	        <td colspan=4><input type="file" id="impFile" name="impFile" class="validate[required,maxSize[512]]" size="75"  style="width:520px; height:22px;"/></td>
	        <td></td>
	      </tr>
	      <tr>
	        <td align="center" colspan=6 >
	          <input type="submit" value="开始导入" class="l-button bn4 mg6" style="width:80px;"/>
	        </td>
	      </tr>
	    </table>
    </form>
	<iframe id="hidden_frame" name='hidden_frame' style='display:none'></iframe>
</body>
</html>


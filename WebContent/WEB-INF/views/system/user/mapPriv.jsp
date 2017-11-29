<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户关联权限</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/minRes.jsp" %>
    <script type="text/javascript">
    	var dataMain = {};
    	var gridMain;
        var selectRowData = null;
        var selectMoveType = null;

        $(function ()
        {
            dataMain.Rows = ${jsonTreeData};
            gridMain = $("#maingrid").ligerGrid({
                columns: 
                    [
                    { display:'权限组', name:'grpName', width:150, align:'left' },
                    { display:'组全选', name:'grpChk', width:60, align:'center' },
                    { display:'关联权限', name:'grpPrivChk', width:800, align:'left' }
               		],  
               	tree: { columnName: 'grpName' },
				data: dataMain,
				alternatingRow: 'true',
                width: '100%',
                height:'100%', 
                pageSize: '50',
                pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
                onSelectRow: function (data, index, dom){
                	selectRowData = data;
                } 
            });
            
            $("#mapForm").ajaxForm({
            	dataType: 'json',
            	success: function(data) { 
           			if(data.result=='success'){
           				$.ligerDialog.success('关联成功！');
           			}
           			else{
           				$.ligerDialog.error('关联失败！');
           			}
            	}
            }); 

        });

        function chkGroup(obj){
        	$("input[group='"+obj.id+"']").attr("checked",obj.checked); 
        }
        
        function chkAll(obj){
        	$("input[name='chkGrp']").attr("checked",obj.checked); 
        	$("input[name='chkpriv']").attr("checked",obj.checked); 
        }
    </script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
  <form style="padding:0px;" id="mapForm" action="${ctx}/system/user/mapPriv.do" method="post">
  <input type="hidden" name="userId" value="${user.id}"/>
  <div style="width:100%; padding:5px; overflow:hidden; ">
    <table>
      <tr>
        <td>当前用户"${user.loginName}" 关联权限设置</td>
        <td width="10"></td>
        <td> <input type="checkbox" onclick="chkAll(this)"/>全选</td>
        <td width="10"></td>
        <td><input id="bnMap" type="submit" value="确 定" class="l-button bn2"/></td>
      </tr>
    </table>
  </div>    
  <div id="maingrid"></div> 
  </form>
</body>
</html>


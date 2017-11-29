<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>移动菜单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/minRes.jsp" %>

    <script type="text/javascript">
    	var dataMain = {};
    	var gridMain;
        var selectRowData = null;
        var selectMoveType = null;

        $(function ()
        {
        	selectMoveType = $("#moveType").ligerComboBox({width:200,selectBoxWidth:200,selectBoxHeight:100});
            dataMain.Rows = ${jsonTreeData};
            gridMain = $("#maingrid").ligerGrid({
                columns: 
                    [
                    { display:'菜单名称', name:'name', width:180, align:'left' },
                    { display:'菜单URL', name:'url', width:250, align:'left' },
                    { display:'菜单说明', name:'remark', width:250, align:'left' }
               		],  
               	tree: { columnName: 'name' },
				data: dataMain,
				alternatingRow: 'true',
                width: '100%',
                height:'100%', 
                pageSize: '20',
                pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
                onSelectRow: function (data, index, dom){
                	selectRowData = data;
                } 
            });
            
        });
        
        function onMove()
        {
            if (!selectRowData) { 
            	window.parent.$.ligerDialog.error('请选择一条记录！'); 
            	return; 
            }
            var moveType = $("#moveType").val();
            move(selectRowData.id, moveType);
        }

        function move(targetId,moveType)
        {
        	var url = timeURL('${ctx}/system/menu/move.do?moveId=${menu.id}&targetId='+targetId+'&moveType='+moveType);
        	$.ajax({
        		url: url,
        		dataType: 'json',
        		success: function(data) {
           			if(data.result='success'){
           				window.parent.loadData();    
           				window.parent.closeDlg();
           			}
        		}
        	});        			
        }

    </script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
  <div style="width:100%; padding:5px; overflow:hidden; ">
    <table>
      <tr>
        <td>当前菜单"${menu.name}" 移动方案：</td>
        <td><s:resSelect id="moveType" name="moveType" code="treeMoveType" ltype="select" /></td>
        <td><a class="l-button" style="width:60px; float:left; margin-left:6px;" onclick="onMove()">确 定</a></td>
      </tr>
    </table>
  </div>    
  <div id="maingrid"></div> 
</body>
</html>


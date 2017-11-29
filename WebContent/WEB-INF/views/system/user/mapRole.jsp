<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户角色分配</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/minRes.jsp" %>
    <script type="text/javascript">
    	var dataMain = {};
    	var gridMain = null;
        var checkedItem = [];
        $(function ()
        {
            checkedItem = ${jsonMapData};
            dataMain.Rows = ${jsonListData};
            gridMain = $("#maingrid").ligerGrid({
                columns: 
                    [
                    { display:'角色名称', name:'name', width:200, align:'left' },
                    { display:'角色编码', name:'code', width:160, align:'left' },
                    { display:'描述', name:'remark', width:300, align:'left' }
               		],  
				data: dataMain,
				alternatingRow: 'true', checkbox: true,
                isChecked: f_isChecked, onCheckRow: f_onCheckRow, onCheckAllRow: f_onCheckAllRow,
                width: '100%', height:'99%', 
                pageSize: '20', pageSizeOptions: [10, 20, 30, 50, 100, 200, 500]
            });
               

            $("#bnMap").click(function(){
            	if((checkedItem != null && checkedItem.length == 1)
            			|| (checkedItem != null && checkedItem.length == 2 && checkedItem[0] == null)) {
            		var checkedIteml;
            		if (checkedItem.length == 1) {
            			checkedIteml = checkedItem[0];
            		} else {
            			checkedIteml = checkedItem[1];
            		}
            		var url = timeURL('${ctx}/system/user/mapRole.do?userId=${user.id}&mapId='+checkedIteml);
            		$.ajax({
                		url: url,
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
            	}else{
            		$.ligerDialog.error('请选择一条记录！');
            	}
            });

        });

        function f_isChecked(rowdata)
        {
            for(var i=0;i<checkedItem.length;i++){
            	if( checkedItem[i] == rowdata.id ){
            		return true;
            	}
            }        	
            return false;
        }
        function f_onCheckAllRow(checked)
        {
            for (var rowid in this.records)
            {
                if(checked)
                    addCheckedItem(this.records[rowid]['id']);
                else
                    removeCheckedItem(this.records[rowid]['id']);
            }
        }
        function findCheckedItem(ItemID)
        {
            for(var i =0;i<checkedItem.length;i++)
            {
                if(checkedItem[i] == ItemID) return i;
            }
            return -1;
        }
        function addCheckedItem(ItemID)
        {
            if(findCheckedItem(ItemID) == -1){
                checkedItem.push(ItemID);
            }
        }
        function removeCheckedItem(ItemID)
        {
            var i = findCheckedItem(ItemID);
            if(i==-1) return;
            checkedItem.splice(i,1);
        }
        function f_onCheckRow(checked, data)
        {
            if (checked) addCheckedItem(data.id);
            else removeCheckedItem(data.id);
        }
        function f_getChecked()
        {
            alert(checkedItem.join(','));
        }
    </script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
  <form style="padding:0px;" id="mapForm" action="${ctx}/system/user/mapPriv.do" method="post">
  <input type="hidden" name="id" value="${user.id}"/>
  <div style="width:100%; padding:5px; overflow:hidden; ">
    <table>
      <tr>
        <td>当前用户"${user.loginName}" 关联角色设置</td>
        <td width="5"></td>
        <td><input id="bnMap" type="button" value="确 定" class="l-button bn2"/></td>
      </tr>
    </table>
  </div>    
  <div id="maingrid"></div> 
  </form>
</body>
</html>


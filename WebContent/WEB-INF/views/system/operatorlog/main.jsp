<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="sysoperatorlog:read"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日志管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />

<%@ include file="/jsp/mainRes.jsp" %>
<script type="text/javascript">

objName = '详细日志';

baseUrl = '${ctx}/system/operator/operatorlog';
isViewDlgExist = true;

$(function ()
{
    $("#toptoolbar").ligerToolBar({ 
		items: 
    		[
        	{ text:'查询', click:f_onSearch, icon:'search' },
        	{ line:true },
        	{ text:'刷新', click:f_onRefresh, icon:'refresh' }
        	]
    });
    dataMain.Rows = ${jsonData};
    gridMain = $("#maingrid").ligerGrid({
        columns: 
            [
            { display:'操作时间', name:'operatorTime', width:150, align:'center', type:'date',
            	render: function (row){
            		return long2StrDate(row.operatorTime);
            	}
            },
            { display:'业务模块', name:'operatorModel', width:150, align:'left' },
            { display:'操作类型', name:'operatorAction', width:100, align:'left' },
            { display:'操作内容', name:'operatorContext', width:450, align:'left' },
            { display:'操作用户', name:'operatorPersion', width:100, align:'left' },
            { display:'客户端IP', name:'clientIpaddr', width:100, align:'left', type:'ip' },
            { display:'查看详情', width:70, render: function (row)
                {
            	
	            	if (row.operatorAction != "导出") {
	            		var html = '';
	                    html += '<div onclick="viewRow(\''+row.operatorTime+'\',\''+row.operatorModel+'\',\''+row.operatorAction+'\',\''+row.operatorPersion+'\',\''+row.clientIpaddr+'\',\''+row.operatorContext+'\');" class="l-bar-button" onmouseover="bnMouseOver(this);" onmouseout="bnMouseOut(this);" ><span class="mg3 l-icon-view"></span></div>';
	                    return "<table style='margin:0 auto'><tr><td>"+html+"</td></tr></table>";
	            	}
                }
            }
       		],  
       		data: dataMain,
			alternatingRow: 'true',
	        width: '100%', height:'99%', 
	        pageSize: 20, pageSizeOptions: [10, 20, 30, 50, 100, 200, 500],
	        onSelectRow: f_onSelectRow
    });
    $("#formSearch").ajaxForm({
    	dataType: 'json',
    	success: function(data) { 
    		dlgSearch.hide();
   			dataMain.Rows = data;
			gridMain.reload();
			gridMain.changePage('first'); 
    	}
    });  
    $("#bnSearch").click(formatDate11);
    $("#bnReset").click(f_bnReset);
});

function formatDate11(){
	
	if( dlgSearch!=null ){
		var form = dlgSearch.dialog[0].ownerDocument.forms[0];
		var ipAddress = form.filter_LIKES_client_ipaddr.value;  
		var part_addr;
		if (ipAddress) {
			var pattern = /^([0-9.]+)$/;
			if(ipAddress &&!pattern.test(ipAddress)){  
				parent.SHOWMESSAGE("ip地址格式不正确，请修改");  
			    return false;  
			}
			part_addr=ipAddress.split(".");
			if (part_addr.length > 4) {
				parent.SHOWMESSAGE("ip地址格式不正确，请修改");  
			    return false;  
			}
			for (var i=0;i<part_addr.length;i++) {
				if (part_addr[i] > 255) {
					parent.SHOWMESSAGE("ip地址格式不正确，请修改");  
				    return false;  
				}
			}
		}
		
		var stTime = form.begin_time.value;
		var enTime = form.end_time.value;
		if (form.begin_time.value==undefined||form.begin_time.value=='') {
			parent.SHOWMESSAGE('请选择起始时间','提示信息');
			return false;
	    }
		if (!enTime) {
			parent.SHOWMESSAGE('请选择截止时间','提示信息');
			return false;
	    }
		
	    stTime = stTime.replace(/-/g,'/');
	    enTime = enTime.replace(/-/g,'/');
	    
	    var today = formatDate(new Date()); 
	    var starttimes = today.replace(/-/g,'/');
	    
	    if (new Date(stTime).getTime() -new Date(starttimes).getTime() > 0) {
	    	parent.SHOWMESSAGE('起始时间不能大于现在，请确认。','提示信息');
			return false;
	    }
	    
	    if (new Date(enTime).getTime() -new Date(starttimes).getTime() > 0) {
	    	parent.SHOWMESSAGE('截止时间不能大于现在，请确认。','提示信息');
			return false;
	    }
	    if (new Date(stTime).getTime() -new Date(enTime).getTime() > 0) {
	    	parent.SHOWMESSAGE('起始时间不能大于截止时间，请确认。','提示信息');
			return false;
	    }
	    var diff = new Date(enTime).getTime() - new Date(stTime).getTime();
        var diff_day = diff/(1000*60*60*24); 
	    if (diff_day > 31 ) {
	    	parent.SHOWMESSAGE('时间间隔大于31天。请重新选择起始时间和截止时间。','提示信息');
			return false;
	    }
	    
		if(stTime!=undefined&&stTime!=''){
			stTime = stTime.replace(/-/g,'/');
			form.filter_LEL_operator_time.value = new Date(stTime).getTime();
		}
		if(enTime!=undefined&&enTime!=''){
			enTime = enTime.replace(/-/g,'/');
			form.filter_GEL_operator_time.value = new Date(enTime).getTime();
		}
	}
	
	
}
function formatDate(datetime){
	return new Date(datetime).Format("yyyy-MM-dd hh:mm:ss");
}
function viewRow(id,operatorModel,operatorAction,operatorPersion,clientIpaddr,operatorContext)
{
	
	var url = timeURL(baseUrl + '/view.do?id=' + id + '&operatorModel='
				+ encodeURI(encodeURI(operatorModel)) + '&operatorAction=' + encodeURI(encodeURI(operatorAction))
				+ '&operatorPersion=' + encodeURI(encodeURI(operatorPersion)) + '&operatorContext='
				+ encodeURI(encodeURI(operatorContext)) + '&clientIpaddr='+clientIpaddr);
		window.parent.parent.showDlg('查看' + objName, viewDlgWidth,
				viewDlgHeight, url);
	}
	
function f_bnReset()
{
   	var form = $(this).closest("form")[0];
   	resetForm(form);
   	$(form).find("input[name~='begin_time']").val('');
   	$(form).find("input[name~='end_time']").val('');
   	$(form).find("input[ltype='begin_time']:first").focus();
	return false;
}	
</script>
</head>
<body style="padding:0; overflow:hidden; position:relative;"> 
	<div id="toptoolbar"></div>
    <div id="maingrid"></div> 
    <div style="display:none;">
    <div id="searchDlg" style="padding:0; position:relative;">
    	<form id="formSearch" action="${ctx}/system/operator/operatorlog/search.do" method="post">
    	<input  type="hidden" name="filter_LEL_operator_time" value="" />
    	<input  type="hidden" name="filter_GEL_operator_time" value="" />
    	<table align="center" class="tb_edit Tab_box">
<!--             <tr>                
            	<td class="td_w1"></td><td class="td_w2"></td><td class="td_w3"></td>
                <td class="td_w4"></td><td class="td_w5"></td><td class="td_w6"></td>
			</tr>
 -->            <tr>
                <td align="right"><span class="T_title">起始时间:</span></td>
                <td>
                <input name="begin_time" id="begin_time" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'end_time\')}'})" class="Wdate" style="width:196px"/>
                </td>
                <!-- <td><input name="begin_time" class="ip1" type="text" ltype="date"/></td> -->
                <td></td>
                <td align="right"><span class="T_title">截止时间:</span></td>
                <td>
                <input name="end_time" id="end_time" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'begin_time\')}'})" class="Wdate" style="width:196px"/>
                </td>
                <!-- <td><input name="end_time" class="ip1" type="text" ltype="date"/></td> -->
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">业务模块:</span></td>
                <td>
                <div class="content3">
                <s:resSelect name="filter_EQI_operator_model" code="operatorModel" prompt="全部" ltype="select" style="border:none;width:197px; " /></div>
                </td>
                <td></td>
                <td align="right"><span class="T_title">操作类型:</span></td>
                <td>
                <div class="content3"> <s:resSelect name="filter_EQI_operator_action" code="operatorAction" prompt="全部" ltype="select" style="border:none;width:197px; "/></div>
                 </td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><span class="T_title">操作用户:</span></td>
                <td><div class="content3"><input name="filter_LIKES_operator_persion" class="ip1" type="text" ltype="text"/></div></td>
                <td></td>
                <td align="right"><span class="T_title">客户端IP:</span></td>
                <td><div class="content3"><input name="filter_LIKES_client_ipaddr" class="ip1" type="text" ltype="text"/></div></td>
                <td></td>
            </tr>
            <tr>
                <td colspan=3 align="right" style="padding:10px">
                <input id="bnSearch" type="submit" value="查 询" class="buttonl1"/>
                </td>
                <td colspan=3 style="padding:10px">
                <input id="bnReset" type="button" value="重 置" class=" buttonl1 "/>
                </td>
            </tr>   
        </table>
        </form>
    </div>
    </div>
</body>
</html>
</shiro:hasPermission>  


<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/mytld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />

<%@ include file="/jsp/mainRes.jsp"%>
<title>新浪看点采集</title>
</head>
<style type="text/css">
.tdl{
	border:solid #AAAAAA;
	border-width:0px 1px 1px 0px;
	vertical-align:middle;
	text-align:center;
}
.tdl2{
	border:solid #AAAAAA;
	border-width:0px 1px 1px 0px;
	vertical-align:middle;
	text-align:right;
}
.tablel{
	border:solid #AAAAAA;
	border-width:1px 0px 0px 1px;
	font-family: inherit;
	font-size:13px;
}

</style> 
<body style="padding:0; overflow:hidden; position:relative;height:550px"> 
<div>
  <div >
  <dl class="list clear fw_lineHeight50" style="padding:0px;margin-left:1%;margin-top:1%;">
		<dt>
		<table class="Tab_box">
		<tr>
			<td>
				<table>
				 <tr>	
				    <td><span class="T_title">视频内容：</span>
						<select class="content" id="hasVideo"> 
						   <option value="">---请选择---</option>
						   <option value="0">无视频</option>
						   <option value="1">有视频</option>
						</select>
					</td>					
					 <td>
			    <span class="T_title">采集时间：</span>
             	<input name="beginDate" id="beginDate" placeholder="开始时间" class="Wdate" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'#F{$dp.$D(\'endDate\',{M:-11})}'})" style="width:150px;" />
            		至	
                <input name="endDate" id="endDate" placeholder="结束时间" class="Wdate" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'#F{$dp.$D(\'beginDate\',{M:11})}'})" style="width:150px;"/>
            </td>
            <td>
            <input id="" type="button" value="查询" class="buttonl1"  onclick = "submit()"/>
            <input id="" type="button" value="开始采集" class="buttonl1"  onclick = "startCollect()"/>
            </td>    
				 </tr>
				</table>
			</td>			
		</tr>  
         </table>  
		</dt>
  </dl>
  </div> 
  
<p class="fw_title"><span>新浪看点采集</span></p>
<div id="maingrid" style="margin-top:1%;"></div> 
</div> 
</body>
<script type="text/javascript"> 
var ctx = '${ctx}';
var baseUrl = '${ctx}/reptile';
$(function(){
	submit();
});

function submit() {
	var hasVideo = $("#hasVideo").val();
	var beginDate = $("#beginDate").val();
	var endDate = $("#endDate").val();	
	queryData(hasVideo,beginDate,endDate);
}

var columns = [  {
	display : '采集时间',
	name : "collectTime",
	align : 'center',
	width : '150'
},{
	display : '标题',
	name : "title",
	align : 'center'
},{
	display : '评论数',
	name : "commentNum",
	align : 'center',
	width : '100'
}, {
	display : '点赞数',
	name : "likedNum",
	align : 'center',
	width : '100'
}, {
	display : '视频内容',
	name : "hasVideo",
	align : 'center',
	width : '100',
	render:function(row,index,value){
		if(value =="0"){
			return "无视频"
		}else if(value=="1"){
			return "有视频";
		}else{
			return "未知";
		}
	}
},{
	display : '操作',
	align : 'center',
	width:'100',
	render:function(row){
		var html = '<a href="javascript:viewDetail(\''+row.id+'\');">查看</a></span>';
		return html;
	}
} 
];
function queryData(hasVideo,startTime,endTime){
	var url = baseUrl + '/list.do';
	var count = 0;//onSuccess事件会被重复触发导致提示消息多次出现，所以通过count变量控制让提示消息只出现一次
	$("#maingrid").ligerGrid({
			columns : columns,
			url:url,	
			root:'rows',
			record:'total',
			parms:[{name:"hasVideo",value:hasVideo},{name:"startTime",value:startTime},{name:"endTime",value:endTime}],
			usePager:true,
			enabledSort:true,
			dataAction:'server',
			width : '100%',
			height : '100%',
			fixedCellHeight:false,
			pageSize:30,
			pageSizeOptions:[30,50,100],
			onSuccess:function(data,grid){
				if(data.total==0 && count==0){
					$.ligerDialog.warn('没有查询到数据', '提示信息');
				}
				count++;
			}
		});	
}
function startCollect(){
	$.ajax({
		url: baseUrl + "/start.do",
		dataType:'json',
		async: false,
		cache:false,
		success: function(data) {
			if(data.result=="Y"){
				$.ligerDialog.success('已启动采集程序，请稍后点击"查询"钮查看采集到的数据', '提示信息');
			}else{
				$.ligerDialog.warn('启动采集程序发送错误，请联系管理员', '提示信息');
			}
		}
	});
}
function viewDetail(id){
	var url = timeURL(baseUrl + '/detail.do?id=' + id);
	window.open(url);
}
</script>
</html>
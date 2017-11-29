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
<script type="text/javascript" src="${ctx}/clipboard/clipboard.min.js"></script>
<title>文章内容</title>
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
p{
font-size:18px;
line-height:32px;
}
</style> 
<body style=""> 
  <p>
      <input id="top_copy" type="button" value="复制文章内容" class="copy buttonl1" data-clipboard-action="copy" data-clipboard-target="#content"/>
      <input id="" type="button" value="查看原文" class="view_source buttonl1"/>
  </p>
  <div style="margin:0 auto;width:70%;"><h2 id="title"></h2></div>
  <div id="content" style="padding-top:10px;width:70%;margin:0 auto;">
  
  </div>
    <p>
      <input id="bottom_copy" type="button" value="复制文章内容" class="copy buttonl1" data-clipboard-action="copy" data-clipboard-target="#content"/>
      <input class="view_source buttonl1" type="button" value="查看原文"/>
  </p>
</body>
<script type="text/javascript"> 
var ctx = '${ctx}';
var baseUrl = '${ctx}/reptile';
var id = "${id}";
$(function(){
	 var clipboard = new Clipboard("#top_copy");
     clipboard.on('success', function(e) {
         alert("复制成功");
     });

     clipboard.on('error', function(e) {
         console.log("复制失败");
     });
     
     var bclipboard = new Clipboard("#bottom_copy");

     bclipboard.on('success', function(e) {
         alert("复制成功");
     });

     bclipboard.on('error', function(e) {
         console.log("复制失败");
     });
	$.ajax({
		url: baseUrl + "/getone.do?id="+id,
		dataType:'json',
		async: false,
		cache:false,
		success: function(data) {
			if(data){
				$("#title").html(data.title);
				$("#content").html(data.content);				
                $(".view_source").click(function(){
					window.open(data.sourceUrl);
				});
               
			}
		}
	});
});
function winclose(){
	parent.closeDlg();
}
</script>
</html>
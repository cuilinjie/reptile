<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
  request.setAttribute("ctx", request.getContextPath());
%>
<!-- <%="http://" + request.getServerName() + ":"
					+ request.getLocalPort() + request.getRequestURI()%> -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>上传</title>
<!-- bootstrap css -->
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link href="${ctx}/bootstrap-3.3.5-dist/css/bootstrapv2.3.1.min.css" rel="stylesheet" type="text/css"/>
<!-- upload css -->
<link rel="stylesheet" type="text/css" href="${ctx}/upload/css/webuploader.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/upload/css/style.css" />

<!-- page common css -->
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css">

<!-- jquery js -->
<script src="${ctx}/jquery-easyui-1.4.1/jquery.min.js"></script>
<!-- upload js -->
<script src="${ctx}/upload/Math.uuid.js"></script>
<script src="${ctx}/upload/require.js" data-main="${ctx}/apjs/apInformation/apApply/uploader.js"></script>
<script>
	var ctx = '${ctx}';
	$(function(){
		$("[name=btnViewImage]").click(function(){
			var iWidth=600; //弹出窗口的宽度;
			var iHeight=400; //弹出窗口的高度;
			var imageType = $(this).attr("imageType");
			var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
			var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
			window.open(ctx+"/ap/file/getImage/baidu.do?applyid="+$("#entity_applyid").val()+'&imageType='+imageType,'newwindow','height='+iHeight+', width='+iWidth+', top='+iTop+', left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no' );
		});
		getpictrue();
		
	});
	function getpictrue() {
		var url = ctx+'/ap/file/querypicture.do?applyid='+$("#entity_applyid").val();
		$.ajax({
		type:"GET",
		url:url,
		cache:false,
		dataType:"json",
		success:function (data){
			$("#aaa").val(data.jsonData);
			$("#bbb").val(data.jsonData2);
			$("#ccc").val(data.jsonData3);
			if (!$("#aaa").val()) {
				$(".qqq").hide();
			} else {
				$(".qqq").show();
			}
			if (!$("#bbb").val()) {
				$(".www").hide();
			} else {
				$(".www").show();
			}
			if (!$("#ccc").val()) {
				$(".eee").hide();
			} else {
				$(".eee").show();
			}
		}
		});
	}
</script>
</head>
<body>
<input id="entity_applyid" type="hidden" value="${param.applyid }"/>
<input id="aaa" type="hidden" value="}"/>
<input id="bbb" type="hidden" value=""/>
<input id="ccc" type="hidden" value=""/>
<input id="TYPE" type="hidden" value="APAPPLY" /> 
	<div id="wrapper">
		<div id="container">
			<!--头部，相册选择和格式选择-->
			<div id="uploader">
				<div class="queueList">
					<div id="dndArea" class="placeholder">
						<div id="filePicker"></div>
						<p>单次最多可选3张</p>

					</div>
				</div>
				<div class="statusBar" style="display: none;">
					<div class="progress">
						<span class="text">0%</span> <span class="percentage"></span>
					</div>
					<div class="info"></div>
					<div class="btns">
						<div id="filePicker2"></div>
						<div class="uploadBtn">开始上传</div>
					</div>
				</div>
				<div
					style="border-radius: 5px; margin: 10px 20px; padding: 10px 0px; border: 1px solid red;">
					<p style="font-size: 14px; color: red; padding-left: 20px;">
						注：请按照以下规则命名图片名字<br /> 营业执照-bussiness<br />
						组织机构代码证-org<br /> eID服务商牌照-service<br /> 例如：bussiness.png
					</p>
				</div>
			</div>
			<dl class="list line-height50">
				<dt>
					<label>营业执照：</label>
					<button type="button" name="btnViewImage" imageType="bussiness"
						class="btn btn-info qqq">查看</button>
				</dt>
				<dt>
					<label>组织机构代码证：</label>
					<button type="button" name="btnViewImage" imageType="org"
						class="btn btn-info www">查看</button>
				</dt>
				<dt>
					<label>eID服务商牌照：</label>
					<button type="button" name="btnViewImage" imageType="service"
						class="btn btn-info eee" >查看</button>
				</dt>
				<div style="clear: both; visibility: hidden;"></div>
			</dl>
		</div>
	</div>
</body>
</html>
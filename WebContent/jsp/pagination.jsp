<%@ page language="java" pageEncoding="UTF-8"%>
<div class="tableContrl right">
	共
	<b class="bluefont" id="total">${data.total}</b>条, 每页显示条,
	<span id="pageNum">${data.pageNum}</span>/
	<span id="pages">${data.pages}</span>&nbsp;&nbsp;
	<a id="firstPage" href="javascript:search(1)">首页</a>&nbsp;
	<a id="hasPreviousPage" href="javascript:search(${data.prePage})">&lt;&lt;前页</a>&nbsp;
	<a id="hasNextPage" href="javascript:search(${data.nextPage})">后页&gt;&gt;</a>&nbsp;
	<a id="lastPage" href="javascript:search(${data.pages})">尾页</a>&nbsp;
	<input name="" type="text" id="pageNo" class="inputText" value="${data.pages}" />
	<span>页</span>
	<input onclick="tiaozhuan();" name="" type="button" value="跳转" class="go_sub" title="前往" />
</div>
<script type="text/javascript">
	function tiaozhuan() {
		var no = $("#pageNo").val().replace(/[^\d]/g, '');
		if(no<1 || no>$("#pages").val()){
			alert("页号超出允许的范围！");
			return false;
		} else {
			search(no);
		}
	}
	function setpage(json) {
		$('#pageSize a').removeClass('hover');
		//分页设置
		$("#total").html(json.total);
		$("#pageNum").html(json.pageNum);
		$("#pages").html(json.pages);
		$("#pageNo").attr("value", json.pages);
		if (json.isFirstPage) {
			$("#firstPage").removeAttr("href");
			$("#firstPage").addClass("uncheck");
		} else {
			$("#firstPage").attr("href", "javascript:search(1)");
			$("#firstPage").removeClass("uncheck");
		}
		if (json.isLastPage) {
			$("#lastPage").removeAttr("href");
			$("#lastPage").addClass("uncheck");
		} else {
			$("#lastPage")
					.attr("href", "javascript:search(" + json.pages + ")");
			$("#lastPage").removeClass("uncheck");
		}
		if (json.hasNextPage) {
			$("#hasNextPage").attr("href",
					"javascript:search(" + json.nextPage + ")");
			$("#hasNextPage").removeClass("uncheck");
		} else {
			$("#hasNextPage").removeAttr("href");
			$("#hasNextPage").addClass("uncheck");
		}
		if (json.hasPreviousPage) {
			$("#hasPreviousPage").attr("href",
					"javascript:search(" + json.prePage + ")");
			$("#hasPreviousPage").removeClass("uncheck");
		} else {
			$("#hasPreviousPage").removeAttr("href");
			$("#hasPreviousPage").addClass("uncheck");
		}
	}
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="/imgl/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
var pageFunc=function(params,url){
	var pageNo=1;
	var pageLimit=10;
	$("#prev").click(function(){
		if(pageNo<=1){
			alert("当前已是第一页");
			return;
		}
		if(params==null){
			params={"pageNo":parseInt(pageNo)-1};
		}else{
			params.pageNo=parseInt(pageNo)-1;
		}
		$.post(url,params,function(data){
			$("#tbody").html(data);
			pageNo=parseInt(pageNo)-1;
			rePage(pageNo);
		});
	});
	$("#next").click(function(){
		var totalPage=parseInt("${total}"/pageLimit)+1;
		if(pageNo==totalPage){
			alert("当前已是最后一页");
			return;
		}
		if(params==null){
			params={"pageNo":parseInt(pageNo)+1};
		}else{
			params.pageNo=parseInt(pageNo)+1;
		}
		$.post(url,params,function(data){
			$("#tbody").html(data);
			pageNo=parseInt(pageNo)+1;
			rePage(pageNo);
		});
	});
	$("#goto").click(function(){
		var expectPage=$("#page").val();
		var totalPage=parseInt("${total}"/pageLimit)+1;
		if(!/^[0-9]*$/.test(expectPage) || expectPage>totalPage || expectPage<1){
			alert("请输入范围内的页码");
			return;
		}
		if(params==null){
			params={"pageNo":expectPage};
		}else{
			params.pageNo=expectPage;
		}
		$.post(url,params,function(data){
			$("#tbody").html(data);
			pageNo=expectPage;
			rePage(pageNo);
		});
	});
}
function rePage(pageNo){
	var i=$("#pageSpan").html().indexOf("/");
	var str=$("#pageSpan").html().substring(0,3)
			+pageNo
			+$("#pageSpan").html().substring(i);
	$("#pageSpan").html(str);
}
</script>
<div class="pagination">
	<ul>
		<li><a href="javascript:;" id="prev">上一页</a></li>
		<li><span id="pageSpan">当前第1/<fmt:parseNumber
					integerOnly="true" value="${total/10+1}" />页
		</span></li>
		<li><a href="javascript:;" id="next">下一页</a></li>
		<li><span> <input type="tel" id="page" maxlength="3"
				onkeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;"
				style="width: 25px; padding: 0; margin: 0; ime-mode: disabled" /> 页
				<a href="javascript:;" id="goto">跳转</a>
		</span></li>
	</ul>
</div>
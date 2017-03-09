<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>我是团长</title>
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="/imgl/css/bootstrap.min.css">
<link rel="stylesheet" href="/imgl/css/ratchet.min.css">
<link rel="stylesheet" href="/imgl/css/normalize.css">
<link rel="stylesheet" href="/imgl/css/main.css">
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#tab1").click(function(){
		$(this).attr("class","control-item active");
		$("#item1mobile").attr("class","control-content bg has_nav active");
		$("#tab2").attr("class","control-item");
		$("#item2mobile").attr("class","control-content bg has_nav");
	});
	$("#tab2").click(function(){
		$("#tab1").attr("class","control-item");
		$("#item1mobile").attr("class","control-content bg has_nav");
		$(this).attr("class","control-item active");
		$("#item2mobile").attr("class","control-content bg has_nav active");
	});
	var pageNo=1;
	$("#loadMore1,#loadMore2").click(function(){
		var loadMore1=$("#loadMore1");
		var loadMore2=$("#loadMore2");
		$.post("/imgl/myOrder.do",{"pageNo":pageNo,"sub":"1"},function(data){
			if(data.trim()==""){
				loadMore1.after("<div style='color: #777; margin-left:10px;line-height:48px;margin-bottom:0;text-align: center;'>没有更多了</div>");
				loadMore1.remove();
				loadMore2.after("<div style='color: #777; margin-left:10px;line-height:48px;margin-bottom:0;text-align: center;'>没有更多了</div>");
				loadMore2.remove();
			}else{
				$("#thelist1").append(data);
			}
		});
		$.post("/imgl/myOrder.do",{"pageNo":pageNo,"sub":"2"},function(data){
			if(data.trim()==""){
			}else{
				$("#thelist2").append(data);
			}
		});
		pageNo++;
	});
	$("#item1mobile").height(window.innerHeight-94);
	$("#item2mobile").height(window.innerHeight-94);
});
function confirmReceive(pJnlNo){
	if(!confirm("是否确认收货"))
		return;
	var arr=$("input[type='hidden'][value='"+pjnlNo+"']");
	$.post("/imgl/confirmReceive.do",{pjnlNo:pjnlNo},function(data){
		$(arr).parent().prev("div").html("已签收");
		$(arr).parent().html("");
	});
}

</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="segmented-control" style="position:absolute;top:44px;z-index:5">
	<a id="tab1" class="control-item active" href="#item1mobile">全部订单</a>
	<a id="tab2" class="control-item" href="#item2mobile">待收货</a>
</div>
<div class="content">  
<div class="card bg" >
<span id="item1mobile" class="control-content bg has_nav active" style="overflow: scroll;">
  <ul class="table-view list cards" id="thelist1" style="margin-top: 50px;">
  <c:forEach items="${orderList }" var="xx">
	<li class="table-view-cell media">
	  <a data-ajax="false" href="/imgl/queryOrder.do?pJnlNo=${xx.pJnlNo}" data-transition="slide-in">
	 	<div class="date">下单时间：${xx.payTime }</div>
		<div class="media-left">
			<c:forEach items="${xx.activity.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
			  <img class="media-object" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		  	</c:forEach>
		</div>
		<div class="media-body">
          	<div class="h4">${xx.activity.title}</div>
		  	<p><span class="pull-left">数量：1</span><span class="text-danger pull-right">¥${xx.activity.originPrice }/件</span></p>
		</div>
		<p style="margin-top:5px"><span class="pull-left">共1件商品，免运费</span><span class="pull-right">实付&nbsp;<span class="text-danger">¥${xx.amount }</span></span></p>
		<div class="clear"></div>
	  </a>
	  <div class="btns">
		<div id="statusDiv" class="text-danger pull-left"><fmt:message key="payStatus.${xx.payStatus}" /></div>
		<div id="buttonDiv">
			<c:if test="${xx.payStatus=='AS' }">
			<input type="hidden" value="${xx.pJnlNo}"/>
			<input type="button" class="btn pull-right" onclick="confirmReceive(${xx.pJnlNo})" href="javascript:;" value="确认收货">
			<input type="button" class="btn pull-right" data-toggle="modal" data-target="#info1" href="#info1" value="查看物流"/>
			</c:if>
		</div>
	  </div>
	</li>
  </c:forEach>
  </ul>
  <a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
  	id="loadMore1" href="javascript:;">点击加载更多</a>
</span>
<span id="item2mobile" class="control-content bg has_nav" style="overflow: scroll;">
  <ul class="table-view list cards" id="thelist2" style="margin-top: 50px;">
  <c:forEach items="${orderList }" var="xx">
	<c:if test="${xx.payStatus=='AS' }">
	<li class="table-view-cell media">
	  <a data-ajax="false" href="/imgl/queryOrder.do?pJnlNo=${xx.pJnlNo}" data-transition="slide-in">
	 	<div class="date">${xx.payTime }</div>
		<div class="media-left">
			<c:forEach items="${xx.activity.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
			  <img class="media-object" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		  	</c:forEach>
		</div>
		<div class="media-body">
          	<div class="h4">${xx.activity.title}</div>
		  	<p><span class="pull-left">数量：1</span><span class="text-danger pull-right">¥${xx.activity.originPrice }/件</span></p>
		</div>
		<p style="margin-top:5px"><span class="pull-left">共1件商品，免运费</span><span class="pull-right">实付&nbsp;<span class="text-danger">¥${xx.amount }</span></span></p>
		<div class="clear"></div>
	  </a>
		<div class="btns">
			<div id="statusDiv" class="text-danger pull-left"><fmt:message key="payStatus.${xx.payStatus}" /></div>
			<div id="buttonDiv">
				<input type="hidden" value="${xx.pJnlNo}"/>
				<input type="button" class="btn pull-right" onclick="confirmReceive(${xx.pJnlNo})" href="javascript:;" value="确认收货">
				<input type="button" class="btn pull-right" data-toggle="modal" data-target="#info1" href="#info1" value="查看物流"/>
			</div>
		</div>
	</li>
	</c:if>
  </c:forEach>
  </ul>
  <a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
  	id="loadMore2" href="javascript:;">点击加载更多</a>
</span>
</div>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="3" /> 
</jsp:include>

<div class="modal fade info" tabindex="-1" role="dialog" aria-labelledby="info1" id="info1">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">物流信息</h4>
		</div>
		<div class="modal-body">
			<p>圆通快递，运单号：805007798210</p>
			<ul class="table-view">
				<li class="table-view-cell">
					<p class="text-danger">2015-10-22&nbsp;08:59:51</p>
					<p class="text-danger">【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
			</ul>
		</div>
	</div>
</div>

</body>
</html>
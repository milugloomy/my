<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta charset="utf-8">
<title>我是团长</title>
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/ratchet.min.css">
<link rel="stylesheet" href="css/normalize.css">
<link rel="stylesheet" href="css/main.css">
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/jquery.cookie.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#addrSpan").click(function(){
		$.cookie("pay_activityId","${activity.activityId}");
		$.cookie("pay_groupId","${groupId}");
		$.cookie("pay_buyType","${buyType}");
		window.location.href="/imgl/addrPayList.do";
	});
	$("#groupPay").click(function(){
		if("${address}"=="" || "${address}"==null){
			alert("请先添加地址");
			return;
		}
		var params = {
				"addrSeq" : "${address.addrSeq}",
				"activityId" : "${activity.activityId}",
				"groupId" : "${groupId}",
				"price" : "${price}"
			};
		if("${buyType}"=="group"){
			$.post("/imgl/groupPay.do",params,function(data){
				$.cookie("pay_activityId",null);
				$.cookie("pay_groupId",null);
				$.cookie("pay_buyType",null);
				window.location.href="myOrder.do";
			},"text");
		}
		if("${buyType}"=="single"){
			$.post("/imgl/singlePay.do",params,function(data){
				$.cookie("pay_activityId",null);
				$.cookie("pay_groupId",null);
				$.cookie("pay_buyType",null);
				window.location.href="myOrder.do";
			},"text");
		}
	});
});
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
<div class="info_panel cards pay">
  <div class="media" style="padding-top:15px;" >
    <a data-rel="dialog" id="addrSpan" href="javascript:;">
	<span class="icon icon-right-nav pull-right" style="font-size:14px;display:inline;line-height:28px;"></span>
	<div class="media-body">
  	<c:if test="${address!=null }">
	  	<p style="margin-left:0;">
	  	  <strong style="color:#555;">送至</strong>&nbsp;&nbsp;${address.name}&nbsp;&nbsp;${address.phoneNo}&nbsp;&nbsp;
	  	  <span class="label label-danger"><fmt:message key="addrType.${address.addrType}"/></span>
	  	</p>
	  	<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${address.provinceName}${address.cityName}${address.districtName}&nbsp;&nbsp;${address.detail}</p>
  	</c:if>
  	<c:if test="${address==null }">
  		<p>地址列表为空，点击添加新地址</p>
  	</c:if>
	</div>
	<div class="clear"></div>
	</a>
  </div>

  <div class="media" style="padding-bottom:15px;">
	<div class="media-left">
		<c:forEach items="${activity.imgUrlList}" var="yy">
		<c:if test="${yy.mainPic=='1' }">
			<img class="media-object" src="/imgl/img/${yy.imgUrl}" />
		</c:if>
		</c:forEach>
	</div>
	<div class="media-body">
	  <div class="h4">${activity.title}</div>
	  <p><span class="pull-left">数量：1</span><span class="text-danger pull-right">¥${price}/件</span></p>
	</div>
  </div>
  <p style="margin-top:10px;">
  	<span class="pull-right">快递&nbsp;¥0.00&nbsp;总价&nbsp;
  	  <span class="text-danger"><strong style="font-size:18px;">¥${price}</strong></span>
  	</span>
  </p>
  <div class="clear"></div>
  <p style="background-color:#ddd;margin:10px 0 0 0;padding-left:10px;">请选择支付方式</p>
  <ul class="table-view">	
	<li class="table-view-cell">
	  <label>
		<input type="radio" class="hidden-input" name="optionsRadios" id="optionsRadios1" value="option1" checked/>
		<span class="icon icon-check"></span><img src="img/pay1.png">微信支付
	  </label>
	</li>
  </ul>
  <a id="groupPay" class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;">立即支付</a>
</div>
			
<jsp:include page="intro.jsp"/>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="1" /> 
</jsp:include>

</body>
</html>
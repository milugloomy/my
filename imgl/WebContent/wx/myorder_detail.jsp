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
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav"">
<div class="info_panel">
<c:if test="${payJnl.payStatus=='APNG' or payJnl.payStatus=='AGNS'}" >
<img src="/imgl/img/tab3_detail2.jpg" class="img-responsive center-block" style="margin-top:15px;margin-bottom:15px;width:80%" >
</c:if>
<c:if test="${payJnl.payStatus=='AS'}" >
<img src="/imgl/img/tab3_detail3.jpg" class="img-responsive center-block" style="margin-top:15px;margin-bottom:15px;width:80%" >
</c:if>
<c:if test="${payJnl.payStatus=='AR'}" >
<img src="/imgl/img/tab3_detail.jpg" class="img-responsive center-block" style="margin-top:15px;margin-bottom:15px;width:80%" >
</c:if>
<table class="table order">
<tr>
	<td style="padding-top:15px;">订单状态：</td>
	<td class="text-danger" style="padding-top:15px;">
		<fmt:message key="payStatus.${payJnl.payStatus}"/>
	</td>
</tr>
<tr>
	<td>总额：</td>
	<td><strong class="text-danger">${payJnl.amount}</strong>（微信支付）</td>
</tr>
<tr>
	<td>送至：</td>
	<td>${payJnl.address.provinceName}&nbsp;${payJnl.address.cityName}&nbsp;${payJnl.address.districtName}&nbsp;${payJnl.address.detail}</td>
</tr>
<tr><td>收货人：</td><td>${payJnl.address.name}&nbsp;${payJnl.address.phoneNo}</td></tr>
<tr><td>订单编号：</td><td>${payJnl.pJnlNo}</td></tr>
<tr><td>下单时间：</td><td>${payJnl.payTime}</td></tr>
<tr><td>优惠信息：</td><td>无</td></tr>
<tr>
	<td>配送方式：</td>
	<td>
		<c:if test="${payJnl.deliComp!='' && payJnl.deliComp!=null}">
		<fmt:message key="deliComp.${payJnl.deliComp}"/>&nbsp;${payJnl.deliNo}
		</c:if>
	</td>
</tr>
</table>
</div>
<div class="info_panel cards">
	<div class="order_h4">商品信息</div>
	<div class="media" style="padding-bottom:15px;">
		<div class="media-left">
		<c:forEach items="${payJnl.activity.imgUrlList}" var="yy">
		  <c:if test="${yy.mainPic=='1' }">
			<img class="media-object" src="/imgl/img/${yy.imgUrl}" />
		  </c:if>
		</c:forEach>
		</div>
		<div class="media-body">
			<div class="h4">${payJnl.activity.title}</div>
			<p><span class="pull-left">数量：1</span><span class="text-danger pull-right">¥${payJnl.amount}/件</span></p>
		</div>
		<p style="margin-top:5px"><span class="pull-left">共1件商品，免运费</span><span class="pull-right">实付&nbsp;<span class="text-danger">¥${payJnl.amount}</span></span></p>
		<div class="clear"></div>
	</div>
</div>

</div>
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="3" /> 
</jsp:include>

</body>
</html>
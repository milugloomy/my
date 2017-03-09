<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
<div class="info_panel" style="margin-bottom:5px">
<div class="order_h4">拼团流程</div>
  <ol class="step0"><li><div class="t">选择心仪的商品</div>
  <img class="img-responsive" src="/imgl/img/step0_1.jpg">
  <p>选择拼团商品下单</p>
  </li><li><div class="t">支付开团或参团</div>
  <img class="img-responsive" src="/imgl/img/step0_2.jpg">
  <p>团长完成支付后，团即刻开启</p>
  </li>
  <li><div class="t">等待好友参团</div>
  <img class="img-responsive" src="/imgl/img/step0_3.jpg"></li>
  <li><div class="t">达到人数团购成功</div>
  <img class="img-responsive" src="/imgl/img/step0_4.jpg">
  <p>团购成功：从团长开团24小时内找到相应开团人数的好友参团，即该团成功，卖家发货</p>
  <img class="img-responsive" src="/imgl/img/step0_5.jpg">
  <p>团购失败：</p>
  <p>a、超过团有效期的24小时之后，为达到相应参团人数的团，则该团失败</p>
  <p>b、在团有效期24小时内，商品已提前售罄，但团人数还未达到相应要求，则该团也失效</p>
  </li></ol>
</div>
<div class="note0">
<p>拼团，是基于好友的组团购买，获取团购优惠，为了保证广大消费者的权益，我是团长有权判定为黄牛倒货的团解散并取消订单</p>
<p>团长：开团且该团第一位支付成功的人</p>
<p>参加成员：通过团长邀请购买该商品的成员即为参团成员，参团成员也可以邀请更多的成员参团</p>
<p>退款说明：系统会在1-2天内提交微信/支付宝处理，微信/支付宝审核后在2-5个工作日自动原路退款至您的支付账号</p></div>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="1" /> 
</jsp:include>

</body>
</html>
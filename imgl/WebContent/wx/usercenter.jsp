<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script>
$(document).ready(function(){
	$("#customerService").click(function(){
		$("#serviceTele").toggle();
	});
});	
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content has_nav">
	<img class="img-circle center-block" style="width:72px;height:72px;margin-top:30px;margin-bottom:10px" src="/imgl/img/user_img.png">
	<div class="text-center" style="margin-bottom:30px">${sessionScope.User.nickName}</div>
      <ul class="table-view">
  <li class="table-view-cell media">
    <a data-ajax="false" class="navigate-right" href="/imgl/myOrder.do">
      <span class="media-object pull-left icon icon-list" style="color:#A30008"></span>
      <div class="media-body">
        我的订单
      </div>
    </a>
  </li>
  <li class="table-view-cell media">
    <a data-ajax="false" class="navigate-right" href="/imgl/myGroup.do">
      <span class="media-object pull-left icon icon-pages" style="color:#A30008"></span>
      <div class="media-body">
        我的团
      </div>
    </a>
  </li>
  <li class="table-view-cell media">
    <a data-ajax="false" class="navigate-right" href="/imgl/myAddress.do">
      <span class="media-object pull-left icon icon-compose" style="color:#A30008"></span>
      <div class="media-body">
        我的地址
      </div>
    </a>
  </li>
   <li class="table-view-cell media">
    <a class="navigate-right">
      <span class="media-object pull-left icon icon-star-filled" style="color:#A30008"></span>
      <div class="media-body">
        我的优惠券
      </div>
    </a>
  </li>
  <li class="table-view-cell media">
    <a class="navigate-right" href="javascript:;" id="customerService">
      <span class="media-object pull-left icon icon-info" style="color:#A30008"></span>
      <div class="media-body">
        售后服务
      </div>
      <div id="serviceTele" style="display:none; color: #A30008;; margin-left:10px;line-height:48px; font-weight: bold; text-indent: 20px;">请拨打电话：135xxxxxxxx</div>
    </a>
  </li>
</ul>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="4" /> 
</jsp:include>
</body>
</html>
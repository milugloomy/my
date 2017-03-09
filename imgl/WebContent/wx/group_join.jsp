<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我是团长</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
<div class="info_panel">	
     <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
  <!-- Indicators -->
  <ol class="carousel-indicators">
    <c:forEach items="${activity.imgUrlList}" varStatus="status">
  	<c:if test="${status.index=='0' }">
    	<li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
  	</c:if>
  	<c:if test="${status.index!='0' }">
    	<li data-target="#carousel-example-generic" data-slide-to="${status.index}"></li>
  	</c:if>
    </c:forEach>
  </ol>
  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
  <c:forEach items="${activity.imgUrlList}" var="xx">
  	<c:if test="${xx.mainPic=='1' }">
    <div class="item active">
      <img src="/imgl/img/${xx.imgUrl}">
      <div class="carousel-caption"></div>
    </div>
    </c:if>
  	<c:if test="${xx.mainPic!='1' }">
    <div class="item">
      <img src="/imgl/img/${xx.imgUrl}">
      <div class="carousel-caption"></div>
    </div>
    </c:if>
  </c:forEach>
  </div>
  <!-- Controls -->
  <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </a>
  <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </a>
</div>

<div class="h4">${activity.title }</div>
<p>${activity.introduction }</p>

<p class="pull-left">市场价：${activity.originPrice }</p>
<p class="pull-right">已售：${activity.totalQuantity - activity.remain}件</p>
<div class="clear"></div>
<div class="row price">
  <div class="col-xs-12">
  	<a href="/imgl/payPre.do?buyType=group&groupId=${groupId}&activityId=${activity.activityId}"><strong>立即参团</strong></a>
  </div>
  <div class="col-xs-12">
  	<a style="background-color:#555;line-height:30px;" >${activity.groupPrice}/件&nbsp;<span class="icon icon-right-nav" style="font-size:16px;display:inline;"></span></a>
  </div>
</div>
</div>
  <jsp:include page="intro.jsp"/>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="1" /> 
</jsp:include>
</body>
</html>
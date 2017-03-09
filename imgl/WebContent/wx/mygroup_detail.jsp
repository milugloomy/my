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
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/ratchet.min.css">
<link rel="stylesheet" href="css/normalize.css">
<link rel="stylesheet" href="css/main.css">
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
<div class="text-center alert-success" style="padding:10px 0;font-size:20px;font-weight:bold">
	<span class="icon icon-check"></span>&nbsp;<fmt:message key="groupStatus.${group.status}"/>
	<p style="margin-bottom:0">
		<c:if test="${group.status=='BG'}">快去邀请好友加入吧</c:if>
		<c:if test="${group.status=='CP'}">我们会尽快为您发货，请耐心等待</c:if>
	</p>
</div>
<div class="info_panel cards">
	<div class="media" style="padding-top:10px; padding-bottom:10px;">
		<div class="media-left">
			<c:forEach items="${group.activity.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
			  	<img class="media-object" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		  	</c:forEach>
		</div>
		<div class="media-body">
        <div class="h4">${group.activity.title}</div>
			<p>
			<span class="pull-left">${group.activity.groupVol}人团：</span>
			<span class="text-danger">¥${group.activity.groupPrice}/件</span>
			</p>
		</div>
	</div>
	<c:if test="${group.status=='CP'}">
		<img style="position:absolute;top:10px;right:30px;width:72px;height:72px;" src="/imgl/img/group.png">
	</c:if>
</div>
<ul class="table-view group">
  <c:forEach items="${orderList}" var="xx" varStatus="status">
	<li class="table-view-cell media">
		<div class="media-left">
		  	<img class="media-object img-circle" src="${xx.user.headImgUrl }" />
		</div>
		<div class="media-body">
			<p>
				<span class="pull-left">${xx.user.nickName}&nbsp;<span class="label label-danger">
				<c:choose> 
  					<c:when test="${status.index=='0'}">团长 </c:when> 
				  	<c:when test="${status.index=='1'}">沙发</c:when> 
				  	<c:otherwise>&nbsp;&nbsp;</c:otherwise> 
				</c:choose> 
				</span></span>
				<span class="text-danger pull-right">${xx.payTime}&nbsp;
					<c:if test="${status.index=='0'}">开团</c:if>
					<c:if test="${status.index!='0'}">参团</c:if>
				</span>
			</p>
		</div>
	</li>
  </c:forEach>
</ul>
<a data-ajax="false" href="/imgl/queryActivity.do?activityId=${group.activity.activityId}" class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" >我也开个团</a>
<jsp:include page="intro.jsp"/>	
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="2" /> 
</jsp:include>

</body>
</html>
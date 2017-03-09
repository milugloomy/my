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
	var pageNo=1;
	$("#loadMore").click(function(){
		var obj=$(this);
		$.post("/imgl/aroundGroup.do",{"pageNo":pageNo,"activityId":"${activity.activityId}"},function(data){
			if(data.trim()==""){
				obj.after("<div style='color: #777; margin-left:10px;line-height:48px;margin-bottom:0;text-align: center;'>没有更多了</div>");
				obj.remove();
			}else{
				$("#thelist").append(data);
				pageNo++;
			}
		});
	});
});
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
<ul id="thelist" class="table-view groups">
	<c:forEach items="${groupList}" var="xx">
	<li class="table-view-cell media">
		<div class="media-left">
			  <img class="media-object" src="${sessionScope.User.headImgUrl}" />
		</div>
		<div class="media-body">
			<div class="h4">${activity.title}</div>
			<div class="progress">
				<div class="progress-bar progress-bar-danger" role="progressbar"
					aria-valuenow="2" aria-valuemin="0" aria-valuemax="100"
					style="min-width: 2em; width: ${xx.count/xx.total*100}%;"><fmt:formatNumber type="number" value="${xx.count/xx.total*100}" maxFractionDigits="2"/>%</div>
			</div>
			<p>
				<span class="pull-left">团长:${xx.leader.nickName}</span><span
					class="text-danger pull-right">已有${xx.count}/${xx.total}人加入</span>
			</p>
		</div>
		<div class="media-right">
			<a data-ajax="false" class="btn pull-right" href="/imgl/joinGroup.do?groupId=${xx.groupId}&activityId=${activity.activityId}">参团</a>
		</div>
	</li>
	</c:forEach>
</ul>
<a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
  	id="loadMore" href="javascript:;">点击加载更多</a>
<a data-ajax="false" style="padding-left:40%"
	href="/imgl/payPre.do?buyType=group&activityId=${activity.activityId}">我要做团长</a>
</div>

<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="1" /> 
</jsp:include>


  </body>
</html>
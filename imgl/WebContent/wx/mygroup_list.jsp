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
		$.post("/imgl/myGroup.do",{"pageNo" : pageNo},function(data){
			if(data.trim()==""){
				obj.after("<div style='color: #777; margin-left:10px;line-height:48px;margin-bottom:0;text-align: center;'>没有更多了</div>");
				obj.remove();
			}else{
				$("#thelist").append(data);
				pageNo++;
			}
		});
	});
	$("#dddd").height(window.innerHeight-94);
});
</script>
 </head>
<body>
<div id="aaaa"></div>
<jsp:include page="header.jsp"/>
<div id="dddd"  style="overflow: scroll;">	
  <ul class="table-view list cards"  id="thelist"  >
	<c:forEach items="${groupList}" var="xx">
	<li class="table-view-cell media">
		<a href="#" data-transition="slide-in">
		<div class="date">开团时间：${xx.startTime }</div>
		<div class="media-left">
			<c:forEach items="${xx.activity.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
			  <img class="media-object" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		  	</c:forEach>
		</div>
		<div class="media-body">
 			<div class="h4">${xx.activity.title }</div>
			<div style="position:relative;margin-left:20px;">
				<span class="price1">成团价：¥${xx.activity.groupPrice }/件&nbsp;&nbsp;</span><span class="price2">&nbsp;<span class="icon icon-right-nav" style="font-size:16px;display:inline;"></span>&nbsp;</span>
				<img style="position:absolute;top:1px;left:-15px;width:32px;height:32px" src="/imgl/img/list2.png">
			</div>
		</div>
		</a>
		<div class="btns">
			<div class="text-danger pull-left">
				<fmt:message key="groupStatus.${xx.status}"/>
			</div>
			<a data-ajax="false" class="btn pull-right" href="/imgl/queryOrderByGroupId.do?groupId=${xx.groupId}">查看订单详情</a>
			<a data-ajax="false" class="btn pull-right" href="/imgl/groupDetail.do?groupId=${xx.groupId }">查看团详情</a>
		</div>
	</li>
	</c:forEach>
  </ul>
  <c:if test="${groupList.size()==0}">
  	<p>您还没有团购过，点击首页开始拼团之旅吧</p>
  </c:if>
  <c:if test="${groupList.size()!=0}">
  <a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
  	id="loadMore" href="javascript:;">点击加载更多</a>
  </c:if>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="2" /> 
</jsp:include>

</body>
</html>
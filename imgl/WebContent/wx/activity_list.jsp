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
<link rel="stylesheet" type="text/css" href="css/scrollbar.css">
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
<script src="/imgl/js/iscroll.js"></script>
<script type="text/javascript">
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', loaded, false); 
/**
 * 滚动翻页 （自定义实现此方法）
 * myScroll.refresh();		// 数据加载完成后，调用界面更新方法
 */
 
var pageNo=1;
function pullUpAction () {
	$.post("/imgl/activityList.do",{"pageNo" : pageNo},function(data){
		if(data.trim()==""){
			$("#pullUpLabel").html("没有更多了");
			myScroll.refresh();		// 数据加载完成后，调用界面更新方法 Remember to refresh when contents are loaded (ie: on ajax completion)
		}else{
			$("#thelist").append(data);
			pageNo++;
			myScroll.refresh();		// 数据加载完成后，调用界面更新方法 Remember to refresh when contents are loaded (ie: on ajax completion)
		}
	});
}

</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav" id="dddd">
  <div id="wrapper">
	<div id="scroller">
		<div id="pullDown">
		</div>
      <ul class="table-view list" id="thelist">	  
      <c:forEach items="${activityList}" var="xx">
		<li class="table-view-cell media">
		<a data-ajax="false" href="/imgl/queryActivity.do?activityId=${xx.activityId}" data-transition="slide-in" >
		  <c:forEach items="${xx.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
			  <img class="img-responsive" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		  </c:forEach>
		<div class="h4" >${xx.title}</div>
		<p>${xx.introduction}</p>
		<div style="position:relative;margin-left:20px;">
			<span class="price1">${xx.groupVol}人团<strong>&nbsp;&nbsp;${xx.groupPrice}&nbsp;&nbsp;</strong></span><span class="price2">&nbsp;&nbsp;去开团&nbsp;
				<span class="icon icon-right-nav" style="font-size:16px;display:inline;"></span>&nbsp;
			</span>
			<img style="position:absolute;top:-8px;left:-15px;" src="/imgl/img/list2.png">
		</div>
		<p style="text-indent:50px;">市场价：<del>${xx.originPrice}</del></p>
		</a>
		<div class="mark"><p>${xx.discount}折</p><p>${xx.groupVol}人团</p></div>
		</li>
	  </c:forEach>
	  </ul>
	  <div id="pullUp">
		<span class="pullUpIcon"></span>&nbsp;&nbsp;&nbsp;&nbsp;<span class="pullUpLabel" id="pullUpLabel">加载更多...</span>
	  </div>
  	</div>
  </div>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="1" /> 
</jsp:include>
</body>
</html>
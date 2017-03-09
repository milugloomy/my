<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach items="${activityList}" var="xx">
<li class="table-view-cell media">
	<a data-ajax="false" href="/imgl/queryActivity.do?activityId=${xx.activityId}" data-transition="slide-in"  data-ajax="false"> 
		<c:forEach items="${xx.imgUrlList}" var="yy">
			<c:if test="${yy.mainPic=='1' }">
				<img class="img-responsive" src="/imgl/img/${yy.imgUrl}" />
			</c:if>
		</c:forEach>
		<div class="h4">${xx.title}</div>
		<p>${xx.introduction}</p>
		<div style="position: relative; margin-left: 20px;">
			<span class="price1">${xx.groupVol}人团<strong>&nbsp;&nbsp;${xx.groupPrice}&nbsp;&nbsp;</strong></span>
			<span class="price2">&nbsp;&nbsp;去开团&nbsp; 
			<span class="icon icon-right-nav"
				style="font-size: 16px; display: inline;"></span>&nbsp;
			</span> <img style="position: absolute; top: -8px; left: -15px;"
				src="/imgl/img/list2.png">
		</div>
		<p style="text-indent: 50px;">
			市场价：
			<del>${xx.originPrice}</del>
		</p>
	</a>
	<div class="mark">
		<p>${xx.discount}折</p>
		<p>${xx.groupVol}人团</p>
	</div>
</li>
</c:forEach>

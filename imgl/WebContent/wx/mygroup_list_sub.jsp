<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach items="${groupList}" var="xx">
	<li class="table-view-cell media">
		<a href="#" data-transition="slide-in">
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

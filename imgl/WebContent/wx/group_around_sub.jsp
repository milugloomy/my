<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

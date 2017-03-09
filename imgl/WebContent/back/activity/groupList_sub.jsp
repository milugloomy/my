<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach items="${groupList}" var="xx" varStatus="status">
<tr>
	<td>${status.index+1}</td>
	<td><a href="/imgl/backGroupDetail.do?groupId=${xx.groupId}">${xx.groupId}</a></td>
	<td>${xx.leader.nickName}</td>
	<td>
		<img class="media-object img-circle" src="${xx.leader.headImgUrl }" />
	</td>
	<td>${xx.count}/${xx.total}人</td>
	<td><fmt:message key="groupStatus.${xx.status}"/></td>
	<td>${xx.startTime}</td>
	<td>${xx.endTime}</td>
	<td>${xx.activity.title}</td>
	<td>
		<c:forEach items="${xx.activity.imgUrlList}" var="yy">
		<c:if test="${yy.mainPic=='1' }">
		  	<img  src="/imgl/img/${yy.imgUrl}" />
		</c:if>
	  	</c:forEach>
	</td>
	<td>
		<button class="btn" onclick="location='/imgl/backGroupDetail.do?groupId=${xx.groupId}'" title="详情">
			<i class="icon-th-list" ></i>
		</button>
	</td>
</tr>
</c:forEach>
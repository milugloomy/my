<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach items="${activityList}" var="xx" varStatus="status">
<tr>
	<td>${status.index+1}</td>
	<td>${xx.title}</td>
	<td>
		<c:forEach items="${xx.imgUrlList}" var="yy">
		<c:if test="${yy.mainPic=='1' }">
			<img class="img-responsive" src="/imgl/img/${yy.imgUrl}" />
		</c:if>
	  	</c:forEach>
	</td>
	<td>${xx.originPrice}</td>
	<td>${xx.groupPrice}</td>
	<td>${xx.groupVol}人</td>
	<td>${xx.totalQuantity}件</td>
	<td>${xx.remain}件</td>
	<td>${xx.startTime}</td>
	<td>
		<button class="btn" title="详情">
			<i class="icon-th-list" ></i>
		</button>
		<button class="btn" title="编辑">
			<i class="icon-pencil" ></i>
		</button>
		<button class="btn" href="#myModal" role="button"
			data-toggle="modal" title="删除">
			<i class="icon-remove" ></i>
		</button>
	</td>
</tr>
</c:forEach>

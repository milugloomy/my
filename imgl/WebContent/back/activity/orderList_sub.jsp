<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach items="${orderList}" var="xx" varStatus="status">
	<tr>
		<td>${status.index+1}</td>
		<td><a href="/imgl/backOrderDetail.do?pJnlNo=${xx.pJnlNo}">${xx.pJnlNo}</a></td>
		<td>${xx.payTime }</td>
		<td>${xx.address.name}&nbsp;${xx.address.phoneNo}</td>
		<td>${xx.amount}</td>
		<td>${xx.activity.title}</td>
		<td><fmt:message key="payStatus.${xx.payStatus}"/></td>
		<td>
			<button class="btn">
				<i class="icon-pencil" title="编辑"></i>
			</button>
		</td>
	</tr>
</c:forEach>
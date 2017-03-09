<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach items="${backJnlList}" var="xx" varStatus="status">
<tr>
	<td>${status.index+1}</td>
	<td><c:out value="${xx.bJnlNo}"/></td>
	<td><c:out value="${xx.userId}" /></td>
	<td><c:out value="${xx.transCode}" /></td>
	<td><c:out value="${xx.transTime}" /></td>
	<td><c:out value="${xx.returnCode}" /></td>
	<td><c:out value="${xx.returnMsg}" /></td>
	<td><c:out value="${xx.clientIp}" /></td>
</tr>
</c:forEach>

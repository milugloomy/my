<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/jquery.cookie.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
<script type="text/javascript">
function payPre(addrSeq){
	var buyType=$.cookie("pay_buyType");
	var activityId=$.cookie("pay_activityId");
	var groupId=$.cookie("pay_groupId");
// 	alert(buyType+"--"+activityId+"--"+groupId);
	window.location.href="/imgl/payPre.do?buyType="+buyType+"&groupId="+groupId+"&activityId="+activityId+"&addrSeq="+addrSeq;
}
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="content bg has_nav">
  <ul class="table-view forms">
  <c:forEach items="${addrList}" var="xx">
	<li class="table-view-cell media">
	  <a onclick="payPre(${xx.addrSeq})" href="javascript:;">
		<label>
		<span class="icon icon-check checkbox"></span>
		  <span name="defaultImg">
		  <c:if test="${xx.mainAddr == '1' }">
		  	<img class="def" src="/imgl/img/default.png">
		  </c:if>
		  </span>
			<div class="media-body">
			  <span class="label label-danger pull-right">
			    <fmt:message key="addrType.${xx.addrType}"/>
			  </span>
              <div class="h4">
              	${xx.name }
              </div>
			  <p>${xx.phoneNo }</p>
			  <p>${xx.provinceName }-${xx.cityName }-${xx.districtName }</p>
			  <p>${xx.detail }</p>
			</div>
    	</label>
      </a>
	</li>
  </c:forEach>
  </ul>
  <a data-ajax="false" href="/imgl/addrPayNewPre.do" class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" >新增地址</a>
</div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="4" /> 
</jsp:include>

  </body>
</html>
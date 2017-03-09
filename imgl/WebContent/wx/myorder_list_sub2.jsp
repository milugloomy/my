<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach items="${orderList }" var="xx">
<c:if test="${xx.payStatus=='AS' }">
<li class="table-view-cell media">
  <a data-ajax="false" href="/imgl/queryOrder.do?pJnlNo=${xx.pJnlNo}" data-transition="slide-in">
 	<div class="date">${xx.payTime }</div>
	<div class="media-left">
		<c:forEach items="${xx.activity.imgUrlList}" var="yy">
		<c:if test="${yy.mainPic=='1' }">
		  <img class="media-object" src="/imgl/img/${yy.imgUrl}" />
		</c:if>
	  	</c:forEach>
	</div>
	<div class="media-body">
         	<div class="h4">${xx.activity.title}</div>
	  	<p><span class="pull-left">数量：1</span><span class="text-danger pull-right">¥${xx.activity.originPrice }/件</span></p>
	</div>
	<p style="margin-top:5px"><span class="pull-left">共1件商品，免运费</span><span class="pull-right">实付&nbsp;<span class="text-danger">¥${xx.amount }</span></span></p>
	<div class="clear"></div>
  </a>
	<div class="btns">
		<div id="statusDiv" class="text-danger pull-left"><fmt:message key="payStatus.${xx.payStatus}" /></div>
		<div id="buttonDiv">
			<input type="hidden" value="${xx.pJnlNo}"/>
			<input type="button" class="btn pull-right" onclick="confirmReceive(${xx.pJnlNo})" href="javascript:;" value="确认收货">
			<input type="button" class="btn pull-right" data-toggle="modal" data-target="#info1" href="#info1" value="查看物流"/>
		</div>
	</div>
</li>
</c:if>
</c:forEach>

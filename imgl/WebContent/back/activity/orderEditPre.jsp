<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ch">
<head>
<meta charset="utf-8">
<title>我是团长管理后台</title>
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/imgl/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="/imgl/css/theme.css">
<link rel="stylesheet" href="/imgl/css/font-awesome/css/font-awesome.css">
<script src="/imgl/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="/imgl/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#doIt").click(function(){
		$("#errSpan").html("");
		var deliComp=$("#deliComp").val();
		if(deliComp==""){
			$("#errSpan").html("物流公司不能为空");return;
		}
		var deliNo=$("#deliNo").val();
		if(deliNo==""){
			$("#errSpan").html("物流单号不能为空");return;
		}
		$("#form").submit();
	});
});
</script>
</head>
<body class="">
<jsp:include page="../header.jsp" />
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">订单管理编辑</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> 
			<span class="divider">/</span></li>
		<li class="active">订单管理编辑</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="well">
			<div class="tab-pane active in" id="home">
			<form id="form" method="post" action="/imgl/backOrderEdit.do">
			<table class="table">
				<tr>
					<td style="width: 25%; border-top: none;">订单号:</td>
					<td style="border-top: none;">
					${payJnl.pJnlNo}<input type="hidden" name="pJnlNo" value="${payJnl.pJnlNo}">
					</td>
				</tr>
				<tr>
					<td>支付时间:</td>
					<td>${payJnl.payTime}</td>
				</tr>
				<tr>
					<td>收货人名:</td>
					<td>${payJnl.address.name}</td>
				</tr>
				<tr>
					<td>电话:</td>
					<td>${payJnl.address.phoneNo}</td>
				</tr>
				<tr>
					<td>详细地址:</td>
					<td>${payJnl.address.provinceName}${payJnl.address.cityName}${payJnl.address.districtName}${payJnl.address.detail}</td>
				</tr>
				<tr>
					<td>付款金额:</td>
					<td>${payJnl.amount}</td>
				</tr>
				<tr>
					<td>商品名称:</td>
					<td>${payJnl.activity.title}</td>
				</tr>
				<tr>
					<td>缩略图:</td>
					<td>
						<c:forEach items="${payJnl.activity.imgUrlList}" var="yy">
						<c:if test="${yy.mainPic=='1' }">
						  <img style="width: 250px;" src="/imgl/img/${yy.imgUrl}" />
						</c:if>
					  	</c:forEach>
					</td>
				</tr>
				<tr>
					<td>订单状态:</td>
					<td><fmt:message key="payStatus.${payJnl.payStatus}"/></td>
				</tr>
				<tr>
					<td>物流公司:</td>
					<td><input type="text" placeholder="请输入物流公司" id="deliComp" name="deliComp"
						value="${payJnl.deliComp}"></td>
				</tr>
				<tr>
					<td>物流单号:</td>
					<td>
						<input type="text" placeholder="请输入物流单号" id="deliNo" name="deliNo"
						value="${payJnl.deliNo}" >
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<span id="errSpan" style="color:red;"></span>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<button type="button" class="btn btn-primary" id="doIt">
							<i class="icon-save"></i> 保存
						</button>(填写订单号保存，自动更新订单状态)
						<button type="button" class="btn" onclick="window.history.go(-1)">取消</button>
					</td>
				</tr>
			</table>
			</form>
			</div>
			</div>
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>
</body>
</html>



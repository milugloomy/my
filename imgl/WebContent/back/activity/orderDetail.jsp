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
		<h1 class="page-title">订单管理详情</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> 
			<span class="divider">/</span>
		</li>
		<li class="active">订单管理详情</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<c:if test="${payJnl.payStatus eq 'AGNS' || payJnl.payStatus eq 'AS'}">
			<div class="btn-toolbar">
				<button class="btn btn-primary" onclick="location='/imgl/backOrderEditPre.do?pJnlNo=${payJnl.pJnlNo}'">
					<i class="icon-pencil"></i> 编辑
				</button>
				<div class="btn-group"></div>
			</div>
			</c:if>
			<div class="well"><div class="tab-pane active in" id="home">
			<table class="table">
				<tr>
					<td style="width: 25%; border-top: none;">订单号:</td>
					<td style="border-top: none;">${payJnl.pJnlNo}</td>
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
					<td>发货时间:</td>
					<td>${payJnl.sendTime}</td>
				</tr>
				<tr>
					<td>物流公司:</td>
					<td>${payJnl.deliComp}</td>
				</tr>
				<tr>
					<td>物流单号:</td>
					<td>${payJnl.deliNo}
						<c:if test="${payJnl.payStatus eq 'AS'}">
						<button class="btn btn-primary" data-toggle="modal"
							data-target="#info1" href="#info1">
							<i class="icon-th-list"></i> 查看物流
						</button>
						</c:if>
					</td>
				</tr>
			</table>
			</div></div>
			<button style="margin-left:30%" class="btn btn-primary" onclick="location='/imgl/backOrderList.do'">
					<i class="icon-arrow-left"></i>返回
			</button>
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>

<div class="modal fade info" tabindex="-1" role="dialog"
	aria-labelledby="info1" id="info1">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">物流信息</h4>
		</div>
		<div class="modal-body">
			<strong>圆通快递，运单号：805007798210</strong>
			<ul class="table-view" style="margin-top: 10px;">
				<li class="table-view-cell">
					<p class="text-danger">2015-10-22&nbsp;08:59:51</p>
					<p class="text-danger">【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
				<li class="table-view-cell">
					<p>2015-10-22&nbsp;08:59:51</p>
					<p>【浙江省杭州市南站公司】派件人：毛华英&nbsp;派件中，派件员电话：13588115213</p>
				</li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>



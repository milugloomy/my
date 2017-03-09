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
	//下拉框默认选中
	$("#payStatus").find("option[value='${payStatus}']").attr("selected",true);
	if("${endDate}"=="" || "${beginDate}"==""){
		var endDate = new Date();
		$("#endDate").val(endDate.getFullYear() + "-" + (endDate.getMonth() + 1)
					+ "-" + endDate.getDate());
		var beginDate = new Date();
		beginDate.setTime(endDate.getTime() - 30 * 24 * 3600 * 1000);//30天前
		$("#beginDate").val(beginDate.getFullYear() + "-"
					+ (beginDate.getMonth() + 1) + "-"
					+ beginDate.getDate());
	}else{
		$("#endDate").val("${endDate}");
		$("#beginDate").val("${beginDate}");
	}
	$("#ok").click(function() {
		var beginDate=$("#beginDate").val();
		var endDate=$("#endDate").val();
		var reg=/^\d{4}-\d{2}-\d{2}$/;
		if(!reg.test(beginDate) || !reg.test(endDate)){
			alert("日期格式错误");
			return false;
		}
		$("#form").submit();
	});	
	//分页代码
	var params={
			"beginDate":$("#beginDate").val(),
			"endDate":$("#endDate").val(),
			"payStatus":$("#payStatus").val()
	}
	pageFunc(params,"/imgl/backOrderSubList.do");
});
</script>
</head>
<body class="">
<jsp:include page="../header.jsp"/>
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">订单管理列表</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">订单管理列表</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="btn-toolbar">
				<div class="btn-group pull-right">
					<form class="form-inline" id="form" action="backOrderList.do">
						<label>起始日期：</label>
						<input name="beginDate" id="beginDate" placeholder="请选择时间" type="date" /> 
						<label>结束日期：</label>
						<input name="endDate" id="endDate" placeholder="请选择时间" type="date" /> 
						<label>订单状态：</label>
						<select name="payStatus" id="payStatus" class="input-xlarge" style="width:120px">
							<option value="ALL">全部</option>
							<option value="APNG">已支付未确认</option>
							<option value="AGNS">已确认未发货</option>
							<option value="AS">已发货</option>
							<option value="AR">已接收</option>
							<option value="FL">支付失败</option>
						</select>
						<button class="btn" id="ok" style="margin-bottom:9px" type="button">
							<i class="icon-search"></i> 查找
						</button>
					</form>
				</div>
			</div>
			<div class="well">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 1%">#</th>
							<th style="width: 8%">订单号</th>
							<th style="width: 12%">支付时间</th>
							<th style="width: 18%">收货人</th>
							<th style="width: 9%">付款金额</th>
							<th style="width: 27%">商品名称</th>
							<th style="width: 9%">订单状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
					<c:forEach items="${orderList}" var="xx" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td><a href="/imgl/backOrderDetail.do?pJnlNo=${xx.pJnlNo}">${xx.pJnlNo }</a></td>
						<td>${xx.payTime }</td>
						<td>${xx.address.name}&nbsp;${xx.address.phoneNo}</td>
						<td>${xx.amount}</td>
						<td>${xx.activity.title}</td>
						<td><fmt:message key="payStatus.${xx.payStatus}"/></td>
						<td>
							<button class="btn" onclick="location='/imgl/backOrderDetail.do?pJnlNo=${xx.pJnlNo}'" title="详情">
								<i class="icon-th-list" ></i>
							</button>
							<c:if test="${xx.payStatus eq 'AGNS' || xx.payStatus eq 'AS'}">
							<button class="btn" onclick="location='/imgl/backOrderEditPre.do?pJnlNo=${xx.pJnlNo}'" title="编辑">
								<i class="icon-pencil"> </i>
							</button>
							</c:if>
						</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<jsp:include page="../page.jsp" />
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>
</body>
</html>
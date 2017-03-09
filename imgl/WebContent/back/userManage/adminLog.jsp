<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
			"endDate":$("#endDate").val()
	}
	pageFunc(params,"/imgl/adminLogSub.do");
});

</script>
</head>
<body class="">
<jsp:include page="../header.jsp" />
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="2" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">日志列表</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span
			class="divider">/</span></li>
		<li class="active">日志列表</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="btn-toolbar">
				<div class="btn-group pull-right">
					<form class="form-inline" id="form" action="adminLog.do">
						<label>起始日期：</label>
						<input name="beginDate" id="beginDate" placeholder="请选择时间" type="date" /> 
						&nbsp;&nbsp;&nbsp;
						<label>结束日期：</label>
						<input name="endDate" id="endDate" placeholder="请选择时间" type="date" /> 
						<button class="btn" id="ok" style="margin-bottom: 9px" type="button">
							<i class="icon-search"></i> 查找
						</button>
					</form>
				</div>
			</div>
			<div class="well">
				<table class="table">
					<tr>
						<th style="width: 1%">#</th>
						<th>流水号</th>
						<th>登录ID</th>
						<th>交易码</th>
						<th>交易时间</th>
						<th>返回码</th>
						<th>错误信息</th>
						<th>登录IP</th>
					</tr>
					<tbody id="tbody">
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
					</tbody>
				</table>
			</div>
			<jsp:include page="../page.jsp" />
			<div class="modal small hide fade" id="myModal" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">删除确认</h3>
				</div>
				<div class="modal-body">
					<p class="error-text">
						<i class="icon-warning-sign modal-icon"></i>确定要删除这条信息吗？
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
					<button class="btn btn-danger" data-dismiss="modal">确认</button>
				</div>
			</div>
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>
</body>
</html>
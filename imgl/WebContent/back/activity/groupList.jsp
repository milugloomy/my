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
	$("#status").find("option[value='${status}']").attr("selected",true);
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
			"status":$("#status").val()
	}
	pageFunc(params,"/imgl/backGroupSubList.do");
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
	<h1 class="page-title">团管理列表</h1>
</div>
<ul class="breadcrumb">
	<li><a href="/imgl/back/backIndex.jsp">我是团长</a> 
		<span class="divider">/</span></li>
	<li class="active">团管理列表</li>
</ul>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="btn-toolbar">
		<div class="btn-group pull-right">
			<form class="form-inline" id="form" action="backGroupList.do">
				<label>开团起始日期：</label>
				<input name="beginDate" id="beginDate" placeholder="请选择时间" type="date" /> 
				<label>结束日期：</label>
				<input name="endDate" id="endDate" placeholder="请选择时间" type="date" /> 
				<label>组团状态：</label>
				<select name="status" id="status" class="input-xlarge" style="width:120px">
					<option value="ALL">全部</option>
					<option value="BG">组团中</option>
					<option value="CP">已成团</option>
					<option value="FL">组团失败</option>
				</select> 
				<button class="btn" id="ok" style="margin-bottom: 9px" type="button">
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
			<th style="width: 6%">团号ID</th>
			<th style="width: 8%">团长昵称</th>
			<th style="width: 10">团长头像</th>
			<th style="width: 8%">团员数/总数</th>
			<th style="width: 7%">组团状态</th>
			<th style="width: 10%">组团时间</th>
			<th style="width: 10%">成团时间</th>
			<th style="width: 14%">商品名称</th>
			<th style="width: 20%">缩略图</th>
			<th style="width: 6%">操作</th>
			</tr>
		</thead>
		<tbody id="tbody">
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
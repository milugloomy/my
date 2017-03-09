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
<style type="text/css">
	.member-img { margin: 0; }
	.member-img li { display: inline-block; }
	.member-img img { margin: 0 10px; }
	.member-img p { margin: 0; text-align: center; }
	.member-img button { margin-top: 8px; }
	.member-img .pic { float: left; }
</style>
</head>
<body class="">
<jsp:include page="../header.jsp" />
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
<div class="header">
	<h1 class="page-title">团管理详情</h1>
</div>
<ul class="breadcrumb">
	<li><a href="index.html">我是团长</a> <span class="divider">/</span></li>
	<li class="active">团管理详情</li>
</ul>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="well">
		<div class="tab-pane active in" id="home">
		<table class="table">
			<tr>
				<td style="width: 30%; border-top: none;">团号ID:</td>
				<td style="border-top: none;">${groupJnl.groupId }</td>
			</tr>
			<tr>
				<td>团长昵称:</td>
				<td>${groupJnl.leader.nickName}</td>
			</tr>
			<tr>
				<td>团员数/总数:</td>
				<td>${groupJnl.count}/${groupJnl.total}人</td>
			</tr>
			<%-- <tr>
				<td>团员:</td>
				<td>
					<ul class="member-img">
					<c:forEach items="${orderList}" var="xx">
					<li>
						<img src="${xx.user.headImgUrl}" style="height:48px;">
						<p>${xx.user.nickName }</p>
						<p>订单号：${xx.pJnlNo }</p>
						<button class="btn btn-primary" 
							onclick="location='/imgl/backOrderDetail.do?pJnlNo=${xx.pJnlNo}'">查看订单</button>
					</li>
					</c:forEach>
					</ul>
				</td>
			</tr> --%>
			<tr>
				<td>团员:</td>
				<td>
					<ul class="member-img">
					<c:forEach items="${orderList}" var="xx">
					<li><div class="pic">
							<img style="width: 48px; height: 48px;" src="${xx.user.headImgUrl}">
							<p>${xx.user.nickName }</p>
						</div>
						<div>
							订单号：${xx.pJnlNo }
							<button class="btn btn-primary" onclick="location='/imgl/backOrderDetail.do?pJnlNo=${xx.pJnlNo}'">
								<i class="icon-th-list"></i> 查看订单
							</button>
						</div>
					</li>
					</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<td>组团状态:</td>
				<td><fmt:message key="groupStatus.${groupJnl.status}"/></td>
			</tr>
			<tr>
				<td>组团时间:</td>
				<td>${groupJnl.startTime}</td>
			</tr>
			<tr>
				<td>组团完毕时间:</td>
				<td>${groupJnl.endTime}</td>
			</tr>
			<tr>
				<td>商品名称:</td>
				<td>${groupJnl.activity.title}</td>
			</tr>
			<tr>
				<td>缩略图:</td>
				<td>
					<c:forEach items="${groupJnl.activity.imgUrlList}" var="yy">
					<c:if test="${yy.mainPic=='1' }">
					  	<img style="width:250px" src="/imgl/img/${yy.imgUrl}" />
					</c:if>
				  	</c:forEach>
				</td>
			</tr>
		</table>
		</div>
		</div>
		<button style="margin-left:30%" class="btn btn-primary" onclick="location='/imgl/backGroupList.do'">
				<i class="icon-arrow-left"></i>返回
		</button>
		<jsp:include page="../footer.jsp" />
	</div>
</div>
</div>
</body>
</html>



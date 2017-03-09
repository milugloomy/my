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
});
function deleteActivity(activityId){
	var form = $("<form method='post' action='/imgl/deleteActivity.do'></form>");
    var input1 = $("<input type='hidden' name='activityId' />");
    input1.val(activityId);
    form.append(input1);
    form.submit();
    form.remove();
}
</script>
</head>
<body class="">
<jsp:include page="../header.jsp" />
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">活动管理详情</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">活动管理详情</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="btn-toolbar">
				<button onclick="location='/imgl/backActivityEditPre.do?activityId=${activity.activityId}'" class="btn btn-primary">
					<i class="icon-pencil"></i> 编辑
				</button>
				<a href="#myModal" data-toggle="modal" class="btn"><i
					class="icon-remove"></i> 删除</a>
				<div class="btn-group"></div>
			</div>
			<div class="well">
			<div class="tab-pane active in" id="home">
			<table class="table">
				<tr>
					<td style="width: 20%; border-top: none;">商品名称:</td>
					<td style="border-top: none;">${activity.productName}</td>
				</tr>
				<tr>
					<td>标题:</td>
					<td>${activity.title}</td>
				</tr>
				<tr>
					<td>活动图片:<br/>(点击查看原图)</td>
					<td>
					<c:forEach items="${activity.imgUrlList}" var="yy">
						<a href="/imgl/img/${yy.imgUrl}" target="_blank">
						<label style="position: relative; min-width: 500px; margin-bottom:0px;">
							<img style="align:left;width:80%" src="/imgl/img/${yy.imgUrl}">&nbsp; 
							<c:if test="${yy.mainPic=='1' }">
								<span style="position:absolute;z-index:99;bottom:0;left:0;color:transparent;padding:15px 60px;color:#fff;background-color:#999;" name="default">默认</span>
							</c:if>
						</label>
						</a>
					</c:forEach>
					</td>
				</tr>
				<tr>
					<td>原价:</td>
					<td>${activity.originPrice}元</td>
					</td>
				</tr>
				<tr>
					<td>单独购买价格:</td>
					<td>${activity.singlePrice}元</td>
				</tr>
				<tr>
					<td>团购折扣:</td>
					<td>${activity.discount}</td>
				</tr>
				<tr>
					<td>团购价:</td>
					<td>${activity.groupPrice}元</td>
				</tr>
				<tr>
					<td>成团组员数:</td>
					<td>${activity.groupVol}</td>
				</tr>
				<tr>
					<td>商品总数量:</td>
					<td>${activity.totalQuantity}件</td>
				</tr>
				<tr>
					<td>库存量:</td>
					<td>${activity.remain}件</td>
				</tr>
				<tr>
					<td>开始时间:</td>
					<td>${activity.startTime}</td>
				</tr>
				<tr>
					<td>详细介绍:</td>
					<td>${activity.introduction}</td>
				</tr>
			</table>
			</div>
			</div>
			<button style="margin-left:45%" class="btn btn-primary" onclick="location='/imgl/backActivityList.do'">
					<i class="icon-arrow-left"></i>返回
			</button>
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
					<button onclick="deleteActivity(${activity.activityId})" class="btn btn-danger" data-dismiss="modal">确认</button>
				</div>
			</div>
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>
</body>
</html>



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
	//分页代码
	pageFunc(null,"backActivitySubList.do");
});
var activityId=null;
function setActivityId(activityId){
	this.activityId=activityId;
}
function deleteActivity(){
	var form = $("<form method='post' action='/imgl/deleteActivity.do'></form>");
    var input1 = $("<input type='hidden' name='activityId' />");
    input1.val(activityId);
    form.append(input1);
    form.appendTo("body");
    form.hide();
    form.submit();
}
</script>
</head>
<body class="">
<jsp:include page="../header.jsp"/>
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">活动管理列表</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">活动管理列表</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="btn-toolbar">
				<button class="btn btn-primary" onclick="location='/imgl/back/activity/activityNew.jsp'">
					<i class="icon-plus"></i> 添加
				</button>
				<div class="btn-group"></div>
			</div>
			<div class="well">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 1%">#</th>
							<th style="width: 20%">商品名称</th>
							<th style="width: 20%">缩略图</th>
							<th style="width: 7%">原价</th>
							<th style="width: 7%">团购价</th>
							<th style="width: 7%">团组员数</th>
							<th style="width: 7%">商品总数</th>
							<th style="width: 7%">库存量</th>
							<th style="width: 10%">开始时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
					<c:forEach items="${activityList}" var="xx" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td><a href="/imgl/backActivityDetail.do?activityId=${xx.activityId}">${xx.title}</a></td>
							<td>
								<c:forEach items="${xx.imgUrlList}" var="yy">
								<c:if test="${yy.mainPic=='1' }">
									<img class="img-responsive" src="/imgl/img/${yy.imgUrl}" />
								</c:if>
							  	</c:forEach>
							</td>
							<td>${xx.originPrice}</td>
							<td>${xx.groupPrice}</td>
							<td>${xx.groupVol}人</td>
							<td>${xx.totalQuantity}件</td>
							<td>${xx.remain}件</td>
							<td>${xx.startTime}</td>
							<td>
								<button class="btn" onclick="location='/imgl/backActivityDetail.do?activityId=${xx.activityId}'" title="详情">
									<i class="icon-th-list" ></i>
								</button>
								<button class="btn" onclick="location='/imgl/backActivityEditPre.do?activityId=${xx.activityId}'" title="编辑">
									<i class="icon-pencil" ></i>
								</button>
								<button class="btn" onclick="setActivityId(${xx.activityId})" href="#myModal" role="button" data-toggle="modal" title="删除">
									<i class="icon-remove" ></i>
								</button>
							</td>
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
					<button onclick="deleteActivity()" class="btn btn-danger" data-dismiss="modal">确认</button>
				</div>
			</div>
			<jsp:include page="../footer.jsp"/>
		</div>
	</div>
</div>
</body>
</html>
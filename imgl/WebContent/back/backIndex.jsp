<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
$(document).ready(function(){
	var date=new Date();
	var month=date.getMonth()+1;
	var day=date.getDate();
	$("#month").html(month);
	$("#day").html(day);
});
</script>
</head>
<body class="">
<jsp:include page="header.jsp"/>
<jsp:include page="navi.jsp">
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<div class="stats">
			<p class="stat">
				<span class="number" id="day"></span>日
			</p>
			<p class="stat">
				<span class="number" id="month"></span>月
			</p>
		</div>
		<h1 class="page-title">主页</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">主页</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="row-fluid">
				<div class="alert alert-info">
					<button type="button" class="close" data-dismiss="alert">×</button>
					<strong>今日提醒：</strong> 通知提醒通知提醒！
				</div>
			</div>
			<jsp:include page="footer.jsp"/>
		</div>
	</div>
</div>
</body>
</html>



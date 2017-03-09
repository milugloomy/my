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
		<h1 class="page-title">密码修改</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">密码修改</li>
	</ul>
	<div style="padding-left:45%;padding-top:10%">
		<img style="height:100px;width:100px;" src="/imgl/img/gou.png"><br/>
		修改成功
	</div>
</div>
</body>
</html>
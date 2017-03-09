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
<script src="/imgl/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/imgl/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	if($.cookie("imgl_back_loginId")!=undefined){
		$("#adminName").val($.cookie("imgl_back_loginId"));
	}
	$("#login").click(function(){
		if($("#rememberMe").prop("checked")==true)
			$.cookie("imgl_back_loginId",$("#adminName").val());
		
		$("#form").submit();
	});
});
</script>
</head>
<body class="">
<div class="navbar">
	<div class="navbar-inner">
		<ul class="nav pull-right">

		</ul>
		<a class="brand" href="index.html"><span class="second">我是团长管理后台</span></a>
	</div>
</div>
<div class="row-fluid">
	<div class="dialog">
		<div class="block">
			<p class="block-heading">登录</p>
			<div class="block-body">
				<form id="form" method="post" action="/imgl/backLogin.do">
					<label>登录ID</label> 
					<input type="text" name="adminName" id="adminName"class="span12"> 
					<label>密码</label>
					<input type="password" name="password" class="span12"> 
					<input type="button" id="login" class="btn btn-primary pull-right" value="登录"></a> 
					<labelclass="remember-me"><input type="checkbox" id="rememberMe" checked="checked"> 记住我</label>
					<div class="clearfix"></div>
				</form>
			</div>
		</div>
		<p class="pull-right" style="">
			<a href="#" target="blank">没有注册？</a>
		</p>
		<p>
			<a href="#">忘记密码？</a>
		</p>
	</div>
</div>
</body>
</html>



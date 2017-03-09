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
	$("#ok").click(function(){
		$("#error").html("");
		var newPassword=$("#newPassword").val();
		var confirmPassword=$("#confirmPassword").val();
		if(newPassword.length<6){
			$("#error").html("新密码至少6位");
			return;
		}
		if(newPassword!=confirmPassword){
			$("#error").html("新密码与确认密码不一致");
			return;
		}
		$("#form").submit();
	});
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
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="btn-toolbar">
				<button class="btn btn-primary" id="ok">
					<i class="icon-save"></i> 保存
				</button>
				<button class="btn">取消</button>
				<div class="btn-group"></div>
			</div>
			<div class="well">
				<div class="tab-pane active in" id="home">
					<form id="form" action="/imgl/modifyPwd.do" method="post">
						<table class="table">
							<tr>
								<td style="width: 20%; border-top: none;">旧密码:</td>
								<td style="border-top: none;">
									<input type="password" name="oldPassword" id="oldPassword" placeholder="请输入原密码">
								</td>
							</tr>
							<tr>
								<td>新密码:</td>
								<td><input type="password" name="newPassword" id="newPassword" placeholder="请输入密码"></td>
							</tr>
							<tr>
								<td>确认密码:</td>
								<td><input type="password" name="confirmPassword" id="confirmPassword" placeholder="请再次输入密码"></td>
							</tr>
						</table>
						<div id="error" style="color:red"></div>
					</form>
				</div>
			</div>
			<jsp:include page="../footer.jsp"/>
		</div>
	</div>
</div>
</body>
</html>



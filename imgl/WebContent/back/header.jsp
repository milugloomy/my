<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="navbar">
	<div class="navbar-inner">
		<ul class="nav pull-right">
			<li id="fat-menu" class="dropdown">
				<a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown"> 
					<i class="icon-user"></i> 
					${sessionScope.Manager.userId}
					<i class="icon-caret-down"></i>
				</a>
				<ul class="dropdown-menu">
					<li><a tabindex="-1" href="/imgl/back/userManage/modifyPwdPre.jsp">密码修改</a></li>
					<li><a tabindex="-1" href="/imgl/adminLog.do">日志查看</a></li>
					<li class="divider"></li>
					<li><a tabindex="-1" href="/imgl/logOut.do">登出</a></li>
				</ul>
			</li>
		</ul>
		<a class="brand" href="index.html"><span class="second">我是团长管理后台</span></a>
	</div>
</div>
<div class="copyrights">版权信息</div>
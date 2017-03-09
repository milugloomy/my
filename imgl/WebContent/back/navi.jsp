<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function() {
	var open="${param.open}";
	var i=1;
	$(".nav-header").each(function(){
		if(i==open){
			$(this).next().attr("class","nav nav-list collapse in");
		}else{
			$(this).next().attr("class","nav nav-list collapse");
		}
		i++;
	});
});
</script>
<div class="sidebar-nav">
	<a href="#dashboard-menu" class="nav-header" data-toggle="collapse">
		<i class="icon-dashboard"></i>我是团长
	</a>
	<ul id="dashboard-menu" class="nav nav-list collapse in">
		<li><a href="/imgl/back/backIndex.jsp">主页</a></li>
		<li><a href="/imgl/backActivityList.do">活动管理</a></li>
		<li><a href="/imgl/backOrderList.do">订单管理</a></li>
		<li><a href="/imgl/backGroupList.do">团管理</a></li>
		<li><a href="#">批量程序管理</a></li>
	</ul>
	<a href="#accounts-menu" class="nav-header" data-toggle="collapse">
		<i class="icon-briefcase"></i>用户管理
	</a>
	<ul id="accounts-menu" class="nav nav-list collapse">
		<li><a href="/imgl/back/userManage/modifyPwdPre.jsp">密码修改</a></li>
		<li><a href="/imgl/adminLog.do">日志查看</a></li>
	</ul>
</div>
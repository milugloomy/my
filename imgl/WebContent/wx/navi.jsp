<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function() {
	var red="${param.red}";
	var i=1;
	$("nav a").each(function(){
		if(i==red){
			$(this).attr("class","tab-item active");
		}else{
			$(this).attr("class","tab-item");
		}
		i++;
	});
});
</script>
<nav class="bar bar-tab">
<a class="tab-item"  href="/imgl/activityList.do" data-ajax="false">
<span class="icon icon-home"></span>
<span class="tab-label">首页</span>
</a>
<a class="tab-item" href="/imgl/myGroup.do" data-ajax="false">
<span class="icon icon-star"></span>
<span class="tab-label">我的团</span>
</a>
<a class="tab-item" href="/imgl/myOrder.do" data-ajax="false">
<span class="icon icon-list"></span>
<span class="tab-label">我的订单</span>
</a>
<a class="tab-item" href="/imgl/userCenter.do" data-ajax="false">
<span class="icon icon-person"></span>
<span class="tab-label">个人中心</span>
</a>
</nav>

<a id="to_top" class="icon icon-up" href="#aaaa" style="color:#fff;line-height:48px;text-align:center;width:48px;height:48px;position:fixed;z-index:999;bottom:60px;right:20px;background-color:#A30008;border-radius:50%;"></a>

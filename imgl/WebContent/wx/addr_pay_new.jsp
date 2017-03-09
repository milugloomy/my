<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我是团长</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="/imgl/css/bootstrap.min.css">
<link rel="stylesheet" href="/imgl/css/ratchet.min.css">
<link rel="stylesheet" href="/imgl/css/normalize.css">
<link rel="stylesheet" href="/imgl/css/main.css">
<script src="/imgl/js/jquery-2.1.1.min.js"></script>
<script src="/imgl/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#province").change(function(){
		var provinceName=$(this).val();
		$.post("/imgl/cityList.do",{provinceName:provinceName},function(data){
			var s="";
			for(var i=0;i<data.length;i++){
				s+="<option value='"+data[i].cityName+"'>"+data[i].cityName+"</option>";
			}
			$("#city").html(s);
			$("#city").change();
		},"json");
	});
	$("#city").change(function(){
		var cityName=$(this).val();
		$.post("/imgl/districtList.do",{cityName:cityName},function(data){
			var s="";
			for(var i=0;i<data.length;i++){
				s+="<option value='"+data[i].districtName+"'>"+data[i].districtName+"</option>";
			}
			$("#district").html(s);
		},"json");
	});
	//手机输入框只能输入数字
	$("#phoneNo").keyup(function(){     
        var tmptxt=$(this).val();     
        $(this).val(tmptxt.replace(/\D|^0/g,''));     
    }).bind("paste",function(){     
        var tmptxt=$(this).val();     
        $(this).val(tmptxt.replace(/\D|^0/g,''));     
    }).css("ime-mode", "disabled");
	//表单提交
	$("#doIt").click(function(){
		if($("#name").val()==""){
			alert("姓名不能为空");
			return;
		}
		if($("#phoneNo").val().length<11){
			alert("手机号码需为11位");
			return;
		}
		if($("#province").val()=="0"){
			alert("请选择省份");
			return;
		}
		if($("#city").val()==null){
			alert("请选择城市");
			return;
		}
		if($("#district").val()==null){
			alert("请选择区/县");
			return;
		}
		if($("#detail").val()==""){
			alert("请输入详细地址");
			return;
		}
		$('#form1').submit();
	});
});
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
    <div class="content bg has_nav">
	<div class="info_panel">
<div class="order_h4">添加收货地址</div>
<form method="post" action="/imgl/addrPayNew.do" id="form1">
<table class="table order">
<tr>
	<td style="padding-top:15px;">收货人：</td>
	<td style="padding-top:15px;">
		<input type="text" placeholder="姓名" id="name" name="name" value="">
	</td>
</tr>
<tr>
	<td>手机号码：</td>
	<td>
		<input type="text" maxlength="11" id="phoneNo" name="phoneNo" placeholder="手机" value="">
	</td>
</tr>
<tr>
	<td>省份：</td>
	<td>
	  	<select class="form-control" id="province" name="province">
	  	<option  value="0">请选择省份</option>
		<c:forEach items="${provinceList}" var="xx">
			<option  value=${xx.provinceName }>${xx.provinceName }</option>
		</c:forEach>
	  	</select>
	</td>
</tr>
<tr>
	<td>城市：</td>
	<td>
		<select class="form-control" id="city" name="city">
		</select>
	</td>
</tr>
<tr>
	<td>区/县：</td>
	<td>
		<select class="form-control" id="district" name="district">
		</select>
	</td>
</tr>
<tr>
	<td>地址类别：</td>
	<td>
		<select class="form-control" id="addrType" name="addrType">
			<option value="C" >公司</option>
			<option value="H" >家庭</option>
		</select>
	</td>
</tr>
<tr>
	<td>详细地址：</td>
	<td>
		<textarea class="form-control" rows="3" id="detail" name="detail"></textarea>
	</td>
</tr>
</table>
</form>
</div>

<a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
	href="javascript:;" id="doIt">确认</a>
    </div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="4" /> 
</jsp:include>


  </body>
</html>
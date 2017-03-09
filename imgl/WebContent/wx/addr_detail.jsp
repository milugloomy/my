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
	$("#deleteAddress").click(function(){
		var addrSeq="${address.addrSeq}";
		var res=confirm("确认要删除该地址么");
		if(res)
			window.location.href="/imgl/deleteAddress.do?addrSeq="+addrSeq;
		else
			return;
	});
	
});
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
    <div class="content bg has_nav">
	<div class="info_panel">
<div class="order_h4">收货地址</div>
<form method="post" action="/imgl/modifyAddr.do" id="form1">
<input type="hidden" value="${address.addrSeq }" id="addrSeq" name="addrSeq" />
<table class="table order">
<tr>
	<td style="padding-top:15px;">收货人：</td>
	<td style="padding-top:15px;">
		<input type="text" placeholder="姓名" id="name" name="name" value="${address.name}">
	</td>
</tr>
<tr>
	<td>手机号码：</td>
	<td>
		<input type="text" maxlength="11" id="phoneNo" name="phoneNo" placeholder="手机" value="${address.phoneNo}">
	</td>
</tr>
<tr>
	<td>省份：</td>
	<td>
	  	<select class="form-control" id="province" name="province">
		<c:forEach items="${provinceList}" var="xx">
			<option <c:if test="${xx.provinceName==address.provinceName }">selected</c:if> value=${xx.provinceName }>${xx.provinceName }</option>
		</c:forEach>
	  	</select>
	</td>
</tr>
<tr>
	<td>市：</td>
	<td>
		<select class="form-control" id="city" name="city">
		<c:forEach items="${cityList}" var="xx">
			<option <c:if test="${xx.cityName==address.cityName }">selected</c:if> value=${xx.cityName }>${xx.cityName }</option>
		</c:forEach>
		</select>
	</td>
</tr>
<tr>
	<td>区/县：</td>
	<td>
		<select class="form-control" id="district" name="district">
		<c:forEach items="${districtList}" var="xx">
			<option <c:if test="${xx.districtName==address.districtName }">selected</c:if> value=${xx.districtName }>${xx.districtName }</option>
		</c:forEach>
		</select>
	</td>
</tr>
<tr>
	<td>地址类别：</td>
	<td>
		<select class="form-control" id="addrType" name="addrType">
			<option value="C" <c:if test="${address.addrType=='C' }">selected</c:if>>公司</option>
			<option value="H" <c:if test="${address.addrType=='H' }">selected</c:if>>家庭</option>
		</select>
	</td>
</tr>
<tr>
	<td>详细地址：</td>
	<td>
		<textarea class="form-control" rows="3" id="detail" name="detail">${address.detail}</textarea>
	</td>
</tr>
</table>
</form>
</div>

<a class="btn btn-block" style="background-color:#A30008;color:#fff;border:none;border-radius:0;" 
	href="javascript:$('#form1').submit();">确认</a>
<a class="btn btn-block" style="background-color:rgb(63, 63, 150);color:#fff;border:none;border-radius:0;" 
	id="deleteAddress" href="javascript:;">删除</a>
    </div>
	
<jsp:include page="navi.jsp" flush="true">
	<jsp:param name="red" value="4" /> 
</jsp:include>


  </body>
</html>
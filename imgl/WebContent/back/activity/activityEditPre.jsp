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
<script src="/imgl/js/jquery.form.js" type="text/javascript"></script>
<script src="/imgl/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#originPrice,#discount").blur(function(){
		var originPrice=$("#originPrice").val();
		var discount=$("#discount").val();
		if(originPrice!="" && discount!=""){
			var groupPrice=originPrice*discount/10;
			groupPrice=groupPrice.toFixed(2);
			$("#groupPrice").val(groupPrice);
		}
	});
	$("#addPic").click(function(){
		var count=$("img").length;
		if(count>=3){
			alert("至多只能上传三张图片");
			return;
		}
		var li=$("<li></li>");
		li.load("/imgl/back/activity/img_sub.jsp");
		$(".item-img").append(li);
	});
	$("#doIt").click(function(){
		$("#errSpan").html("");
		var productName=$("#productName").val();
		if(productName==""){
			$("#errSpan").html("商品名称不能为空");return;
		}
		var title=$("#title").val();
		if(title==""){
			$("#errSpan").html("标题不能为空");return;
		}
		var defaultImg=$("input[name=options]:checked").val();
		if(defaultImg==undefined || defaultImg=="on"){
			$("#errSpan").html("请选择默认图片(点击任意一张图片)");return;
		}
		var imgSrc=document.getElementsByTagName("img");
		for(var i=0;i<imgSrc.length;i++){
		    if($(imgSrc[i]).attr("src")==""){
		    	$("#errSpan").html("上传图片不能为空");return;
		    }
		}
		var originPrice=$("#originPrice").val();
		if(originPrice==""){
			$("#errSpan").html("原价不能为空");return;
		}
		var singlePrice=$("#singlePrice").val();
		if(singlePrice==""){
			$("#errSpan").html("单独购买价格不能为空");return;
		}
		var discount=$("#discount").val();
		if(discount==""){
			$("#errSpan").html("团购折扣不能为空");return;
		}
		var groupVol=$("#groupVol").val();
		if(groupVol==""){
			$("#errSpan").html("成团组员数不能为空");return;
		}
		var totalQuantity=$("#totalQuantity").val();
		if(totalQuantity==""){
			$("#errSpan").html("商品总数量不能为空");return;
		}
		var remain=$("#remain").val();
		if(remain==""){
			$("#errSpan").html("库存量不能为空");return;
		}
		var introduction=$("#introduction").val();
		if(introduction==""){
			$("#errSpan").html("详细介绍不能为空");return;
		}
		$("#form").submit();
	});
});
function uploadPic(obj){
	var input=$(obj).prev("input[type=file]");
	var name=input.val();
	if(name==""){
		alert("请选择上传图片");
		return;
	}
	var form=$("<form method='post' enctype='multipart/form-data'></form>");
	form.append(input);
	form.ajaxSubmit({
        type:'post',
        url:'/imgl/imgUpload.do',
        success:function(data){
        	var jsonData= eval("(" + data + ")");
        	var imgName=jsonData.imgName;
        	$(obj).parent().children().children("input[type=radio]").val(imgName);
        	$(obj).parent().children().children("img").attr("src","/imgl/imgTemp/"+imgName);
        	$(obj).next().val(imgName);
        }
    });
	$(obj).before(input);
	form.remove();
}
function deletePic(obj){
	$(obj).parent("li").remove();
}
function selectDefault(obj){
	$("span[name=default]").html("");
	$(obj).next("span[name=default]").html("默认");
}
</script>
<style type="text/css">
.item-img { margin: 0; }
.item-img li { display: inline-block; }
.item-img img { width: 70%; margin-right: 10px; margin-top:15px;}
.item-img button { margin: 0;}

.hidden-input { opacity: 0; position: absolute; z-index: -1; }
.item-img label { position: relative; min-width: 500px; margin-bottom:0px;}
.item-img input[type=radio]~span { position: absolute; z-index: 99; bottom: 0; left: 0; color: transparent; padding: 15px 60px; }
.item-img input[type=radio]:checked~span { color: #fff; background-color: #999; }
.item-img input[type=file]{height:20px;width:150px;padding:4px 1px;}
</style>
</head>
<body class="">
<jsp:include page="../header.jsp" />
<jsp:include page="../navi.jsp" >
	<jsp:param name="open" value="1" /> 
</jsp:include>

<div class="content">
	<div class="header">
		<h1 class="page-title">活动管理编辑</h1>
	</div>
	<ul class="breadcrumb">
		<li><a href="/imgl/back/backIndex.jsp">我是团长</a> <span class="divider">/</span></li>
		<li class="active">活动管理编辑</li>
	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="well">
			<div class="tab-pane active in" id="home">
			<form id="form" method="post" action="/imgl/backActivityEdit.do">
			<table class="table">
				<tr>
					<td style="width: 20%; border-top: none;">商品名称:</td>
					<td style="border-top: none;">
					<input type="hidden" name="activityId" value="${activity.activityId}">
					<input type="text" id="productName" name="productName" value="${activity.productName}" placeholder="请输入商品名称">
					</td>
				</tr>
				<tr>
					<td>标题:</td>
					<td><input type="text" id="title" name="title" value="${activity.title}" style="width:90%" placeholder="请输入标题"></td>
				</tr>
				<tr>
					<td>活动图片:<br/>点击图片选择默认图片</td>
					<td><ul class="item-img">
		  				<c:forEach items="${activity.imgUrlList}" var="yy">
						<li><label>
							<c:if test="${yy.mainPic=='1' }">
								<input checked="checked" value="${yy.imgUrl}" onclick="selectDefault(this);" type="radio"  name="options" class="hidden-input" > 
								<span name="default">默认</span>
							</c:if>
							<c:if test="${yy.mainPic!='1' }">
								<input  value="${yy.imgUrl}" onclick="selectDefault(this);" class="hidden-input" type="radio" name="options"> 
								<span name="default"></span>
							</c:if>
								<img src="/imgl/img/${yy.imgUrl}">
							</label>
							<input type="file"  class="btn btn-primary " name="changePic" />
							<button type="button" class="btn btn-primary " onclick="uploadPic(this);">
								<i class="icon-upload"></i> 上传
							</button>
							<input type="hidden" name="imgUrl" value="${yy.imgUrl}">
							<button type="button" class="btn btn-primary"  onclick="deletePic(this);">
								<i class="icon-remove"></i> 删除
							</button>
						</li>
						</c:forEach>
						</ul>
						<button style="margin-top:10px;" type="button" class="btn icon-plus" id="addPic" >
							新增
						</button>
					</td>
				</tr>
				<tr>
					<td>原价:</td>
					<td>
						<input type="tel" name="originPrice" id="originPrice"
						onkeypress="if (event.keyCode!=46 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false;"
						value="${activity.originPrice}"  placeholder="请输入价格" /> 元</td>
				</tr>
				<tr>
					<td>单独购买价格:</td>
					<td><input type="tel" name="singlePrice" id="singlePrice"
						onkeypress="if (event.keyCode!=46 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false;"
						value="${activity.singlePrice}" placeholder="请输入价格" /> 元</td>
					</td>
				</tr>
				<tr>
					<td>团购折扣:</td>
					<td><input type="tel" name="discount" id="discount"
						onkeypress="if (event.keyCode!=46 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false;"
						value="${activity.discount}" placeholder="请输入折扣" /> 折</td>
				</tr>
				<tr>
					<td>团购价:</td>
					<td><input type="tel" readonly="readonly" id="groupPrice" name="groupPrice" value="${activity.groupPrice}"></td>
				</tr>
				<tr>
					<td>成团组员数:</td>
					<td><input type="tel" name="groupVol" id="groupVol" maxlength="2"
						onkeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;"
						value="${activity.groupVol}" placeholder="请输入成团组员数" /> 人</td>
				</tr>
				<tr>
					<td>商品总数量:</td>
					<td><input type="tel" name="totalQuantity" id="totalQuantity" maxlength="5"
						onkeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;"
						value="${activity.totalQuantity}" placeholder="请输入商品总数量" /> 件</td>
				</tr>
				<tr>
					<td>库存量:</td>
					<td><input type="tel" name="remain" id="remain" maxlength="5"
						onkeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;"
						value="${activity.remain}" placeholder="请输入库存量" /> 件</td>
				</tr>
				<tr>
					<td>开始时间:</td>
					<td>${activity.startTime}</td>
				</tr>
				<tr>
					<td>详细介绍:</td>
					<td><textarea class="form-control" rows="3" 
						style="width:80%; height:120px;" id="introduction" name="introduction"
						placeholder="请输入详细介绍">${activity.introduction}</textarea></td>
				</tr>
				<tr>
					<td></td>
					<td>
						<span id="errSpan" style="color:red;"></span>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<button type="button" class="btn btn-primary" id="doIt">
							<i class="icon-save"></i> 保存
						</button>
						<button type="button" class="btn" onclick="window.history.go(-1)">取消</button>
					</td>
				</tr>
			</table>
			</form>
			</div>
			</div>
			<jsp:include page="../footer.jsp" />
		</div>
	</div>
</div>
</body>
</html>



<%@ page language="java" contentType="text/html; charset=utf-8"%>
<label>
	<input type="radio" class="hidden-input" name="options" onclick="selectDefault(this);"> 
	<span name="default"></span>
	<img src="">
</label>
<input type="file" class="btn btn-primary" name="changePic" />
<button type="button" class="btn btn-primary " onclick="uploadPic(this);">
	<i class="icon-upload"></i> 上传
</button>
<input type="hidden" name="imgUrl">
<button type="button" class="btn btn-primary" onclick="deletePic(this);">
	<i class="icon-remove"></i> 删除
</button>

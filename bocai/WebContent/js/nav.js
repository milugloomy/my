Vue.component('mynav', {
	props: {
		menu1:{
			default:false
		},
		menu2:{
			default:false
		},
		menu3:{
			default:false
		}
	},
	//v-bind:class 中 样式名带-的 需加单引号
	template: '<nav class="mui-bar mui-bar-tab">\
		    <a class="mui-tab-item" href="counter.html" v-bind:class="{\'mui-active\':menu1}">\
				<span class="mui-icon mui-icon-plus"></span>\
				<span class="mui-tab-label">{{menuName1}}</span>\
			</a>\
			<a class="mui-tab-item" href="nbaQry.html" v-bind:class="{\'mui-active\':menu2}">\
				<span class="mui-icon mui-icon-search"></span>\
				<span class="mui-tab-label">{{menuName2}}</span>\
			</a>\
			<a class="mui-tab-item" href="my.html" v-bind:class="{\'mui-active\':menu3}">\
				<span class="mui-icon mui-icon-person"></span>\
				<span class="mui-tab-label">{{menuName3}}</span>\
			</a>\
		</nav>\
	',
	data:function(){
		return{
			menuName1:'计算器',
			menuName2:'查询',
			menuName3:'我'
		}
	}
});

var nav=new Vue({
	el:"#nav"
});
jQuery.post2SRV=function(url,param,callback,dataType){
	if(dataType==undefined){
		callback=param;
		dataType=callback;
	}
	var loadLayer=layer.load(1,{shade: [0.5,'#fff']});
	$.ajax({
		url:url,
		data:param,
		timeout:5000,
		success:function(data){
			layer.close(loadLayer);
			callback(data);
		},
		error:function(){
			layer.close(loadLayer);
			layer.alert("<div style='font-size:18px'>系统忙,请稍后再试</div>",{
				icon: 5,
				title:"<div style='font-size:18px'>出错啦</div>"
			});
		},
		dataType:dataType
	});
}

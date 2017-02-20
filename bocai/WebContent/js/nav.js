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
				<span class="mui-icon mui-icon-home"></span>\
				<span class="mui-tab-label">{{menuName1}}</span>\
			</a>\
			<a class="mui-tab-item" href="nbaQry.html" v-bind:class="{\'mui-active\':menu2}">\
				<span class="mui-icon mui-icon-gear"></span>\
				<span class="mui-tab-label">{{menuName2}}</span>\
			</a>\
			<a class="mui-tab-item" v-bind:class="{\'mui-active\':menu3}">\
				<span class="mui-icon mui-icon-person"></span>\
				<span class="mui-tab-label">{{menuName3}}</span>\
			</a>\
		</nav>\
	',
	data:function(){
		return{
			menuName1:'计算器',
			menuName2:'查询',
			menuName3:'我的'
		}
	}
});

var nav=new Vue({
	el:"#nav"
});
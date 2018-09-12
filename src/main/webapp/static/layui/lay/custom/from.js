layui.define(['jquery','layer'],function(exports){
	var	$ = layui.$,
		layer = parent.layer === undefined ? layui.layer : top.layer;
	var customerFrom = {
			// 表单序列化Json
			serializeJson : function(from) {
				var data = from.serializeArray();
				var obj = {};
				$.each(data,function(i,v){
					obj[v.name] = v.value;
				})
				return obj;
			}
	};
	exports('customerFrom', customerFrom);
})

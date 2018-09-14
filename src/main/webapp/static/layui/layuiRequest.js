layui.define(['jquery','layer'],function(exports){
	var	$ = layui.$,
		layer = parent.layer === undefined ? layui.layer : top.layer;
	var layuiRequest = {
			// 用户登录
			login: function (url, data, loginBtn) {
				$.ajax({
					method: 'POST',
					url : url,
					data : data,
					dataType : 'json',
					beforeSend: function(){
						loginBtn.text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
					},
					success : function(result) {
						if (result.code == "0") {
							location.href = ctx + '/sys/area/index';
						} else {
							layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
							loginBtn.text("登录").attr("disabled",false).removeClass("layui-disabled");
						}
					},
					error : function(result) {
						// 错误信息
						layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
						loginBtn.text("登录").attr("disabled",false).removeClass("layui-disabled");
					}
				});
			},
			// 获取菜单最大排序
			getMaxMenuSort: function (url) {
				var returnMsg;
        		$.ajax({
        			async : false,
        			method : 'GET',
        			url : url,
        			dataType : 'json',
        			success : function(result) {
        				returnMsg = result;
        			},
        			error : function(result) {
        			}
        		});
        		return returnMsg;
			},
			// 添加菜单
			addMenu: function (url, data, btn) {
				layuiRequest.jsonPostBtn(url, data, btn);
			},
			// 编辑菜单
			editMenu: function (url, data, btn) {
				layuiRequest.jsonPostBtn(url, data, btn);
			},
			// 删除菜单
			delMenu: function (data, index, url, tableIns) {
				layuiRequest.jsonPostLoad(data, index, url, tableIns);
			},
			// 加载Echarts图表
			loadEcharts: function (url, data, echarts) {
				$.ajax({
					method: 'POST',
					url : url,
					contentType : 'application/json',
					dataType : 'json',
					data : JSON.stringify(data),
					beforeSend: function() {
						layer.load();
					},
					success : function(result) {
						layer.closeAll('loading');
						if (result.code == "0") {
							layer.msg(result.msg, {icon: 6,time: 1000});
							echarts.setOption(result.customObj,true);
						} else {
							layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
						}
					},
					error : function(result) {
						layer.closeAll('loading');
						layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
					}
				});
			},
			// json提交(按钮提示)
			jsonPostBtn: function (url, data, btn) {
        		$.ajax({
        			method: 'POST',
        			url : url,
        			contentType : 'application/json',
        			dataType : 'json',
        			data : JSON.stringify(data),
        			beforeSend: function() {
        				btn.text("提交中...").attr("disabled","disabled").addClass("layui-disabled");
        			},
        			success : function(result) {
        				if (result.code == "0") {
        					layer.msg(result.msg, {icon: 6,time: 1000});
        					parent.layer.close(parent.layer.getFrameIndex(window.name));
        				} else {
							layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
							btn.text("提交").attr("disabled",false).removeClass("layui-disabled");
        				}
        			},
        			error : function(result) {
        				layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
        				btn.text("提交").attr("disabled",false).removeClass("layui-disabled");
        			}
        		});
			},
			// json提交(Load提示提示)
			jsonPostLoad: function (data, index, url, tableIns) {
        		$.ajax({
        			method: 'POST',
        			url : url,
        			data : JSON.stringify(data),
        			contentType : 'application/json',
        			dataType : 'json',
        			beforeSend: function() {
        				layer.close(index);
        				layer.load();
        			},
        			success : function(result) {
        				layer.closeAll('loading');
        				if (result.code == "0") {
        					layer.msg(result.msg, {icon: 6,time: 1000});
        					tableIns.reload();
        				} else {
        					layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
        				}
        			},
        			error : function(result) {
        				// 错误信息
        				layer.closeAll('loading');
        				layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
        			}
        		});
			}
	};
	exports('layuiRequest', layuiRequest);
})

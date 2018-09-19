layui.use(['jquery','layer'],function() {
	var $ = layui.jquery,
		layer = parent.layer === undefined ? layui.layer : top.layer;

	var websocket = null;  
	if ('WebSocket' in window) {
	    websocket = new WebSocket('ws://' + 'localhost:8008' + ctx + '/websocket/socketsever');  
	} else if ('MozWebSocket' in window) {
	    websocket = new MozWebSocket('ws://' + ctx + '/websocket/socketsever');  
	} else {
		// 低版本的浏览器，则用SockJS
	    websocket = new SockJS('http://' + ctx + '/sockjs/socketsever');  
	}  
	  
	// 连接打开事件的事件监听器
	websocket.onopen = function (evnt) {
		console.log(evnt);
	};
	
	// 消息事件的事件监听器
	websocket.onmessage = function (evnt) {
		console.log(evnt);
	}
	
	// 监听error事件
	websocket.onerror = function (evnt) {
		console.log('error：' + $.parseJSON(evnt));
	};
	
	// 监听连接关闭事件
	websocket.onclose = function (evnt) {
		console.log(evnt);
	}
})

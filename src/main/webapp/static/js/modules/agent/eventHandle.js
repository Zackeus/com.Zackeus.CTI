var onCloseEvent,
	onMessageEvent,
	LAY_CALL;

layui.use(['layer','layuiRequest'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        layuiRequest = layui.layuiRequest;
    
    // 关闭事件
    onCloseEvent = function (event) {
		var closeMsg;
		switch (event.code) {
		
		case 1000:
		case 1001:
			break;
		
		case 1006:
			closeMsg = "验证未通过！";
			openCloseLay(closeMsg);
			break;

		default:
			closeMsg = event.reason;
			if ($.isEmptyObject(closeMsg)) {
				closeMsg = "账号已登出！";
			} 
			openCloseLay(closeMsg);
			break;
		}
	}
    
	// 消息事件
	onMessageEvent = function (event) {
		window.sessionStorage.setItem("eventCode", event.eventCode);
		window.sessionStorage.setItem("eventType", event.eventType);
		switch (event.eventCode) {
		
		// 坐席状态切换
		case 1:
			window.sessionStorage.setItem("agentState", event.additionalContent.agentState);
			$(document.getElementById('sysMain').contentWindow.document).find("#agentState").children('span').text(event.additionalContent.agentStateText);
			$(document.getElementById('sysMain').contentWindow.document).find("#agentState").children('span').css('color', event.additionalContent.agentStateColor);
			break;
			
		// 坐席发起呼叫请求
		case 2:
			openCallLay(event.eventCode);
			break;
			
		// 坐席来电
		case 3:
			openCallLay(event.eventCode);
			break;
			
		// 语音通话结束
		case 4:
			layer.close(LAY_CALL);
			layer.close(LAY_RINGING);
			break;

		default:
			break;
		}
	}
	
	// 消息提示框
	function openCloseLay(msg) {
		layer.open({
			type: 1,
			title: false,
			closeBtn: false,
			area: '300px;',
			shade: 0.8,
			id: 'LAY_Onclose',
			btn: ['确认'],
			btnAlign: 'c',
			moveType: 1,
			zIndex: 999,
			content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; text-align: center;">' + msg + '请重新登录！</div>',
			success: function(layero) {
				layero.find('.layui-layer-btn').find('.layui-layer-btn0').attr({href : ctx + '/sys/logout'});
			}
	    });
	}
	
	// 呼叫弹屏
	function openCallLay(eventCode) {
		LAY_CALL = layer.open({
	          type: 2,
	          title: false, 
	          closeBtn: false,
	          area: ['390px', '180px'],
	          shade: 0.8, 				
	          shadeClose: false, 		
	          anim: 0, 				
	          isOutAnim: true, 			
	          scrollbar: false, 		
	          id: 'LAY_CALL', 			
	          btn: ['挂断'],
	          btnAlign: 'c',
	          moveType: 1,
	          content: [ctx + '/sys/agent/popUp/' + eventCode, 'no'],
	          success: function(layero) {
	              var btn = layero.find('.layui-layer-btn');
	              btn.css('position', 'absolute');
	              btn.css('bottom', '0');
	              btn.css('margin-left', '38%');
	          },
	          yes: function(index, layero) {
	        	  layuiRequest.callEnd(ctx + '/sys/agent/voiceCallEnd', layero.find('.layui-layer-btn0'));
	          }
	   });
	}
})
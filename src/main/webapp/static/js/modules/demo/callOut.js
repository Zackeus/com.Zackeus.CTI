layui.extend({
	layuiRequest: '{/}' + ctxStatic + '/layui/layuiRequest'
})

layui.use(['layuiRequest','form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        layuiRequest = layui.layuiRequest;
    
    // 接口语音外呼
    form.on('submit(collOutDemo)', function(data) {
    	$.ajax({
			type : 'POST',
			url : ctx + '/sys/demo/voiceCallTest',
			data: {
				userId: data.field.userId,
				postUrl: data.field.postUrl,
				called: data.field.calledDemo
		    },
			dataType : 'json',
			success : function(result) {
				if (result.code == 0) {
					openCallLay(data.field.calledDemo);
				} else {
					layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
				}
			},
			error : function(result) {
				layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
			}
		});
    	return false;
    });
    
	function openCallLay(called) {
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
	          content: [ctx + '/sys/agent/popUp/2/' + called, 'no'],
	          success: function(layero) {
	              var btn = layero.find('.layui-layer-btn');
	              btn.css('position', 'absolute');
	              btn.css('bottom', '0');
	              btn.css('margin-left', '38%');
	          },
	          yes: function(index, layero) {
	        	  var btn = layero.find('.layui-layer-btn0');
	        	  $.ajax({
						type : 'POST',
						url : ctx + '/sys/demo/voiceCallEndTest',
						dataType : 'json',
						beforeSend: function() {
							btn.text("挂断中...").attr("disabled","disabled").addClass("layui-disabled");
						},
						success : function(result) {
							if (result.code == 0) {
								layer.close(index);
							} else {
								layer.msg(result.msg, {icon: 5,time: 2000,shift: 6}, function(){});
								btn.text("挂断").attr("disabled",false).removeClass("layui-disabled");
							}
						},
						error : function(result) {
							btn.text("挂断").attr("disabled",false).removeClass("layui-disabled");
							layer.msg('响应失败', {icon: 5,time: 2000,shift: 6}, function(){});
						}
					});
	          }
	   });
	}
})
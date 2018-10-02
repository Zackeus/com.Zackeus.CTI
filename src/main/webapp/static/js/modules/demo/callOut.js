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
    	layuiRequest.callOut(ctx + '/sys/demo/voiceCallTest/' + data.field.calledDemo);
    	return false;
    });
})
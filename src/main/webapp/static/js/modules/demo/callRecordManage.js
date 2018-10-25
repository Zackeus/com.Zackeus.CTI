layui.extend({
	layuiRequest: '{/}' + ctxStatic + '/layui/layuiRequest',
	customerFrom: '{/}' + ctxStatic + '/layui/lay/custom/from'
})

layui.use(['form','layer','table','laytpl','layuiRequest','customerFrom'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        layuiRequest = layui.layuiRequest,
        customerFrom = layui.customerFrom;

    form.on('submit(search)', function(data) {
    	$.ajax({
			type : 'POST',
			url : ctx + '/sys/demo/callRecordManage',
			data: {
				userId: data.field.userId,
				page: data.field.page,
				pageSize: data.field.pageSize,
				callid: data.field.callid,
				userName: data.field.userName,
				type: data.field.type
		    },
			dataType : 'json',
			success : function(result) {
				if (result.code == 0) {
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

})
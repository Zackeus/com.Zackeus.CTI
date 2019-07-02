layui.extend({
	layuiRequest: '{/}' + ctxStatic + '/layui/layuiRequest',
	customerFrom: '{/}' + ctxStatic + '/layui/lay/custom/from'
})

layui.use(['layuiRequest','form','layer','customerFrom','laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        layuiRequest = layui.layuiRequest,
        customerFrom = layui.customerFrom;
    
    laydate.render({
    	elem:'#startDate',			// 绑定元素
    	theme: '#393D49', 			// 自定义背景色主题
    	calendar: true,				// 显示公历节日
    	mark: {'0-0-10': '工资'}, 	//每个月10号
		done: function(value, date, endDate){
			if($('#endDate').val() == '' || 
					new Date(Date.parse(value)) >= new Date(Date.parse($('#endDate').val()))) {
				$("#endDate").val(value);
			}
		}
    });
    laydate.render({
    	elem:'#endDate',
    	theme: '#393D49',
    	calendar: true,
    	mark: {'0-0-10': '工资'},
		done: function(value, date, endDate){
			if($('#startDate').val() == '') {
				return;
			}
			// 判断结束日期是否大于开始日期
			if(new Date(Date.parse($('#startDate').val())) >= 
				new Date(Date.parse(value))) {
				$('#endDate').val($('#startDate').val());
			}
		}
    });
    
    $('#close').click(function () {
    	parent.layer.close(parent.layer.getFrameIndex(window.name));
    });
    
    // 导出数据
    form.on('submit(exportData)', function(data) {
    	var url = ctx + '/sys/agent/callDataExport';
    	var form = $("<form></form>").attr("action", url).attr("method", "post");
        for (var key in data.field) {
        	form.append($("<input></input>").attr("type", "hidden").attr("name", key).attr("value", data.field[key]));
        }
        form.appendTo('body').submit().remove();
    	return false;
    });
    
})
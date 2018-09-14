layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    layer.load();
    var userListtIns =  table.render({
        elem: '#userList',
        title: '用户表',								//  定义 table 的大标题（在文件导出等地方会用到）layui 2.4.0 新增
        url : ctx + '/sys/user/manage',
        toolbar: '#toolbarDemo',					// 	头部工具栏 (layui 2.4.0 开始新增)
        cellMinWidth : 50, 							//	（layui 2.2.1 新增）全局定义所有常规单元格的最小宽度（默认：60），一般用于列宽自动分配的情况。其优先级低于表头参数中的 minWidth
        loading : true, 							//	是否显示加载条
        page : true, 								//	开启分页
        limit : 15, 								//	每页显示的条数（默认：10）。值务必对应 limits 参数的选项。优先级低于 page 参数中的 limit 参数。
        limits : [10,15,20,25], 					//	每页条数的选择项
        id : "userList", 							//	设定容器唯一ID
        text: { 									//	自定义文本
            none: '暂无相关数据' 						//	默认：无数据。注：该属性为 layui 2.2.5 开始新增
        },
        parseData: function(res) { 					//解析成 table 组件所规定的数据 layui 2.4.0 开始新增
        	return {
              'code': res.code, 					//解析接口状态
              'msg': res.msg, 						//解析提示文本
              'count': res.total, 					//解析数据长度
              'data': res.list 						//解析数据列表
            };
        },
        request: {									// 定义前端 json 格式
        	  pageName: 'page', 					// 页码的参数名称，默认：page
        	  limitName: 'pageSize' 				// 每页数据量的参数名，默认：limit
        },  
        cols : [[
            {templet: '<div>{{d.company.name}}</div>', title: '归属公司', align:'center'},
            {templet: '<div>{{d.office.name}}</div>', title: '归属部门', align:'center'},
            {field: 'loginName', title: '登录名',  minWidth: 30, align:'center'},
            {field: 'name', title: '姓名', align:'center'},
            {field: 'userType', title: '用户类型', align:'center'},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'mobile', title: '手机', align:'center'},
            {title: '操作', fixed:"right", align: 'center', templet:'#timerListBar'}
        ]],
        done: function(res, curr, count) {
        	layer.closeAll('loading');
        }
    });
    
    //头工具栏事件
    table.on('toolbar(userList)', function(obj) {
      switch(obj.event) {
      
        case 'add':
        	console.log(obj);
        break;
      };
    });
    
    //监听行工具事件
    table.on('tool(userList)', function(obj){
		switch (obj.event) {
		
		case "edit":
			console.log(obj.data);
//			editTimer(obj.data);
			break;
			
		case "del":
//			delTimer(obj.data);
			break;
			
		default:
			break;
		}
    });
    
    //控制表格编辑时文本的位置【跟随渲染时的位置】
    $("body").on("click",".layui-table-body.layui-table-main tbody tr td",function() {
        $(this).find(".layui-table-edit").addClass("layui-"+$(this).attr("align"));
    });

})
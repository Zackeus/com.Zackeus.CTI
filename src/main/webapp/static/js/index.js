var $,tab,dataStr,layer,websocket;
layui.extend({
	bodyTab: '{/}' + ctxStatic + '/js/bodyTab',
	websocket: '{/}' + ctxStatic + '/layui/lay/modules/websocket'
})

layui.use(['bodyTab','form','element','layer','jquery','websocket'],function(){
	var form = layui.form,
		element = layui.element;
		$ = layui.$;
    	layer = parent.layer === undefined ? layui.layer : top.layer;
		tab = layui.bodyTab({
			openTabNum : "50",  			//最大可打开窗口数量
			url : ctx + "/sys/menu/list" 	//获取菜单json地址
		}),
		websocket = layui.websocket.init(socketUrl + '/websocket/socketsever', socketUrl + '/sockjs/socketsever');
	
	//通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
	function getData(json){
		$.getJSON(tab.tabConfig.url + "/" + json,function(data) {
			dataStr = data;
			// 重新渲染左侧菜单
			tab.render();
		})
	}
	
	// 初始化左侧菜单
	getData($(".topLevelMenus li").first().data("menu"));
	
	//页面加载时判断左侧菜单是否显示  通过顶部菜单获取左侧菜单
	$(".topLevelMenus li,.mobileTopLevelMenus dd").click(function(){
		if($(this).parents(".mobileTopLevelMenus").length != "0"){
			$(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}else{
			$(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}
		$(".layui-layout-admin").removeClass("showMenu");
		$("body").addClass("site-mobile");
		getData($(this).data("menu"));
		//渲染顶部窗口
		tab.tabMove();
	})

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		if($(".topLevelMenus li.layui-this a").data("url")){
			layer.msg("此栏目状态下左侧菜单不可展开");  //主要为了避免左侧显示的内容与顶部菜单不匹配
			return false;
		}
		
		// 隐藏按钮图标变化
		if($(this).is('.layui-icon-shrink-right')) {
		    $(this).addClass('layui-icon-spread-left');
		    $(this).removeClass('layui-icon-shrink-right');
		} else {
		    $(this).addClass('layui-icon-shrink-right');
		    $(this).removeClass('layui-icon-spread-left');
		}
		
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	})

	//手机设备的简单适配
    $('.site-tree-mobile').on('click', function(){
		$('body').addClass('site-mobile');
	});
    $('.site-mobile-shade').on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})

	//清除缓存
	$(".clearCache").click(function() {
		window.sessionStorage.clear();
        window.localStorage.clear();
        var index = layer.msg('清除缓存中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("缓存清除成功！");
        },1000);
    })

	//刷新后还原打开的窗口
    if(cacheStr == "true") {
        if (window.sessionStorage.getItem("menu") != null) {
            menu = JSON.parse(window.sessionStorage.getItem("menu"));
            curmenu = window.sessionStorage.getItem("curmenu");
            var openTitle = '';
            for (var i = 0; i < menu.length; i++) {
                openTitle = '';
                if (menu[i].icon) {
                    if (menu[i].icon.split("-")[0] == 'icon') {
                        openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                    } else {
                        openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                    }
                }
                openTitle += '<cite>' + menu[i].title + '</cite>';
                openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                element.tabAdd("bodyTab", {
                    title: openTitle,
                    content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                    id: menu[i].layId
                })
                //定位到刷新前的窗口
                if (curmenu != "undefined") {
                    if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                        element.tabChange("bodyTab", '');
                    } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                        element.tabChange("bodyTab", menu[i].layId);
                    }
                } else {
                    element.tabChange("bodyTab", menu[menu.length - 1].layId);
                }
            }
            //渲染顶部窗口
            tab.tabMove();
        }
    }else{
		window.sessionStorage.removeItem("menu");
		window.sessionStorage.removeItem("curmenu");
	}
	
	// 连接打开事件的事件监听器
	websocket.onopen = function (evnt) {};
	
	// 消息事件的事件监听器
	websocket.onmessage = function (evnt) {
		console.log(evnt);
	}
	
	// 监听error事件
	websocket.onerror = function (evnt) {};
	
	// 监听连接关闭事件
	websocket.onclose = function (evnt) {
		var closeMsg;
		switch (evnt.code) {
		
		case 1000:
		case 1001:
			break;
		
		case 1006:
			closeMsg = "验证未通过！";
			openCloseLay(closeMsg);
			break;

		default:
			if ($.isEmptyObject(evnt.reason)) {
				closeMsg = "账号已登出！";
			}
			closeMsg = evnt.reason;
			openCloseLay(closeMsg);
			break;
		}
	}
	
	// 信息提示框
	function openCloseLay(closeMsg) {
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
			content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; text-align: center;">' + closeMsg + '请重新登录！</div>',
			success: function(layero) {
				var btn = layero.find('.layui-layer-btn');
				btn.find('.layui-layer-btn0').attr({
					href : ctx + '/sys/logout'
				});
			}
	    });
	}
})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

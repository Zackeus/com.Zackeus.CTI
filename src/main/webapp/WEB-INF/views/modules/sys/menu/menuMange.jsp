<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<blockquote class="layui-elem-quote quoteBox">
		<div class="demoTable">
			<div class="layui-inline">
				<div class="layui-input-inline">
		    		<button class="layui-btn" id="btn-add"><i class="layui-icon">&#xe608;</i> 添加</button>
		    	</div>
			</div>
		
			<div class="layui-inline">
		    	<div class="layui-input-inline">
		      		<button class="layui-btn" id="btn-expand">全部展开</button>
		    	</div>
			</div>
		    
		    <div class="layui-inline">
		    	<div class="layui-input-inline">
		      		<button class="layui-btn" id="btn-fold">全部折叠</button>
		    	</div>
			</div>
		</div>
	</blockquote>
	<table id="menulist" class="layui-table" lay-filter="menulist"></table>
	
	<!-- 图标 -->
	<script type="text/html" id="menu-icon">
		{{#  if(d.icon != undefined && d.icon != ''){ }}
			<i class="layui-icon {{d.icon}}"></i>
		{{#  } else { }}
			无
		{{#  }}}
	</script>
	
	<!-- 操作列 -->
	<script type="text/html" id="menu-operate">
  		<a title="添加子类" class="layui-btn layui-btn-xs layui-btn-primary" lay-event="add" href="javascript:;"><i class="layui-icon">&#xe654;</i></a>
		<a title="修改" class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit" href="javascript:;"><i class="layui-icon">&#xe642;</i></a>
		<a title="删除" class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" href="javascript:;"><i class="layui-icon">&#xe640;</i></a>
	</script>
	
	<script type="text/javascript" src="${ctxStatic}/js/modules/sys/menu/menuMange.js"></script>
</body>
</html>
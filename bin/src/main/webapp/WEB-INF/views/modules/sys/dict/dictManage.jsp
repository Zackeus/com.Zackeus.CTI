<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<form class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<select id="type" name="type" lay-verify="" lay-search>
						<option value="">请选择字典类型</option>
						<c:forEach items="${typeList}" var="obj">
							<option value="${obj}">${obj}</option>
						</c:forEach>
					</select>
				</div>
	    		<div class="layui-input-inline">
					<input id="description" name="description" type="text" class="layui-input" placeholder="字典描述">
				</div>
				<button class="layui-btn" lay-submit lay-filter="search"><i class="layui-icon layui-icon-search"></i></button>
			</div>
		</blockquote>
	</form>

	<table id="dictList" lay-filter="dictList" class="layui-hide"></table>
		
	<script type="text/html" id=dictListToolBar>
 		<div class="layui-btn-container">
			<button class="layui-btn" type="button" lay-event="add"><i class="layui-icon">&#xe608;</i> 添加</button> -->
  		</div>
	</script>
	
	<script type="text/html" id="dictListBar">
		<a title="修改" class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit" href="javascript:;"><i class="layui-icon">&#xe642;</i></a>
		<a title="删除" class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" href="javascript:;"><i class="layui-icon">&#xe640;</i></a>
		<a title="添加键值" class="layui-btn layui-btn-xs layui-btn-primary" lay-event="add" href="javascript:;"><i class="layui-icon">&#xe654;</i></a>
	</script>
	
	<script type="text/javascript" src="${ctxStatic}/js/modules/sys/dict/dictMange.js"></script>
</body>
</html>
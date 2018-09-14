<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<form class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<table id="userList" lay-filter="userList" class="layui-hide"></table>
		
		<script type="text/html" id="toolbarDemo">
 			<div class="layui-btn-container">
				<button class="layui-btn" type="button" lay-event="add"><i class="layui-icon">&#xe608;</i> 添加</button> -->
  			</div>
		</script>
		
		<script type="text/html" id="timerListBar">
			<shiro:hasRole name="admin">
				<a title=">编辑" class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe642;</i></a>
			</shiro:hasRole>
		</script>
	</form>
	<script type="text/javascript" src="${ctxStatic}/js/modules/sys/user/userMange.js"></script>
</body>
</html>
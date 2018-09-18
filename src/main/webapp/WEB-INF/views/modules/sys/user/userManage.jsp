<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.downpanel .layui-select-title span{line-height: 38px;}
	.downpanel dl dd:hover{background-color: inherit;}
	.layui-icon-input i{position: absolute;left:15px;top:50%;margin-top: 8px;}
	.layui-icon-input .layui-input {padding-right: 30px;margin-left: 40px;}
	.layui-input-inline button{margin-left: 40px;}
	</style>
</head>
<body class="childrenBody">
	<form id="sesarchForm" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<div class="layui-unselect layui-form-select downpanel">
						<div class="layui-select-title">
							<span id="treeclass" class="layui-input layui-unselect">归属部门</span>
							<input id="office.id" name="office.id" type="hidden">
							<i class="layui-edge"></i>
						</div>
						<dl id="divid" class="layui-anim layui-anim-upbit">
							<dd>
								<ul id="classtree"></ul>
							</dd>
						</dl>						
					</div>
				</div>
				<div class="layui-input-inline">
					<input id="loginName" name="loginName" type="text" class="layui-input" placeholder="请输入登录名">
				</div>
				<div class="layui-input-inline">
					<input id="name" name="name" type="text" class="layui-input" placeholder="请输入姓名">
				</div>
				<div class="layui-input-inline">
					<input id="agentUser.workno" name="agentUser.workno" type="text" class="layui-input" placeholder="请输入坐席工号">
				</div>
				<div class="layui-input-inline">
					<input id="agentUser.phonenumber" name="agentUser.phonenumber" type="text" class="layui-input" lay-verify="myNumber" placeholder="请输入座机号">
				</div>
				<button class="layui-btn" lay-submit lay-filter="search"><i class="layui-icon layui-icon-search"></i></button>
			</div>
		</blockquote>
	</form>
	
	<table id="userList" lay-filter="userList" class="layui-hide"></table>
	
	<script type="text/html" id=userListToolBar></script>
	
	<script type="text/html" id="timerListBar">
		<shiro:hasRole name="admin">
			<a title=">编辑" class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe642;</i></a>
			<a title="注销" class="layui-btn layui-btn-xs layui-btn-danger" lay-event="cancer"><i class="layui-icon">&#xe640;</i></a>
		</shiro:hasRole>
	</script>
	
	<script type="text/html" id="workNoTpl">
  		{{#  if(d.agentUser == undefined || d.agentUser.workno == undefined){ }}
			 <span style="color: #F581B1;">未注册</span>
  		{{#  } else { }}
			{{ d.agentUser.workno }}
  		{{#  } }}
	</script>
	
	<script type="text/html" id="phoneTpl">
  		{{#  if(d.agentUser == undefined || d.agentUser.phonenumber == undefined || d.agentUser.phonenumber == null || d.agentUser.phonenumber == ""){ }}
			 <span style="color: #F581B1;">未绑定</span>
  		{{#  } else { }}
			{{ d.agentUser.phonenumber }}
  		{{#  } }}
	</script>
	
	<script type="text/javascript" src="${ctxStatic}/js/modules/sys/user/userMange.js"></script>
</body>
</html>
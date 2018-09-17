<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>编辑用户</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">

	<form id="editFrom" class="layui-form layui-form-pane">
		<div class="layui-form-item layui-row layui-col-xs12" style="display:none">
			<label class="layui-form-label">用户标识</label>
			<div class="layui-input-block">
				<input id="id" name="id" type="text" class="layui-input" lay-verify="required" value="${user.id}">
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">归属公司</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" value="${user.company.name}" disabled>
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">归属部门</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" value="${user.office.name}" disabled>
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">工号</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" value="${user.no}" disabled>
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">坐席工号</label>
			<div class="layui-input-block">
				<input id="agentUser.workno" name="agentUser.workno" type="text" class="layui-input" value="${user.agentUser.workno}">
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">姓名</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" value="${user.name}" disabled>
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">座机</label>
			<div class="layui-input-block">
				<input id="agentUser.phonenumber" name="agentUser.phonenumber" type="text" class="layui-input" lay-verify="myNumber" value="${user.agentUser.phonenumber}">
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">手机</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" value="${user.mobile}" disabled>
			</div>
		</div>
		<div class="layui-form-item">
	    	<div class="layui-input-block">
	      		<button class="layui-btn" lay-submit lay-filter="editUser">提交</button>
	      		<button id="closeUser" class="layui-btn layui-btn-primary" type="button">关闭</button>
	    	</div>
	 	</div>
	</form>

	<script type="text/javascript" src="${ctxStatic}/js/modules/sys/user/editUser.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>通话记录管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<form id="sesarchForm" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<div class="layui-input-inline">
					<input id="page" name="page" type="text" class="layui-input" placeholder="请输入当前页码">
				</div>
				<div class="layui-input-inline">
					<input id="pageSize" name="pageSize" type="text" class="layui-input" placeholder="请输入每页限制条数">
				</div>
				<div class="layui-input-inline">
					<input id="callid" name="callid" type="text" class="layui-input" placeholder="请输入呼叫流水号">
				</div>
				<div class="layui-input-inline">
					<input id="userName" name="userName" type="text" class="layui-input" placeholder="请输入受理人姓名">
				</div>
				<div class="layui-input-inline" style="width: 100px;">
	    			<select id="type" name="type" lay-filter="type">
	    				<option value="" selected="selected">呼叫类型</option>
						<c:forEach items="${fns:getDictList('call_record_type')}" var="obj">
							<option value="${obj.value}">${obj.label}</option>
						</c:forEach>
					</select>
    			</div>
				<button class="layui-btn" lay-submit lay-filter="search"><i class="layui-icon layui-icon-search"></i></button>
			</div>
		</blockquote>
	</form>

	<script type="text/javascript" src="${ctxStatic}/js/modules/demo/callRecordManage.js"></script>
</body>
</html>
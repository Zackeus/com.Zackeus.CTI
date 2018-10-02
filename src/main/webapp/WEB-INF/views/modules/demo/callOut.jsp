<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>语音外呼</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<form class="layui-form">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input id="calledDemo" name="calledDemo" type="text" class="layui-input" lay-verify="required" placeholder="请输入呼叫号码"/>
				</div>
				<button title="外呼" class="layui-btn" lay-submit lay-filter="collOutDemo"><i class="layui-icon layui-icon-zzphone"></i></button>
			</div>
		</blockquote>
	</form>
	<script type="text/javascript" src="${ctxStatic}/js/modules/demo/callOut.js"></script>
</body>
</html>
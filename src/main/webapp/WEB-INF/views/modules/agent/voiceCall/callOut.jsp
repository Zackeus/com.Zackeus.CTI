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
					<input type="text" class="layui-input callVal" placeholder="请输入呼叫号码"/>
				</div>
				<button class="layui-btn callOut_btn"><i class="layui-icon">&#xe63b;</i>呼叫</button>
			</div>
		</blockquote>
	</form>
	<script type="text/javascript" src="${ctxStatic}/Layui/js/voiceCall/callOut.js"></script>
</body>
</html>
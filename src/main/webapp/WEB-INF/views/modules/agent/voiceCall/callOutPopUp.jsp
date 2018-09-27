<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>语音呼叫弹屏</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	i.layui-anim{display:inline-block}
	.callLayer{padding: 50px;line-height: 22px;background-color: #393D49;color: #fff;font-weight: 300;text-align: center;font-size: 18px;}
	.layui-icon{font-size: 48px !important;}
	</style>
</head>
<body>
<form class="layui-form layui-row">
	<div class="layui-col-md6 layui-col-xs12">
	<div class="callLayer">
		<div class="layui-form-item">
			<span id="callEvent">${callEvent}</span>&nbsp<span id="callNum">${callNum}</span>
		</div>
		<div class="layui-form-item">
			<span id="callIcon"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i></span>
		</div>
	</div>
	</div>
</form>
	<script type="text/javascript" src="${ctxStatic}/js/modules/agent/voiceCall/callOutPopUp.js"></script>
</body>
</html>
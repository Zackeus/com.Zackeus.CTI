<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>演示案例</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">
	<form id="callOut" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">接口呼叫</blockquote>
		<blockquote class="layui-elem-quote quoteBox">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" lay-verify="required" placeholder="请输入用户编号"/>
				</div>
				<div class="layui-input-inline">
					<input id="postUrl" name="postUrl" type="text" class="layui-input" placeholder="请输入推送地址"/>
				</div>
				<div class="layui-input-inline">
					<input id="calledDemo" name="calledDemo" type="text" class="layui-input" lay-verify="required" placeholder="请输入呼叫号码"/>
				</div>
				<button title="外呼" class="layui-btn" lay-submit lay-filter="collOutDemo"><i class="layui-icon layui-icon-zzphone"></i></button>
			</div>
		</blockquote>
	</form>
	
	<form id="sesarchForm" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">呼叫记录查询</blockquote>
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
	
	<form id="play" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">录音播放</blockquote>
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<div class="layui-input-inline">
					<input id="callid" name="callid" type="text" class="layui-input" placeholder="请输入呼叫流水号">
				</div>
				<div class="layui-input-inline">
					<input id="recordID" name="recordID" type="text" class="layui-input" placeholder="请输入录音流水号">
				</div>
				<button class="layui-btn" lay-submit lay-filter="playSubmit">回放</button>
			</div>
		</blockquote>
	</form>
	
	<form id="stopplay" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<button class="layui-btn" lay-submit lay-filter="stopplaySubmit">停止</button>
			</div>
		</blockquote>
	</form>
	
	<form id="pauserecord" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<button class="layui-btn" lay-submit lay-filter="pauserecordSubmit">暂停</button>
			</div>
		</blockquote>
	</form>
	
	<form id="resumerecord" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<button class="layui-btn" lay-submit lay-filter="resumerecordSubmit">恢复</button>
			</div>
		</blockquote>
	</form>
	
	<form id="forefast" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<div class="layui-input-inline">
					<input id="fastTime" name="fastTime" type="text" class="layui-input" placeholder="请输入快进秒数">
				</div>
				<button class="layui-btn" lay-submit lay-filter="forefastSubmit">快进</button>
			</div>
		</blockquote>
	</form>
	
	<form id="backfast" class="layui-form" onkeydown="if(event.keyCode==13) return false;">
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<div class="layui-input-inline">
					<input id="userId" name="userId" type="text" class="layui-input" placeholder="请输入用户ID">
				</div>
				<div class="layui-input-inline">
					<input id="fastTime" name="fastTime" type="text" class="layui-input" placeholder="请输入快退秒数">
				</div>
				<button class="layui-btn" lay-submit lay-filter="backfastSubmit">快退</button>
			</div>
		</blockquote>
	</form>
	<script type="text/javascript" src="${ctxStatic}/js/modules/demo/demo.js"></script>
</body>
</html>
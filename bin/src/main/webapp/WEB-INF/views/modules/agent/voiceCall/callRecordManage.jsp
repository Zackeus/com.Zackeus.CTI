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
		<!-- 工具按钮 -->
		<blockquote class="layui-elem-quote quoteBox">
			<div class="demoTable">
				<button class="layui-btn" lay-submit lay-filter="export"><i class="layui-icon layui-icon-wenjianxiazai"></i></button>
			</div>
		</blockquote>
	</form>
	
	<table id="callRecordList" lay-filter="callRecordList" class="layui-hide"></table>
	<script type="text/html" id=callRecordListToolBar></script>
	
	<script type="text/html" id="callRecordListBar">
		{{#  if(d.agentRecord != undefined && d.agentRecord.recordID != ''){ }}
			<a title="播放录音" class="layui-btn layui-btn-xs" lay-event="play"><i class="layui-icon layui-icon-zheadset"></i></a>
		{{#  } }}
	</script>
	
	<script type="text/html" id="typeTpl">
  		{{#  if(d.type == "main"){ }}
			 <span style="color: #006000;"><i class="layui-icon layui-icon-iconfont17"></i> 去电</span>
  		{{#  } else if (d.type == "called"){ }}
			 <span style="color: #0080FF;"><i class="layui-icon layui-icon-download-circle"></i> 来电</span>
  		{{#  } else { }}
			<span style="color: black">未知</span>
  		{{#  } }}
	</script>
	
	<script type="text/html" id="resultTpl">
  		{{#  if(d.result){ }}
			 <span style="color: blue"><i class="layui-icon layui-icon-zzvolume-control-phone"></i> 已接</span>
  		{{#  } else { }}
			<span style="color: red"><i class="layui-icon layui-icon-zztty"></i> 未接</span>
  		{{#  } }}
	</script>

	<script type="text/javascript" src="${ctxStatic}/js/modules/agent/voiceCall/callRecordManage.js"></script>
</body>
</html>
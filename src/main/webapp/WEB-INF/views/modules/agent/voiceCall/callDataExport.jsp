<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>通话报表导出</title>
	<meta name="decorator" content="default"/>
</head>
<body class="childrenBody">

	<form id="exportFrom" class="layui-form layui-form-pane">
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">起始日期</label>
			<div class="layui-input-block">
				<input id="startDate" name="startDate" class="layui-input" type="text" lay-verify="required|date" placeholder="起始日期" 
       			autocomplete="off" value="<fmt:formatDate value="${defaultSearch.startDay}" pattern="yyyy-MM-dd" />">
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">结束日期</label>
			<div class="layui-input-block">
				<input id="endDate" name="endDate" class="layui-input" type="text" lay-verify="required|date" placeholder="结束日期" 
       			autocomplete="off" value="<fmt:formatDate value="${defaultSearch.endDay}" pattern="yyyy-MM-dd" />">
			</div>
		</div>
		<div class="layui-form-item">
	    	<div class="layui-input-block">
	      		<button class="layui-btn" lay-submit lay-filter="exportData">导出</button>
	      		<button id="close" class="layui-btn layui-btn-primary" type="button">关闭</button>
	    	</div>
	 	</div>
	</form>

	<script type="text/javascript" src="${ctxStatic}/js/modules/agent/voiceCall/callDataExport.js"></script>
</body>
</html>
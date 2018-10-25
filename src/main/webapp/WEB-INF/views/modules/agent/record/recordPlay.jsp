<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>录音回放</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/record/style.css"/>
	
	<script type="text/javascript">
		var ctx = '${ctx}', 
		ctxStatic = '${ctxStatic}', 
		socketUrl = location.hostname + ':' + location.port + '${ctx}';
	</script>
</head>
<body>

<div id="background"></div>
<div id="player">
	<div class="cover">
		<img src='${ctxStatic}/images/record/record.jpg'>
	</div>
	<div class="ctrl">
		<div class="tag">
			<strong>${recordTitle}</strong>
<%-- 			<span class="artist">${artist}</span> --%>
			<span class="album">${album}</span>
		</div>
		<div class="control">
			<div class="left">
				<div class="rewind icon"></div>
				<div class="playback icon"></div>
				<div class="fastforward icon"></div>
			</div>
			<div class="volume right">
				<div class="mute icon left"></div>
				<div class="slider left">
					<div class="pace"></div>
				</div>
			</div>
		</div>
		<div class="progress">
			<div class="slider">
				<div class="loaded"></div>
				<div class="pace"></div>
			</div>
			<div class="timer left">0:00</div>
		</div>
	</div>
</div>

	<script src="${ctxStatic}/jquery/jquery-ui-1.12.1/jquery.js"></script>
	<script src="${ctxStatic}/layui/layui.js"></script>
	<script src="${ctxStatic}/jquery/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<script src="${ctxStatic}/js/modules/agent/record/recordPlay.js"></script>
</body>
</html>
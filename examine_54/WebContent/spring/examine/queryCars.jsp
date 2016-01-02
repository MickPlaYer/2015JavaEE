<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("queryCars.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("queryCars.title")</p>
		
		<form action="queryCars" method="POST">
		<!-- 
			<p class="content">
				#springMessage("owner.id"):
				<input type="text" name="id"/><br/>
				<input type="submit" value="#springMessage("button.query")"/>
			</p>
		 -->
		</form>
		
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
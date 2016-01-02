<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("test1.sell.title")</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("test1.sell.title")</p>

		<form action="doSell" method="POST">
			<p class="content">
				#springMessage("test1.buyername"):
				<input type="text" name="buyer"/><br/>
				#springMessage("test1.carname"):
				<input type="text" name="name"/><br/>
				<input type="submit" value="#springMessage("test1.sell.button")"/>
			</p>
		</form>
		
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>

</html>
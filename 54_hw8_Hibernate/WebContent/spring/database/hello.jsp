<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("hello.title")</title>
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
		<p class="title">#springMessage("hello.title")</p>

		<form action="doHello" method="POST">
			<p class="content">
				#springMessage("hello.question")
				<input type="text" name="name" size="10"/> 
				<input type="submit" value="?" class="QuestionMark" />
			</p>
		</form>
		
		<a href="#springMessage("homeURL")"> #springMessage("home")</a>
	</div>
</body>

</html>
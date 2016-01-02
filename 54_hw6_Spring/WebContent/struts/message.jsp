
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>#stext('name=message.title')</title>
	<link href="../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
	
		<p class="title">
			#stext('name=message.title')
		</p>
		<p class="message">
			${helloForm.result}
		</p>
		<p class="stamp">
			${helloForm.stamp}
		</p>
		<a href="../index.html">#stext('name=home')</a>
	</div>
</body>
</html>
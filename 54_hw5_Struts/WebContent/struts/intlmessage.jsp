
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>#stext('name=message.title')</title>
	<link href="../css/web.css" rel="stylesheet" type="text/css">
</head>

<body>
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
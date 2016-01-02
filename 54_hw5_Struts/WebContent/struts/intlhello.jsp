
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../css/web.css" rel="stylesheet" type="text/css">
	<title>#stext('name=hello.title')</title>
</head>
	
<body>
	<div class="body">
		<p class="title">#stext('name=hello.title')</p>
		
		<form action="doHello" method="Post">
			<p class="content">#stext('name=hello.question')
				<input type="text" name="helloForm.name" size="10"/>
				<input type="submit" value="?" class="QuestionMark"/>
			</p>
		</form>
		
		<a href="../index.html">#stext('name=home')</a>
	</div>
</body>

</html>
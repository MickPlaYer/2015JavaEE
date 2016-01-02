<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Quit</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/54_hw10_WebService/css/web.css" type="text/css">
</head>
<body>
	<div class="body">
		<p class="title">Quit</p>
		
		<form id="form" action="doQuit" method="POST">
		
			<p class="content">
				I'd like to quit
				<input type="text" name="name" size="10"/>
				<input type="submit" value="!" class="QuestionMark" />
			</p>
			
		</form>
		
		<a href="#springMessage("homeURL")">home</a>
	</div>
</body>
</html>
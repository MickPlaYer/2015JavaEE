<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
	<title>Lucky Mechine</title>
</head>

<body>
	<div class="body">
		<p class="title">Welcome!</p>
		
		<form action="doEnter" method="POST">
			<p class="content">
				What's your name?<br>
				<input type="text" name="name" size="10"/><br>
				<input type="submit" value="Enter"/>
			</p>
		</form>
		
		<a href="#springMessage("homeURL")">Home</a>
	</div>
</body>
</html>
<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
	<title>Lucky Mechine</title>
</head>

<body>
	<div class="body">
		<p class="title">Lucky Mechine</p>
		<p class="content">
			Name: $LuckyModel.name<br>
			Money: $LuckyModel.dollars Dollars<br><br>
			$LuckyModel.display<br>
		</p>
		
		<form action="doPlay" method="POST">
			<input type="hidden" name="name" value="$LuckyModel.name"/>
			<input type="hidden" name="dollars" value="10"/>
			<input type="submit" value="Play (10$)" />
		</form>
		
		<br><br><a href="#springMessage("homeURL")">Home</a>
	</div>
</body>
</html>
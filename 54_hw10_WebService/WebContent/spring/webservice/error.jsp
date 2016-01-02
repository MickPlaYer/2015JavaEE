<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/54_hw10_WebService/css/web.css" type="text/css">
<title>Error</title>
</head>
<body>
	<div class="body">
		<p class="title">
			Error
		</p>
		<p class="content">
		#foreach( $ErrorMessage in $ErrorModel )
   			$ErrorMessage.getDefaultMessage() <br>
		#end
		</p>
	</div>
	<center><a href="#springMessage("homeURL")">home</a></center>
</body>
</html>
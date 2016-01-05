<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>#springMessage("error.title")</title>
</head>

<body>
	<div class="body">
		<p class="title">
			#springMessage("error.title")
		</p>
		<p class="content">
		#foreach( $ErrorMessage in $ErrorModel )
   			$ErrorMessage.getDefaultMessage()<br>
		#end
		</p>
		<a href="javascript: history.go(-1)">#springMessage("back")</a>
		<a href="/54_WarehouseLogistics">#springMessage("home")</a>
	</div>
</body>

</html>
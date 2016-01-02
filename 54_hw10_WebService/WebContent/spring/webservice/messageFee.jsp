<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>Message Fee</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/54_hw10_WebService/css/web.css" type="text/css">
</head>

<body>
	<div class="body">
	
			<p class="title">Message Fee</p>
			
			<p class="content">	
			$MessageProviderModel.MessageModel.result <font size="3">(by Java$MessageProviderModel.messageProvider)</font>
			<br><br>
			$MessageProviderModel.MessageModel.stamp <font size="3">(by Java$MessageProviderModel.mailProvider)</font>
			<br><br> 
			Fee = NT$ $MessageProviderModel.MessageModel.count <font size="3">(by Java$MessageProviderModel.feeProvider)</font>
			</p>
			
		<a href="#springMessage("homeURL")">home</a>
			
	</div>
</body>
</html>
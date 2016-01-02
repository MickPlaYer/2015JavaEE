<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Pay</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/54_hw10_WebService/css/web.css" type="text/css">
</head>
<body>
	<div class="body">
		<p class="title">Pay</p>

		<form id="form" action="doPay" method="POST">
		
			<p class="content">I'd like to pay
					<input type="text" name="name" size="10"/>
					<input type="submit" value="!" class="QuestionMark"/>
		   </p>
		   
		</form>
		
		<a href="#springMessage("homeURL")">home</a>
	</div>
</body>
</html>
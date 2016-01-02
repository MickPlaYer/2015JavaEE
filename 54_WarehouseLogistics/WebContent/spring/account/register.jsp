<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
	<title>註冊帳號</title>
</head>

<body>
	<div class="body">
		<p class="title">註冊帳號</p>
		
		<form action="./" method="POST">
			<table class="navTable">
				<tr>
					<td>名稱：</td>
					<td><input class="inputBox" type="text" name="name"/></td>
				</tr>
				<tr>
					<td>密碼：</td>
					<td><input class="inputBox" type="password" name="password"/></td>
				</tr>
				<tr>
					<td>密碼確認：</td>
					<td><input class="inputBox" type="password" name="passwordCheck"/></td>
				</tr>
			</table>
			<input type="submit" value="註冊"/>
		</form>
		<p class="message">
			名稱需為8至32中英數字<br>
			密碼需為8至32英數字
		</p>
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>

</html>
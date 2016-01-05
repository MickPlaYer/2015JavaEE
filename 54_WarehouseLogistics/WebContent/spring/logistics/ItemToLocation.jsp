<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<style>
	input[type="submit"] {
		margin-top: 5px;
		width: 200px;
	}
</style>

<body>
	<div class="body">
		<p class="title">運送貨物</p>
		
		<form action="" method="POST">
			<table class="navTable">
				<tr>
					<td>內容物：</td>
					<td><input class="inputBox" type="text" name="contents"/></td>
				</tr>
				<tr>
					<td>目的地：</td>
					<td><input class="inputBox" type="text" name="location"/></td>
				</tr>
			</table>
			<input type="submit" value="送貨"/>
		</form>
		
		<a href="javascript: history.go(-1)">#springMessage("back")</a>
	</div>
</body>

</html>
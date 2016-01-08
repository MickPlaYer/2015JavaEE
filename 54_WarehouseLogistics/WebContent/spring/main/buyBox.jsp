<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">購買倉庫</p>
		
		<form action="" method="POST">
			<table class="navTable">
				<tr>
					<td>倉庫名稱：</td>
					<td><input class="inputBox" type="text" name="boxName"/></td>
				</tr>
				<tr>
					<td>倉庫地點：</td>
					<td><input class="inputBox" type="text" name="boxLocation"/></td>
				</tr>
				<tr>
					<td>購買方案：</td>
					<td>
						<select name="boxPeriod">
	  						<option value="year">一年 $500</option>
	  						<option value="helf-year">半年 $350</option>
	  						<option value="month">一個月 $100</option>
						</select>
					</td>
				</tr>
			</table>
			<input type="submit" value="購買"/>
		</form>
		<p class="message">本公司使用53銀行付款系統<br></p>
		<a href="/54_WarehouseLogistics/spring/main/myBox">#springMessage("back")</a>
	</div>
</body>

</html>
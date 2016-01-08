<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">倉庫啟用</p>
		<p class="message">
			注意！你可能尚未完成前次繳款，<br>請確認您在瀏覽器中其他分頁是否有未完成的繳款！<br>
		</p>
		<form action="" method="POST">
			<table class="navTable">
				<tr>
					<td>倉庫：</td>
					<td>
						<input class="inputBox" type="hidden" name="boxId" value="${Box.id}"/>
						<input class="inputBox" type="text" value="${Box.name}" readonly/>
					</td>
				</tr>
				<tr>
					<td>啟用方案：</td>
					<td>
						<select class="inputBox" name="boxPeriod">
	  						<option value="year">一年 $500</option>
	  						<option value="helf-year">半年 $350</option>
	  						<option value="month">一個月 $100</option>
						</select>
					</td>
				</tr>
			</table>
			<input type="submit" value="購買"/>
		</form>
		<p class="message">
			本公司使用53銀行付款系統<br>
		</p>
		<a href="/54_WarehouseLogistics/spring/main/myBox">#springMessage("back")</a>
	</div>
</body>

</html>
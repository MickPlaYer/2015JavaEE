<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<style>
	button {
		margin-bottom: 5px;
		width: 200px;
	}
</style>

<body>
	<div class="body">
		<p class="title">Java54 倉儲物流</p>
		
		<p class="content">帳戶：${Name}</p>
		<button onclick="window.location.href='/54_WarehouseLogistics/spring/main/myAccount'">我的帳戶</button><br>
		<button onclick="window.location.href='/54_WarehouseLogistics/spring/main/myBox'">我的倉庫列表</button><br>
		<button onclick="window.location.href='/54_WarehouseLogistics/spring/main/logistics'">我要送貨</button><br>
		<button onclick="window.location.href='/54_WarehouseLogistics/spring/logistics'">我的運貨單列表</button><br><br>
		
		<a href="/54_WarehouseLogistics/">登出</a>
	</div>
</body>

</html>
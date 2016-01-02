<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
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
		<p class="title">我要送貨</p>
		
			<button onclick="window.location.href='../logistics/boxToBox'">倉庫至倉庫</button><br>
			<button onclick="window.location.href='../logistics/boxToLocation'">倉庫至地點</button><br>
			<button onclick="window.location.href='../logistics/itemToBox'">貨物至倉庫</button><br>
			<button onclick="window.location.href='../logistics/itemToLocation'">貨物至地點</button><br><br>
		
		<a href="javascript: history.go(-1)">#springMessage("back")</a>
	</div>
</body>

</html>
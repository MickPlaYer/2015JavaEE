<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">移除貨物</p>
		
		<form action="/54_WarehouseLogistics/spring/box/removeItem/$BoxId" method="POST">
			<table class="navTable">
				<tr>
					<td>名稱：</td>
					<td><input class="inputBox" type="text" name="name" value="${Item.name}" ${ReadOnly}/></td>
				</tr>
				<tr>
					<td>數量：</td>
					<td><input class="inputBox" type="number" min="1" max="${Item.amount}" value="${Item.amount}" name="amount"/></td>
				</tr>
			</table>
			<input type="submit" value="移除"/>
		</form>
		
		<a href="javascript: history.go(-1)">#springMessage("back")</a>
	</div>
</body>

</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">我的倉庫</p>
		
		<table class="navTable" border="1px" cellspacing="0px" cellpadding="6px">
			<tr><th colspan="7">$Box.name</th></tr>
			<tr>
				<th>序號</th>
				<th>名稱</th>
				<th>數量</th>
				<th>新增</th>
				<th>移除</th>
				<th>運送至倉庫</th>
				<th>運送至地點</th>
			</tr>
			#foreach( $Item in $ItemList )
			<tr>
				<td>$Item.id</td>
				<td>$Item.name</td>
				<td>$Item.amount</td>
				<td><button onclick="window.location.href='./addItem/$Box.id/$Item.id'">新增</button></td>
				<td><button onclick="window.location.href='./removeItem/$Box.id/$Item.id'">移除</button></td>
				<td><button onclick="window.location.href='../logistics/boxToBox/$Box.id/$Item.id'">運送至倉庫</button></td>
				<td><button onclick="window.location.href='../logistics/boxToLocation/$Box.id/$Item.id'">運送至地點</button></td>
			</tr> 
			#end
			<tr>
				<td colspan="7">
					<button onclick="window.location.href='./addItem/$Box.id'">新增貨物</button>
					<button onclick="window.location.href='./removeItem/$Box.id'">移除貨物</button>
				</td>
			</tr> 
		</table>
		<br>
		<a href="/54_WarehouseLogistics/spring/main/myBox">#springMessage("back")</a>
	</div>
</body>

</html>
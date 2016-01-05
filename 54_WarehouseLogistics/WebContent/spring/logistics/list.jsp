<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/54_WarehouseLogistics/css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">我的運貨單列表</p>
		
		<table class="navTable" border="1px" cellspacing="0px" cellpadding="6px">
			<tr>
				<th>序號</th>
				<th>內容物</th>
				<th>送出地</th>
				<th>目的地</th>
				<th>費用</th>
				<th>送貨狀態</th>
				<th>銷毀貨單</th>
			</tr>
			#foreach( $W in $WaybillList )
			<tr>
				<td>$W.id</td>
				<td>$W.contents</td>
				<td>$W.from</td>
				<td>$W.to</td>
				<td>$W.fee</td>
				<td>$W.stsr</td>
				<td>
					<form action="/54_WarehouseLogistics/spring/logistics/delete/${W.id}" method="POST" style="margin-bottom: 0px;">
					<input type="submit" value="銷毀貨單"/></form>
				</td>
			</tr> 
			#end
		</table>
		<br>
		<a href="/54_WarehouseLogistics/spring/main">#springMessage("back")</a>
	</div>
</body>

</html>
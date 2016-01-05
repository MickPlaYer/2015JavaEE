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
		
		<form action="/54_WarehouseLogistics/spring/logistics/boxToBox/" method="POST">
			<table class="navTable">
				<tr>
					<td>取貨倉庫：</td>
					<td>
						<input class="inputBox" type="hidden" name="fromBoxId" value="${Box.id}"/>
						<input class="inputBox" type="text" value="${Box.name}" readonly/>
					</td>
				</tr>
				<tr>
					<td>貨物名稱：</td>
					<td><input class="inputBox" type="text" name="item" value="${Item.name}" readonly/></td>
				</tr>
				<tr>
					<td>數量：</td>
					<td><input class="inputBox" type="number" name="amount" min="1" max="${ItemBox.amount}" value="1"/></td>
				</tr>
				<tr>
					<td>目的倉庫：</td>
					<td>
						<select class="inputBox" name="toBoxId">
							#foreach(${B} in ${BoxList})
								<option value="${B.id}">${B.name}</option>
							#end
						</select>
					</td>
				</tr>
			</table>
			<input type="submit" value="送貨"/>
		</form>
		
		<a href="javascript: history.go(-1)">#springMessage("back")</a>
	</div>
</body>

</html>
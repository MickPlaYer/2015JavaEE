<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
	<title>Java54 倉儲物流</title>
</head>

<body>
	<div class="body">
		<p class="title">我的倉庫列表</p>
		
		<table class="navTable" border="1px" cellspacing="0px" cellpadding="6px">
			<tr>
				<th>倉庫序號</th>
				<th>倉庫名稱</th>
				<th>倉庫地點</th>
				<th>倉庫期限</th>
				<th>查看貨物</th>
				<th>新增貨物</th>
				<th>移除貨物</th>
			</tr>
			#foreach( $Box in $BoxList )
			<tr>
				<td style="text-align: right">$Box.id</td>
				<td>$Box.name</td>
				<td>$Box.location</td>
				<td>$Box.deadline</td>
				<td><button onclick="window.location.href='../box/$Box.id'">查看貨物</button></td>
				<td><button onclick="window.location.href='../box/addItem/$Box.id'">新增貨物</button></td>
				<td><button onclick="window.location.href='../box/removeItem/$Box.id'">移除貨物</button></td>
			</tr>
			#end
			<tr>
				<td colspan="7">
					<button onclick="window.location.href='./buyBox'">購買新的倉庫</button>
					<button onclick="window.location.href='./renewBox'">倉庫續約</button>
				</td>
			</tr>
		</table>
		<br>
		<a href="#springMessage("mainURL")">#springMessage("back")</a>
	</div>
</body>

</html>
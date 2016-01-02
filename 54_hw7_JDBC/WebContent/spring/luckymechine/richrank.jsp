<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
	<title>Lucky Mechine</title>
</head>

<body>
	<div class="body">
		<p class="title">Rich Rank</p>
		<p class="content">
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle">
					<tr>
						<th>Id</th>
						<th>Name</th>
						<th>Dollars</th>
					</tr>
					#foreach( $LuckyModel in $LuckyModelList )
					<tr>
			   			<td>$LuckyModel.id</td>
						<td>$LuckyModel.name</td>
						<td>$LuckyModel.dollars</td>
			   		</tr>
					#end
				</table>
			</center><br><br>
		</p>
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
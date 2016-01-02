<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("myAccount.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("myAccount.title")</p>
		<!-- 
		<p class="content">
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th style="text-align:left">#springMessage("owner.id")</th>
						<td style="text-align:center">$Owner.id</td>
					</tr>
					<tr>
						<th style="text-align:left">#springMessage("owner.name")</th>
						<td style="text-align:center">$Owner.name</td>
					</tr>
					<tr>
						<th style="text-align:left">#springMessage("owner.cash")</th>
						<td style="text-align:center">$Owner.cash</td>
					</tr>
					<tr>
						<th style="text-align:left">#springMessage("owner.asset")</th>
						<td style="text-align:center">$Owner.asset</td>
					</tr>
				</table>
			</center>
		</p>
		 -->
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
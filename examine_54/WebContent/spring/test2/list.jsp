<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("list.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("list.title")</p>
		<p class="content">
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th>#springMessage("list.id")</th>
						<th>#springMessage("list.name")</th>
						<th>#springMessage("list.count")</th>
					</tr>
					#foreach( $Nice in $NiceList )
					<tr>
			   			<td>$Nice.id</td>
						<td>$Nice.name</td>
						<td>$Nice.count</td>
			   		</tr>
					#end
				</table>
			</center><br><br>
		</p>
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
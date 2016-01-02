<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("carList.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("carList.title")</p>
		<p class="content">
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th>#springMessage("carList.carId")</th>
						<th>#springMessage("carList.carName")</th>
						<th>#springMessage("carList.carPrice")</th>
						<th>#springMessage("carList.ownerId")</th>
					</tr>
					#foreach( $Car in $CarList )
					<tr>
			   			<td>$Car.id</td>
						<td>$Car.name</td>
						<td>$Car.price</td>
						<td>$Car.ownerId</td>
			   		</tr>
					#end
				</table>
			</center>
		</p>
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
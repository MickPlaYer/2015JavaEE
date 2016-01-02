<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("myCars.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<script>
	var ownerId;
	var carCount = 0;
</script>

#foreach( $Car in $CarList )
<script>
	ownerId = $Car.ownerId;
	carCount++;
</script>
#end

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("myCars.title")</p>
		
		<p class="content">
			#springMessage("owner.id") = <script>document.write(ownerId);</script><br>
			#springMessage("owner.count") = <script>document.write(carCount);</script><br>
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th>#springMessage("car.id")</th>
						<th>#springMessage("car.name")</th>
						<th>#springMessage("car.price")</th>
					</tr>
					#foreach( $Car in $CarList )
					<tr>
			   			<td>$Car.id</td>
						<td>$Car.name</td>
						<td>$Car.price</td>
			   		</tr>
					#end
				</table>
			</center>
		</p>
		
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
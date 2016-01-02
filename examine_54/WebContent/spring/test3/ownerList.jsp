<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>#springMessage("ownerList.title")</title>
	<link href="../../css/snow.css" rel="stylesheet" type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
	<script src="../../javascript/jquery.snow.min.1.0.js"></script>
	<script>
		$(document).ready(function(){$.fn.snow();});
	</script>
</head>

<script>
	var ownerCount = 0;
	var cash = 0;
	var asset = 0;
	var carCount = 0;
</script>

#foreach( $Owner in $OwnerList )
<script>
	ownerCount++;
	cash += $Owner.cash;
	asset += $Owner.asset;
	carCount += $Owner.count;
</script>
#end

<body class="snow">
	<div class="body">
		<p class="title">#springMessage("ownerList.title")</p>
		<p class="content">
			<center>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th style="text-align:left">#springMessage("ownerList.ownerCount")</th>
						<td style="text-align:center">
							<script>document.write(ownerCount);</script>#springMessage("unit.owner")
						</td>
					</tr>
					<tr>
						<th style="text-align:left">#springMessage("ownerList.cash")</th>
						<td style="text-align:center">
							<script>document.write(cash);</script>#springMessage("unit.money")
						</td>
					</tr>
					<tr>
						<th style="text-align:left">#springMessage("ownerList.asset")</th>
						<td style="text-align:center">
							<script>document.write(asset);</script>#springMessage("unit.money")
						</td>
			   		</tr>
			   		<tr>
						<th style="text-align:left">#springMessage("ownerList.carCount")</th>
						<td style="text-align:center">
							<script>document.write(carCount);</script>#springMessage("unit.car")
						</td>
			   		</tr>
				</table><br>
				<table border="1px" cellspacing="0px" cellpadding="6px" valign="middle" style="color:FFFFFF;">
					<tr>
						<th>#springMessage("owner.id")</th>
						<th>#springMessage("owner.name")</th>
						<th>#springMessage("owner.cash")</th>
						<th>#springMessage("owner.asset")</th>
						<th>#springMessage("owner.count")</th>
					</tr>
					#foreach( $Owner in $OwnerList )
					<tr>
			   			<td>$Owner.id</td>
						<td>$Owner.name</td>
						<td>$Owner.cash</td>
						<td>$Owner.asset</td>
						<td>$Owner.count</td>
			   		</tr>
					#end
				</table>
			</center><br><br>
		</p>
		<a href="#springMessage("homeURL")">#springMessage("home")</a>
	</div>
</body>
</html>
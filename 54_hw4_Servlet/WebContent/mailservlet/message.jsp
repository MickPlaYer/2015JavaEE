<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Hello JSP</title>
	<link href="../css/web.css" rel="stylesheet" type="text/css">
</head>

<body>
	<div class="body">
		<p class="message">
			<!-- Get message result from servlet. -->
			${requestScope.result}
		</p>
		<p class="stamp">
			<!-- Get mail stamp from servlet. -->
			${requestScope.stamp}
		</p>
		<a href="../index.html">Home</a>
	</div>
</body>
</html>
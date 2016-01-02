<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pick Car</title>
	<link href="../../css/web.css" rel="stylesheet" type="text/css">
</head>

<body>
	<div class="body">
		<p class="title">You pick a car.</p>
		<p class="content">
			${requestScope.name}
		</p>
		<a href="../../index.html">Home</a>
	</div>
</body>
</html>
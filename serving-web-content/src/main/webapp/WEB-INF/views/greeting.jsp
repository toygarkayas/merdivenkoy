<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <p th:text="'Hello, ' + ${name} + '!'" />
    <div class="folders">
    	<div class = "dev"><strong>DEV</strong><br></div>
    	<div class = "prod"><strong>PROD</strong><br></div>
    	<div class = "stable"><strong>STABLE</strong><br></div>
    	<div class = "stage"><strong>STAGE</strong><br></div>
    </div>
    <div class="events"></div>
	<script src="/resources/js/index.js"></script>
</body>
</html>
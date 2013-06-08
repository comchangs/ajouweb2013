<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>My Search</title>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" charset="utf-8" />
  <script src="http://code.jquery.com/jquery-1.9.1.js" charset="utf-8"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" charset="utf-8"></script>
  </head>
<body>
<div id="wrapper">
	<div id="menu-wrapper">
	</div>
	<div id="logo" class="container">
		<h1><a href="#">My Search</a></h1>
	</div>
	<div id="page" class="container">
		<div id="content">
			  
 			<div id="login_box">
				<form action="./Login" method="post">
					<input id="mode" name="mode" type="hidden" value="login" />
					<label class="block login_label" for="userId">ID</label>
					<input class="block login_input" id="userId" name="userId" type="text" /><br />
					<label class="block login_label" for="password">PW</label>
					<input class="block login_input" id="password" name="password" type="password" /><br />
					<input class="block button login_button" type="submit" value="Login" /><br />
					<input class="block button login_button" type="button" value="Join" onclick="javascript:location.href='./JoinForm'" />
				</form>
			</div>
  
 
		</div>
	</div>

</div>
<div id="footer">
	<p>© 2013 Ajou Webprogramming - Project team MySearch. All rights reserved.</p>
</div>
</body>
</html>

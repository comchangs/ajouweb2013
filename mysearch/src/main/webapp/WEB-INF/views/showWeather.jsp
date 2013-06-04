<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shows weather</title>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
var asdf = 'qwer';
	$(document).ready(function() {
		$.getJSON('http://smart-ip.net/geoip-json?callback=?', function(ip) {
			$.getJSON('http://query.yahooapis.com/v1/public/yql?q=select%20forecastList%20from%20json%20where%20url%3D%22http%3A%2F%2Fi.wxbug.net%2FREST%2FDirect%2FGetForecast.ashx%3Fla%3D' + ip.latitude + '%26lo%3D' + ip.longitude + '%26nf%3D1%26units%3D1%26api_key%3Dvxwdyz3evgtvuv9d5e53sckc%26_%3D1370388189457%22&format=json&diagnostics=true&callback=', function(weather) {
				alert(weather.query.results.json.forecastList.dayDesc);
			});
		});
	});
</script>
</head>
<body>
	<div id="weather"></div>
</body>
</html>
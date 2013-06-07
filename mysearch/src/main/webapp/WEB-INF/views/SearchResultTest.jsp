<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$.getJSON('http://smart-ip.net/geoip-json?callback=?', function(ip) {
			$.getJSON("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20xml%20where%20url%3D'http%3A%2F%2Fapi.wxbug.net%2FgetLiveWeatherRSS.aspx%3FACode%3DA5350497592%26lat%3D" + ip.latitude + "%26long%3D" + ip.longitude + "%26unittype%3D1'&format=json&callback=", function(weather) {
				if(weather.query.results.rss.channel.weather.ob['ob-date'].hour['hour-24'] < 6 || weather.query.results.rss.channel.weather.ob['ob-date'].hour['hour-24'] > 18) {
					$('body').css("backgroundImage", "url('http://farm9.staticflickr.com/8248/8505945175_1f109030c3_h.jpg')");
				} else {
					$('body').css("backgroundImage", "url('http://farm8.staticflickr.com/7114/7429037498_a9b383ff2e_h.jpg')");
				}
				var imgStr = weather.query.results.rss.channel.weather.ob['current-condition'].icon;
				$('#weather').append("<img src=http://img.weather.weatherbug.com/forecast/icons/localized/105x88/en/trans/cond" + imgStr.substr(imgStr.length - 7, 3) + ".png>");
				$('#weather').append(" " + weather.query.results.rss.channel.weather.ob['current-condition'].content + "<br />");
				$('#weather').append("Temperature: " + weather.query.results.rss.channel.weather.ob.temp.content + "℃<br />");
				$('#weather').append("Humidity: " + weather.query.results.rss.channel.weather.ob.humidity.content + "%<br />");
			});
		});
	});
</script>
</head>
<body>
	<form method="get" action="SearchResult">
		Search String :<input type="text" name="searchKeyword" />
		<input type="submit" value="Search"/>
	</form>
	<div id="weather" style="background-color:white"></div>
	<c:forEach var="bookmarkList" items="${userBookmarkList }">
		<div id="bookmarkList">
			${bookmarkList }<br />
		</div>
	</c:forEach>
</body>
</html>
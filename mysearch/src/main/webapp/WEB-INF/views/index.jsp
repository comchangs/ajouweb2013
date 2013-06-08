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
  <script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$.getJSON('http://smart-ip.net/geoip-json?callback=?', function(ip) {
			$.getJSON("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20xml%20where%20url%3D'http%3A%2F%2Fapi.wxbug.net%2FgetLiveWeatherRSS.aspx%3FACode%3DA5350497592%26lat%3D" + ip.latitude + "%26long%3D" + ip.longitude + "%26unittype%3D1'&format=json&callback=", function(weather) {
				if(weather.query.results.rss.channel.weather.ob['ob-date'].hour['hour-24'] < 6 || weather.query.results.rss.channel.weather.ob['ob-date'].hour['hour-24'] > 18) {
					$('#page').css("backgroundImage", "url('http://farm9.staticflickr.com/8248/8505945175_1f109030c3_h.jpg')");
				} else {
					$('#page').css("backgroundImage", "url('http://farm8.staticflickr.com/7114/7429037498_a9b383ff2e_h.jpg')");
				}
				var imgStr = weather.query.results.rss.channel.weather.ob['current-condition'].icon;
				$('#weather').append("<img src=http://img.weather.weatherbug.com/forecast/icons/localized/105x88/en/trans/cond" + imgStr.substr(imgStr.length - 7, 3) + ".png><br />");
				$('#weather').append(" " + weather.query.results.rss.channel.weather.ob['current-condition'].content + "<br />");
				$('#weather').append("Temperature: " + weather.query.results.rss.channel.weather.ob.temp.content + "℃<br />");
				$('#weather').append("Humidity: " + weather.query.results.rss.channel.weather.ob.humidity.content + "%<br />");
			});
		});
	});
</script>
  <script charset="utf-8">
  $(function() {
    $( ".column" ).sortable({
      connectWith: ".column"
    });
 
    $( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
      .find( ".portlet-header" )
        .addClass( "ui-widget-header ui-corner-all" )
        .prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
        .end()
      .find( ".portlet-content" );
 
    $( ".portlet-header .ui-icon" ).click(function() {
      $( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
      $( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
    });
 
    $( ".column" ).disableSelection();
  });
  </script>
  <script type="text/javascript" charset="utf-8">
 $(function() {
  $( "#searchKeyword" ).autocomplete({
   minLength: 1,
   source: function( request, response ) {
    $.ajax({
     url: "autocomplete",
     dataType: "json",
     data: "keyword=" + $("#searchKeyword").val(),
     success: function( data ) {
      response( $.map( data, function( item ) {
       return {
        label: item.label,
        value: item.value
       };
      }));
     }
    });
   },
   focus: function( event, ui )
   {
    $('#searchKeyword').val( ui.item.label );
    return false;
   },
  });
 });
 </script>
 
 
</head>
<body>
<div id="wrapper">
	<div id="menu-wrapper">
		<div id="logout"><input class="button logout" type="button" value="Logout" onclick="javascript:location.href='./?mode=logout'" /></div>
	</div>
	<div id="logo" class="container">
		<h1><a href="#">My Search</a></h1>
		<form method="get" action="SearchResult">
				<input type="text" id="searchKeyword" name="searchKeyword" value="${searchKeyword }" />
				<input type="submit" value="Search"/>
			</form>
	</div>
	<div id="page" class="container">
		<div id="content">
			<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Weather</div>
    <div class="portlet-content">
   		 <div id="weather" style="background-color:white"></div>
	</div>
  </div>
 
  
 
</div>
 
<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Time</div>
    <div class="portlet-content">
    <div id="time"></div>
    <script>
      window.onload = function(){show_time()};  
</script>
<script>
/* ********** 시계 보여주기 ********** */
function show_time(){
     var time = document.getElementById( "time" );
     // 1000ms 단위로 시간 갱신 시키기
     window.setInterval("get_current_time()", 1000); 
     get_current_time();
}


/* ********** 현재 시간 가져오기 ********** */
function get_current_time(){
     var time = document.getElementById( "time" );
     var now = new Date();
     var week = new Array("일","월","화","수","목","금","토");

     var clock = now.getFullYear() + "년 ";
     clock += (now.getMonth() + 1) + "월 ";
     clock += now.getDate() + "일 ";
     clock += week[now.getDay()] + "요일 ";

     clock += now.getHours() + "시 ";
     clock += now.getMinutes() + "분 ";
     clock += now.getSeconds() + "초 ";

     time.innerHTML= clock;
}

/* 즐겨찾기*/
function removeBookmark(removeUrl)
{
	var url = "./removeBookmark" + "?bookmarkUrl=" + removeUrl;
	location.href = url;
}
</script>
</div>
  </div>
 
</div>
 
<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Bookmark</div>
    <div class="portlet-content">
   		<c:forEach var="bookmarkList" items="${userBookmarkList }">
			<div id="bookmarkList">
			<button onclick="javascript:removeBookmark('${bookmarkList.url }');">
				<img src="${pageContext.request.contextPath}/resources/images/bookmarkOn.png"></img>
			</button>	
				${bookmarkList.name } <a href="${bookmarkList.url }" target="_blank">${bookmarkList.url }</a><br />
			</div>
		</c:forEach>
	</div>
  </div>
  
 
  
 
</div>
			<div style="clear: both;">&nbsp;</div>
		</div>
	</div>

</div>
<div id="footer">
	<p>© 2013 Ajou Webprogramming - Project team MySearch. All rights reserved.</p>
</div>
</body>
</html>

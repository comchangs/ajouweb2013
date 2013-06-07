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
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/SearchResultPaging.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/SearchResult.css" media="screen"/>
<script src="${pageContext.request.contextPath}/resources/js/d3.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/d3.layout.cloud.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/SearchResult.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.paginate.js"></script>

</head>
<body>
<div id="wrapper">
	<div id="menu-wrapper">
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
			<div class="post">
			<script>
	var data = ${cloud};
	var w = 1200,
		h = 200;
	var r = 40.5,
		d = 90;
	var fill = d3.scale.category20();

  
  d3.layout.cloud()
  .timeInterval(10)
  .size([w, h])
      .words(data.map(function(d) {
        return {text: d.relation_keyword, size: 20 + d.count * 3};
      }))
      .rotate(function() { return ~~(Math.random() * 6) * 60 * (Math.random()*6); })
      .font("Impact")
      .fontSize(function(d) { return d.size; })
      .on("end", draw)
      .start();
  
  function draw(words) {
	  var text = d3.select("#vis").append("svg")
	  .attr("width", w)
        .attr("height", h)
      .append("g")
        .attr("transform", "translate(" + [w >> 1, h >> 1] + ")").selectAll("text")
      .data(words, function(d) { return d.text.toLowerCase(); });
  text.transition()
      .duration(1000)
      .attr("transform", function(d) { return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")"; })
      .style("font-size", function(d) { return d.size + "px"; });
  text.enter().append("text")
      .attr("text-anchor", "middle")
      .attr("transform", function(d) { return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")"; })
      .style("font-size", function(d) { return d.size + "px"; })
      .on("click", function(d) {
        load(d.text);
      })
      .style("opacity", 1e-6)
    .transition()
      .duration(1000)
      .style("opacity", 1);
  text.style("font-family", function(d) { return d.font; })
      .style("fill", function(d) { return fill(d.text.toLowerCase()); })
      .text(function(d) { return d.text; });
  }
  
  function load(d) {
	  window.location.href = '?searchKeyword='+d;
	}
</script>
<div id="vis"></div>
				<h2 class="title"><a href="#">검색결과: ${numFound }개</a></h2>
				<div class="entry">
				<div id="DivResult">
					<c:forEach var="result" items="${resultList }">
				<div id="DivResultList">
					<a href="${result.url }">${result.title }</a><br />
					${result.content }<br />
					<a href="${result.url }">${result.url }</a>
					<div id="DivBookmark${result.index}">
					</div>
				</div>
				<br />
				<script type="text/javascript">
				
				if("${result.bookmark}" == "true")
				{
					var text = "removeBookmark(" + "${searchKeyword}" + "," + "${start}" + "," + "\"${result.url}\"" + ")";
					button = document.createElement("button");
					button.setAttribute("onclick", text);
					img = document.createElement("img");
					img.setAttribute("src", "${pageContext.request.contextPath}" + "/resources/images/bookmarkOn.png");
					button.appendChild(img);
					
			 		document.getElementById("DivBookmark${result.index}").appendChild(button);
				}
				else if("${result.bookmark}" == "false")
				{

					var text = "addBookmark(" + "\"${searchKeyword}\"" + "," + "${start}" + "," + "\"${result.url}\"" + ")";
					button = document.createElement("button");
					button.setAttribute("onclick", text);
					img = document.createElement("img");
					img.setAttribute("src", "${pageContext.request.contextPath}" + "/resources/images/bookmarkOff.png");
					button.appendChild(img);

			 		document.getElementById("DivBookmark${result.index}").appendChild(button);
				}
				function addBookmark(searchKeyword, start, bookmarkUrl)
				{
					var url = "" + "?searchKeyword=" + searchKeyword + "&start=" + start + "" + "&bookmarkUrl=" + bookmarkUrl + "&bookmarkSelect=" + "add";
					location.href = url; 
				}

				function removeBookmark(searchKeyword, start, bookmarkUrl)
				{
					var url = "" + "?searchKeyword=" + searchKeyword + "&start=" + start + "" + "&bookmarkUrl=" + bookmarkUrl + "&bookmarkSelect=" + "remove";
					location.href = url;
				}
		 		</script>
			</c:forEach>
			</div>
			<div id="DivPaging">
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

	<script type="text/javascript">
		$(document).ready(function() {
			if("${numFound }" > 1)
				$("#DivPaging").paginate({
					count 		: Math.ceil("${numFound }"/10),
					start 		: Math.floor("${start}"/10) + 1,
					display     : 10,
					border					: true,
					border_color			: '#BEF8B8',
					text_color  			: '#68BA64',
					background_color    	: '#E3F2E1',	
					border_hover_color		: '#68BA64',
					text_hover_color  		: 'black',
					background_hover_color	: '#CAE6C6', 
					rotate      : false,
					images		: false,
					mouse		: 'press',
					onChange     			: function(page){
						var url = "" + "?searchKeyword=" + "${searchKeyword}" + "&start=" + ((page-1) * 10);
												$(location).attr('href', url); 
											  }
			});
		});
    </script>

</body>
</html>






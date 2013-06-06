<?xml version="1.0" encoding="utf-8" ?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Word Cloud Generator</title>
<script src="${pageContext.request.contextPath}/resources/js/d3.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/d3.layout.cloud.js"></script>
</head>
<body>
<script>
	var data = ${json_data};
	var w = 960,
		h = 300;
	var r = 40.5,
		d = 90;
	var fill = d3.scale.category20();

  
  d3.layout.cloud()
  .timeInterval(10)
  .size([w, h])
      .words(data.map(function(d) {
        return {text: d.relation_keyword, size: 20 + d.count * 5};
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
	  window.location.href = '?keyword='+d;
	}
</script>
<div id="vis"></div>
</body>
</html>
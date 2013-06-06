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
	var w = 960,
		h = 300;
	
	var fill = d3.scale.category20();

  
  d3.layout.cloud()
  .timeInterval(10)
  .size([w, h])
      .words(${json_data}.map(function(d) {
        return {text: d.relation_keyword, size: 20 + d.count * 5};
      }))
      .rotate(function() { return ~~(Math.random() * 2) * 60; })
      .font("Impact")
      .fontSize(function(d) { return d.size; })
      .on("end", draw)
      .start();

  function draw(words) {
    d3.select("#vis").append("svg")
        .attr("width", w)
        .attr("height", h)
      .append("g")
        .attr("transform", "translate(" + [w >> 1, h >> 1] + ")")
      .selectAll("text")
        .data(words)
      .enter().append("text")
        .style("font-size", function(d) { return d.size + "px"; })
        .style("font-family", "Impact")
        .style("fill", function(d, i) { return fill(i); })
        .attr("text-anchor", "middle")
        .attr("transform", function(d) {
          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) { return d.text; });
  }
</script>
<div id="vis"></div>
</body>
</html>
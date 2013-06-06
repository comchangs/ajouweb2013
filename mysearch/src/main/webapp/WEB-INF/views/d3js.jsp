<?xml version="1.0" encoding="utf-8" ?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Word Cloud Generator</title>

<link href="${pageContext.request.contextPath}/resources/css/cloud.css" rel="stylesheet">
<link rel="image_src" href="${pageContext.request.contextPath}/resources/images/amazing.png">
</head>
<body>
<div id="vis"></div>

<form id="form">

  <p style="position: absolute; right: 0; top: 0" id="status"></p>

  <div style="text-align: center">
    <div id="presets"></div>
    <p><label for="keyword">Keyword:</label>
      <input type="text" id="keyword" value="">
      <button id="go" type="submit">Go!</button>
    <div id="custom-area">
      <p><label for="text">Enter a URL below, or paste some text. If the URL
        contains <code>{word}</code>, it will automatically be replaced with
        the current keyword.</label>
      <p><textarea id="text"></textarea>
    </div>
  </div>

<hr>

<div style="float: right; text-align: right">
  <p><label for="max">Number of words:</label> <input type="number" value="250" min="1" id="max">
  <!--<p><label for="colours">Colours:</label> <a href="#" id="random-palette">get random palette</a>-->
  <p><label>Download:</label>
    <a id="download-svg" href="#" target="_blank">SVG</a> |
    <a id="download-png" href="#" target="_blank">PNG</a>
</div>

<div style="float: left">
  <p><label>Spiral:</label>
    <label for="archimedean"><input type="radio" name="spiral" id="archimedean" value="archimedean" checked="checked"> Archimedean</label>
    <label for="rectangular"><input type="radio" name="spiral" id="rectangular" value="rectangular"> Rectangular</label>
  <p><label for="scale">Scale:</label>
    <label for="scale-log"><input type="radio" name="scale" id="scale-log" value="log" checked="checked"> log n</label>
    <label for="scale-sqrt"><input type="radio" name="scale" id="scale-sqrt" value="sqrt"> √n</label>
    <label for="scale-linear"><input type="radio" name="scale" id="scale-linear" value="linear"> n</label>
  <p><label for="font">Font:</label> <input type="text" id="font" value="Impact">
</div>

<div id="angles">
  <p><input type="number" id="angle-count" value="5" min="1"> <label for="angle-count">orientations</label>
    <label for="angle-from">from</label> <input type="number" id="angle-from" value="-60" min="-90" max="90"> °
    <label for="angle-to">to</label> <input type="number" id="angle-to" value="60" min="-90" max="90"> °
</div>

<hr style="clear: both">

<p style="float: right"><a href="about/">How the Word Cloud Generator Works</a>.
<p>Copyright &copy; <a href="http://www.jasondavies.com/">Jason Davies</a> 2012.

</form>
<script src="${pageContext.request.contextPath}/resources/js/d3.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/d3.layout.cloud.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/cloud.js"></script>
</body>
</html>
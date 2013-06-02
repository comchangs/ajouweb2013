<?xml version="1.0" encoding="utf-8" ?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<script>
var script = document.createElement("script");
script.src = decodeURIComponent(location.search.substring(1)) + "&callback=callback";
document.body.appendChild(script);

function callback(json) {
  parent.postMessage(JSON.stringify(json), "*");
}
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="DivSearch">
		<form method="post" action="SearchResult">
			Search String :<input type="text" name="searchKeyword" value="${searchKeyword }" />
			<input type="submit" value="Search"/>
		</form>
	</div>
	<div id="DivResult">
		<c:forEach var="result" items="${resultList }">
			<div id="DivResultList">
				<a href="${result.url }">${result.title }</a><br />
				${result.content }<br />
				<a href="${result.url }">${result.url }</a>
			</div>
			<br />
		</c:forEach>
	</div>
	<div id="DivNext">
		
	</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/SearchResult.js"></script>
</html>
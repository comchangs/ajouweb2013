<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css" media="screen"/>
<title>Insert title here</title>
</head>

<body>
	<div id="DivSearch">
		<form method="get" action="SearchResult">
			Search String :<input type="text" name="searchKeyword" value="${searchKeyword }" />
			<input type="submit" value="Search"/>
		</form>
	</div>
	<div id="DivResult">
		<div id="DivNumFound">검색결과 ${numFound }개</div>
		<c:forEach var="result" items="${resultList }">
			<div id="DivResultList">
				<a href="${result.url }">${result.title }</a><br />
				${result.content }<br />
				<a href="${result.url }">${result.url }</a>
			</div>
			<br />
		</c:forEach>
	</div>
	<div id="DivPaging">
	</div>
<!-- 	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/SearchResult.js"></script> -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.paginate.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#DivPaging").paginate({
				count 		: Math.ceil("${numFound }"/10),
				start 		: Math.floor("${start}"/10) + 1,
				display     : 10,
				border					: false,
				text_color  			: '#79B5E3',
				background_color    	: 'none',	
				text_hover_color  		: '#2573AF',
				background_hover_color	: 'none', 
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
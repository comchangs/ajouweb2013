<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/SearchResultPaging.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/SearchResult.css" media="screen"/>
<title>Insert title here</title>
</head>

<body>
	<div id="DivSearchResult">
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/SearchResult.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.paginate.js"></script>
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
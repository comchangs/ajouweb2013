<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert URL for test</title>
</head>
<body>
	<h2>Insert URL to parse(for test)</h2>
	<form method="post" action="parseHtml">
		<table>
			<tr>
				<td><label for="keyword">keyword</label></td>
				<td><input type="text" name="keyword"></td>
			</tr>
			<tr>
				<td><label for="url">Url</label></td>
				<td><input type="text" name="url" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="parse url" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
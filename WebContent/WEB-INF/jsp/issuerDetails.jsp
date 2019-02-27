<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<table>
<c:forEach items="${details}" var="columnMap">
	<tr>
		<c:forEach items="${columnMap}" var="entry">
			<th>${entry.key}</th>
		</c:forEach>
	</tr>
	<tr>
		<c:forEach items="${columnMap}" var="entry">
			${entry.value}
		</c:forEach>
	</tr>
</c:forEach>
</table>

</body>
</html>
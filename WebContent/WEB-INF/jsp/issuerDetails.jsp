<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript">
	var _formConfirm_submitted = false;
</script>

<title>Issuer Details</title>
</head>
<body>

<form action="getIssuerDetails" method="post" onsubmit="if( _formConfirm_submitted == false ){ _formConfirm_submitted = true;return true }else{ alert('Please Wait... Your request is being processed!!!'); return false;  }">
	<table>
				<tr>
					<td>BankDirName</td>
					<td><input type="text" name="bankdirname" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="Get Details" /></td>
				</tr>
	</table>
</form>

<p style="color:red">${msg}</p>

<!-- Callout Details -->
<c:if test="${!empty headers_callouts}">
<table border="1">
<tr>
	<c:forEach items="${headers_callouts}" var="headers">
		<th>${headers}</th>
	</c:forEach>
</tr>
<c:forEach items="${list_callouts}" var="columnMap">
<tr>
	<c:forEach items="${columnMap}" var="entry">
		<td>${entry.value}</td>
	</c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

<!-- RA Details -->
<c:if test="${!empty headers_ra}">
<table border="1">
<tr>
	<c:forEach items="${headers_ra}" var="headers">
		<th>${headers}</th>
	</c:forEach>
</tr>
<c:forEach items="${list_ra}" var="columnMap">
<tr>
	<c:forEach items="${columnMap}" var="entry">
		<td>${entry.value}</td>
	</c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

<!-- CAP Details -->
<c:if test="${!empty headers_cap}">
<table border="1">
<tr>
	<c:forEach items="${headers_cap}" var="headers">
		<th>${headers}</th>
	</c:forEach>
</tr>
<c:forEach items="${list_cap}" var="columnMap">
<tr>
	<c:forEach items="${columnMap}" var="entry">
		<td>${entry.value}</td>
	</c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

<!-- Content Details -->
<c:if test="${!empty headers_contents}">
<table border="1">
<tr>
	<c:forEach items="${headers_contents}" var="headers">
		<th>${headers}</th>
	</c:forEach>
</tr>
<c:forEach items="${list_contents}" var="columnMap">
<tr>
	<c:forEach items="${columnMap}" var="entry">
		<td>${entry.value}</td>
	</c:forEach>
</tr>
</c:forEach>
</table>
</c:if>

</body>
</html>
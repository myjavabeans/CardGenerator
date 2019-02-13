<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<style type="text/css">

#frame { width: 100%; height: 100%; 
        border: 1px solid black; position:relative; }
#frame {
zoom: 0.75;
-moz-transform: scale(0.75);
-moz-transform-origin: 0 0;
-o-transform: scale(0.75);
-o-transform-origin: 0 0;
-webkit-transform: scale(0.75);
-webkit-transform-origin: 0 0;
}

</style>

<title>Card Generator</title>
</head>
<body>

	<p>The time on the server is ${serverTime}. Author - Anjan</p>

	<table border="1">
		<tr>
			<td><a href="<c:url value='/validate'/>">Validate Card Number</a></td>
			<td><a href="<c:url value='/generate'/>">Generate Card Number</a></td>
			<td><a href="<c:url value='/decode'/>">Base64 Decoder</a></td>
		</tr>
	</table>
	
	<c:if test="${!empty cardGen}">
		<h2>Generate Mod10 Card Numbers</h2>
		<form action="generateCard" method="post">
			<table>
				<tr>
					<td>Begin Range</td>
					<td><input type="text" name="beginRange" /></td>
				</tr>
				<tr>
					<td>End Range</td>
					<td><input type="text" name="endRange" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="Generate" /></td>
				</tr>
			</table>
		</form>
	</c:if>
	
	<c:if test="${!empty valCard}">
		<h2>Validate Mod10 Card Number</h2>
		<form action="validateCard" method="post">
			<table>
				<tr>
					<td>Card Number</td>
					<td><input type="text" name="beginRange" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="Validate" /></td>
				</tr>
			</table>
		</form>
	</c:if>
	
	<c:if test="${!empty decImg}">
		<h2>Decode Base64 String</h2>
		<form action="decode" method="post">
			<table>
				<tr>
					<td>Enter Base64 String</td>
					<td><input type="text" name="baseStr" /></td>
				</tr>
				<tr>
					<td>Select Extension</td>
					<td>
						<select name="extnType">
							<option value="png">png</option>
							<option value="jpeg">jpeg</option>
							<option value="jpg">jpg</option>
							<option value="gif">gif</option>
							<option value="html">html</option>
							<option value="htm">htm</option>
							<option value="txt">txt</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="Decode String" /></td>
				</tr>
			</table>
		</form>
		
		<c:if test="${!empty fileLink}">
		
			<c:if test="${isImage == 'true' }">
				Decoded Image
				<p><img alt="" src="<c:url value='/fileLink?loc=${fileLink}'/>"></p>
			</c:if>
			<c:if test="${isImage == 'false' }">
				<p><a href="<c:url value='/fileLink?loc=${fileLink}'/>" target="_blank">Click to View Decoded File</a></p>
			</c:if>
			
		</c:if>
		
	</c:if>
	
	<p style="color:red">${msg}</p>
	
	<c:if test="${!empty cardNumbers}">
		<h2>Card Numbers</h2>

		<table>
			<c:forEach items="${cardNumbers}" var="cardNumber">
				<tr>
					<td>${cardNumber}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	
</body>
</html>
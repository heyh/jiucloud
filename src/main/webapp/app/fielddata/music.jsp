<%@ page import="sy.util.PropertyUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${v}</title>
</head>
<body style="background-color: #dddddd; text-align: center;">
	<div
		style="width: 600px; margin-left: auto; margin-right: auto; margin-top: 200px;">
<%
	sy.util.GetRealPath grp = new sy.util.GetRealPath(request
			.getSession().getServletContext());
%>
		<%-- <audio src="${pageContext.request.contextPath }/upload/source/${v}"
			controls="controls">
		</audio> --%>
		<embed src="<%= PropertyUtil.getFilePathHome()%>upload/source/${v}"  autoplay="false"></embed>
	</div>
</body>
</html>
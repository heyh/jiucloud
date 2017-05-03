<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="sy.util.PropertyUtil" pageEncoding="UTF-8"%>
<%
	String pdfViewPath = PropertyUtil.getFilePathHome() + "/web/viewer.html?file=../upload/pdfsource/" +  request.getAttribute("pdfPath");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>


</head>
<body>
<div style="text-align:center;">
    <iframe src="<%= pdfViewPath %>" width="100%" height="800"></iframe>
</div>
</body>
</html>

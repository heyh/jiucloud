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
    <%--add by heyh--%>
    <style type="text/css">
        <!--
        @media (min-width: 992px) {
            #pic img{
                width: 992px;
            }
        }
        @media (max-width: 992px) {
            #pic img{
                width: 780px;
            }
        }
        -->
    </style>

</head>
<body style="background-color: #cccccc; text-align: center;">
    <%-- modify by heyh --%>
    <div id="pic">
        <img
            src="<%= PropertyUtil.getFilePathHome() %>/upload/source/${v}"
            alt="<%= PropertyUtil.getFilePathHome() %>/upload/source/${v}" />
    </div>
</body>
</html>
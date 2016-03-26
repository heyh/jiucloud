<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="sy.util.PropertyUtil"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.OutputStream"%>
<%
    String filePath = PropertyUtil.getFileRealPath() + "/upload/source/";
    String fileName = request.getParameter("name");
    String realFile = filePath + fileName;
    fileName = fileName.substring(fileName.indexOf("/")+1);

    String realFileName = request.getParameter("realFileName");

    response.reset();
    response.setContentType("application/x-download;");
    response.setHeader("Content-disposition", "attachment; filename=\"" + new String(realFileName.getBytes("utf-8"), "ISO8859-1")+ "\"");

	OutputStream outp = null;
	FileInputStream in = null;
	try {
		outp = response.getOutputStream();
		in = new FileInputStream(realFile);

		byte[] b = new byte[1024];
		int i = 0;

		while ((i = in.read(b)) > 0) {
			outp.write(b, 0, i);
		}
		outp.flush();
	} catch (Exception e) {
		System.out.println("Error!");
		e.printStackTrace();
	} finally {
		if (in != null) {
			in.close();
			in = null;
		}
		if (outp != null) {
			outp.close();
			outp = null;
		}
	}
	out.clear();
	out = pageContext.pushBody();
%>
<html>
<head>
<title>下载</title>
</head>
<body>
</body>
</html>



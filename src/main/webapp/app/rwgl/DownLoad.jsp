<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.File"%>
<%
	sy.util.GetRealPath grp = new sy.util.GetRealPath(request.getSession().getServletContext());
	String sysPath = grp.getRealPath() + "upload/rwgl/";
	String fileName = request.getParameter("name");//下载apk
	fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
	String realFile = sysPath+fileName;
	response.reset();
	response.setContentType("application/x-download");
	//fileName = java.net.URLEncoder.encode(fileName,"UTF-8");  
	response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1"));

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
	out=pageContext.pushBody();
%>
<html>
<head>
<title>下载</title>
</head>
<body>
</body>
</html> 



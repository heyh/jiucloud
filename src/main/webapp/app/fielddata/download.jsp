<%@page import="java.io.*,java.util.*,java.util.zip.*"%>
<%@page import="sy.model.po.GCPo"%>
<%@page import="sy.service.GCPoServiceI"%>
<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%><%@page
	contentType="text/html;charset=UTF-8"%>




<%
	String type = request.getParameter("ty");
	String mpid = request.getParameter("mpid");
	ZipOutputStream zos = null;
	ServletOutputStream sos = null;
	try {
		response.reset();
		response.setContentType("application/x-msdownload"); //通知客户文件的MIME类型：
		String filename = "jiucloud-zipfile.zip";
		response.setHeader("Content-disposition",
				"attachment;filename=" + filename);
		sos = response.getOutputStream();
		zos = new ZipOutputStream(sos);
		ZipEntry ze = null;
		byte[] buf = new byte[2048]; //输出文件用的字节数组,每次发送2048个字节到输出流：
		int readLength = 0;
		int z = 0;
		ServletContext sc = session.getServletContext();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(ConfigUtil.getSessionInfoName());
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		GCPoServiceI g = (GCPoServiceI) ac2.getBean("gcpoServiceImpl");
		List<GCPo> lgcPo = g.getGcpoListFile(type, mpid);

		String FilePath = request.getRealPath("upload") + "/source/";
		for (GCPo gfile : lgcPo) {
			//list为存放路径的数组 循环可以得到路径和文件名
			String FileName = gfile.getSourceFilePath();
			System.out.println("File ================== :" + FilePath
					+ "\t" + FileName);
			File f = new File(FilePath + FileName);

			if (!f.exists()) {
				continue;
			}
			ze = new ZipEntry(FileName);
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(
					new FileInputStream(f));
			while ((readLength = is.read(buf, 0, 2048)) != -1) {
				zos.write(buf, 0, readLength);
			}
			is.close();
		}
	} catch (Exception ex) {
		System.out.println("Error downloadsss:" + ex.toString());
	} finally {
		if (zos != null) {
			try {
				zos.close();
			} catch (Exception ex) {
				System.out.println("Error download:" + ex.toString());
			}
		}
	}
%>
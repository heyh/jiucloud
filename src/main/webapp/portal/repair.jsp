<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@page import="sy.service.LogServiceI"%>
<%@page import="java.util.List"%>
<%@page import="sy.pageModel.Log"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<script type="text/javascript">
function loct3(){
var url = '<%=request.getContextPath()%>/rzglController/rzglmanager';
		var text = "日志管理";
		var params = {
			url : url,
			title : text,
			iconCls : 'wrench'
		}
		window.parent.ac(params);
	}
</script>
<ul style="height: 150px;">
	<%
		ServletContext sc = session.getServletContext();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(ConfigUtil.getSessionInfoName());
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		LogServiceI logServiceI = (LogServiceI) ac2
				.getBean("logServiceImpl");
		if (sessionInfo != null) {
			List<Integer> ugroup = sessionInfo.getUgroup();
			List<Log> list = logServiceI.getfindList(ugroup);
	  		if(list!=null && list.size()!=0){
			for (Log tem : list) {
				String title = tem.getTitle();
				title = title != null && title.length() > 35 ? title
						.substring(0, 35) + "..." : title;
	%>
	<li><%=title%></li>
		<%
		}
			}else{ %>
                <li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: center;">暂无信息</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>
				<li style="list-style-type: none; text-align: right;">&nbsp;</li>

    <%		
			}
		}
	%></ul>
	<ul>
	<li style="list-style-type: none; text-align: right;"><a
		onclick="loct3()" href="#">[更多]</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>
</ul>
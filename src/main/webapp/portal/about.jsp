<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@page import="java.util.List"%>
<%@page import="sy.service.impl.TaskServiceImpl"%>
<%@page import="sy.service.TaskServiceI"%>
<%@page import="sy.pageModel.Task"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>


<script type="text/javascript">
function loct(){
var url = '<%=request.getContextPath()%>/rwglController/rwglmanager';
		var text = "任务管理";
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
		TaskServiceI taskServiceI = (TaskServiceI) ac2
				.getBean("taskServiceImpl");
		if (sessionInfo != null) {
			List<Integer> ugroup = sessionInfo.getUgroup();
			List<Task> list = taskServiceI.getfindList(ugroup);
			if(list!=null && list.size()!=0){
			for (Task tem : list) {
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
	%>
	</ul>
	
	<ul>
	<%-- href="<%=request.getContextPath()%>/rwglController/rwglmanager --%>
	<li style="list-style-type: none; text-align: right;"><a
		onclick="loct()" href="#">[更多]</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>
	</ul>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@page import="sy.service.MessageTextServiceI"%>
<%@page import="java.util.List"%>
<%@page import="sy.model.po.MessageText"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<script type="text/javascript">
function locts(){
var url = '<%=request.getContextPath()%>/messageController/recMsgList';
		var text = "收件箱";
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
		MessageTextServiceI messageServiceI = (MessageTextServiceI) ac2
				.getBean("messageTextServiceImpl");
		if (sessionInfo != null) {
			String uid = sessionInfo.getId();
			List<MessageText> list = messageServiceI.getfindList(uid);
			if(list!=null && list.size()!=0){
			for (MessageText tem : list) {
				String title = tem.getTitle();
				title = title != null && title.length() > 35 ? title
						.substring(0, 35) + "..." : title;
	%>
	<li><%=title%></li>
	<%
		}
			}else{%>
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
		onclick="locts()" href="#">[更多]</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>
</ul>
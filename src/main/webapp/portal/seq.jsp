<%@page import="sy.pageModel.PageHelper"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@page import="sy.service.FieldDataServiceI"%>
<%@page import="java.util.List"%>
<%@page import="sy.pageModel.FieldData"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<script type="text/javascript">
window.onload=function(){
	/* var url = '${pageContext.request.contextPath}/fieldDataController/fieldDataShow';
	var text = "现场数据管理";
	var params = {
		url : url,
		title : text,
		iconCls : 'wrench'
	}
	window.parent.ac(params);
	parent.$.modalDialog.handler.dialog('close'); */
}

function loct4(){
		var url = '<%=request.getContextPath()%>/fieldDataController/fieldDataShow';
		var text = "现场数据";
		var params = {
			url : url,
			title : text,
			iconCls : 'wrench'
		}
		window.parent.ac(params);
	}
</script>
<ul style="height: 320px;">
	<%
		ServletContext sc = session.getServletContext();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(ConfigUtil.getSessionInfoName());
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		FieldDataServiceI fieldServiceI = (FieldDataServiceI) ac2
				.getBean("fieldDataServiceImpl");
		if (sessionInfo != null) {
			List<Integer> ugroup = sessionInfo.getUgroup();
			PageHelper ph = new PageHelper();
			ph.setPage(1);
			ph.setRows(12);
			List<FieldData> list = fieldServiceI.dataGrid(null, ph, ugroup)
					.getRows();
			if (list != null && list.size() != 0) {
				for (FieldData tem : list) {
					String title = tem.getDataName();
					title = title != null && title.length() > 35 ? title
							.substring(0, 35) + "..." : title;
	%>
	<li><%=title%> &nbsp;&nbsp;&nbsp;&nbsp;<%=tem.getCostType()%>
		&nbsp;&nbsp;&nbsp;&nbsp;<%=tem.getPrice()%> &nbsp;&nbsp;&nbsp;&nbsp;<%=tem.getUnit()%>
		&nbsp;&nbsp;&nbsp;&nbsp;<%=tem.getCount()%></li>
	<%
		}
			} else {
	%>
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
	<li style="list-style-type: none; text-align: right;"><a
		onclick="loct4()" href="#">[更多]</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>
</ul>
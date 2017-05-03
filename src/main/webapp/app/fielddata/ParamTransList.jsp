<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
	JSONArray costTree = new JSONArray();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
		costTree = sessionInfo.getCostTree();
    }

%>

<!DOCTYPE html>
<html>
<head>
<title>服务号管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/paramTransController/securi_dataGrid',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 20,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'transParamCode',
							sortOrder : 'asc',
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							columns : [ [
									{
										field : 'id',
										title : '多选框',
										width : 100,
										checkbox : true
									},
									{
										field : 'paramName',
										title : '氿上费用名称',
										width : 250

									},
									{
										field : 'transParamCode',
										title : '管理系统费用编码',
										width : 250
									},
									{
										field : 'transParamName',
										title : '管理系统费用名称',
										width : 250
									},
									{
										field : 'action',
										title : '操作',
										width : 150,
										formatter : function(value, row, index) {
											var str = '';
                                            var userId = <%= userId%>;

											str += $
													.formatString(
													'<img onclick="editFun(\'{0}\');" src="{1}" title="编辑" />',
													row.id,
													'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');

											str += '&nbsp;';
											str += $
													.formatString(
													'<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
													row.id,
														'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');

											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							}
                        });
    });

	//删除
    function deleteFun(id) {
        debugger;
        parent.$.messager
            .confirm(
                '询问',
                '您是否要删除当前附件？',
                function(b) {
                    if (b) {
                        parent.$.messager.progress({
                            title : '提示',
                            text : '数据处理中，请稍后....'
                        });
                        $
                            .ajax({
                                type : "post",
                                url : '${pageContext.request.contextPath}/paramTransController/securi_delParamTrans?',
                                data : {
                                    id : id
                                },
                                dataType : "json",
                                success : function(data) {
                                    if (data.success == true) {
                                        refreshFun();
                                    }
                                }
                            });
                    }
                });
    }

    function refreshFun() {
        $(dataGrid).datagrid('load', {});

    }

    //编辑
    function editFun(id) {
        parent.$
            .modalDialog({
                title: '编辑',
                width: 460,
                height: 300,
                href: '${pageContext.request.contextPath}/paramTransController/securi_updateParamTransPage?id=' + id,
                buttons: [{
                    text: '修改',
                    handler: function () {
                        parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                        var f = parent.$.modalDialog.handler.find('#form');
                        f.submit();
                    }
                }]
            });
    }

	// 添加
	function addFun() {
        parent.$
            .modalDialog({
                title: '编辑',
                width: 460,
                height: 300,
                href: '${pageContext.request.contextPath}/paramTransController/securi_addParamTransPage',
                buttons: [{
                    text: '新增',
                    handler: function () {
                        parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                        var f = parent.$.modalDialog.handler.find('#form');
                        f.submit();
                    }
                }]
            });
	}

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;padding: 5px">
		<a onclick="addFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'add_new'">添加</a>


	</div>
</body>
</html>
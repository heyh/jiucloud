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
	String projectInfos = null;
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
		projectInfos = sessionInfo.getProjectInfos();
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
							url : '${pageContext.request.contextPath}/featureController/securi_dataGrid',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'projectName',
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
										field : 'costType',
										title : '费用类型',
										width : 250

									},
									{
										field : 'features',
										title : '项目特征',
										width : 500

									},
									{
										field : 'action',
										title : '操作',
										width : 150,
										formatter : function(value, row, index) {
											var str = '';
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
                                $('#searchForm table').show();
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
                                url : '${pageContext.request.contextPath}/featureController/securi_del?',
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

    function batchDeleteFun() {
        var rows = dataGrid.datagrid('getChecked');
        var ids = [];
        if (rows.length > 0) {
            parent.$.messager
                .confirm(
                    '确认',
                    '您是否要删除当前选中的数据？',
                    function(r) {
                        if (r) {
                            parent.$.messager.progress({
                                title : '提示',
                                text : '数据处理中，请稍后....'
                            });
                            for (var i = 0; i < rows.length; i++) {
                                ids.push(rows[i].id);
                            }
                            $
                                .getJSON(
                                    '${pageContext.request.contextPath}/itemController/securi_bunchdelete?',
                                    {
                                        ids : ids.join(',')
                                    },
                                    function(result) {
                                        if (result.success) {
                                            dataGrid
                                                .datagrid('load');
                                            dataGrid
                                                .datagrid(
                                                    'uncheckAll')
                                                .datagrid(
                                                    'unselectAll')
                                                .datagrid(
                                                    'clearSelections');
                                        }
                                        parent.$.messager
                                            .alert(
                                                '提示',
                                                result.msg,
                                                'info');
                                        parent.$.messager
                                            .progress('close');
                                    });
                        }
                    });
        } else {
            parent.$.messager.show({
                title : '提示',
                msg : '请勾选要删除的记录！'
            });
        }
    }

    function refreshFun() {
        dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
    }

    //编辑
    function editFun(id) {
        parent.$
            .modalDialog({
                title: '编辑',
                width: 450,
                height: 350,
                href: '${pageContext.request.contextPath}/featureController/securi_updatePage?id=' + id,
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
                width: 450,
                height: 350,
                href: '${pageContext.request.contextPath}/featureController/securi_addnPage',
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

    function searchFun() {
        dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
    }
</script>
</head>
<body>

	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'',border:false" style="height: 5px; overflow: hidden;">
			<form id="searchForm">
			</form>
		</div>

		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;padding: 5px">
		<a onclick="addFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'add_new'">添加</a>
		<%--<a onclick="batchDeleteFun();" href="javascript:void(0);"--%>
		<%--class="easyui-linkbutton" data-options="plain:true,iconCls:'batdelete_new'">批量删除</a>--%>


	</div>
</body>
</html>
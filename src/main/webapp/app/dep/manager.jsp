<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<title>部门费用管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>

<script type="text/javascript">
	var dataGrid;
	//Ajax加载数据库中数据，传入rows与total
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/costController/securi_departmentGrid',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'name',
							sortOrder : 'asc',
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							columns : [ [
									{
										field : 'name',
										title : '部门名称',
										width : 400,
									},
									{
										field : 'action',
										title : '操作',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
											str += $
													.formatString(
															'<img onclick="editCostFun(\'{0}\');" src="{1}" title="编辑费用类型"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/eye.png');
											str += $
													.formatString(
															' <img onclick="addCostFun(\'{0}\');" src="{1}" title="添加费用类型"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/bell_add.png');
											str += '&nbsp;';
											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							},
							onRowContextMenu : function(e, rowIndex, rowData) {
								e.preventDefault();
								$(this).datagrid('unselectAll').datagrid(
										'uncheckAll');
								$(this).datagrid('selectRow', rowIndex);
							}
						});
	});

	//查看
	function editCostFun(id) {
		parent.$
				.modalDialog({
					title : '编辑费用类型',
					width : 550,
					height : 520,
					href : '${pageContext.request.contextPath}/costController/securi_todepartment_list?id='
							+ id,
					buttons : [ {
						text : '关闭',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}

	function addCostFun(id) {
		parent.$
				.modalDialog({
					title : '添加费用类型',
					width : 550,
					height : 520,
					href : '${pageContext.request.contextPath}/costController/securi_todepartment_costlist?id='
							+ id,
					buttons : [ {
						text : '关闭',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}
</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<!-- 显示表格内容，list -->
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>

	<!-- toolBar -->
	<div id="toolbar" style="display: none;"></div>
</body>
</html>
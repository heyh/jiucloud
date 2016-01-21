<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<title>工程名称管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>

<script type="text/javascript">
	var dataGrid;
	//Ajax加载数据库中数据，传入rows与total
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/priceController/securi_dataGrid',
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
										title : '分类名称',
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
															'<img onclick="editFun(\'{0}\');" src="{1}" title="编辑条目"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
											str += '&nbsp;';
											str += $
													.formatString(
															'<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
											str += '&nbsp;';
											str += $
													.formatString(
															'<img onclick="editCostFun(\'{0}\');" src="{1}" title="编辑费用类型"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/gth-blue.png');
											str += $
													.formatString(
															' <img onclick="addCostFun(\'{0}\');" src="{1}" title="添加费用类型"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/zengjiaxiang-blue.png');
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

	//删除
	function deleteFun(id) {
		parent.$.messager
				.confirm(
						'询问',
						'您是否要删除当前分类？',
						function(b) {
							if (b) {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								$
										.ajax({
											type : "post",
											url : '${pageContext.request.contextPath}/priceController/securi_delete',
											data : {
												id : id
											},
											dataType : "json",
											success : function(data) {
												if (data.success == true) {
													searchFun();
												}
											}
										});
							}
						});
	};

	//查看
	function editCostFun(id) {
		parent.$
				.modalDialog({
					title : '编辑费用类型',
					width : 550,
					height : 520,
					href : '${pageContext.request.contextPath}/priceController/securi_toEditCost?id='
							+ id,
					buttons : [ {
						text : '关闭',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}

	//添加
	function editFun(id) {
		parent.$
				.modalDialog({
					title : '编辑分类',
					width : 300,
					height : 150,
					href : '${pageContext.request.contextPath}/priceController/securi_toEditPage?id='
							+ id,
					buttons : [ {
						text : '修改',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
	};

	//添加
	function addFun() {
		parent.$
				.modalDialog({
					title : '添加分类',
					width : 300,
					height : 150,
					href : '${pageContext.request.contextPath}/priceController/securi_toAddPage',
					buttons : [ {
						text : '添加',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
	};

	function addCostFun(id) {
		parent.$
				.modalDialog({
					title : '添加费用类型',
					width : 550,
					height : 520,
					href : '${pageContext.request.contextPath}/priceController/securi_toAddCost?id='
							+ id,
					buttons : [ {
						text : '关闭',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}

	//过滤条件查询
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	};

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	};
</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 80px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td style="text-align: right; width: 120px;">类型名称:&nbsp;&nbsp;</td>
						<td><input name="name" placeholder="可以模糊查询" class="span2" /></td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 显示表格内容，list -->
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>

	<!-- toolBar -->
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'pencil_add'">添加分类</a> <a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">条件查询</a><a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>
</body>
</html>
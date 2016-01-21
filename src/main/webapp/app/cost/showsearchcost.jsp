<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<title>费用类型管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>

<script type="text/javascript">
	var dataGrid;
	//Ajax加载数据库中数据，传入rows与total
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/costController/dataGrid',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 50,
							pageList : [ 10, 20, 50, 100, 500 ],
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
										title : '费用类型名称',
										field : 'costType',
										width : 350
									},
									{
										title : '费用编码',
										field : 'itemCode',
										width : 250
									},
									{
										field : 'action',
										title : '操作',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
											if (row.pid != 0) {
												str += $
														.formatString(
																'<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>',
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
						'如果该费用类型有子元素，将会全部删除,确定?',
						function(b) {
							if (b) {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								$
										.ajax({
											type : "post",
											url : '${pageContext.request.contextPath}/costController/delCost',
											data : {
												id : id
											},
											dataType : "json",
											success : function(data) {
												parent.$.messager
														.progress('close');
												if (data.success == true) {
													alert("删除成功")
													window.location.href = '${pageContext.request.contextPath}/costController/searchcost';
												} else
													alert(data.msg);
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
													'${pageContext.request.contextPath}/costController/securi_buntchDelCost',
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

	//编辑
	function editFun(id) {
		parent.$
				.modalDialog({
					title : '编辑',
					width : 300,
					height : 220,
					href : '${pageContext.request.contextPath}/costController/updateCostPage?id='
							+ id,
					buttons : [ {
						text : '编辑',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
							window.location.href = '${pageContext.request.contextPath}/costController/searchcost';
						}
					} ]
				});
	}

	//添加
	function addFun() {
		parent.$
				.modalDialog({
					title : '添加',
					width : 300,
					height : 220,
					href : '${pageContext.request.contextPath}/costController/addCostPage',
					buttons : [ {
						text : '添加',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
							//window.location.href = '${pageContext.request.contextPath}/costController/searchcost';
						}
					} ]
				});
	}

	function importFun() {
		parent.$
				.modalDialog({
					title : '文件导入',
					width : 550,
					height : 180,
					href : '${pageContext.request.contextPath}/costController/securi_uploadCostPage',
					buttons : [ {
						text : '确认',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
	}

	//过滤条件查询
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	};

	function gotoTree() {
		window.location.href = '${pageContext.request.contextPath}/costController/costShow';
	}

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
						<td style="text-align: right; width: 80px;">费用类型:&nbsp;&nbsp;</td>
						<td><input name="title" style="width: 250px"
							placeholder="可以模糊查询" class="span2" /></td>
						<td style="text-align: right; width: 80px;">费用编码:&nbsp;&nbsp;</td>
						<td><input name="code" style="width: 250px"
							placeholder="可以模糊查询" class="span2" /></td>
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
			data-options="plain:true,iconCls:'pencil_add'">添加</a> <a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="gotoTree();">树形展示</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a><a onclick="batchDeleteFun();"
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="plain:true,iconCls:'delete'">批量删除</a> <a
			onclick="importFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'pencil_add'">导入</a>
	</div>
</body>
</html>
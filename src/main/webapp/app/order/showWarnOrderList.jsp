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
							url : '${pageContext.request.contextPath}/userController/warnOrderDataGrid',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'zjsorder_id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'zjsorder_id',
							sortOrder : 'asc',
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							columns : [ [ {
								field : 'zjsorder_id',
								title : '订单编号',
								width : 100,
								sortable : true
							}, {
								field : 'zjsorder_name',
								title : '订单名称',
								width : 120
							}, {
								field : 'zjsorder_bz',
								title : '订单标准',
								width : 150
							}, {
								field : 'zjsorder_money',
								title : '订单金额',
								width : 100
							}, {
								field : 'time',
								title : '订单时间',
								width : 100
							}, {
								field : 'zjsorder_state',
								title : '订单状态',
								width : 100
							}, {
								field : 'zjsinfo_id',
								title : '订单信息id',
								width : 100
							}, {
								field : 'zjsorder_customer',
								title : '顾客id',
								width : 100
							}, ] ],
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
			style="height: 75px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td>订单编号:&nbsp;<input name="orderid" placeholder="可以模糊查询"
							class="span2" /></td>
						<td>订单名称:&nbsp;<input name="orderName" placeholder="可以模糊查询"
							class="span2" /></td>
						<td>起止时间:&nbsp;<input class="span2" name="startTime"
							placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" />-<input class="span2" name="endTime"
							placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" /></td>
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
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>
</body>
</html>
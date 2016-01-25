<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/priceController/securi_priceList?price_id='
									+ $('#price_id').val(),
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							columns : [ [ {
								field : 'id',
								title : '多选框',
								width : 100,
								checkbox : true,
							}, {
								title : '费用类型名称',
								field : 'costType',
								width : 300
							}, {
								title : '费用编码',
								field : 'itemCode',
								width : 220
							} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							}
						});
	});

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
													'${pageContext.request.contextPath}/priceController/securi_deletecost?',
													{
														ids : ids.join(','),
														price_id : $(
																'#price_id')
																.val()
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

	//过滤条件查询
	function searchFun() {
		$(dataGrid).datagrid('load', $.serializeObject($('#searchForm')));

	};

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		$(dataGrid).datagrid('load', {});
	};
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',title:'查询条件',border:false"
		style="height: 85px; overflow: hidden;">
		<form id="searchForm">
			<table class="table table-hover table-condensed"
				style="display: none;">
				<tr>
					<td style="text-align: left"><input type="hidden"
						id='price_id' name='price_id' value='${price_id}'>费用类型:&nbsp;&nbsp;</td>
					<td><input name="title" id="title" style="width: 140px;"
						placeholder="可以模糊查询" class="span2" /></td>
					<td style="text-align: left">费用编码:&nbsp;&nbsp;</td>
					<td><input name="code" id="code" style="width: 140px;"
						placeholder="可以模糊查询" class="span2" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataGrid"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'batdelete_new',plain:true"
			onclick="batchDeleteFun();">删除选中费用</a> <a href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>
</div>

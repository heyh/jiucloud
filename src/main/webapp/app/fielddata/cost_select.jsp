<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		$
				.ajax({
					type : 'POST',
					url : '${pageContext.request.contextPath}/costController/securi_treeGrid?title=1&source=data',
					cache : false,
					dataType : 'json',
					success : function(data) {
						dataGrid = $('#dataGrid')
								.treegrid(
										{
											idField : 'id',
											treeField : 'costType',
											width : '100%',
											height : '450',
											parentField : 'pid',
											striped : true,
											data : $.parseJSON(data),
											animate : true,
											rownumbers : true,
											columns : [ [
													{
														title : '费用类型名称',
														field : 'costType',
														width : 200
													},
													{
														title : '费用编码',
														field : 'itemCode',
														width : 100
													},
													{
														field : 'action',
														title : '操作（选择）',
														width : 100,
														formatter : function(
																value, row,
																index) {
															var str = '';
															if (row.isend == 1) {
																str += $
																		.formatString(
																				'<img onclick="viewFun(\'{0}\',\'{1}\',\'{2}\');" src="{3}" title="选择"/>',
																				row.nid,
																				row.costType,
																				row.itemCode,
																				'${pageContext.request.contextPath}/style/images/extjs_icons/eye.png');
															}
															return str;
														}
													} ] ],
											toolbar : '#toolbar',
											onLoadSuccess : function() {
												$('#searchForm table').show();
												$(this).treegrid('collapseAll');
												parent.$.messager
														.progress('close');
												$(this).treegrid('tooltip');
											},
										});
					}
				});

	});

	function viewFun(id, name, itemCode) {
		document.getElementById("proidh").value = id;
		document.getElementById("proNameh").value = name;
		document.getElementById("code").value = itemCode;
	}

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',title:'查询条件',border:false"
		style="height: 80px; overflow: hidden;">
		<form id="searchForm">
			<table class="table table-hover table-condensed"
				style="display: none;">
				<!-- 				<tr>
					<td style="text-align: right; width: 80px;">费用类型:&nbsp;&nbsp;</td>
					<td><input name="title" id="title" style="width: 180px;"
						placeholder="可以模糊查询" class="span2" /></td>
				</tr> -->
				<tr>
					<td><input name="proidh" id="proidh" value='' type="hidden" /><input
						name="code" id="code" type="hidden" />已选费用类型:</td>
					<td><input name="proNameh" id="proNameh" value=''
						readonly="readonly" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataGrid"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<!-- 		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a> -->
	</div>
</div>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	var name = '123';
	var id = '234';
	$(function() {
		var dataGrid;
		//Ajax加载数据库中数据，传入rows与total
		$(function() {
			dataGrid = $('#dataGrid')
					.datagrid(
							{
								url : '${pageContext.request.contextPath}/projectController/dataGrid?source=field',
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
											field : 'projectId',
											title : '项目编号',
											width : 50
										},
										{
											field : 'proName',
											title : '项目名称',
											width : 170
										},
										{
											field : 'action',
											title : '操作（选择）',
											width : 40,
											formatter : function(value, row,
													index) {
												var str = '';
												str += $
														.formatString(
																'<img onclick="viewFun(\'{0}\',\'{1}\');" src="{2}" title="选择"/>',
																row.id,
																row.proName,
																'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/choose-blue.png');
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

	});

	function viewFun(id, name) {
		document.getElementById("proidh").value = id;
		document.getElementById("proNameh").value = name;
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
		style="height: 100px; overflow: hidden;">
		<form id="searchForm">
			<table class="table table-hover table-condensed"
				style="display: none;">
				<tr>
					<td style="text-align: right; width: 60px;">项目编号:&nbsp;&nbsp;</td>
					<td><input name="projetcId" id="id" style="width: 100px;"
						placeholder="可以模糊查询" class="span2" /></td>
					<td style="text-align: right; width: 60px;">项目名称:&nbsp;&nbsp;</td>
					<td><input name="proName" id="proName" style="width: 150px;"
						placeholder="可以模糊查询" class="span2" /></td>
				</tr>
				<tr>
					<td colspan=2><input name="proidh" id="proidh" value=''
						type="hidden" />您当前选择的工程为:</td>
					<td colspan=2><input name="proNameh" id="proNameh" value=''
						readonly="readonly" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataGrid"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>
</div>

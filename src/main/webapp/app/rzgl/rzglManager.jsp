<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>日志管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<c:if
	test="${fn:contains(sessionInfo.resourceList, '/rzglController/searchrzPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/rzglController/searchrz',
							fit : true,
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'id',
							sortOrder : 'asc',
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							frozenColumns : [ [
									{
										field : 'title',
										title : '标题',
										width : 150,
										sortable : true
									},
									{
										field : 'content',
										title : '内容',
										width : '300',
										sortable : true
									},
									{
										field : 'ctime',
										title : '时间',
										width : '200',
										sortable : true
									},
									{
										field : 'uid',
										title : '姓名',
										width : '150',
										sortable : true
									},
									{
										field : 'action',
										title : '操作',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
											if ($.canEdit) {
												str += $
														.formatString(
																'<img onclick="editFun(\'{0}\');" src="{1}" title="查看"/>',
																row.id,
																'${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
											}
											str += '&nbsp;';
											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
							},
							onRowContextMenu : function(e, rowIndex, rowData) {
								e.preventDefault();
								$(this).datagrid('unselectAll').datagrid(
										'uncheckAll');
								$(this).datagrid('selectRow', rowIndex);
								$('#menu').menu('show', {
									left : e.pageX,
									top : e.pageY
								});
							}
						});
	});

	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$
				.modalDialog({
					title : '查看日志',
					width : 680,
					height : 550,
					href : '${pageContext.request.contextPath}/rzglController/searchrzPage?id='
							+ id,
					buttons : [ {
						text : '关闭',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							parent.$.modalDialog.handler.dialog('close');
							/* var f = parent.$.modalDialog.handler.find('#form');
							f.submit(); */
						}
					} ]
				});
	}

	function addFun() {
		parent.$
				.modalDialog({
					title : '添加日志',
					width : 680,
					height : 550,
					href : '${pageContext.request.contextPath}/rzglController/addrzPage',
					buttons : [ {
						text : '添加',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
	}

	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 70px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td colspan="3">日期&nbsp;:&nbsp; <input class="span2"
							name="beginTime" placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							readonly="readonly" style="width: 150px;" /> &nbsp;-&nbsp;&nbsp;<input
							class="span2" name="endTime" placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							readonly="readonly" style="width: 150px;" />
						</td>
						<!-- <td>
							部门:
							<select name="type" style="width:150px;">
								<option value="">--不限--</option>
								<option value="1"></option>
								<option value="2"></option>
							</select>
						</td>
						<td>
							姓名:<input name="realname" placeholder="可以模糊查询标题" class="span2" style="width:200px;"/>
						</td> -->
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if
			test="${fn:contains(sessionInfo.resourceList, '/rzglController/addrzPage')}">
			<a onclick="addFun();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a><a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>

	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if
			test="${fn:contains(sessionInfo.resourceList, '/rzglController/addrzPage')}">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if
			test="${fn:contains(sessionInfo.resourceList, '/rzglController/searchrzPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">查看</div>
		</c:if>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<title>服务号管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var dataGrid;
	$(function() {
	
		dataGrid = $('#dataGrid').datagrid({
			url : '${pageContext.request.contextPath}/messageController/dataGrid',
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
			frozenColumns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				checkbox : true
			}, {
				field : 'recName',
				title : '收信人',
				width : 150,
				sortable : true
			} ] ],
			columns : [ [ {
				field : 'title',
				title : '标题',
				width : 250
			}, {
				field : 'statue',
				title : '已读',
				width : 100,
				formatter : function(value, row, index) {
 					if(row.statue==0){
						return "未读";
 	 	 			}else if(row.statue==1){
 	 	 				return "已读";
 	 	 	 		}
				}
			}, {
				field : 'pdate',
				title : '发送时间',
				width : 130
			}, {
				field : 'action',
				title : '操作',
				width : 150,
				formatter : function(value, row, index) {
					var str = '';
					str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="查看"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
					str += '&nbsp;';
					str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/key.png');
					//str += '&nbsp;';
					//str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title=""/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
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
				$(this).datagrid('unselectAll').datagrid('uncheckAll');
				$(this).datagrid('selectRow', rowIndex);
				//$('#menu').menu('show', {
				//	left : e.pageX,
				///	top : e.pageY
				//});
			}
		});
		
	});

	
	//删除
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前数据？', function(b) {
			if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
	        	$.ajax({
						type: "post",
						url: '${pageContext.request.contextPath}/messageController/deleteSend',
						 data: {id:id},
						  dataType: "json",
						  success: function(data){
							  if(data.success==true){
								  searchFun();
							}
						  }
	        	});
			}
		});
	}

	//批量删除
	function batchDeleteFun() {
		var rows = dataGrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.$.messager.confirm('确认', '您是否要删除当前选中的数据？', function(r) {
				if (r) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var getTimestamp=new Date().getTime();
					$.getJSON('${pageContext.request.contextPath}/messageController/batchDeleteSend?t='+getTimestamp, {
						ids : ids.join(',')
					}, function(result) {
						if (result.success) {
							dataGrid.datagrid('load');
							dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						}
						parent.$.messager.alert('提示', result.msg, 'info');
						parent.$.messager.progress('close');
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
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '邮件详情',
			width : 720,
			height : 500,
			href : '${pageContext.request.contextPath}/messageController/detail?msgid=' + id
			 
		});
	}
	
	 

	//发送邮件
	function addFun(){
		parent.$.modalDialog({
			title : '发送站内信',
			width : 720,
			height : 300,
			href : '${pageContext.request.contextPath}/messageController/tosendmsg',
			buttons : [ {
				text : '发送',
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
	}
	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 70px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="display: none; font-size:12px;">
					<tr>
						<td style="text-align: right;width: 120px;">邮件标题:&nbsp;&nbsp;</td>
						<td><input name="title" style="height: 13px;width:250px;"  placeholder="可以模糊查询" class="span2" /></td>
					
						<td style="text-align: right;width: 120px;">发送时间:&nbsp;&nbsp;</td>
						<td><input class="span2" style="height: 13px;" name="createdatetimeStart" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
						&nbsp;至&nbsp;<input class="span2" style="height: 13px;"  name="createdatetimeEnd" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" /></td>
						
						<td style="text-align: right;width: 80px; height: 13px;" >是否已读:&nbsp;&nbsp;</td>
						<td>
						  <select style="width:60px;" name="appcomp">
							<option value="">所有</option>
							<option value="1">已读</option>
							<option value="0">未读</option>
						</select></td>
					   
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">发送邮件</a>
			<a onclick="batchDeleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'delete'">批量删除</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="cleanFun();">清空条件</a>
	</div>

	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
	</div>
</body>
</html>
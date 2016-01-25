<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link
	href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	//上传文件
	function uploadfile(filed) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据正在更新，请不要离开此页面，请稍后....'
		});
		var id = $("#id").val();
		$
				.ajaxFileUpload({
					url : '${pageContext.request.contextPath }/fieldDataController/upload',
					secureuri : false,
					fileElementId : filed,
					dataType : 'json',
					data : {
						id : id,
						name : filed,
						fileds : filed,
					},
					success : function(data) {
						parent.$.messager.progress('close');
						if (data.success) {
							$.messager.alert('成功', data.msg, 'info');
							$('#file').attr('value', '');
							$('#dataGrid').datagrid('load', {});
						} else {
							$.messager.alert('错误', data.msg, 'error');
							parent.$.modalDialog.handler.dialog('refresh');//重新刷新表单
						}
					},
					error : function(data, status, e) {
						$.messager.alert('错误', data.msg, 'error');
						parent.$.messager.progress('close');
					}
				});
	}

	$(function() {
		var dataGrid;
		//Ajax加载数据库中数据，传入rows与total
		$(function() {
			dataGrid = $('#dataGrid')
					.datagrid(
							{
								url : '${pageContext.request.contextPath}/fieldDataController/securi_filedataGrid?id=${id}',
								fit : true,
								fitColumns : true,
								border : false,
								pagination : true,
								idField : 'id',
								pageSize : 10,
								pageList : [ 10, 20, 30, 40, 50 ],
								sortOrder : 'asc',
								checkOnSelect : false,
								selectOnCheck : false,
								nowrap : false,
								columns : [ [
										{
											field : 'id',
											title : '编号',
											width : 30,
											checkbox : true
										},
										{
											field : 'fileName',
											title : '文件名',
											width : 170
										},
										{
											field : 'ext',
											title : '文件格式',
											width : 70
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
																'<img onclick="viewFun(\'{0}\');" src="{1}" title="预览"/>',
																row.id,
																'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/preview-blue.png');
                                                str += '&nbsp;';
                                                str += $
														.formatString(
																'<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
																row.id,
																'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                                str += '&nbsp;';
                                                str += $
														.formatString(
																'<img onclick="download(\'{0}\');" src="{1}" title="下载"/>',
																row.sourceFilePath,
																'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/download-blue.png');
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

	function deleteFun(id) {
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
											url : '${pageContext.request.contextPath}/fieldDataController/securi_filedelete?',
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
	}

	function viewFun(id) {
		window
				.open('${pageContext.request.contextPath}/fieldDataController/securi_showfile?id='
						+ id);
	}

	//批量下载
	function downloadFun() {
		var rows = $('#dataGrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			{
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$("#mpid").val(ids);
				document.getElementById("mpid").value = ids.join(',');
				document.getElementById("ttform").submit();
			}
		} else {
			parent.$.messager.show({
				title : '提示',
				msg : '请勾选要下载的附件！'
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

	function download(path) {
		window.location.href = "app/fielddata/DownLoa.jsp?name=" + path;
	}
</script>
<form method="post" id="ttform" target="_blank"
	action="${pageContext.request.contextPath}/fieldDataController/securi_downloadfile">
	<input type="hidden" id="mpid" name="mpid" /> <input type="hidden"
		id="ty" name="ty" value='2' />
</form>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',title:'附件',border:false"
		style="height: 100px; overflow: hidden;">
		<form id="searchForm">
			<table class="table table-hover table-condensed"
				style="display: none;">
				<tr>
					<td colspan=4 style="text-align: center;"><input
						style="width: 230px; font-size: 12px;" type="file" name="file"
						id="file" /> <a onclick="uploadfile('file');"
						href="javascript:void(0);" class="easyui-linkbutton"
						data-options="plain:true,iconCls:'pencil_add'">上传</a></td>
				</tr>
				<tr>
					<td style="text-align: right; width: 80px;">文件名:&nbsp;&nbsp;</td>
					<td><input name="filename" id="filename" style="width: 180px;"
						placeholder="可以模糊查询" class="span2" /><input type="hidden" id="id"
						value="${id}" /></td>
					<td style="text-align: right; width: 80px;">文件类型:&nbsp;&nbsp;</td>
					<td><select name="filetype" id="filetype"
						style="width: 180px;" class="span2">
							<option value="">全部格式</option>
							<option value="doc">doc</option>
							<option value="jpg">jpg</option>
							<option value="xls">xls</option>
							<option value="txt">txt</option>
							<option value="ppt">ppt</option>
							<option value="pdf">pdf</option>
							<option value="mp3">mp3</option>
							<option value="mp4">mp4</option>
					</select></td>
				</tr>

			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false">
		<table id="dataGrid"></table>
	</div>
	<div id="toolbar" style="display: none;">
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">清空条件</a> <a onclick="downloadFun();"
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="plain:true,iconCls:'tux'">批量下载</a>
	</div>
</div>

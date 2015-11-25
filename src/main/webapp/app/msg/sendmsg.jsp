<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');

		//value : $.stringToList('${user.roleIds}')
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/messageController/securi_savemsg',
							onSubmit : function() {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								var isValid = $(this).form('validate');
								if (!isValid) {
									parent.$.messager.progress('close');
								}
								return isValid;
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);

								if (result.success) {
									alert("发送成功!");
									parent.$.modalDialog.openner_dataGrid
											.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
									parent.$.modalDialog.handler
											.dialog('close');
									//parent.$.modalDialog.openner_dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
								} else {
									parent.$.messager.alert('错误', result.msg,
											'error');
								}
							}
						});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed"
				style="font-size: 12px;">
				<tr>
					<td style="width: 80px;">收件人</td>
					<td><select name="sendName" id="sendName"
						class="easyui-combogrid" style="width: 450px"
						data-options="
			            panelWidth: 400,
			            multiple: true,
			            idField: 'id',
			            textField: 'username',
			            url: '${pageContext.request.contextPath}/messageController/securi_toloadsduser',
			            method: 'get',
			            columns: [[
			                {field:'id',checkbox:true},
			                {field:'username',title:'姓名',width:60,align:'center'}
			            ]],
			            fitColumns: true">
					</select> <img
						src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png"
						onclick="$('#roleIds').combotree('clear');" /> <br /> <font
						style="font-size: 12px; color: red;">此处请选择发送人，不可手动录入</font></td>

				</tr>
				<tr>
					<td>邮件标题</td>
					<td><input type="text" name="title" style="width: 450px;" /></td>
				</tr>
				<tr>
					<td>邮件内容</td>
					<td><textarea rows="5" cols="8" name="message"
							style="width: 450px;"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>
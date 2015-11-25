<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/collcontroller/securi_add?pid=${pid}',
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
									alert("添加成功!");
									parent.$.modalDialog.openner_dataGrid
											.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
									parent.$.modalDialog.handler
											.dialog('close');
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
					<td>单位名称</td>
					<td><input type="text" name="name" style="width: 208px;" /></td>
				</tr>
				<tr>
					<td>单位资质</td>
					<td><input type="text" name="power" style="width: 208px;" /></td>
				</tr>
				<tr>
					<td>公司电话</td>
					<td><input type="text" name="tel" style="width: 208px;" /></td>
				</tr>
				<tr>
					<td>公司地址</td>
					<td><input type="text" name="address" style="width: 208px;" /></td>
				</tr>
				<tr>
				<tr>
					<td>所属行业</td>
					<td><select name="industry">
							<option>建设单位</option>
							<option>施工单位</option>
							<option>咨询公司</option>
							<option>政府部门</option>
							<option>监理公司</option>
							<option>设计单位</option>
							<option>材料商</option>
							<option>租赁单位</option>
							<option>劳务公司</option>
							<option>其他</option>
					</select></td>
				</tr>
				<tr>
					<td>企业邮箱</td>
					<td><input type="text" name="email" style="width: 208px;" /></td>
				</tr>
				<tr>
					<td>公司介绍</td>
					<td><textarea rows="" cols="" name="remark"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>
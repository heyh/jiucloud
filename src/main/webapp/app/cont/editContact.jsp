<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<script type="text/javascript">
	parent.$.messager.progress('close');

	$(function() {
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/contactController/securi_update',
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
									alert("修改成功!");
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
			<input name="id" type="hidden" class="span2" value="${pro.id}"
				readonly="readonly">
			<table class="table table-hover table-condensed"
				style="font-size: 12px;">
				<tr>
					<td>姓名</td>
					<td><input type="text" name="name" style="width: 208px;"
						value='${detail.name }' /></td>
				</tr>
				<tr>
					<td>职务</td>
					<td><input type="text" name="job" style="width: 208px;"
						value='${detail.job }' /></td>
				</tr>
				<tr>
					<td>手机</td>
					<td><input type="text" name="phone" style="width: 208px;"
						value='${detail.phone }' /></td>
				</tr>
				<tr>
					<td>专业</td>
					<td><input type="text" name="major" style="width: 208px;"
						value='${detail.major }' /></td>
				</tr>
				<tr>
					<td>资格证书</td>
					<td><input type="text" name="qualification"
						style="width: 208px;" value='${detail.qualification }' /></td>
				</tr>
				<tr>
					<td>个人邮箱</td>
					<td><input type="text" name="email" style="width: 208px;"
						value='${detail.qualification }' /></td>
				</tr>
				<tr>
					<td>家庭住址</td>
					<td><input type="text" name="address" style="width: 208px;"
						value='${detail.address }' /><input type="hidden" name="ththth"
						style="width: 208px;" value='${detail.id }' /><input
						type="hidden" name="pid" style="width: 208px;"
						value='${detail.pid }' /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
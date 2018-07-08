<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		var url = '';
		if ($('ids').val() == '') {
            url = '${pageContext.request.contextPath}/fieldDataController/securi_approvedField'
		} else {
            url = '${pageContext.request.contextPath}/fieldDataController/securi_batchApprovedField'
        }
		$('#form')
				.form(
						{
							url : url,

							onSubmit : function() {
                                approvedOption = prompt("审批意见","");
                                if (approvedOption === null) {
                                    return false;
                                }
                                $('#approvedOption').val(approvedOption);
								return true;
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									alert("审批成功!");
									parent.$.modalDialog.openner_dataGrid
											.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
									parent.$.modalDialog.handler
											.dialog('close');
								} else {
									parent.$.messager.alert('错误', result.msg, 'error');
								}
							}
						});

	});

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-inline" name="form" id="form" method="post" role="form">
			<div class="control-group" style="padding-left:10px;padding-top: 20px; ">

				<div class="controls">
					<select id="currentApprovedUser" name="currentApprovedUser">
						<c:forEach items="${userList}" var="tem">
							<option value="${tem.id}">${tem.username}</option>
						</c:forEach>
					</select>
					<input type="hidden" id="id" name="id" value="${id}">
					<input type="hidden" id="approvedState" name="approvedState" value="8">
					<input type="hidden" id="approvedOption" name="approvedOption" value="">
					<input type="hidden" id="ids" name="ids" value="${ids}">
				</div>
			</div>

		</form>
	</div>
</div>
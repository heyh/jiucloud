<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/clockinginTimeController/securi_updateClockinginTime',

							onSubmit : function() {
								return true;
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
									parent.$.messager.alert('错误', result.msg, 'error');
								}
							}
						});

	});

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-horizontal" name="form" id="form" method="post" role="form">

			<input type="hidden" id="id" name="id" value="${clockinginTime.id}" />

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="clockinginStartTime">上班时间:</label>

				<div class="controls">
					<input style="" class="Wdate span2" name="clockinginStartTime" id='clockinginStartTime' placeholder="上班时间" value="${clockinginTime.clockinginStartTime}" onclick="WdatePicker({dateFmt:'HH:mm:ss'})" />
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="clockinginEndTime">下班时间:</label>

				<div class="controls">
					<input style="" class="Wdate span2" name="clockinginEndTime" id='clockinginEndTime' placeholder="上班时间" value="${clockinginTime.clockinginEndTime}" onclick="WdatePicker({dateFmt:'HH:mm:ss'})" />
				</div>
			</div>
		</form>
	</div>
</div>
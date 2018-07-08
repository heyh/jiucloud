<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/locationController/securi_add',

							onSubmit : function() {
							    if($("#mc").val() == '') {
							        alert("请填写设施名称");
							        return false;
                                }

								return true;
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
									parent.$.messager.alert('错误', result.msg, 'error');
								}
							}
						});

	});

</script>



<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-horizontal" name="form" id="form" method="post" role="form" style="padding-right: 100px">

			<div class="control-group" style="padding-top: 30px; padding-right: 50px">
				<label class="control-label" for="mc">设施名称:</label>
				<div class="controls">
					<input type="text" id="mc" name="mc" required>
				</div>
			</div>


		</form>
	</div>
</div>
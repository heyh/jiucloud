<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/featureController/securi_update',

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

			<input type="hidden" id="id" name="id" value="${feature.id}" />

			<%--<div class="control-group" style="padding-top: 20px; padding-right: 50px">--%>
				<%--<label class="control-label" for="costTypeRef">费用类型</label>--%>
				<%--<div class="controls">--%>
					<%--<input class="easyui-combotree" name="costTypeRef" id="costTypeRef" style="width: 220px" placeholder="请选择">--%>
					<%--<input type="hidden" name="costType" id="costType">--%>
					<%--<input type="hidden" name="itemCode" id="itemCode">--%>
				<%--</div>--%>
			<%--</div>--%>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="mc">名称</label>
				<div class="controls">
					<input type="text" name="mc" id="mc" value="${feature.mc}">
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="count">数量</label>
				<div class="controls">
					<input type="text" name="count" id="count" value="${feature.count}">
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="dw">单位</label>
				<div class="controls">
					<input type="text" name="dw" id="dw" value="${feature.dw}">
				</div>
			</div>
		</form>
	</div>
</div>

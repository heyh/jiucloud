<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/itemController/securi_addSection',

							onSubmit : function() {
							    if($("#projectId").val() == '') {
							        alert("请选择工程");
							        return false;
                                }
                                if($("#name ").val() == '') {
                                    alert("请标段名称");
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
		<form class="form-horizontal" name="form" id="form" method="post" role="form">
			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="projectId">工程名称</label>

				<div class="controls">
					<select id="projectId" name="projectId">
						<option value="">请选择</option>
						<c:forEach items="${projects}" var="tem">
							<option value="${tem.id}">${tem.text}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="name">标段名称</label>

				<div class="controls">
					<input type="text" name="name" id="name" >
				</div>
			</div>

			<div class="control-group">
				<label class="control-label" for="supInfo">标段附加信息</label>

				<div class="controls">
					<textarea name="supInfo" id="supInfo" style="width: 218px;" cols=50 rows=6>多行输入</textarea>
				</div>
			</div>

		</form>
	</div>
</div>
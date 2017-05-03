<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<script type="text/javascript">
	var industr = document.getElementById('industr').value;
	for (var i = 0; i < document.getElementById('industry').options.length; i++) {
		if (document.getElementById('industry').options[i].value == industr) {
			document.getElementById('industry').options[i].selected = true;
			break;
		}
	}

	parent.$.messager.progress('close');

	$(function() {
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/collcontroller/securi_update',
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

    var sectionId = document.getElementById('sectionId').value;
    for (var i = 0; i < document.getElementById('section').options.length; i++) {
        if (document.getElementById('section').options[i].value == sectionId) {
            document.getElementById('section').options[i].selected = true;
            break;
        }
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post">
			<input type="hidden" id="sectionId" value="${detail.section}"/>
			<input name="id" type="hidden" class="span2" value="${pro.id}"
				readonly="readonly">
			<table class="table table-hover table-condensed" style="font-size: 12px;">
				<tr>
					<td>标段</td>
					<td>
						<select id="section" name="section">
							<c:forEach items="${selectItems}" var="selectItem">
								<option value="${selectItem.id}">${selectItem.text}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>单位名称</td>
					<td><input type="text" name="name" style="width: 208px;"
						value='${detail.name }' /></td>
				</tr>
				<tr>
					<td>单位资质</td>
					<td><input type="text" name="power" style="width: 208px;"
						value='${detail.power }' /></td>
				</tr>
				<tr>
					<td>公司电话</td>
					<td><input type="text" name="tel" style="width: 208px;"
						value='${detail.tel }' /></td>
				</tr>
				<tr>
					<td>公司地址</td>
					<td><input type="text" name="address" style="width: 208px;"
						value='${detail.address }' /></td>
				</tr>
				<tr>
				<tr>
					<td>所属行业</td>
					<td><select name="industry" id="industry">
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
					</select><input type="hidden" name="industr" id="industr"
						style="width: 208px;" value='${detail.industry }' /><input
						type="hidden" name="ththth" style="width: 208px;"
						value='${detail.id }' /><input type="hidden" name="pid"
						style="width: 208px;" value='${detail.pid }' /></td>
				</tr>
				<tr>
					<td>企业邮箱</td>
					<td><input type="text" name="email" style="width: 208px;"
						value='${detail.email }' /></td>
				</tr>
				<tr>
					<td>公司介绍</td>
					<td><input type="text" name="remark" style="width: 208px;"
						value='${detail.remark }' /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
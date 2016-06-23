<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/fieldDataController/updatefieldData',

							onSubmit : function() {
								var dataName = document
										.getElementById("dataName").value;
								if (dataName == '') {
									alert('请输入名称');
									return false;
								}
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
									parent.$.messager.alert('错误', result.msg,
											'error');
								}
							}
						});

	});
	//获得上传文件信息
	function getFileInfo(pic, icon) {
		$("#" + pic).change(function() {
			var size = document.getElementById(pic).files[0].size;
			var pname = document.getElementById(pic).files[0].name;
			size = formatFileSize(size);
			$("#" + icon).val(pname);
			$("#appSize").val(size);
			alert(size + "," + pname);
		});
	}

    $(function() {
        if($('.priview').val() == 'true'){
            debugger;
            $('.easyui-validatebox').attr('disabled', true);
        } else {
            $('.easyui-validatebox').removeAttr('disabled');
        }
    });
	//上传文件
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
        <input class="priview" value="${preview}" style="display: none;"/>
		<form id="form" method="post" enctype="multipart/form-data">
			<table class="table table-hover table-condensed">
				<tr>
					<td style="width: 80px;">工程名称</td>
					<td><input type="hidden" id="id" name="id" value="${tfielddata.id}" />
                        <input type="hidden" id="itemCode" name="itemCode" value="${tfielddata.itemCode}" />
                        <input type="hidden" id="creatTime" name="creatTime" value="${tfielddata.creatTime}" />
						<input type="hidden" id="cid" name="cid" value="${tfielddata.cid}" />
						<input type="hidden" id="uid" name="uid" value="${tfielddata.uid}" />
						<input type="hidden" id="uname" name="uname" value="${tfielddata.uname}" />
                        <input type="hidden" id="company" name="company" value="${tfielddata.company}" />
                        <input type="hidden" id="costType" name="costType" value="${tfielddata.costType}" />
                        <input type="hidden" id="projectName" name="projectName" value="${tfielddata.projectName}" />
                        <input type="text" placeholder="工程名称" style="width: 250px;"
                               class="easyui-validatebox span2" data-options="required:true"
                               value="${project.proName}" readonly='readonly'></td>
				</tr>
				<tr>
					<td style="width: 80px;">费用类型</td>
					<td><input type="text" style="width: 250px;"
						placeholder="费用类型" class="easyui-validatebox span2"
						data-options="required:true" value="${cost.costType}"
						readonly='readonly'></td>
				</tr>
				<tr>
					<td style="width: 80px;">名称</td>
					<td><input name="dataName" id="dataName" type="text"
						style="width: 250px;" placeholder="名称"
						class="easyui-validatebox span2" data-options="required:true"
						value="${tfielddata.dataName}"></td>
				</tr>
				<tr>
					<td style="width: 80px;">单价</td>
					<td><input name="price" style="width: 250px;" type="text"
						placeholder="单价" class="easyui-validatebox span2"
						data-options="required:true" value="${tfielddata.price}"></td>
				</tr>
				<tr>
					<td style="width: 80px;">数量</td>
					<td><input name="count" style="width: 250px;" type="text"
						placeholder="数量" class="easyui-validatebox span2"
						data-options="required:true" value="${tfielddata.count}"></td>
				</tr>
				<tr>
					<td style="width: 80px;">单位</td>
					<td><input name="unit" style="width: 250px;" type="text"
						placeholder="单位" class="easyui-validatebox span2"
						data-options="required:true" value="${tfielddata.unit}"></td>
				</tr>
				<tr>
					<td style="width: 80px;">规格型号</td>
					<td><input style="width: 250px;" name="specifications"
						type="text" placeholder="规格型号" class="easyui-validatebox span2"
						data-options="required:true" value="${tfielddata.specifications}">
				</tr>
				<tr>
					<td style="width: 80px;">备注说明</td>
					<td><input name="remark" style="width: 250px;" type="text"
						placeholder="备注说明" class="easyui-validatebox span2"
						data-options="required:true" value="${tfielddata.remark}"></td>
				</tr>
                <input name="needApproved" value="${tfielddata.needApproved}" type="hidden"/>
			</table>
		</form>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/paramTransController/securi_updateEeclParamTrans',

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

    var paramCodeId = document.getElementById('paramCodeId').value;
    for (var i = 0; i < document.getElementById('paramCode').options.length; i++) {
        if (document.getElementById('paramCode').options[i].value == paramCodeId) {
            document.getElementById('paramCode').options[i].selected = true;
            break;
        }
    }

    var transParamCodeId = document.getElementById('transParamCodeId').value;
    for (var i = 0; i < document.getElementById('transParamCode').options.length; i++) {
        if (document.getElementById('transParamCode').options[i].value == transParamCodeId) {
            document.getElementById('transParamCode').options[i].selected = true;
            break;
        }
    }

    function getParamName() {
        $('#paramName').val($('#paramCode').find("option:selected").text())
    }

    function getTransParamName() {
        $('#transParamName').val($("#transParamCode").find("option:selected").text());
    }

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-horizontal" name="form" id="form" method="post" role="form">

			<input type="hidden" id="id" name="id" value="${paramTrans.id}" />

			<input type="hidden" id="paramCodeId" id="paramCodeId" value="${paramTrans.paramCode}"/>
			<input type="hidden" id="paramName" name="paramName" value="${paramTrans.paramName}"/>

			<input type="hidden" id="transParamCodeId" id="transParamCodeId" value="${paramTrans.transParamCode}"/>
			<input type="hidden" id="transParamName" name="transParamName" value="${paramTrans.transParamName}"/>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="paramCode">Execl费用:</label>

				<div class="controls">
					<select id="paramCode" name="paramCode" onchange="getParamName()">
						<option value="">请选择</option>
						<c:forEach items="${execlFees}" var="tem">
							<option value="${tem.feeCode}">${tem.feeName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="transParamCode">管理系统费用:</label>

				<div class="controls">
					<select id="transParamCode" name="transParamCode" onchange="getTransParamName()">
						<option value="">请选择</option>
						<c:forEach items="${glxtfee}" var="tem">
							<option value="${tem.itemCode}">${tem.costType}</option>
						</c:forEach>
					</select>
				</div>
			</div>

		</form>
	</div>
</div>
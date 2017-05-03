<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/paramTransController/securi_addExeclParamTrans',

							onSubmit : function() {
							    if($("#paramCode").val() == '') {
							        alert("请选择Execl费用");
							        return false;
                                }
                                if($("#transParamCode").val() == '') {
                                    alert("请选择管理系统费用");
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

			<input type="hidden" id="paramName" name="paramName"/>
			<input type="hidden" id="transParamName" name="transParamName"/>

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
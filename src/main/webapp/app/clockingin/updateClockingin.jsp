<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/clockinginController/securi_updateClockingin',

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

    var approveState = document.getElementById('approveStateRef').value;
    for (var i = 0; i < document.getElementById('approveState').options.length; i++) {
        if (document.getElementById('approveState').options[i].value == approveState) {
            document.getElementById('approveState').options[i].selected = true;
            break;
        }
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-horizontal" name="form" id="form" method="post" role="form">

			<input type="hidden" id="id" name="id" value="${clockingin.id}" />

			<%--<div class="control-group" style="padding-top: 20px; ">--%>
				<%--<label class="control-label" for="clockinginTime">日期:</label>--%>

				<%--<div class="controls">--%>
					<%--<input type="text" id="clockinginDate" name="clockinginDate" value="${clockingin.clockinginDate}" disabled='true'>--%>
				<%--</div>--%>
			<%--</div>--%>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="uname">人员:</label>

				<div class="controls">
					<input type="text" id="uname" name="uname" value="${clockingin.uname}" disabled='true'>
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="cFlag">上/下班:</label>

				<div class="controls">
					<input type="text" id="cFlag" name="cFlag" value="${clockingin.clockinginFlag == '0' ? '上班' : '下班'}" disabled='true'>
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="clockinginTime">时间:</label>

				<div class="controls">
					<input type="text" id="clockinginTime" name="clockinginTime" value="${clockingin.clockinginTime}" disabled='true'>
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="reasonDesc">事由:</label>

				<div class="controls">
					<input type="text" id="reasonDesc" name="reasonDesc" value="${clockingin.reasonDesc}" disabled='true'>
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="approveState"><span style="color: #ff0000">状态:</span></label>

				<div class="controls">
					<select id="approveState" name="approveState">
						<option value="正常">正常</option>
						<option value="迟到">迟到</option>
						<option value="早退">早退</option>
						<option value="事假">事假</option>
						<option value="病假">病假</option>
						<option value="出差">出差</option>
					</select>
					<input type="hidden" id="approveStateRef" value='${clockingin.approveState }'></td>
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; ">
				<label class="control-label" for="approveDesc">审核意见:</label>

				<div class="controls">
					<input type="text" id="approveDesc" name="approveDesc"/>
				</div>
			</div>
		</form>
	</div>
</div>
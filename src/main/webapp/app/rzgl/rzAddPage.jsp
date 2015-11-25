<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link
	href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '${pageContext.request.contextPath}/rzglController/addrz',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});

	function uploadfile(filed) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据正在更新，请不要离开此页面，请稍后....'
		});
		var id = $("#id").val();
		$
				.ajaxFileUpload({
					url : '${pageContext.request.contextPath }/rzglController/securi_uploadfj',
					secureuri : false,
					fileElementId : filed,
					dataType : 'json',
					data : {
						id : id,
						fileds : filed
					},
					success : function(data) {
						parent.$.messager.progress('close');
						if (data.success) {
							$.messager.alert('成功', '上传成功', 'info');
							var str = new Array();
							str = data.msg.split(",");
							if (str[0] == 'pic1') {
								$("#picUrl").val(str[1]);
							}
						} else {
							$.messager.alert('错误', data.msg, 'error');
						}
					},
					error : function(data, status, e) {
						$.messager.alert('错误', data.msg, 'error');
						parent.$.messager.progress('close');
					}
				});
	}

	//单选框否无法输入个
	function choice() {
		if (document.getElementById("sf").checked == true) {
			document.getElementById("price").readOnly = false;
		}
		if (document.getElementById("bsf").checked == true) {
			document.getElementById("price").value = "0";
			document.getElementById("price").readOnly = true;
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>标题</th>
					<td><input type="text" name="title" id="title"
						style="width: 600px;" /></td>
				</tr>
				<tr>
					<th>描述</th>
					<td><textarea rows="17" cols="420" name="content"
							style="width: 600px; resize: none;"></textarea></td>
				</tr>
				<tr>
					<th>附件</th>
					<td colspan="3"><input type="file" name="pic1" id="pic1" />
						<a onclick="uploadfile('pic1');" href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="plain:true,iconCls:'pencil_add'">上传</a> <input
						type="hidden" name="picUrl" id="picUrl" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
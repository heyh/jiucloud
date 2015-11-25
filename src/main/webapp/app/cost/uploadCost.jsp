<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link
	href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>

<script type="text/javascript">
	parent.$.messager.progress('close');
	//上传文件
	function uploadfile(filed) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据正在更新，请不要离开此页面，请稍后....'
		});
		$
				.ajaxFileUpload({
					url : '${pageContext.request.contextPath }/costController/securi_upload',
					secureuri : false,
					fileElementId : filed,
					dataType : 'json',
					data : {
						name : filed,
						fileds : filed,
					},
					success : function(data) {
						parent.$.messager.progress('close');
						if (data.success) {
							$.messager.alert('成功', data.msg, 'info');
							$('#file').attr('value', '');
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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post" enctype="multipart/form-data">
			<table class="table table-hover table-condensed">
				<tr>
					<td style="width: 80px;">上传文件:</td>
					<td><input style="width: 300px; font-size: 12px;" type="file"
						name="file" id="file" /><a onclick="uploadfile('file');"
						href="javascript:void(0);" class="easyui-linkbutton"
						data-options="plain:true,iconCls:'pencil_add'">上传</a></td>
				</tr>
				<tr>
					<td colspan=2>上传文件如果存在中文请使用utf-8编码</td>
				</tr>
			</table>
		</form>
	</div>
</div>
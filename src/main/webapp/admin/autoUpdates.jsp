<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>Android版本更新</title>
<jsp:include page="../inc.jsp"></jsp:include>
<link
	href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	 
	 
$(function(){
	parent.$.messager.progress('close');
 	defaultUserInfoDialog = $('#defaultUserInfoDialog').show().dialog({
		top : 0,
		left : 1200
		}); 
	});
	//上传
	function uploadfile(){
		parent.$.messager.progress({
			title : '提示',
			text : '数据正在更新，请不要离开此页面，请稍后....'
		}); 
	  	var v = $("#version").val();
	  	var description = $("#description").val();
	   	var apkName = $("#apkName").val();
	   	var datas = {apkName:apkName,description:description,version:v}
	   	$.ajaxFileUpload({
			url:'${pageContext.request.contextPath }/autoUpdateController/douploadfile',
			secureuri:false,
			fileElementId:'proPicFiled',
			dataType: 'json',
			data:datas,
			success: function (data){
				parent.$.messager.progress('close');
				if(data.success){
					$.messager.alert('成功', data.msg, 'info');
				}else{
					$.messager.alert('错误', data.msg, 'error');
				}
			},error: function (data, status, e){
					$.messager.alert('错误', data.msg, 'error');
					parent.$.messager.progress('close');
				}
			});
	}

</script>
<body>
	<br/>
	<form name="listForm">
		<table width="100%" border="0" cellpadding="4" cellspacing="1"
			bgcolor="#B7BEC6" style="font-size: 12px;">
			<tr align="center">
				<td align="left" colspan="7" class="tile"></td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#F9FEFF" align="center">
					请选择要上传客户端(APK):
				</td>
				<td colspan="2" bgcolor="#F9FEFF">
					<input name="proPicFiled" id="proPicFiled" type="file" class="span5" />&nbsp;&nbsp; 
					<strong><font color="#FF0000">${isSuccess}</font></strong>
				</td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#F9FEFF" align="center">客户端版本号:</td>
				<td colspan="2" bgcolor="#F9FEFF">
					<input type="hidden" name="url" id="url" value="${url}" style="width: 190px; height: 25px" />
					<input type="text" name="version" id="version" class="easyui-validatebox" data-options="required:true" value="${version}" style="width: 190px; height: 25px" />
				</td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#F9FEFF" align="center">客户端描述:</td>
				<td colspan="2" bgcolor="#F9FEFF"><input type="text"
					name="description" id="description" value="${description}"
					class="easyui-validatebox" data-options="required:true"
					style="width: 190px; height: 25px" /></td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#F9FEFF" align="center">客户端名称:</td>
				<td colspan="2" bgcolor="#F9FEFF"><input type="text"
					name="apkName" id="apkName" class="easyui-validatebox"
					data-options="required:true" value="${apkName}"
					style="width: 190px; height: 25px" /></td>
			</tr>
			<tr>
				<td colspan="4" bgcolor="#F9FEFF" align="center"><input
					type="button" class="bttn" value="保  存" style="width: 50px;"
					onclick="uploadfile();" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
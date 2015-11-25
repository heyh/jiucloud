<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '${pageContext.request.contextPath}/userController/edit',
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
		
		//获得文件信息
		getFileInfo('pic','uariva');
		
		var path = $("#uariva").val();
		$("#img").show().attr("src", "${pageContext.request.contextPath }/upload/uariva/"+path);//显示图片
		
		var isFw = $("#isFw").val();
		if(isFw=="1"){
			$("#rad").attr("checked",true);
		}
	});
	
	//获得上传文件信息
	function getFileInfo(pic,icon){
		$("#"+pic).change(function (){
		       var pname = document.getElementById(pic).files[0].name;
		        $("#"+icon).val(pname);
		        alert(pname);
		});
	}
	
	//上传文件
	function uploadfile(filed){
		parent.$.messager.progress({
				title : '提示',
				text : '数据正在更新，请不要离开此页面，请稍后....'
		}); 
		$.ajaxFileUpload
		(
			{
				url:'${pageContext.request.contextPath }/userController/uploaduariva',
				secureuri:false,
				fileElementId:filed,
				dataType: 'json',
				data:{fileds:filed},
				success: function (data)
				{
					parent.$.messager.progress('close');
					if(data.success){
						$.messager.alert('成功', data.msg, 'info');
						var path = $("#uariva").val();
						$("#img").show().attr("src", "${pageContext.request.contextPath }/upload/uariva/"+path);//显示图片
					}else{
						$.messager.alert('错误', data.msg, 'error');
						parent.$.modalDialog.handler.dialog('refresh');//重新刷新表单
					}
				},error: function (data, status, e)
				{
					$.messager.alert('错误', data.msg, 'error');
					parent.$.messager.progress('close');
				}
			});
	}

	
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="isFw" value="${user.isFw}"/>
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<form id="form" method="post" enctype="multipart/form-data">
			<table class="table table-hover table-condensed">
				<tr>
					<th>编号</th>
					<td><input name="id" type="text" class="span2" value="${user.id}" readonly="readonly"></td>
					<th style="width: 100px;text-align: right;">&nbsp;&nbsp;</th>
					<td>
						<input type="radio" name="isFw" value="1" id="rad" />&nbsp;服务账号
					</td>
				</tr>
				<tr>
					<th style="width: 60px;">登录名</th>
					<td><input name="name" type="text" placeholder="请输入登录名称" readonly="readonly" class="easyui-validatebox span2" data-options="required:true" value="${user.name}"></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th style="width: 60px;">头像</th>
					<td>
					  <input style="width: 190px;"  type="text" name="uariva" id="uariva" value="${user.uariva}" readonly="readonly">
					</td>
					<th style="width: 60px;">名称</th>
					<td><input name="realname" type="text" placeholder="请输入名称" class="easyui-validatebox span2" data-options="required:true" value="${user.realname}"></td>
				</tr>
				<tr style="height: 70px;">
					<th colspan="3" style="text-align: center;">
						<img alt="" src="" id="img" style="display: none;width: 72px;height: 72px;">
					</th>
					<td>
						<input type="file"  name="pic" id="pic" />
					    <a onclick="uploadfile('pic');" href="javascript:void(0);"
				         class="easyui-linkbutton"
				         data-options="plain:true,iconCls:'pencil_add'">上传</a>
					</td>
				</tr>
				<tr>
					<th style="width: 60px;">描述</th>
					<td colspan="3">
						<textarea rows="5" cols="6" name="description">${user.description}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
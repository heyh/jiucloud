<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '${pageContext.request.contextPath}/applicationController/addApp',
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
		
		//获得文件大小  
		getFileInfo('pic','icon');
		getFileInfo('apk','upPath');
		
		
	});
	//获得上传文件信息
	function getFileInfo(pic,icon){
		$("#"+pic).change(function (){
		       var size = document.getElementById(pic).files[0].size;
		       var pname = document.getElementById(pic).files[0].name;
		        size = formatFileSize(size);
		        $("#"+icon).val(pname);
		        $("#appSize").val(size);
		        alert(size+","+pname);
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
				url:'${pageContext.request.contextPath }/applicationController/uploadFile',
				secureuri:false,
				fileElementId:filed,
				dataType: 'json',
				data:{fileds:filed},
				success: function (data)
				{
					parent.$.messager.progress('close');
					if(data.success){
						$.messager.alert('成功', data.msg, 'info');
						var path = $("#icon").val();
						$("#img").show().attr("src", "${pageContext.request.contextPath }/upload/icon/"+path);//显示图片
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
	//计算文件大小
	function formatFileSize(size){
	    var sizF = 'Bytes';
	    if(parseFloat(size) > 1024){
	        //In KB
	        size = parseFloat(size/1024);
	        sizF = 'KB';
	        if(parseFloat(size) > 1024){
	            //In MB
	            size = parseFloat(size/1024);
	            sizF = 'MB';
	        }
	        if(parseFloat(size) > 1024){
	            //In GB
	            size = parseFloat(size/1024);
	            sizF = 'GB';
	        }
	        if(parseFloat(size) > 1024){
	            //In TB
	            size = parseFloat(size/1024);
	            sizF = 'TB';
	        }
	    }
	    size = parseFloat(size).toFixed(1) + ' ' + sizF;
	    return size;
	}
	
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post" enctype="multipart/form-data">
			<table class="table table-hover table-condensed">
				<tr>
					<td style="width: 70px;">应用名称</td>
					<td>
						<input name="appName" type="text" placeholder="请输入应用名称" class="easyui-validatebox span2" data-options="required:true" value="">
						<select id="rep" name="isRep" style="width: 100px;">
							<option value="0">待审核</option>
							<option value="1">立即发布</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">图标<img alt="" src="" id="img" style="display: none;">
					</td>
					<td>
						<input type="text" name="icon" id="icon" value="" readonly="readonly">
						<input type="file"  name="pic" id="pic" />
					    <a onclick="uploadfile('pic');" href="javascript:void(0);"
				         class="easyui-linkbutton"
				         data-options="plain:true,iconCls:'pencil_add'">上传</a>
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">上传路径</td>
					<td>
						<input type="text" name="upPath" id="upPath" value="" readonly="readonly">
						<input type="file"  name="apk" id="apk" />
					    <a onclick="uploadfile('apk');" href="javascript:void(0);"
				         class="easyui-linkbutton"
				         data-options="plain:true,iconCls:'pencil_add'">上传</a>
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">文件大小</td>
					<td>
						<input name="appSize" id="appSize" type="text" placeholder="文件大小" class="easyui-validatebox span2" readonly="readonly" value="">
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">版本号</td>
					<td>
						<input name="versionNum" type="text" placeholder="版本号" class="easyui-validatebox span2" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">包路径</td>
					<td>
						<input style="width:300px;" name="sketch" type="text" placeholder="请输入包路径" class="easyui-validatebox span2" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td style="width: 70px;">内容</td>
					<td>
						<textarea name="content" placeholder="请输入内容" style="width:300px;height:130px;" data-options="required:true" class="easyui-validatebox span2"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
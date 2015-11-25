<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		//ck
		var ck1 = CKEDITOR.replace( 'content',    
				{        
					width: 600,
					height: 290  
		});
		$('#form').form({
			url : '${pageContext.request.contextPath}/informController/addInform',
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
		getFileInfo('pic','picPath');
		
		var path = $("#picPath").val();
		$("#img").show().attr("src", "${pageContext.request.contextPath}/upload/pic/"+path);//显示图片
		
		
		 //初始下拉框选中
		var isopen=$('#isopen').val();
		   $("#isok  option[value="+isopen+"]").attr("selected",true);
		   
		   $("#isok").change( function() {
				$("#time").hide();
				$("#time1").val('');
				if($(this).val()=="2")
					$("#time").show();
			});
		   if(isopen==2){
			   $("#time").show();
		   }
	});
	//获得上传文件信息
	function getFileInfo(pic,icon){
		$("#"+pic).change(function (){
		       var size = document.getElementById(pic).files[0].size;
		       var pname = document.getElementById(pic).files[0].name;
		        size = formatFileSize(size);
		        $("#"+icon).val(pname);
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
				url:'${pageContext.request.contextPath }/informController/uploadPic',
				secureuri:false,
				fileElementId:filed,
				dataType: 'json',
				data:{fileds:filed},
				success: function (data)
				{
					parent.$.messager.progress('close');
					if(data.success){
						$.messager.alert('成功', data.msg, 'info');
						var path = $("#picPath").val();
						$("#img").show().attr("src", "${pageContext.request.contextPath }/upload/pic/"+path);//显示图片
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
		<input type="hidden" id="isopen" value="${info.isok}"/>
		<form id="form" method="post" enctype="multipart/form-data">
			<input type="hidden" name="cdate" value="${info.ctime}"/>
			<input type="hidden" name="id" value="${info.id}"/>
			<table class="table table-hover table-condensed">
				<tr>
					<td style="width: 50px;">标题</td>
					<td>
						<input name="title" style="width: 360px;" type="text" placeholder="请输入标题名称" class="easyui-validatebox span2" data-options="required:true" value="${info.title}">
					</td>
				</tr>
				<tr>
					<td style="width: 55px;">是否推送</td>
					<td>
						<select id="isok" name="isok">
							<option value="0">待审核</option>
							<option value="1">推送</option>
							<option value="2">定时推送</option>
							<option value="3">已推送</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span id="time" style="display: none;line-height: ">
							 <input name="time1"  id="time1" class="Wdate" value="${info.isoktime}"   onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm'})" readonly="readonly"/>
						</span>
					</td>
				</tr>
				<tr style="height: 99px;">
					<td style="width: 50px;">缩略图<img alt="" src="" id="img" style="display: none;width: 72px;height: 72px;">
					</td>
					<td>
						<input type="text" style="width: 360px;" name="picPath" id="picPath" value="${info.picPath}" readonly="readonly">
						<br/>
						<input type="file"  name="pic" id="pic" />
					    <a onclick="uploadfile('pic');" href="javascript:void(0);"
				         class="easyui-linkbutton"
				         data-options="plain:true,iconCls:'pencil_add'">上传</a>
					</td>
				</tr>
				<tr>
					<td style="width: 50px;">内容</td>
					<td>
						<textarea name="content" id="content" placeholder="请输入内容" style="width:300px;height:130px;" class="easyui-validatebox span2">${info.content}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
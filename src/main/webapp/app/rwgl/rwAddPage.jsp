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
			url : '${pageContext.request.contextPath}/rwglController/addrw',
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
					url : '${pageContext.request.contextPath }/rwglController/securi_uploadfj',
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

	var cfg = {
		url : '${pageContext.request.contextPath}/rwglController/securi_getUsers',
		type : 'GET',
		dataType : 'json',
		success : function(dataObj) {
			$("#principal").empty();
			$.each(dataObj, function(idx, item) {
				$(
						"<option value='" + item.id + "'>" + item.username
								+ "</option>").appendTo($("#principal"));
			});
		}
	};

	function getUser() {
		//获取表单值，并以json的数据形式保存到data中  
		cfg.data = {
			id : $("#department").val()
		}
		$.ajax(cfg);
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
					<th>名称</th>
					<td><input type="text" name="title" id="title"
						style="width: 600px;" /></td>
				</tr>
				<tr>
					<th>内容</th>
					<td><textarea rows="13" cols="420" name="content"
							style="width: 600px; resize: none;"></textarea></td>
				</tr>
				<tr>
					<th>时间</th>
					<td><input class="span2" name="beginTime" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" style="width: 150px;" /> &nbsp;-&nbsp;&nbsp;<input
						class="span2" name="endTime" placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly="readonly" style="width: 150px;" /></td>
				</tr>
				<tr>
					<td colspan="3">部门&nbsp;&nbsp;&nbsp;<select id="department"
						name="department" onchange="getUser()">
							<option value="0">--请选择--</option>
							<c:forEach items="${departments}" var="tem">
								<option value="${tem.id}">${tem.name}</option>
							</c:forEach>
					</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 负责人&nbsp;&nbsp;&nbsp;<select
						id="principal" name="principal">
					</select></td>
				</tr>
				<tr>
					<th>附件</th>
					<td colspan="3"><input type="file" name="pic1" id="pic1" /> <a
						onclick="uploadfile('pic1');" href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="plain:true,iconCls:'pencil_add'">上传</a> <input
						type="hidden" name="picUrl" id="picUrl" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$("#att").val('${t.attachmentname}');
		$('#form').form({
			url : '${pageContext.request.contextPath}/rwglController/editrwBycomment',
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
	
    function download() {
        window.location.href = "app/rwgl/DownLoad.jsp?name="+$("#att").val();
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>名称</th>
					<td >
						<input type="text" name="title" id="title" style="width:600px;" readonly="readonly" value="${t.title}"/>
					</td>
				</tr>
				<tr>
					<th>发布人</th>
					<td >
						<input type="text" name="title" id="title" style="width:600px;" readonly="readonly" value="${t.uids}"/>
					</td>
				</tr>
				<tr>
					<th>部门</th>
					<td colspan="3">
						<input type="text" name="title" id="title" style="width:250px;" readonly="readonly" value="${t.department}"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">负责人</span>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" name="title" id="title" style="width:250px;" readonly="readonly" value="${t.principal}"/>
					</td>
				</tr>
				<tr>
					<th>时间</th>
					<td >
						<input type="text" name="title" id="title" style="width:600px;" readonly="readonly" value="${t.time}"/>
					</td>
				</tr>
				<tr>
					<th>内容</th>
					<td >
						<textarea rows="6" cols="420" name="content" style="width:600px;resize:none;" readonly="readonly">${t.content}</textarea>
					</td>
				</tr>
				<tr>
					<th>附件</th>
					<td colspan="3"><a href="javascript:download();" >${t.attachmentname}</a>
					    <a href="javascript:download();" >下载</a>
					</td>
				</tr>
				<tr>
					<th>状态</th>
					<td >
						<input type="text" name="status" id="status" style="width:600px;" readonly="readonly" value="${t.status}"/>
					</td>
				</tr>
				<tr>
					<th>评论</th>
					<td >
						<textarea rows="2" cols="420" name="comments" style="width:600px;resize:none;" >${t.comment}</textarea>
					</td>
				</tr>
				<input type="hidden" id="att">
			</table>
		</form>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		
		//parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
		parent.$.messager.progress('close');
		//parent.$.modalDialog.handler.dialog('close');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed" style="font-size:12px;">
				<tr>
					<td>
					发件人 
					<input name="id" type="hidden" class="span2" value="${messageObject.id}"> </td>
					<td>${messageObject.sendName }</td>
				</tr>
				<tr>
					<td>发件时间</td>
					<td>${messageObject.pdate }</td>
				</tr>
				<tr>
					<td>邮件标题</td>
					<td>${messageObject.title }</td>
				</tr>
				<tr>
					<td>邮件内容</td>
					<td>${messageObject.message }</td>
				</tr>
			</table>
		</form>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post">
			<input name="id" type="hidden" class="span2" value="${pro.id}"
				readonly="readonly">
			<table class="table table-hover table-condensed"
				style="font-size: 12px;">
				<tr>
					<td>姓名</td>
					<td>${detail.name }</td>
				</tr>
				<tr>

					<td>职务</td>
					<td>${detail.job }</td>
				</tr>
				<tr>
					<td>手机</td>
					<td>${detail.phone }</td>
				</tr>
				<tr>

					<td>专业</td>
					<td>${detail.major }</td>
				</tr>
				<tr>
					<td>资格证书</td>
					<td>${detail.qualification }</td>
				</tr>
				<tr>
					<td>个人邮箱</td>
					<td>${detail.email }</td>
				</tr>
				<tr>
					<td>家庭住址</td>
					<td>${detail.address }</td>
				</tr>
			</table>
		</form>
	</div>
</div>
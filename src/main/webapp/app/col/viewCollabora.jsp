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
					<td>单位名称</td>
					<td>${detail.name }</td>
				</tr>
				<tr>
					<td>单位资质</td>
					<td>${detail.power }</td>
				</tr>
				<tr>
					<td>公司电话</td>
					<td>${detail.tel }</td>
				<tr>

					<td>公司地址</td>
					<td>${detail.address }</td>
				</tr>
				<tr>
					<td>所属行业</td>
					<td>${detail.industry }</td>
				</tr>
				<tr>
					<td>企业邮箱</td>
					<td>${detail.email }</td>
				</tr>
				<tr>
					<td>公司介绍</td>
					<td>${detail.remark }</td>
				</tr>
			</table>
		</form>
	</div>
</div>
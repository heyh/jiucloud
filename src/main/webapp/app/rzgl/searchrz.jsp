<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$("#att").val('${t.attachmentname}');
	});
	
    function download() {
        window.location.href = "app/rzgl/DownLoad.jsp?name="+$("#att").val();
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>标题</th>
					<td >
						<input type="text" name="title" id="title" style="width:600px;" readonly="readonly" value="${t.title}"/>
					</td>
				</tr>
				<tr>
					<th>描述</th>
					<td >
						<textarea rows="17" cols="420" name="content" style="width:600px;resize:none;" readonly="readonly">${t.content}</textarea>
					</td>
				</tr>
				<tr>
					<th>附件</th>
					<td colspan="3"><a href="javascript:download();" >${t.attachmentname}</a>
					    <a href="javascript:download();" >下载</a>
					</td>
				</tr>
				<input type="hidden" id="att">
			</table>
		</form>
	</div>
</div>
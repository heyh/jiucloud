<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>费用类型管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/costController/securi_treeGrid',
			cache : false,
			dataType : 'json',
			success : function(data) {
				dataGrid = $('#dataGrid').treegrid({
					idField : 'id',
					treeField : 'costType',
					width : '100%',
					height : '450',
					parentField : 'pid',
					striped : true,
					data : $.parseJSON(data),
					animate : true,

					rownumbers : true,
					columns : [ [ {
						title : '费用类型名称',
						field : 'costType',
						width : 450
					}, {
						title : '费用编码',
						field : 'itemCode',
						width : 300
					} ] ],
					toolbar : '#toolbar',
					onLoadSuccess : function() {
						$('#searchForm table').show();
						parent.$.messager.progress('close');
						$(this).treegrid('collapseAll');
						$(this).treegrid('tooltip');
					},
				});
			}
		});

	});

	function searchFun() {
		window.location.href = '${pageContext.request.contextPath}/costController/searchcost';
	}
</script>
</head>
<body>
	<form id="searchForm"
		action="${pageContext.request.contextPath}/costController/searchcost">
	</form>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">列表展示</a>
	</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html>
<head>
<title>氿上网数据统计</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath}/app/analysis/css-table.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/app/analysis/js/style-table.js"></script>

<script type="text/javascript">
	window.onload = function() {
		parent.$.messager.progress('close');
		$('#searchForm table').show();
		var c = '${price.id }';
		for (var i = 0; i < document.getElementById('price_id').options.length; i++) {
			if (document.getElementById('price_id').options[i].value == c) {
				document.getElementById('price_id').options[i].selected = true;
				break;
			}
		}
	}
	//过滤条件查询
	function searchFun() {
		projectName = $('#pName').val();
		if (projectName == '') {
			alert('请选择工程');
			return;
		}
		if ($('#date').val() == '') {
			alert('请选择日期');
			return;
		}
		$('#searchForm').submit();
	};

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	};
	function selectp() {
		parent.$
				.modalDialog({
					title : '选择项目工程',
					width : 550,
					height : 550,
					href : '${pageContext.request.contextPath}/fieldDataController/securi_selectp',
					buttons : [ {
						text : '确认',
						handler : function() {
							var id = parent.$.modalDialog.handler.find(
									"#proidh").val();
							var name = parent.$.modalDialog.handler.find(
									"#proNameh").val();
							document.getElementById("projectName").value = id;
							document.getElementById("pName").value = name;
							parent.$.modalDialog.handler.dialog('close');
						}
                    } ]

                });
	}
    $(document).ready(function() { $("#price_id").select2({
        placeholder: "可以模糊查询",
        allowClear: true
    }); });
</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 110px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td style="background: #ffffff">日期:&nbsp;<input class="span2"
							name="date" id="date" placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly"
							<c:choose>
							<c:when test="${empty datestr}">value="${first }"</c:when>
							<c:otherwise>value="${datestr }"</c:otherwise>
							</c:choose> />
							- <input class="span2" name="date2" id="date2"
							placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly"
							<c:choose>
							<c:when test="${empty datestr2}">value="${last }"</c:when>
							<c:otherwise>value="${datestr2 }"</c:otherwise>
							</c:choose> /></td>
						<td style="background: #ffffff">工程名称<input type="hidden"
							id="projectName" name="projectName" value='${project_id}' /> <input
							type="text" style="width: 250px;" id="pName" name="pName"
							placeholder="工程名称" class="easyui-validatebox span2"
							data-options="required:true" value="${project.proName }"
							readonly="readonly">&nbsp;&nbsp;&nbsp;&nbsp;<img
							alt="选择工程"
                            src="${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/search-blue.png"
							style="cursor: pointer;" onclick="selectp()"></td>
						<td style="background: #ffffff">价目类型:&nbsp;
                            <select id="price_id" name="price_id">
								<c:forEach items="${prices}" var="tem">
									<option value="${tem.id}">${tem.name}</option>
								</c:forEach>
						    </select>
                            <input type="hidden" id="pcid" value='${price.id }' />
                        </td>

					</tr>
					<tr>
						<td style="text-align: center" colspan=3><a
							href="javascript:void(0);" class="easyui-linkbutton"
							data-options="iconCls:'search_new',plain:true"
							onclick="searchFun();">查询明细</a></td>
					</tr>

				</table>
			</form>
		</div>

		<div id="toolbar" style="display: none;">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">列表展示</a>
		</div>

		<!-- 显示表格内容，list -->
		<div data-options="region:'center',border:false"
			<c:if test="${empty project}"> style="display: none;" </c:if>>

			<table id="travel">
				<thead>
					<%--<tr>--%>
						<%--<th>项目名称</th>--%>
						<%--<th colspan="5">${project.proName }</th>--%>
					<%--</tr>--%>
					<%--<tr>--%>
						<%--<th>价目名称</th>--%>
						<%--<th colspan="5">${price.name }</th>--%>
					<%--</tr>--%>
					<tr>
						<td colspan="6"></td>
					</tr>
					<tr>
						<th colspan="6">费用类型明细</th>
					</tr>
					<tr>
						<th scope="col">序号</th>
						<th scope="col">费用类型</th>
						<th scope="col">单位</th>
						<th scope="col">单价</th>
						<th scope="col">数量</th>
						<th scope="col">金额(价格:元)</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${analysisDatas}" var="tem" varStatus="status">
						<tr>
							<th style="text-align: center; width: 50px">${status.index+1}</th>
							<c:choose>
								<c:when test="${tem.isend==0 }">
									<th style="text-align: center"><font color='red'>${tem.costType}</font></th>
									<td colspan=4></td>
								</c:when>
								<c:otherwise>
									<th style="text-align: center">${tem.costType}</th>
									<td style="text-align: right">${tem.unit}</td>
									<td style="text-align: right"><fmt:formatNumber
											value="${tem.price}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
									<td style="text-align: right"><fmt:formatNumber
											value="${tem.count}" pattern="#"></fmt:formatNumber></td>
									<td style="text-align: right"><fmt:formatNumber
											value="${tem.money}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				<tfoot>
					<tr>
						<th style="text-align: center">合计</th>
						<th style="text-align: center" colspan=4>${price.name}</th>
						<td style="text-align: right"><fmt:formatNumber
								value="${total}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

</body>
</html>
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
<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath}/app/analysis/css-table.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/app/analysis/js/style-table.js"></script>

<script type="text/javascript">
	window.onload = function() {
		parent.$.messager.progress('close');
		$('#searchForm table').show();
		var length = '${fn:length(datas)}';
		if (length == '0') {
			$('#travel').hide();
            $('.easyui-linkbutton').removeAttr('onclick');
		}
	}

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
							document.getElementById("project_id").value = id;
							document.getElementById("pName").value = name;
							parent.$.modalDialog.handler.dialog('close');
						}
                    } ]

                });
	}

	function selectc() {
		parent.$
				.modalDialog({
					title : '选择费用类型',
					width : 500,
					height : 500,
					href : '${pageContext.request.contextPath}/analysisController/securi_selectc',
					buttons : [ {
						text : '确认',
						handler : function() {
							var id = parent.$.modalDialog.handler.find(
									"#proidh").val();
							var name = parent.$.modalDialog.handler.find(
									"#proNameh").val();
							document.getElementById("cost_id").value = id;
							document.getElementById("costTypeName").value = name;
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}

	function searchFun() {
		$('#searchForm').submit();
	}

	function exportFun(objTab) {
		var url = "${pageContext.request.contextPath}/analysisController/securi_execl?startTime="
				+ $('#startTime').val()
				+ "&endTime="
				+ $('#endTime').val()
				+ "&project_id="
				+ $('#project_id').val()
				+ "&cost_id="
				+ $('#cost_id').val()
		window.open(url);
	}

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
	}
</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<div data-options="region:'north',title:'&nbsp;',border:false"
			style="height: 60px; overflow: hidden;">
			<form id="searchForm"
				action='${pageContext.request.contextPath}/analysisController/showTable'
				method='post'>
				<table class="table table-hover table-condensed"
					style="display: none; border: 1px">
					<%-- <tr>
						<td style="background: #ffffff">日期&nbsp;<input class="span2"
							name="startTime" id="startTime" placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly"
							<c:choose>
							<c:when test="${fn:length(analysisSearch.startTime)!=10}">value="${first }"</c:when>
							<c:otherwise>value="${analysisSearch.startTime }"</c:otherwise>
							</c:choose> />
							－<input class="span2" name="endTime" id="endTime"
							placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly"
							<c:choose>
							<c:when test="${fn:length(analysisSearch.endTime)!=10}">value="${last}"</c:when>
							<c:otherwise>value="${analysisSearch.endTime }"</c:otherwise>
							</c:choose> /></td>
						<td style="background: #ffffff">工程名称&nbsp;<input
							type="hidden" id="project_id" name="project_id"
							value="${analysisSearch.project_id}" /> <input type="text"
							style="width: 250px;" id="pName" name="pName" placeholder="工程名称"
							class="easyui-validatebox span2" data-options="required:true"
							value="${analysisSearch.pName}" readonly="readonly">&nbsp;&nbsp;&nbsp;&nbsp;<img
							alt="选择工程"
							src="${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png"
							style="cursor: pointer;" onclick="selectp()"></td>

						<td style="background: #ffffff">费用类型&nbsp;<input
							type="hidden" id="cost_id" name="cost_id"
							value="${analysisSearch.cost_id}" /> <input type="text"
							style="width: 250px;" id="costTypeName" name="costTypeName"
							placeholder="费用类型" class="easyui-validatebox span2"
							data-options="required:true"
							value="${analysisSearch.costTypeName}" readonly="readonly">&nbsp;&nbsp;&nbsp;&nbsp;<img
							alt="选择工程"
							src="${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png"
							style="cursor: pointer;" onclick="selectc()"></td>
					</tr> --%>
					<tr>
						<td style="text-align: left" colspan=4><!-- <a
							href="javascript:void(0);" class="easyui-linkbutton"
							data-options="iconCls:'brick_add',plain:true"
							onclick="searchFun();">查询明细</a> --><a href="javascript:void(0);"
							class="easyui-linkbutton"
							data-options="iconCls:'out_new',plain:true"
							onclick="exportFun(travel);">execl导出</a></td>
					</tr>
				</table>
				<%-- <input class="span2" name="startTime" id="startTime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
				readonly="readonly" type="hidden"
				<c:choose>
					<c:when test="${fn:length(analysisSearch.startTime)!=10}">value="${first }"</c:when>
					<c:otherwise>value="${analysisSearch.startTime }"</c:otherwise>
				</c:choose> />
				<input class="span2" name="endTime" id="endTime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
				readonly="readonly" type="hidden"
					<c:choose>
					<c:when test="${fn:length(analysisSearch.endTime)!=10}">value="${last}"</c:when>
					<c:otherwise>value="${analysisSearch.endTime }"</c:otherwise>
				</c:choose> />
				<input type="hidden" id="project_id" name="project_id" value="${analysisSearch.project_id}" />
				<input type="hidden" id="cost_id" name="cost_id" value="${analysisSearch.cost_id}" /> --%>
			</form>
		</div>
		<!-- 显示表格内容，list -->
		<div data-options="region:'center',border:false">
			<table id="travel">

				<thead>
					<tr>
						<th scope="col" rowspan="2">项目名称</th>
						<th scope="col" colspan="${fn:length(datas[0].priceName)}">价目类型(单位:元)</th>
					</tr>

					<tr>
						<%--<c:forEach items="${prices}" var="tem">--%>
							<%--<th scope="col" align="center">${tem.name}</th>--%>
                            <c:forEach items="${datas[0].priceName}" var="name">
                                <th scope="col" align="center">${name}</th>
						</c:forEach>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${datas}" var="tem">
						<tr>
							<th scope="row" style="text-align: center">${tem.project_name}</th>
							<c:forEach items="${tem.moneys}" var="money">
								<td style="text-align: right">
                                    <fmt:formatNumber value="${money==0 ? '' : money}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				<tfoot>
					<tr>
						<th scope="row">合计</th>
						<c:forEach items="${totals}" var="tem">
							<td style="text-align: right">
                                <fmt:formatNumber value="${tem==0 ? '' : tem}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber>
							</td>
						</c:forEach>
					</tr>
				</tfoot>
			</table>

		</div>
	</div>

</body>
</html>
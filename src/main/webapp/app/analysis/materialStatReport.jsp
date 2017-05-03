<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="sy.model.S_department" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.AbstractList" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	String userId = null;
	JSONArray departments = new JSONArray();

	SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	if (sessionInfo == null) {
		response.sendRedirect(request.getContextPath());
	} else {
		userId = sessionInfo.getId();
		departments = JSONArray.fromObject(sessionInfo.getDepartmentIds());
	}

%>
<!DOCTYPE html>
<html>
<head>
<title>氿上网数据统计</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/app/analysis/css-table.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/app/analysis/js/style-table.js"></script>
	<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.js"></script>

	<script type="text/javascript">

        $(document).ready(function () {
            parent.$.messager.progress('close');

            $('#searchForm table').show();

            $("#projectName").select2({
                allowClear: true,
            });

            monthPicker.create('month_picker', {
                trigger : 'month_trigger',
                autoCommit : true,
                all_month_valid : true,
                callback : function(obj){
                    //设置回调句柄,obj是当前已经选择的时间对象
                    $('#selectedMonth').val(obj);
                }
            });

            $("#month_picker").html('${selectedMonth}');
            $("#selectedMonth").val('${selectedMonth}');

            var optionstring = '';
			var arr =  <%= departments %>;
			if (arr.length < 2) {
                $("#selDepartmentId").hide();
			} else {
                $("#selDepartmentId").show();
			}

            for (var i in arr) {
				if ("${selDepartmentId}" == arr[i].id) {
                    optionstring += "<option value=" + arr[i].id + " selected = 'selected' >" + arr[i].name + "</option>";
				} else {
                    optionstring += "<option value=" + arr[i].id + ">" + arr[i].name + "</option>";
				}

            }
            $("#selDepartmentId").html(optionstring);

        });

//        function selDepartment() {
//            $("#selDepartmentId").val($("#departmentId").val());
//        }
        function searchFun() {
            $('#searchForm').submit();
        }
	</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 110px; overflow: hidden;">
			<form id="searchForm" autocomplete="off">
				<table class="table table-hover table-condensed" style="display: none;">
					<tr>
						<%--<td>选择工程:&nbsp;--%>
							<%--<select name="projectName" id="projectName">--%>
								<%--<option></option>--%>
							<%--</select>--%>
						<%--</td>--%>
						<td>
							<input type="hidden" id="selectedMonth" name="selectedMonth">
							<div class="ta_date" id="div_month_picker">
								<span class="date_title" id="month_picker"></span>
								<a class="opt_sel" id="month_trigger" href="javascript:void(0);">
									<i class="i_orderd"></i>
								</a>
							</div>
							<select id="selDepartmentId" name="selDepartmentId" >
							</select>
							<%--<input type="hidden" id="selDepartmentId" name="selDepartmentId">--%>
						</td>
					</tr>
					<tr>
						<td style="text-align: center" colspan=3>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>
						</td>
					</tr>

				</table>
			</form>
		</div>

		<div id="toolbar" style="display: none;">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">列表展示</a>
		</div>

		<!-- 显示表格内容，list -->
		<div data-options="region:'center',border:false" >
			<table style="width: 100%">
				<thead>
					<tr>
						<th colspan="7">材料汇总统计表</th>
					</tr>
					<tr>
						<th scope="col">序号</th>
						<th scope="col">项目</th>
						<th scope="col">上转</th>
						<th scope="col">本月进帐</th>
						<th scope="col">本月发出</th>
						<th scope="col">售出</th>
                        <th scope="col">月末结存</th>
					</tr>
				</thead>

				<tbody>
					<c:set var="lastMonthCarryoverSum" value="0"></c:set>
					<c:set var="inComeMonthlySum" value="0"></c:set>
					<c:set var="consumeMonthlySum" value="0"></c:set>
					<c:set var="monthEndCarrySum" value="0"></c:set>

					<c:forEach items="${materialStatInfos}" var="item" varStatus="status">
                        <tr>
							<c:set var="lastMonthCarryoverSum" value="${lastMonthCarryoverSum + item.lastMonthCarryover}"></c:set>
							<c:set var="inComeMonthlySum" value="${inComeMonthlySum + item.inComeMonthly}"></c:set>
							<c:set var="consumeMonthlySum" value="${consumeMonthlySum + item.consumeMonthly}"></c:set>
							<c:set var="monthEndCarrySum" value="${monthEndCarrySum + item.monthEndCarry}"></c:set>

							<th style="text-align: center; width: 50px">${status.index+1}</th>
							<th style="text-align: center">${item.costType}</th>
							<td style="text-align: right"><fmt:formatNumber value="${item.lastMonthCarryover}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
							<td style="text-align: right"><fmt:formatNumber value="${item.inComeMonthly}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
							<td style="text-align: right"><fmt:formatNumber value="${item.consumeMonthly}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
							<td style="text-align: right"></td>
							<td style="text-align: right"><fmt:formatNumber value="${item.monthEndCarry}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th style="text-align: center" colspan=2>合计</th>
						<td style="text-align: right"><fmt:formatNumber value="${lastMonthCarryoverSum}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
						<td style="text-align: right"><fmt:formatNumber value="${inComeMonthlySum}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
						<td style="text-align: right"><fmt:formatNumber value="${consumeMonthlySum}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
						<td style="text-align: center"></td>
						<td style="text-align: right"><fmt:formatNumber value="${monthEndCarrySum}" pattern="#.##" minFractionDigits="2"></fmt:formatNumber></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

</body>
</html>
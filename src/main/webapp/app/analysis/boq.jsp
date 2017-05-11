<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="sy.model.S_department" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.AbstractList" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String userId = null;
//    JSONArray departments = new JSONArray();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
//        departments = JSONArray.fromObject(sessionInfo.getDepartmentIds());
    }

%>
<!DOCTYPE html>
<html>
<head>
    <title>氿上网数据统计</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/app/analysis/css-table.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/app/analysis/js/style-table.js"></script>
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.js"></script>

    <script type="text/javascript">

        $(document).ready(function () {
            parent.$.messager.progress('close');

            $('#searchForm table').show();

            $("#projectName").select2({
                allowClear: true,
            });

            var optionstring = '';
            debugger
            var arr =  ${departments};
            debugger
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

        function searchFun() {
            $('#searchForm').submit();
        }

        function exportFun(objTab) {
            var str = '';
            str += '&startDate=' + $('#startDate').val();
            str += '&endDate=' + $('#endDate').val();
            str += '&selDepartmentId=' + $('#selDepartmentId').val();
            var url = "${pageContext.request.contextPath}/analysisController/securi_boqExecl?" + str;
            window.open(url);
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
                    <td>
                        <div class="form-group">
                            <input type="text" name="startDate" id='startDate' placeholder="开始时间"
                                   onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                   value='${first}'/> -
                            <input type="text" name="endDate" id='endDate' placeholder="结束时间"
                                   onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                   value='${last}'/>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <select id="selDepartmentId" name="selDepartmentId" >
                            </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center" colspan=3>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'out_new',plain:true" onclick="exportFun();">execl导出</a>
                    </td>
                </tr>

            </table>
        </form>
    </div>

    <div id="toolbar" style="display: none;">
        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">列表展示</a>
    </div>

    <!-- 显示表格内容，list -->
    <div data-options="region:'center',border:false">
        <table style="width: 100%">
            <%--<thead>--%>
            <%--<tr>--%>
                <%--<th colspan="6" style="font-size: medium">市政养护工程清单一览表</th>--%>
            <%--</tr>--%>
            <%--</thead>--%>
            <%--<tbody></tbody>--%>
            <c:forEach items="${projects}" var="project" varStatus="status">
                <thead>
                <tr>
                    <th colspan="7" style="text-align: left;">工程名称：${project.projectName}</th>
                </tr>
                <tr>
                    <th scope="col">序号</th>
                    <th scope="col">项目编码</th>
                    <th scope="col">项目名称</th>
                    <th scope="col">项目特征描述</th>
                    <th scope="col">计量单位</th>
                    <th scope="col">工程量</th>
                    <th scope="col">备注</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${boq}" var="item" varStatus="status">
                    <c:choose>
                        <c:when test="${item.project_id == project.projectId}">
                            <tr>
                                <th style="text-align: center; width: 50px">${status.index+1}</th>
                                <td style="text-align: center">${item.itemCode}</td>
                                <td style="text-align: center">${item.dataName}</td>
                                <td style="text-align: center">${item.remark}</td>
                                <td style="text-align: center">${item.unit}</td>
                                <td style="text-align: center">${item.count}</td>
                                <td style="text-align: center"></td>
                            </tr>
                        </c:when>
                    </c:choose>

                </c:forEach>
                </tbody>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>
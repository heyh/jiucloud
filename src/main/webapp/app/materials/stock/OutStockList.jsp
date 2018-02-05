<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
//    String underlingUsers = null;
    String projectInfos = null;
    List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = new JSONArray();
    JSONArray billCostTree = new JSONArray();
    boolean hasOnlyReadRight = false;
    boolean hasReadEditRight = false;
    boolean hasOutRight = false;
    boolean hasBackFillRight = false;

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
//        underlingUsers = sessionInfo.getUnderlingUsers();
        projectInfos = sessionInfo.getProjectInfos();
        billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
        for (Map<String, Object> nodeMap : billCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray.add(nodeJson);
        }
        billCostTree = sessionInfo.getBillCostTree();
        hasOnlyReadRight = sessionInfo.getRightList().contains("16") && 0 != sessionInfo.getParentId();
        hasReadEditRight = sessionInfo.getRightList().contains("15") || 0 == sessionInfo.getParentId();
        hasOutRight = sessionInfo.getRightList().contains("17");
        hasBackFillRight = sessionInfo.getRightList().contains("18");
    }

%>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <jsp:include page="../../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>


    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/bootstrap-datepicker.js"
            charset="UTF-8"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"
            charset="UTF-8"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.standalone.css">

    <style>

    </style>
    <script type="text/javascript">

        $(document).ready(function () {

//            $("#projectId").select2({
//                placeholder: "请选择项目",
//                allowClear: true
//            });

            $('.input-append').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                autoclose: true,
                todayHighlight: true,
                maxViewMode: 1
            });

            <%--$.getJSON('${pageContext.request.contextPath}/projectController/securi_getProjects', function (data) {--%>
                <%--var projectInfos = data.obj;--%>
                <%--var optionString = '';--%>
                <%--for (var i in projectInfos) {--%>
                    <%--optionString += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";--%>
                <%--}--%>
                <%--$("#projectId").html('<option>请选择项目</option>' + optionString);--%>
            <%--});--%>

        });

        var dataGrid;
        $(function() {

            dataGrid = $('#dataGrid')
                .datagrid(
                    {
                        url : '${pageContext.request.contextPath}/stockController/securi_dataGrid?type=out',
                        fit : true,
                        fitColumns : false,
                        border : false,
                        pagination : true,
                        idField : 'id',
                        pageSize : 10,
                        pageList : [ 10, 20, 30, 40, 50 ],
                        checkOnSelect : false,
                        selectOnCheck : false,
                        nowrap : false,
                        striped:true,
                        columns : [ [
                            {
                                field : 'id',
                                title : '多选框',
                                width : 100,
                                checkbox : true
                            },
                            {
                                field : 'projectName',
                                title : '项目名称',
                                width : 300
                            },
                            {
                                field : 'mc',
                                title : '材料名称',
                                width : 300
                            },

                            {
                                field : 'specifications',
                                title : '规格型号',
                                width : 200
                            },
                            {
                                field : 'count',
                                title : '出库数量',
                                width : 100
                            },
                            {
                                field : 'dw',
                                title : '单位',
                                width : 100
                            },
                            {
                                field : 'uname',
                                title : '录入人',
                                width : 100
                            },
                            {
                                field : 'createTime',
                                title : '录入时间',
                                width : 200
                            },
                            {
                                field : 'uid',
                                title : '用户ID',
                                width : 100,
                                hidden: true
                            } ] ],
                        toolbar : '#toolbar',
                        onLoadSuccess : function() {
                            $('#searchForm table').show();
                            parent.$.messager.progress('close');
                            $(this).datagrid('tooltip');
                        }
                    });
        });

        //过滤条件查询
        function searchFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //清除条件
        function cleanFun() {
            $('#searchForm input').val('');
            dataGrid.datagrid('load', {});
        }

    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north',title:'查询条件',border:false" style="height: 75px; overflow: hidden;">
        <form id="searchForm"  method="post" role="form" >
            <table class="table table-hover table-condensed" style="display: none;">
                <tr>
                    <td>关键字搜索:&nbsp;
                        <input style="margin-top:10px;width:180px" type="text" id="keyword" name="keyword">
                    </td>
                    <%--<td>工程名称:&nbsp;--%>
                        <%--<select style="margin-top:10px; width:180px" name="projectId" id="projectId">--%>
                        <%--</select>--%>
                    <%--</td>--%>
                    <td>起止日期:&nbsp;
                        <div class="input-append date" style="margin-top:10px">
                            <input type="text" value="${first}" id="startDate" name="startDate" readonly style="width:160px;">
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                        -
                        <div class="input-append date" style="margin-top:10px">
                            <input type="text" value="${last}" id="endDate" name="endDate" readonly style="width:160px;">
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                    </td>

                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false" style="margin-top: 5px">
        <table id="dataGrid"></table>
    </div>
</div>
<div id="toolbar" style="display: none;">
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true" onclick="cleanFun();">清空条件</a>

</div>

</body>
</html>
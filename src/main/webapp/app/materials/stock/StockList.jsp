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
    <title>库存列表</title>
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
                        url : '${pageContext.request.contextPath}/stockController/securi_dataGrid?type=in',
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
                                field : 'stockCount',
                                title : '库存数量',
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
                            },
                            {
                                field : 'action',
                                title : '操作',
                                width : 100,
                                formatter: function (value, row, index) {
                                    var str = '';
                                    str += $
                                        .formatString(
                                            '<img onclick="editFun(\'{0}\');" src="{1}" title="编辑" />',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                    str += '&nbsp;';
                                    str += $
                                        .formatString(
                                            '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');

                                    if (row.count > 0) {
                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="outStockFun(\'{0}\');" src="{1}" title="出库"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/out.png');
                                    }
                                    return str;
                                }
                            } ] ],
                        toolbar : '#toolbar',
                        onLoadSuccess : function() {
                            $('#searchForm table').show();
                            parent.$.messager.progress('close');
                            $(this).datagrid('tooltip');
                        }
                    });
        });

        // 入库
        function addFun() {
            parent.$
                .modalDialog({
                    title: '材料入库',
                    width: 1200,
                    height: 600,
                    href: '${pageContext.request.contextPath}/stockController/securi_toAddStockPage',
                    buttons: [{
                        text: '入库',
                        handler: function () {
                            parent.$.modalDialog.openner_dataGrid = dataGrid; //因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }]
                });
        }

        // 出库
        function outStockFun(id) {
            parent.$
                .modalDialog({
                    title : '材料出库',
                    width : 900,
                    height : 520,
                    href : '${pageContext.request.contextPath}/stockController/securi_outStock?id=' + id,
                    buttons : [ {
                        text : '确定',
                        handler : function() {
                            parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    } ]
                });
        }

        //编辑
        function editFun(id) {
            parent.$
                .modalDialog({
                    title: '编辑',
                    width: 510,
                    height: 300,
                    href: '${pageContext.request.contextPath}/stockController/securi_toUpdateStockPage?stockId=' + id,
                    buttons: [{
                        text: '确认',
                        handler: function () {
                            parent.$.modalDialog.openner_dataGrid = dataGrid; //因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }]
                });
        }

        //删除
        function deleteFun(id) {
            if (id == undefined) {//点击右键菜单才会触发这个
                var rows = dataGrid.datagrid('getSelections');
                id = rows[0].id;
            } else {//点击操作里面的删除图标会触发这个
                dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
            }
            parent.$.messager
                .confirm(
                    '询问',
                    '您是否要删除当前配置？',
                    function (b) {
                        if (b) {
                            parent.$.messager.progress({
                                title: '提示',
                                text: '数据处理中，请稍后....'
                            });
                            $
                                .ajax({
                                    type: "post",
                                    url: '${pageContext.request.contextPath}/stockController/securi_delStock',
                                    data: {
                                        id: id
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.success == true) {
                                            searchFun();
                                        }
                                    }
                                });
                        }
                    });
        }





        function exportFun(objTab) {
            var str = '';
//		str += '&uname=' + $('#uname').val();
            str += '&keyword=' + $('#keyword').val();
            str += '&projectName=' + $('#projectName').val();
            str += '&itemCode=' + $('#itemCode').val();
            str += '&startTime=' + $('#startTime').val();
            str += '&endTime=' + $('#endTime').val();
            var url = "${pageContext.request.contextPath}/fieldDataController/securi_execl?a=1&source=bill"
                + str;
            window.open(url);
        }





        //过滤条件查询
        function searchFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //清除条件
        function cleanFun() {
            $('#searchForm input').val('');
            dataGrid.datagrid('load', {});
        }

        function execlImportFun(id) {
            parent.$
                .modalDialog({
                    title : 'execl导入',
                    width : 450,
                    height : 400,
                    href : '${pageContext.request.contextPath}/fieldDataController/securi_execlProjects',
                    buttons : [{
                        text : '关闭',
                        handler : function() {
                            parent.$.modalDialog.handler.dialog('destroy');
                            parent.$.modalDialog.handler = undefined;
                        }
                    }, {
                        text : '导入',
                        handler : function() {
                            parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }
                    ]
                });
        };

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
    <a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'">材料入库</a>
    <%--<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'out_new',plain:true" onclick="exportFun();">表格导出</a>--%>
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true" onclick="cleanFun();">清空条件</a>

</div>

</body>
</html>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
    String projectInfos = null;
    List<Integer> ugroup = new ArrayList<Integer>();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
        projectInfos = sessionInfo.getProjectInfos();
        ugroup = sessionInfo.getUgroup();
    }

%>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

    <script type="text/javascript">
        var dataGrid;
        $(function () {
            dataGrid = $('#dataGrid')
                .datagrid(
                    {
                        url: '${pageContext.request.contextPath}/clockinginTimeController/securi_dataGrid',
                        fit: true,
                        fitColumns: true,
                        border: false,
                        pagination: true,
                        idField: 'id',
                        pageSize: 10,
                        pageList: [10, 20, 30, 40, 50],
                        checkOnSelect: false,
                        selectOnCheck: false,
                        nowrap: false,
                        columns: [[
                            {
                                field: 'clockinginStartTime',
                                title: '上午',
                                width: 400,
                                align: 'center',
                                formatter: function (value, row, index) {
                                    return row.clockinginStartTime.substring(11, 19) + ' ~ ' + row.clockinginStartTime2.substring(11, 19);
                                }
                            },
                            {
                                field: 'clockinginEndTime2',
                                title: '下午',
                                width: 400,
                                align: 'center',
                                formatter: function (value, row, index) {
                                    return row.clockinginEndTime2.substring(11, 19) + ' ~ ' + row.clockinginEndTime.substring(11, 19);
                                }
                            },
                            {
                                field: 'action',
                                title: '操作',
                                width: 150,
                                align: 'center',
                                formatter: function (value, row, index) {
                                    var str = '';
                                    str += $
                                        .formatString(
                                            '<img onclick="editFun(\'{0}\');" src="{1}" title="修改" />',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                    return str;
                                }
                            }]],
                        toolbar: '#toolbar',
                        onLoadSuccess: function () {
                            $('#searchForm table').show();
                            parent.$.messager.progress('close');
                            $(this).datagrid('tooltip');
                        }
                    });
        });

        function refreshFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //编辑
        function editFun(id) {
            parent.$
                .modalDialog({
                    title: '编辑',
                    width: 550,
                    height: 300,
                    href: '${pageContext.request.contextPath}/clockinginTimeController/securi_updateClockinginTimePage?id=' + id,
                    buttons: [{
                        text: '修改',
                        handler: function () {
                            parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }]
                });
        }

        function searchFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }
    </script>
</head>
<body>

<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'center',border:false">
        <table id="dataGrid"></table>
    </div>
</div>
</body>
</html>
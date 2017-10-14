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
    <title>服务号管理</title>
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
                        url: '${pageContext.request.contextPath}/clockinginController/securi_dataGrid',
                        fit: true,
                        fitColumns: true,
                        border: false,
                        pagination: true,
                        idField: 'id',
                        pageSize: 10,
                        pageList: [10, 20, 30, 40, 50],
                        sortName: 'projectName',
                        sortOrder: 'asc',
                        checkOnSelect: false,
                        selectOnCheck: false,
                        nowrap: false,
                        columns: [[
                            {
                                field: 'id',
                                title: '多选框',
                                width: 100,
                                checkbox: true
                            },
                            {
                                field: 'clockinginDate',
                                title: '日期',
                                width: 250,
                                formatter: function (value, row, index) {
                                    return row.clockinginTime.substring(0, 10);
                                }

                            },
                            {
                                field: 'uname',
                                title: '人员',
                                width: 250

                            },
                            {
                                field: 'clockinginFlag',
                                title: '上/下班',
                                width: 250,
                                formatter: function (value, row, index) {
                                    return value == '0' ? '上班' : '下班';
                                }
                            },
                            {
                                field: 'clockinginTime',
                                title: '时间',
                                width: 250
                            },
                            {
                                field: 'address',
                                title: '地点',
                                width: 250
                            },
                            {
                                field: 'approveState',
                                title: '状态',
                                width: 250
                            },
                            {
                                field: 'reasonDesc',
                                title: '事由',
                                width: 250
                            },
                            {
                                field: 'approveDesc',
                                title: '审核意见',
                                width: 250
                            },
                            {
                                field: 'approveUser',
                                title: '审核人',
                                width: 250
                            },
                            {
                                field: 'approveTime',
                                title: '审核时间',
                                width: 250
                            },
                            {
                                field: 'action',
                                title: '操作',
                                width: 150,
                                formatter: function (value, row, index) {
                                    var str = '';
                                    var userId = <%= userId %>;

                                    if (userId != row.uid) {
                                        str += $
                                            .formatString(
                                                '<img onclick="editFun(\'{0}\');" src="{1}" title="审核" />',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');

                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');

                                    }

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

        //删除
        function deleteFun(id) {
            debugger;
            parent.$.messager
                .confirm(
                    '询问',
                    '您是否要删除当前附件？',
                    function (b) {
                        if (b) {
                            parent.$.messager.progress({
                                title: '提示',
                                text: '数据处理中，请稍后....'
                            });
                            $
                                .ajax({
                                    type: "post",
                                    url: '${pageContext.request.contextPath}/clockinginController/securi_delClockingin?',
                                    data: {
                                        id: id
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.success == true) {
                                            refreshFun();
                                        }
                                    }
                                });
                        }
                    });
        }

        function refreshFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //编辑
        function editFun(id) {
            parent.$
                .modalDialog({
                    title: '编辑',
                    width: 530,
                    height: 480,
                    href: '${pageContext.request.contextPath}/clockinginController/securi_updateClockinginPage?id=' + id,
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

        // 添加
        function addFun() {
            parent.$
                .modalDialog({
                    title: '编辑',
                    width: 450,
                    height: 350,
                    href: '${pageContext.request.contextPath}/itemController/securi_addSectionPage',
                    buttons: [{
                        text: '新增',
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

        $(document).ready(function() {
            $("#clockinginFlag").select2({
                allowClear: true
            });
            $("#approveState").select2({
                allowClear: true
            });
        });


        function exportFun(objTab) {
            var str = '';
            str += '&keyword=' + $('#keyword').val();
            str += '&clockinginFlag=' + $('#clockinginFlag').val();
            str += '&approveState=' + $('#approveState').val();
            str += '&startDate=' + $('#startDate').val();
            str += '&endDate=' + $('#endDate').val();
            var url = "${pageContext.request.contextPath}/clockinginController/securi_execl?" + str;
            window.open(url);
        }
    </script>
</head>
<body>

<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north',title:'查询条件',border:false" style="height: 75px; overflow: hidden;">
        <form id="searchForm">
            <table class="table table-hover table-condensed"
                   style="display: none;">
                <tr>
                    <td>关键字:&nbsp;
                        <input type="text" name="keyword" id="keyword" placeholder="可以模糊查询" style="width: 180px;"/>
                    </td>
                    <td>上/下班:&nbsp;
                        <select  style="width: 180px" name="clockinginFlag" id="clockinginFlag">
                            <option value="">全部</option>
                            <option value="0">上班</option>
                            <option value="1">下班</option>
                        </select>
                    </td>

                    <td>
                    <td>状态:&nbsp;
                        <select id="approveState" name="approveState" style="width: 180px">
                            <option value="">全部</option>
                            <option value="正常">正常</option>
                            <option value="迟到">迟到</option>
                            <option value="早退">早退</option>
                            <option value="事假">事假</option>
                            <option value="病假">病假</option>
                            <option value="出差">出差</option>
                        </select>
                    </td>
                    <td>起始时期:&nbsp;
                        <input style="" class="Wdate span2" name="startDate" id='startDate' placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})" />
                        -
                        <input style="" class="Wdate span2" name="endDate" id='endDate' placeholder="截止时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})" />
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <div data-options="region:'center',border:false">
        <table id="dataGrid"></table>
    </div>

    <div id="toolbar" style="display: none;">
        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>
        <a href="javascript:void(0);" class="easyui-linkbutton"
           data-options="iconCls:'out_new',plain:true"
           onclick="exportFun();">execl导出</a>
    </div>
</div>
</body>
</html>
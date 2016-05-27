<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
    }

%>

<!DOCTYPE html>
<html>
<head>
    <title>发票管理</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
    <script type="text/javascript">
        var dataGrid;
        $(function () {
            dataGrid = $('#dataGrid')
                    .datagrid(
                    {
                        url: '${pageContext.request.contextPath}/ticket/dataGrid?ticketType=' + $("#ticketType").val(),
                        fit: true,
                        fitColumns: true,
                        border: false,
                        pagination: true,
                        idField: 'id',
                        pageSize: 10,
                        pageList: [10, 20, 30, 40, 50],
                        sortName: 'name',
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
                                field: 'ticketName',
                                title: '发票名称',
                                width: 100
                            },
                            {
                                field: 'ticketDate',
                                title: '开票日期',
                                width: 100,
                                formatter: formatDatebox
                            },
                            {
                                field: 'contract',
                                title: '合同',
                                width: 100
                            },
                            {
                                field: 'supplier',
                                title: '供应商名称',
                                width: 100,
                                hidden: $("#ticketType").val() == 0 ? true : false
                            },
                            {
                                field: 'consumer',
                                title: '客户名称',
                                width: 100,
                                hidden: $("#ticketType").val() == 1 ? true : false
                            },
                            {
                                field: 'taxNo',
                                title: '纳税识别号',
                                width: 100
                            },
                            {
                                field: 'address',
                                title: '地址',
                                width: 100
                            },
                            {
                                field: 'taxBank',
                                title: '纳税账户开户银行',
                                width: 100
                            },
                            {
                                field: 'taxAccount',
                                title: '纳税账户开户账号',
                                width: 100
                            },
                            {
                                field: 'taxStatus',
                                title: '纳税资格状况',
                                width: 100
                            },
                            {
                                field: 'unit',
                                title: '单位',
                                width: 100
                            },
                            {
                                field: 'price',
                                title: '单价',
                                width: 100
                            },
                            {
                                field: 'count',
                                title: '数量',
                                width: 100
                            },
                            {
                                field: 'specifications',
                                title: '规格类型',
                                width: 100
                            },
                            {
                                field: 'money',
                                title: '金额',
                                width: 100
                            },
                            {
                                field: 'uname',
                                title: '操作人',
                                width: 100
                            },
                            {
                                field: 'createTime',
                                title: '入库时间',
                                width: 100
                            },
                            {
                                field: 'action',
                                title: '操作',
                                width: 100,
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

        function addFun() {
            var ticketType = $("#ticketType").val();
            var title = '';
            if (ticketType == 0) {
                title = '销项发票';
            } else if (ticketType == 1) {
                title = '进项发票';
            }
            parent.$
                    .modalDialog({
                        title: title,
                        width: 1050,
                        height: 600,
                        href: '${pageContext.request.contextPath}/ticket/toAddPage?ticketType=' + ticketType,
                        buttons: [{
                            text: '添加',
                            handler: function () {
                                parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                                var f = parent.$.modalDialog.handler.find('#form');
                                f.submit();
                            }
                        }]
                    });
        }
        ;

        //编辑
        function editFun(id) {
            parent.$
                    .modalDialog({
                        title: '编辑',
                        width: 1050,
                        height: 600,
                        href: '${pageContext.request.contextPath}/ticket/toEditPage?id=' + id,
                        buttons: [{
                            text: '下一步',
                            handler: function () {
                                parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
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
                                        url: '${pageContext.request.contextPath}/ticket/delete',
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

        //附件管理
        function FileFun(id) {
            parent.$
                    .modalDialog({
                        title: '附件管理',
                        width: 800,
                        height: 600,
                        href: '${pageContext.request.contextPath}/fieldDataController/securi_fieldDataFile?id='
                        + id,
                        buttons: [{
                            text: '关闭',
                            handler: function () {
                                parent.$.modalDialog.handler.dialog('close');
                            }
                        }]
                    });
        }

        function exportFun(objTab) {
            var str = '';
            str += '&keyword=' + $('#keyword').val();
            str += '&startTime=' + $('#startTime').val();
            str += '&endTime=' + $('#endTime').val();
            str += '&ticketType=' + $('#ticketType').val();
            var url = "${pageContext.request.contextPath}/ticket/securi_execl?" + str;
            window.open(url);
        }

        //过滤条件查询
        function searchFun() {
            if ($('#startTime').val() != '' && $('#endTime').val() != '') {
                $('#startTime').val($('#startTime').val().substring(0, 10) + ' 00:00:00');
                $('#endTime').val($('#endTime').val().substring(0, 10) + ' 23:59:59');
            }

            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //清除条件
        function cleanFun() {
            $('#searchForm input').val('');
            dataGrid.datagrid('load', {});
        }

        Date.prototype.format = function (format) {
            var o = {
                "M+": this.getMonth() + 1, // month
                "d+": this.getDate(), // day
                "h+": this.getHours(), // hour
                "m+": this.getMinutes(), // minute
                "s+": this.getSeconds(), // second
                "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
                "S": this.getMilliseconds()
                // millisecond
            }
            if (/(y+)/.test(format))
                format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(format))
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            return format;
        }
        function formatDatebox(value) {
            if (value == null || value == '') {
                return '';
            }
            var dt;
            if (value instanceof Date) {
                dt = value;
            } else {
                dt = new Date(value);
            }

            return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)
        }

        $(document).ready(function () {
            <%--$("#projectName").select2({--%>
            <%--placeholder: "可以模糊查询",--%>
            <%--allowClear: true,--%>
            <%--&lt;%&ndash;data:<%=projectInfos%>&ndash;%&gt;--%>
            <%--});--%>
            <%--$("#costType").select2({--%>
            <%--tags: "true",--%>
            <%--placeholder: "可以模糊查询",--%>
            <%--allowClear: true--%>
            <%--});--%>
        });

    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north',title:'查询条件',border:false"
         style="height: 75px; overflow: hidden;">
        <form id="searchForm">
            <table class="table table-hover table-condensed" style="display: none;">
                <tr>
                    <td>搜索关键字:&nbsp;
                        <input name="keyword" id="keyword" placeholder="模糊查询" class="span2"/>
                    </td>
                    <td>起止时间:&nbsp;<input class="span2" name="startTime"
                                          id='startTime' placeholder="点击选择时间"
                                          onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                                          readonly="readonly" value='${first }'/> - <input class="span2"
                                                                                           name="endTime" id='endTime'
                                                                                           placeholder="点击选择时间"
                                                                                           onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                                                                                           readonly="readonly"
                                                                                           value='${last }'/></td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dataGrid"></table>
    </div>
</div>
<div id="toolbar" style="display: none;">
    <a onclick="addFun();" href="javascript:void(0);"
       class="easyui-linkbutton"
       data-options="plain:true,iconCls:'add_new'">添加</a>
    <a href="javascript:void(0);" class="easyui-linkbutton"
       data-options="iconCls:'out_new',plain:true"
       onclick="exportFun();">execl导出</a><a href="javascript:void(0);"
                                            class="easyui-linkbutton"
                                            data-options="iconCls:'search_new',plain:true"
                                            onclick="searchFun();">过滤条件</a><a
        href="javascript:void(0);" class="easyui-linkbutton"
        data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true"
        onclick="cleanFun();">清空条件</a>
</div>

<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
    <div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
    <div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
    <div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
</div>

<input type="hidden" id="ticketType" name="ticketType" value='${ticketType}'/>
</body>
</html>
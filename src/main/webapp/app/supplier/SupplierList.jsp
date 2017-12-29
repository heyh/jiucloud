<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
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
    <title>库存列表</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>
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
            $("#supplierId").select2({
                placeholder: "请选择供应商",
                allowClear: true
            });
        });

        var dataGrid;
        $(function () {
            dataGrid = $('#dataGrid')
                .datagrid(
                    {
                        url: '${pageContext.request.contextPath}/supplierController/securi_dataGrid',
                        fit: true,
                        fitColumns: false,
                        border: false,
                        pagination: true,
                        idField: 'id',
                        pageSize: 10,
                        pageList: [10, 20, 30, 40, 50],
                        checkOnSelect: false,
                        selectOnCheck: false,
                        nowrap: false,
                        striped: true,
                        columns: [[
                            {
                                field: 'id',
                                title: '多选框',
                                width: 100,
                                checkbox: true
                            },
                            {
                                field: 'name',
                                title: '厂家名称',
                                width: 200
                            },

                            {
                                field: 'tel',
                                title: '企业电话',
                                width: 100
                            },
                            {
                                field: 'addr',
                                title: '企业地址',
                                width: 300
                            },
                            {
                                field: 'linkman',
                                title: '联系人',
                                width: 100
                            },
                            {
                                field: 'linkphone',
                                title: '联系电话',
                                width: 100
                            },
                            {
                                field: 'remark',
                                title: '备注',
                                width: 200
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

        // 新增
        function addFun() {
            parent.$
                .modalDialog({
                    title: '新增',
                    width: 530,
                    height: 400,
                    href: '${pageContext.request.contextPath}/supplierController/securi_toAddPage',
                    buttons: [{
                        text: '确定',
                        handler: function () {
                            parent.$.modalDialog.openner_dataGrid = dataGrid; //因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }]
                });
        }

        //编辑
        function editFun(id) {
            parent.$
                .modalDialog({
                    title: '修改',
                    width: 530,
                    height: 400,
                    href: '${pageContext.request.contextPath}/supplierController/securi_toUpdatePage?supplierId=' + id,
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
                                    url: '${pageContext.request.contextPath}/supplierController/securi_del',
                                    data: {
                                        supplierId: id
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

        //过滤条件查询
        function searchFun() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        //清除条件
        function cleanFun() {
            $('#searchForm input').val('');
            dataGrid.datagrid('load', {});
        }

        $(document).ready(function () {
            $("#supplierId").select2({
                placeholder: "可以模糊查询",
                allowClear: true,
                data:${supplierInfos}
            });
        });
    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north',title:'查询条件',border:false" style="height: 75px; overflow: hidden;">
        <form id="searchForm" method="post" role="form" class="form-inline" style="margin-top: 10px; margin-left: 10px">
            <label>关键字:&nbsp;</label>
            <input type="text" id="keyword" name="keyword">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <label>供应商:&nbsp;</label>
            <select id="supplierId" name="supplierId">
                <option></option>
            </select>
        </form>
    </div>
    <div data-options="region:'center',border:false" style="margin-top: 5px">
        <table id="dataGrid"></table>
    </div>
</div>
<div id="toolbar" style="display: none;">
    <a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'">新增供应商</a>
    <a onclick="searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true">条件查询</a>
    <a onclick="cleanFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true">清空条件</a>
</div>
</body>
</html>
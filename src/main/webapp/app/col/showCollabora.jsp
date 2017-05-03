<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
<head>
    <title>工程名称管理</title>
    <jsp:include page="../../inc.jsp"></jsp:include>

    <script type="text/javascript">
        var dataGrid;
        //Ajax加载数据库中数据，传入rows与total
        $(function () {
            dataGrid = $('#dataGrid')
                .datagrid(
                    {
                        url: '${pageContext.request.contextPath}/collcontroller/securi_dataGrid?pid=${pid}',
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
                        frozenColumns: [[{
                            field: 'name',
                            title: '公司名称',
                            width: 100,
                            sortable: true
                        }]],
                        columns: [[
                            {
                                field: 'sectionName',
                                title: '标段',
                                width: 100
                            },
                            {
                                field: 'industry',
                                title: '所属行业',
                                width: 100
                            },
                            {
                                field: 'tel',
                                title: '公司电话',
                                width: 100
                            },
                            {
                                field: 'address',
                                title: '公司地址',
                                width: 100
                            },
                            {
                                field: 'power',
                                title: '公司资质',
                                width: 100
                            },
                            {
                                field: 'email',
                                title: '公司邮箱',
                                width: 100
                            },
                            {
                                field: 'contact',
                                title: '联系人',
                                width: 100,
                                formatter: function (value, row, index) {
                                    var str = '';
                                    str += $
                                        .formatString(
                                            '<img onclick="contactFun(\'{0}\');" src="{1}" title="查看联系人"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/lianxiren.png');
                                    return str;
                                }
                            },
                            {
                                field: 'action',
                                title: '操作',
                                width: 100,
                                formatter: function (value, row, index) {
                                    var str = '';
                                    str += $
                                        .formatString(
                                            '<img onclick="viewFun(\'{0}\');" src="{1}" title="预览"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/preview-blue.png');
                                    str += '&nbsp;';
                                    str += $
                                        .formatString(
                                            '<img onclick="eidtFun(\'{0}\');" src="{1}" title="修改"/>',
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

        //查看联系人
        function contactFun(id) {
            var url = '${pageContext.request.contextPath}/contactController/securi_toShowPage?pid='
                + id;
            var text = "协作单位联系人管理";
            var params = {
                url: url,
                title: text,
                iconCls: 'wrench'
            }
            window.parent.ac(params);
            parent.$.modalDialog.handler.dialog('close');
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
                    '您是否要删除当前协作单位？',
                    function (b) {
                        if (b) {
                            parent.$.messager.progress({
                                title: '提示',
                                text: '数据处理中，请稍后....'
                            });
                            $
                                .ajax({
                                    type: "post",
                                    url: '${pageContext.request.contextPath}/collcontroller/securi_delete',
                                    data: {
                                        id: id
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.success == true) {
                                            dataGrid.datagrid('load',
                                                {});
                                        }
                                    }
                                });
                        }
                    });
        }
        ;

        //查看
        function viewFun(id) {
            if (id == undefined) {
                var rows = dataGrid.datagrid('getSelections');
                id = rows[0].id;
            } else {
                dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
            }
            parent.$
                .modalDialog({
                    title: '协作单位详情',
                    width: 400,
                    height: 300,
                    href: '${pageContext.request.contextPath}/collcontroller/securi_toViewPage?id='
                    + id

                });
        }

        //编辑
        function eidtFun(id) {
            if (id == undefined) {
                var rows = dataGrid.datagrid('getSelections');
                id = rows[0].id;
            } else {
                dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
            }
            parent.$
                .modalDialog({
                    title: '修改协作单位',
                    width: 400,
                    height: 420,
                    href: '${pageContext.request.contextPath}/collcontroller/securi_toEditPage?id='
                    + id,
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

        //添加
        function addFun() {
            parent.$
                .modalDialog({
                    title: '新增协作单位',
                    width: 400,
                    height: 420,
                    href: '${pageContext.request.contextPath}/collcontroller/securi_toAddPage?pid=${pid}',
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
    </script>


</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 显示表格内容，list -->
    <div data-options="region:'center',border:false">
        <table id="dataGrid"></table>
    </div>
</div>

<!-- toolBar -->
<div id="toolbar" style="display: none;">
    <a onclick="addFun();" href="javascript:void(0);"
       class="easyui-linkbutton"
       data-options="plain:true,iconCls:'add_new'">添加</a>
</div>
</body>
</html>
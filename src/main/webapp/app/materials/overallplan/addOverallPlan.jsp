<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>


<%--<jsp:include page="../../../inc.jsp"></jsp:include>--%>

<%--<!-- 新 Bootstrap 核心 CSS 文件 -->--%>
<%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.css">--%>
<%--<!-- 可选的Bootstrap主题文件（一般不用引入） -->--%>
<%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.css">--%>
<%--<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->--%>
<%--<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.js"></script>--%>
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/css/layui.css"/>

<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath }/jslib/bootstrap-2.3.1/css/bootstrap.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/bootstrap-2.3.1/js/bootstrap.js"></script>


<style>
    .table_style tbody tr:nth-child(even) td,
    .table_style tbody tr:nth-child(even) th {
        background-color: #fff;
    }

    .table_style tbody tr:nth-child(odd) td,
    .table_style tbody tr:nth-child(odd) th {
        background-color: #eee;
    }
</style>

<script type="text/javascript">

    parent.$.messager.progress('close');

    var _url = '${pageContext.request.contextPath}/materialsController/securi_materialsTreeGrid';
    var _clickUrl = '${pageContext.request.contextPath}/materialsController/securi_materialsTreeGridChild';
    var dataGrid;
    $(function () {
        dataGrid = $('#dataGrid')
            .treegrid(
                {
                    url: _url,
                    method: 'get',
                    idField: 'id',
                    treeField: 'mc',
                    iconCls: 'icon-ok',
                    pageSize: 300,
                    pageList: [300, 600, 900],
                    rownumbers: true,
                    animate: true,
                    striped: true,//隔行变色,
                    collapsible: true,
                    fitColumns: true,
                    pagination: true,
                    lines: false,
                    dnd: true,
                    onlyLeafCheck: true,
                    cascadeCheck: false,
                    columns: [[
                        {
                            titile:'ID',
                            field: 'id',
                            width: 200,
                            hidden:true
                        },
                        {
                            title: '材料名称',
                            field: 'mc',
                            width: 200,
                            formatter: function (value, row, index) {
                                if (row.state == 'closed') {
                                    return row.mc;
                                } else {
                                    return "<input type='checkbox' id=" + "gridtree_row_" + row.id + " />" + row.mc;
                                }
                            }
                        },

                        {
                            title: '规格型号',
                            field: 'specifications',
                            width: 200
                        },
                        {
                            title: '单位',
                            field: 'dw',
                            width: 100
                        }
                    ]],

                    onBeforeLoad: function (row, param) {
                        if (row) $(this).treegrid('options').url = _clickUrl;
                    },
                    toolbar: '#toolbar',
                    onLoadSuccess: function (data) {
                        delete $(this).treegrid('options').queryParams['id'];
                        $('#searchForm table').show();
                        $(this).treegrid('collapseAll');
                        parent.$.messager.progress('close');
                        $(this).treegrid('tooltip');
                    },
                    onClickRow: function (row) {
                        if ($('#' + "gridtree_row_" + row.id).attr("checked")) {
                            $('#' + "gridtree_row_" + row.id).attr("checked", "true");

                            add(row);
                        } else {
                            $('#' + "gridtree_row_" + row.id).removeAttr("checked");

                            del(row.id);
                        }
                    },
                });
    });

    function add(row) {
        if (document.getElementById("norecord") != undefined) {
            document.getElementById("mainbody").removeChild(document.getElementById("norecord"));
        }
        var trObj = document.createElement("tr");
        trObj.id = "tr_" + row.id;
        trObj.innerHTML =
            "<td style='text-align:center;'>" + document.getElementById("overPlanTable").rows.length + "</td>" +
            "<td>" + row.mc + "</td>" +
            "<td>" + row.specifications + "</td>" +
            "<td style='text-align:center;'><input type='text' class='layui-input' style='margin-bottom:0px;width: 50px'></td>" +
            "<td>" + row.dw + "</td>" +
            "<td>" + row.dw + "</td>" +
            "<td style='display: none;'>" + row.id + "</td>" +
            "<td style='text-align:center; '><button class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + row.id + ")'><i class='layui-icon'></i>删除</button></td>";
        document.getElementById("mainbody").appendChild(trObj);
    }

    function del(_id) {
        document.getElementById("mainbody").removeChild(document.getElementById("tr_" + _id));
        for (var i = 1; i < document.getElementById("overPlanTable").rows.length; i++) {
            document.getElementById("overPlanTable").rows[i].cells[0].innerHTML = i;
        }
        $('#' + "gridtree_row_" + _id).removeAttr("checked");

        if (document.getElementById("overPlanTable").rows.length == 1) {
            var trObj = document.createElement("tr");
            trObj.id = "norecord";
            trObj.innerHTML = "<td colspan='100' style='text-align:center;'>温馨提示:勾选左侧材料库材料，添加材料计划!</td>";
            document.getElementById("mainbody").appendChild(trObj);
        }
    }

    function geneOverallPlan() {
        var tableObj = document.getElementById("overPlanTable");
        var tableInfo = [];
        for (var i = 1; i < tableObj.rows.length; i++) {
            var rowInfo = {};
            var _id = tableObj.rows[i].cells[0].innerText;
            var _mc = tableObj.rows[i].cells[1].innerText;
            var _specifications = tableObj.rows[i].cells[2].innerText;
            var _count = tableObj.rows[i].cells[3].firstElementChild.value;
            var _dw = tableObj.rows[i].cells[4].innerText;
            var _supplier = tableObj.rows[i].cells[5].innerText;
            var _materialsId = tableObj.rows[i].cells[6].innerText;

            rowInfo.id = _id;
            rowInfo.mc = _mc;
            rowInfo.specifications = _specifications;
            rowInfo.count = _count;
            rowInfo.dw = _dw;
            rowInfo.supplier = _supplier;
            rowInfo.materialsId = _materialsId;

            tableInfo.push(rowInfo);
        }
        $('#overallPlanInfo').val(JSON.stringify(tableInfo));
    }

    $(function () {
        parent.$.messager.progress('close');
        $('#form').form({
            url: '${pageContext.request.contextPath}/overallPlanController/securi_geneOverallPlan',
            onSubmit: function () {
                geneOverallPlan();
                parent.$.messager.progress({
                    title: '提示',
                    text: '数据处理中，请稍后....'
                });
                var isValid = $(this).form('validate');
                if (!isValid) {
                    parent.$.messager.progress('close');
                }
                return isValid;
            },
            success: function (result) {
                parent.$.messager.progress('close');
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.messager.progress('close');
                    jQuery.messager.show({
                        title:'温馨提示:',
                        msg:'添加成功!',
                        timeout:3000,
                        showType:'show'
                    });
//                    parent.$.modalDialog.handler.overallPlan($('#projectId').val());//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });
</script>

<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-md4">
            <blockquote class="layui-elem-quote"><a style="font-size:16px;">材料库</a></blockquote>
            <div data-options="region:'center',border:false">
                <table id="dataGrid"></table>
            </div>
        </div>
        <form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
            <input type="hidden" id="projectId" name="projectId" value="${proId}"/>
            <div class="layui-col-md8">
                <blockquote class="layui-elem-quote" style="text-align: center"><a style="font-size:16px;">${proName} 材料总计划</a>
                </blockquote>
                <table class="table_style table table-striped table-bordered table-hover table-condensed" id="overPlanTable">
                    <thead>
                    <tr>
                        <th style="text-align:center; ">序号</th>
                        <th style="text-align:center; ">材料名称</th>
                        <th style="text-align:center; ">规格型号</th>
                        <th style="text-align:center; ">数量</th>
                        <th style="text-align:center; ">单位</th>
                        <th style="text-align:center; ">供应商</th>
                        <th style="display: none;"></th>
                        <th style="text-align:center; ">操作</th>
                    </tr>
                    </thead>
                    <tbody id="mainbody">
                    <tr id="norecord">
                        <td colspan='100' style='text-align:center;'>温馨提示:勾选左侧材料库材料，添加材料计划!</td>
                    </tr>
                    </tbody>
                </table>
                <input type="hidden" id="overallPlanInfo" name="overallPlanInfo">
                <div style='text-align:right;'>
                    <button class='layui-btn layui-btn-normal layui-btn-radius' onclick="geneOverallPlan();">确    定</button>
                </div>
            </div>
        </form>
    </div>
</div>

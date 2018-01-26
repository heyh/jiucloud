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

<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/skin/default/layer.css" media="all"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>

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

    var _url = '${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid';
    var _clickUrl = '${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGridChild';
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
//                    collapsible: true,
//                    fitColumns: true,
                    pagination: true,
                    lines: false,
                    dnd: true,
                    onlyLeafCheck: true,
                    cascadeCheck: false,
                    columns: [[
                        {
                            title: '材料名称',
                            field: 'mc',
                            width: 250,
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
                            width: 100
                        },
                        {
                            title: '单位',
                            field: 'dw',
                            width: 90
                        },
                        {
                            titile: 'ID',
                            field: 'id',
                            width: 10,
                            hidden: true
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';

                                str += $
                                    .formatString(
                                        ' <img style="cursor:pointer" onclick="addNodeFun(\'{0}\');" src="{1}" title="添加"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/addchild-blue.png');

                                str += '&nbsp;';
                                str += $
                                    .formatString(
                                        ' <img style="cursor:pointer" onclick="editNodeFun(\'{0}\');" src="{1}" title="修改"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');

                                str += '&nbsp;';
                                str += $
                                    .formatString(
                                        ' <img style="cursor:pointer" onclick="delNodeFun(\'{0}\');" src="{1}" title="删除"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');

                                return str;
                            }
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

    function searchFun() {
        $.post('${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid',
            {keyword: $('#keyword').val()},
            function (data) {
                dataGrid.treegrid('loadData', data);
            },
            'json');
    }

    // 增加结点
    function addNodeFun(pid) {

        layer.open({
            type: 1,
            title: '材料库维护-增加材料',
            content: '<div style="text-align: center; margin-top: 10px;margin-left: 40px" id="addChildDiv">' +
                        '<div class="controls">' +
                            '<input type="text" id="mc_add" placeholder="材料名称">' +
                        '</div>' +
                        '<div class="controls">' +
                            '<input type="text" id="specifications_add" placeholder="规格型号">' +
                        '</div>' +
                        '<div class="controls">' +
                            '<input type="text" id="dw_add" placeholder="单位">' +
                        '</div>' +
                     '</div>',
            btn: '确定',
            btnAlign: 'r',
            shade: 0.3,
            area: ['300px', '260px'],
            yes: function () {
                debugger;
                if ($('#mc_add').val() == '') {
                    layer.alert('材料名称必填！', {icon: 2});
                    return;
                }
                $.ajax({
                    url: '${pageContext.request.contextPath}/materialManageController/securi_addNode',
                    type: 'post',
                    data: {
                        pid: pid,
                        mc: $('#mc_add').val(),
                        specifications: $('#specifications_add').val(),
                        dw: $('#dw_add').val()
                    },
                    dataType: 'json',
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (data) {
                        if (data.rspCode == '0') {
                            layer.closeAll();
                            layer.msg('增加成功!');
                            searchFun();
                            $('#mc_add').val('');
                            $('#specifications_add').val('');
                            $('#dw_add').val('');
                        }
                    }
                });
            }
        });
    }

    //删除
    function delNodeFun(id) {
        layer.confirm('确认删除此材料?', {
                btn: ['确定', '取消']
            }, function () {
                $.ajax({
                    url: '${pageContext.request.contextPath}/materialManageController/securi_delNode',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        'id': id
                    },
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (data) {
                        if (data.rspCode == '0') {
                            layer.closeAll();
                            layer.msg('删除成功!');
                            searchFun();
                        }
                    }
                });
            },
            function () {
            });
    }

    //编辑
    function editNodeFun(id) {
        $.getJSON('${pageContext.request.contextPath}/materialManageController/securi_getMaterialsById?id=' + id, function (data) {
            var materials = data.materials;
            $('#mc_edit').val(materials.mc.trim());
            $('#specifications_edit').val(materials.specifications.trim());
            $('#dw_edit').val(materials.dw.trim());

            layer.open({
                type: 1,
                title: '材料库维护-修改材料',
                content: '<div style="text-align: center; margin-top: 10px;margin-left: 40px" id="editChildDiv">' +
                            '<div class="controls">' +
                                '<input type="text" id="mc_edit" placeholder="材料名称" value=' + materials.mc.trim() + '>' +
                            '</div>' +
                            '<div class="controls">' +
                                '<input type="text" id="specifications_edit" placeholder="规格型号" value=' + materials.specifications.trim() +  '>' +
                            '</div>' +
                            '<div class="controls">' +
                                '<input type="text" id="dw_edit" placeholder="单位" value=' + materials.dw.trim() +  '>' +
                            '</div>' +
                        '</div>',
                btn: '确定',
                btnAlign: 'r',
                shade: 0.3,
                area: ['300px', '260px'],
                yes: function () {
                    if ($('#_mc').val() == '') {
                        layer.alert('材料名称必填！', {icon: 2});
                        return;
                    }
                    $.ajax({
                        url: '${pageContext.request.contextPath}/materialManageController/securi_editNode',
                        type: 'post',
                        data: {id: id, mc: $('#mc_edit').val(), specifications: $('#specifications_edit').val(), dw: $('#dw_edit').val()},
                        dataType: 'json',
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        success: function (data) {
                            if (data.rspCode == '0') {
                                layer.closeAll();
                                layer.msg('修改成功!');
                                searchFun();
                            }
                        }
                    });
                }
            });
        });
    }

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
            "<td style='text-align:center;'><input required type='number' class='layui-input' style='margin-bottom:0px;width: 50px;text-align: right'></td>" +
            "<td>" + row.dw + "</td>" +
            "<td style='display: none;'>" + row.id + "</td>" +
            "<td style='text-align:center; '><input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + row.id + ")' value='删除'/></td>";
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
            var _materialsId = tableObj.rows[i].cells[5].innerText;

            if (_count == '') {
               layer.msg(_mc + '(' + _specifications + ')' + " 数量不能为空！");
               return;
            }
            rowInfo.id = _id;
            rowInfo.mc = _mc;
            rowInfo.specifications = _specifications;
            rowInfo.count = _count;
            rowInfo.dw = _dw;
            rowInfo.materialsId = _materialsId;

            tableInfo.push(rowInfo);
        }
        $('#overallPlanInfo').val(JSON.stringify(tableInfo));

        $.ajax({
            url: '${pageContext.request.contextPath}/fieldDataController/securi_chooseApprove',
            type: 'post',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (data) {

                if (data.success) {
                    var optionstring = '';
                    var users = data.obj;
                    for (var i in users) {
                        optionstring += "<option value=\"" + users[i].id + "\" >" + users[i].username + "</option>";
                    }
                    $("#currentApprovedUserRef").html(optionstring);
                }
            }
        });

        layer.open({
            type: 1,
            title: '审批人选择',
            content: '<div style="text-align: center; margin-top: 30px"><select id="currentApprovedUserRef" name="currentApprovedUserRef"></select></div>',
            btn: '确定',
            btnAlign: 'c',
            shade: 0.3,
            area: ['250px', '180px'],
            yes: function () {
                parent.$.messager.progress({title: '提示', text: '数据处理中，请稍后....'});
                $.ajax({
                    url: '${pageContext.request.contextPath}/overallPlanController/securi_geneOverallPlan',
                    type: 'post',
                    data: {overallPlanInfo: JSON.stringify(tableInfo), projectId: $('#projectId').val(), currentApprovedUser: $('#currentApprovedUserRef').val()},
                    dataType: 'json',
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (data) {
                        parent.$.messager.progress('close');
                        if (data.success) {
                            layer.closeAll();
                            jQuery.messager.show({
                                title:'温馨提示:',
                                msg:'添加成功!',
                                timeout:3000,
                                showType:'show'
                            });
                            parent.$.modalDialog.handler.dialog('close');

                        }
                    }
                });
            }
        });
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
                        title: '温馨提示:',
                        msg: '添加成功!',
                        timeout: 3000,
                        showType: 'show'
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
        <div class="layui-col-xs6">
            <blockquote class="layui-elem-quote" style="height: 25px">
                <a style="font-size:16px;">材料库</a>
                <input type="text" class="input-medium search-query" style="margin-left: 10px" id="keyword" name="keyword">
                <button class="layui-btn layui-btn-sm layui-btn-normal layui-btn-radius" onclick="searchFun()">搜索</button>
            </blockquote>

            <div data-options="region:'center',border:false">
                <table id="dataGrid"></table>
            </div>
        </div>

        <div class="layui-col-xs6">
            <form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
                <input type="hidden" id="projectId" name="projectId" value="${proId}"/>
                <blockquote class="layui-elem-quote" style="text-align: center;height: 25px">
                    <a style="font-size:16px;">${proName}</a>&nbsp;&nbsp;材料总体计划
                </blockquote>
                <table class="table_style table table-striped table-bordered table-hover table-condensed"
                       id="overPlanTable">
                    <thead>
                    <tr>
                        <th style="text-align:center; ">序号</th>
                        <th style="text-align:center; ">材料名称</th>
                        <th style="text-align:center; ">规格型号</th>
                        <th style="text-align:center; ">数量</th>
                        <th style="text-align:center; ">单位</th>
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
                <%--<input type="hidden" id="currentApprovedUserRef" name="currentApprovedUserRef">--%>
            </form>
            <div style='text-align:right;'>
                <input type="button" class='layui-btn layui-btn-normal layui-btn-radius' onclick="geneOverallPlan();" value="确定"/>
            </div>
        </div>

    </div>
</div>
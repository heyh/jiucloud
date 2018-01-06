<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>layui</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:include page="../../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/css/layui.css" media="all"/>
    <script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/layui.js"></script>

    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/skin/default/layer.css" media="all"/>
    <script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>

    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

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

        $(document).ready(function () {

            $("#proId").select2({
                placeholder: "请选择项目",
                allowClear: true
            });
            $('.input-append').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                autoclose: true,
                todayHighlight: true,
                maxViewMode: 1
            });

            searchFun();
        });


        $.getJSON('${pageContext.request.contextPath}/projectController/securi_getProjects', function (data) {
            var projectInfos = data.obj;
            var optionString = '';
            for (var i in projectInfos) {
                optionString += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
            }
            $("#projectId").html(optionString);
            $("#proId").html('<option/>' + optionString);
        });

        function searchFun() {
            $('#monthPlanDetailsTable').hide();

            var searchParam = {'projectId': $('#proId').val(), 'startDate': $('#startDate').val(), 'endDate': $('#endDate').val()};
            document.getElementById("monthPlanTabBody").innerHTML = '';
            $.getJSON('${pageContext.request.contextPath}/monthPlanController/securi_getApproveMonthPlanList', searchParam, function (data) {
                if (data.length > 0) {
                    for (var i in data) {
                        var row = data[i];
                        var trObj = document.createElement("tr");
                        var _id = document.getElementById("monthPlanTable").rows.length;
                        trObj.id = "tr_" + _id;
                        trObj.innerHTML =
                            "<td style='text-align:center;'>" + _id + "</td>" +
                            "<td>" + row.projectName + "</td>" +
                            "<td style='display: none;'>" + row.projectId + "</td>" +
                            "<td style='display: none;'>" + row.overallPlanId + "</td>" +
                            "<td style='display: none;'>" + row.id + "</td>" +
                            "<td>" + row.uname + "</td>" +
                            "<td style='text-align:center;'>" + row.createTime + "</td>" +
                            "<td style='text-align:center; '>" +
                                "<input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='detailFun(" + row.id + ")' value='明细'/>" +
                                "<input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='approveMonthPlanFun(" + row.id + "," + 2 + ")' value='结束审批'/>" +
                                "<input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='approveMonthPlanFun(" + row.id + "," + 8 + ")' value='继续审批'/>" +
                                "<input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='approveMonthPlanFun(" + row.id + "," + 9 + ")' value='审批驳回'/>" +
                            "</td>";
                            document.getElementById("monthPlanTabBody").appendChild(trObj);
                    }
                }
            });
        }

        function detailFun(monthPlanId) {
            $('#monthPlanDetailsTable').show();

            document.getElementById("monthPlanDetailsTabBody").innerHTML = '';
            $.getJSON('${pageContext.request.contextPath}/monthPlanController/securi_monthPlanDetailsList?monthPlanId=' + monthPlanId , function (data) {
                if (data.length > 0) {
                    for (var i in data) {
                        var row = data[i];
                        var trObj = document.createElement("tr");
                        var _id = document.getElementById("monthPlanDetailsTable").rows.length;
                        trObj.id = "tr_" + _id;
                        trObj.innerHTML =
                            "<td style='text-align:center;'>" + _id + "</td>" +
                            "<td>" + row.mc + "</td>" +
                            "<td>" + row.specifications + "</td>" +
                            "<td>" + row.dw + "</td>" +
                            "<td>" + row.count + "</td>" +
                            "<td style='text-align:right; '>" + row.price + "</td>" +
                            "<td style='text-align:right; '>" + row.total + "</td>" +
                            "<td>" + row.supplierName + "</td>" +
                            "<td style='text-align:center; '><input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='supplierDetail(" + row.supplierId + ")' value='详情'/></td>";
                        document.getElementById("monthPlanDetailsTabBody").appendChild(trObj);
                    }
                }
            });
        }

        function supplierDetail(supplierId) {
            layer.open({
                type: 1,
                title: '供应商信息',
                closeBtn: 1,
                shadeClose: true,
                area: ['300px', '220px'],
                content: $('#supplierDetailDiv')
            });

            $.ajax({
                url: '${pageContext.request.contextPath}/supplierController/securi_detail?supplierId=' + supplierId,
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {

                    if (data.success) {
                        debugger;
                        var supplier = data.obj;
                        $('#supplierName').html(supplier.name);
                        $('#supplierTel').html(supplier.tel);
                        $('#supplierAddr').html(supplier.addr);
                        $('#supplierLinkMan').html(supplier.linkman);
                        $('#supplierLinkPhone').html(supplier.linkphone);
                    }
                }
            });
        }

        function approveMonthPlanFun(monthPlanId, approvedState) {
            var approvedTip = '';
            if (approvedState == '2') {
                approvedTip = '确认审批通过,并结束后续审批?';
            } else if (approvedState == '8') {
                approvedTip = '确认审批通过,并继续后续审批?';
            } else if (approvedState == '9') {
                approvedTip = '确认驳回当前记录?';
            }

            layer.confirm(approvedTip, {
                    btn: ['确定', '取消']
                }, function () {
                    layer.open({
                        type: 1,
                        title: '审批',
                        content: $('#approveMonthPlanDiv'),
                        btn: ['确定', '取消'],
                        btnAlign: 'right',
                        shade: 0.3,
                        area: ['500px', '260px'],
                        yes: function () {
                            layer.closeAll();
                            $.ajax({
                                url: '${pageContext.request.contextPath}/overallPlanController/securi_approveMonthPlan',
                                type: 'post',
                                dataType: 'json',
                                data: {
                                    'monthPlanId': monthPlanId,
                                    'approvedState': approvedState,
                                    'approvedOption': $('#approvedOption').val(),
                                    'currentApprovedUser': $('#currentApprovedUser').val()
                                },
                                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                                success: function (data) {
                                    if (data.success) {
                                        searchFun();
                                        layer.msg(data.msg);
                                    }
                                }
                            });
                        }
                    });

                },
                function () {
                });
        }
    </script>

</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs12">
            <table class="table_style table table-striped table-bordered table-hover table-condensed"
                   id="monthPlanTable">
                <caption>
                    <blockquote class="layui-elem-quote" style="text-align: left"><span style="color:#2ba1fc;">查询区</span>
                        &nbsp;&nbsp;&nbsp;
                        <select id="proId" name="proId">
                        </select>
                        &nbsp;&nbsp;&nbsp;
                        <div class="input-append date" style="margin-top: 10px">
                            <input type="text" value="${first}" id="startDate" name="startDate" readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                        -
                        <div class="input-append date" style="margin-top: 10px">
                            <input type="text" value="${last}" id="endDate" name="endDate" readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                        &nbsp;&nbsp;&nbsp;
                        <button class='layui-btn layui-btn-normal layui-btn-radius' onclick="searchFun();">查询</button>

                    </blockquote>
                    <a style="font-size:20px;">
                        <span>采购计划</span>
                    </a>
                </caption>
                <thead>
                <tr>
                    <th style="text-align:center; ">序号</th>
                    <th style="text-align:center; ">项目名称</th>
                    <th style="display: none;">项目ID</th>
                    <th style="display: none;">总体计划ID</th>
                    <th style="display: none;">采购计划ID</th>
                    <th style="text-align:center; ">录入人</th>
                    <th style="text-align:center; ">录入时间</th>
                    <th style="text-align:center; ">操作</th>
                </tr>
                </thead>
                <tbody id="monthPlanTabBody">
                </tbody>
            </table>
        </div>

        <div class="layui-col-xs12">
            <table class="table_style table table-striped table-bordered table-hover table-condensed" id="monthPlanDetailsTable" style="display: none;">
                <caption>
                    计划明细
                </caption>
                <thead>
                <tr>
                    <th style="text-align:center; ">序号</th>
                    <th style="text-align:center; ">材料名称</th>
                    <th style="text-align:center; ">规格型号</th>
                    <th style="text-align:center; ">单位</th>
                    <th style="text-align:center; ">数量</th>
                    <th style="text-align:center; ">单价</th>
                    <th style="text-align:center; ">总价</th>
                    <th colspan="2" style="text-align:center; ">供应商</th>
                </tr>
                </thead>
                <tbody id="monthPlanDetailsTabBody">
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<div id="supplierDetailDiv" style="display: none;">
    <table class="table_style table table-striped table-bordered table-hover table-condensed">
        <tr>
            <td style="font-weight: bold" width="40%">供应商名称</td>
            <td><span id="supplierName"></span></td>
        </tr>
        <tr>
            <td style="font-weight: bold" width="40%">供应商电话</td>
            <td><span id="supplierTel"></span></td>
        </tr>
        <tr>
            <td style="font-weight: bold" width="40%">供应商地址</td>
            <td><span id="supplierAddr"></span></td>
        </tr>
        <tr>
            <td style="font-weight: bold" width="40%">联系人</td>
            <td><span id="supplierLinkMan"></span></td>
        </tr>
        <tr>
            <td style="font-weight: bold" width="40%">联系电话</td>
            <td><span id="supplierLinkPhone"></span></td>
        </tr>
    </table>
</div>

<div id="approveMonthPlanDiv" style="display: none;">
    <form class="form-horizontal" style="margin-top: 20px">
        <div class="control-group">
            <label class="control-label" for="approvedOption">审批意见:</label>
            <div class="controls">
                <textarea rows="3" id="approvedOption" name="approvedOption" style="width: 218px"></textarea>
                </input>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="currentApprovedUser">审批人:</label>
            <div class="controls">
                <select id="currentApprovedUser" name="currentApprovedUser">
                </select>
            </div>
        </div>
    </form>
</div>
</html>
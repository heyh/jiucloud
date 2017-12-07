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
            $("#projectId").select2({
                placeholder: "请选择项目",
                allowClear: true
            });

            $("#proId").select2({
                placeholder: "请选择项目",
                allowClear: true
            });
        });


        $.getJSON('${pageContext.request.contextPath}/projectController/securi_getProjects', function (data) {
            var projectInfos = data.obj;
            var optionString = '';
            for (var i in projectInfos) {
                optionString += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
            }
            $("#projectId").html(optionString);
            $("#proId").html(optionString);


        });

        layer.open({
            type: 1,
            title: '项目选择',
            content: '<div style="text-align: center; margin-top: 30px"><select id="projectId" name="projectId"></select></div>',
            btn: '确定',
            btnAlign: 'c',
            shade: 0.3,
            area: ['250px', '180px'],
            yes: function () {
                layer.closeAll();

                var projectId = $('#projectId').val();

                $("#proId").select2().val(projectId).trigger("change");
            }
        });

        function overallPlan(projectId) {
            document.getElementById("mainbody").innerHTML = '';
            $.getJSON('${pageContext.request.contextPath}/overallPlanController/securi_overallPlanList?projectId=' + projectId, function (data) {
                if (data.length > 0) {
                    for (var i in data) {
                        var row = data[i];
                        var trObj = document.createElement("tr");
                        var _id = document.getElementById("overPlanTable").rows.length;
                        trObj.id = "tr_" + _id;
                        trObj.innerHTML =
                            "<td style='text-align:center;'>" + _id + "</td>" +
                            "<td>" + row.mc + "</td>" +
                            "<td>" + row.specifications + "</td>" +
                            "<td style='text-align:center;'>row.count</td>" +
                            "<td>" + row.dw + "</td>" +
                            "<td>" + row.dw + "</td>" +
                            document.getElementById("mainbody").appendChild(trObj);
                    }
                } else {
                    var trObj = document.createElement("tr");
                    trObj.innerHTML = "<td colspan='100' style='text-align:center;'><button onclick='addFun();' class='layui-btn layui-btn-normal layui-btn-radius'>制作计划</button></td>";
                    document.getElementById("mainbody").appendChild(trObj);
                }
            });
        }

        function changeProjectId() {
            var projectId = $('#proId').val();
            overallPlan(projectId)
        }

        function addFun() {

            var selProId = $("#proId").select2("val");
            var selProText = $('#proId').find("option:selected").text();

            parent.$
                .modalDialog({
                    title: '新增计划',
                    width: 1200,
                    height: 600,
                    href: encodeURI('${pageContext.request.contextPath}/overallPlanController/securi_toAddOverallPlan?proId=' + selProId + '&proName=' + selProText),

                    buttons: [{
                        text: '确定',
                        handler: function () {
                            var f = parent.$.modalDialog.handler.find('#form');
                            f.submit();
                        }
                    }],
                    onOpen: function () {
                        parent.$('.dialog-button:eq(0) a:eq(0)').hide();
                    }


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

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span2">
            <!--Sidebar content-->
        </div>
        <div class="span10">
            <div class="controls">

            </div>

            <table class="table_style table table-striped table-bordered table-hover table-condensed"
                   id="overPlanTable">
                <caption>
                    <blockquote class="layui-elem-quote" style="text-align: center">
                        <a style="font-size:16px;">
                            <span>材料总计划</span>
                        </a>
                        <select style="width:250px;" id="proId" name="proId" onchange="changeProjectId()">
                        </select>
                    </blockquote>

                </caption>
                <thead>
                <tr>
                    <th style="text-align:center; ">序号</th>
                    <th style="text-align:center; ">材料名称</th>
                    <th style="text-align:center; ">规格型号</th>
                    <th style="text-align:center; ">数量</th>
                    <th style="text-align:center; ">单位</th>
                    <th style="text-align:center; ">供应商</th>
                    <th style="text-align:center; ">操作</th>
                </tr>
                </thead>
                <tbody id="mainbody">
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
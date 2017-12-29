<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<!DOCTYPE html>--%>


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

    $(document).ready(function () {
		overallPlan("${proId}");
    });

    function overallPlan(projectId) {
        debugger;
        var searchParam = {'projectId': projectId};
        document.getElementById("overallPlanTabBody").innerHTML = '';
        $.getJSON('${pageContext.request.contextPath}/overallPlanController/securi_overallPlanDetailsAll', searchParam, function (data) {
            if (data.length > 0) {
                for (var i in data) {
                    var row = data[i];
                    var trObj = document.createElement("tr");
                    var _id = document.getElementById("overallPlanTable").rows.length;
                    trObj.id = "tr_" + _id;
                    trObj.innerHTML =
                        "<td style='text-align:center;'>" + _id + "</td>" +
                        "<td style='display: none;'>" + row.projectId + "</td>" +
                        "<td style='display: none;'>" + row.id + "</td>" +
                        "<td style='display: none;'>" + row.materialsId + "</td>" +
                        "<td>" + row.mc + "</td>" +
                        "<td>" + row.specifications + "</td>" +
                        "<td style='text-align: right'>" + row.count + "</td>" +
                        "<td style='text-align: right;color: #2ba1fc;'>" + row.remainCount + "</td>" +
                        "<td>" + row.dw + "</td>" +
                    	"<td style='text-align:center; '>" +
                    		"<input type='checkbox' onclick='checkRow(" + _id + ")'  id='overallPlanTable_row_" + _id + "'/>" +
						"</td>";
                    document.getElementById("overallPlanTabBody").appendChild(trObj);
                }
            }
        });
    }

    function checkRow(_id) {

        if ($('#' + "overallPlanTable_row_" + _id).attr("checked")) {
            $('#' + "overallPlanTable_row_" + _id).attr("checked", "true");
            add(_id);
        } else {
            $('#' + "overallPlanTable_row_" + _id).removeAttr("checked");
            del(_id);
        }
    }
    function add(checkedId) {
        if (document.getElementById("norecord") != undefined) {
            document.getElementById("monthPlanTabBody").removeChild(document.getElementById("norecord"));
        }

        var row = document.getElementById("overallPlanTable").rows[checkedId];
        var projectId = row.cells[1].innerText;
        var overallPlanId = row.cells[2].innerText;
        var materialsId = row.cells[3].innerText;
        var mc = row.cells[4].innerText;
        var specifications = row.cells[5].innerText;
        var count = row.cells[6].innerText;
        var remainCount = row.cells[7].innerText;
        var dw = row.cells[8].innerText;

        var trObj = document.createElement("tr");
        trObj.id = "tr_monthplan_" + checkedId;
        trObj.innerHTML =
            "<td style='text-align:center;'>" + document.getElementById("monthPlanTable").rows.length + "</td>" +
            "<td style='display: none;'>" + projectId + "</td>" +
            "<td style='display: none;'>" + overallPlanId + "</td>" +
            "<td style='display: none;'>" + materialsId + "</td>" +
            "<td>" + mc + "</td>" +
            "<td>" + specifications + "</td>" +
            "<td>" + dw + "</td>" +
            "<td style='text-align:right;'><input type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' value=' " + count + " '></td>" +
            "<td style='text-align:right;'><input type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:right;'><input type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:center;'>" +  + "</td>" +
            "<td style='text-align:center; '><button class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + checkedId + ")'><i class='layui-icon'></i>删除</button></td>";
        document.getElementById("monthPlanTabBody").appendChild(trObj);
    }

    function del(_id) {
        debugger;
        document.getElementById("monthPlanTabBody").removeChild(document.getElementById("tr_monthplan_" + _id));
        for (var i = 1; i < document.getElementById("monthPlanTable").rows.length; i++) {
            document.getElementById("monthPlanTable").rows[i].cells[0].innerHTML = i;
        }
        $('#' + "overallPlanTable_row_" + _id).removeAttr("checked");

        if (document.getElementById("monthPlanTable").rows.length == 1) {
            var trObj = document.createElement("tr");
            trObj.id = "norecord";
            trObj.innerHTML = "<td colspan='100' style='text-align:center;'>温馨提示:勾选左侧材料总体计划，添加材料采购计划!</td>";
            document.getElementById("monthPlanTabBody").appendChild(trObj);
        }
    }

    function geneMonthPlan() {
        var tableObj = document.getElementById("monthPlanTable");
        var tableInfo = [];

        for (var i = 1; i < tableObj.rows.length; i++) {
            var rowInfo = {};
            var _materialsId = tableObj.rows[i].cells[3].innerText;
            var _count = tableObj.rows[i].cells[7].firstElementChild.value;
            var _price = tableObj.rows[i].cells[8].firstElementChild.value;
            var _total = tableObj.rows[i].cells[9].firstElementChild.value;
            var _supplier = tableObj.rows[i].cells[10].innerText;

            rowInfo.materialsId = _materialsId;
            rowInfo.count = _count;
            rowInfo.price = _price;
            rowInfo.total = _total;
            rowInfo.supplier = _supplier;

            tableInfo.push(rowInfo);
        }
        $('#monthPlanInfo').val(JSON.stringify(tableInfo));

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
                    url: '${pageContext.request.contextPath}/monthPlanController/securi_geneMonthPlan',
                    type: 'post',
                    data: {monthPlanInfo: JSON.stringify(tableInfo), projectId: $('#projectId').val(), currentApprovedUser: $('#currentApprovedUserRef').val()},
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
            url: '${pageContext.request.contextPath}/monthPlanController/securi_geneMonthPlan',
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
<!--<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>-->
<!--<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>-->
<%--<![endif]-->--%>

<div class="layui-container">
	<div class="layui-row">
		<div class="layui-col-xs6">
			<blockquote class="layui-elem-quote" style="height: 25px">
				<a style="font-size:16px;">材料总体计划</a>
			</blockquote>

			<table class="table_style table table-striped table-bordered table-hover table-condensed" id="overallPlanTable">
				<thead>
				<tr>
					<th style="text-align:center; ">序号</th>
					<th style="display: none; ">项目ID</th>
					<th style="display: none;">计划ID</th>
					<th style="display: none; ">材料ID</th>
					<th style="text-align:center; ">材料名称</th>
					<th style="text-align:center; ">规格型号</th>
					<th style="text-align:center; ">计划数量</th>
					<th style="text-align:center; ">计划剩余数量</th>
					<th style="text-align:center; ">单位</th>
					<th style="text-align:center; ">选择</th>
				</tr>
				</thead>
				<tbody id="overallPlanTabBody">
				</tbody>
			</table>
		</div>

		<div class="layui-col-xs6">
			<form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
				<input type="hidden" id="projectId" name="projectId" value="${proId}"/>
				<blockquote class="layui-elem-quote" style="text-align: center;height: 25px">
					<a style="font-size:16px;">${proName}</a>&nbsp;&nbsp;采购计划
				</blockquote>
				<table class="table_style table table-striped table-bordered table-hover table-condensed" id="monthPlanTable">
					<thead>
					<tr>
						<th style="text-align:center; ">序号</th>
						<th style="display: none; ">项目ID</th>
						<th style="display: none;">计划ID</th>
						<th style="display: none; ">材料ID</th>
						<th style="text-align:center; ">材料名称</th>
						<th style="text-align:center; ">规格型号</th>
						<th style="text-align:center; ">数量</th>
						<th style="text-align:center; ">单位</th>
						<th style="text-align:center; ">单价</th>
						<th style="text-align:center; ">总价</th>
						<th style="text-align:center; ">供应商</th>
						<th style="text-align:center; ">操作</th>
					</tr>
					</thead>
					<tbody id="monthPlanTabBody">
					<tr id="norecord">
						<td colspan='100' style='text-align:center;'>温馨提示:勾选左侧材料总体计划，添加材料采购计划!</td>
					</tr>
					</tbody>
				</table>
				<input type="hidden" id="monthPlanInfo" name="monthPlanInfo">
			</form>
			<div style='text-align:right;'>
				<button class='layui-btn layui-btn-normal layui-btn-radius' onclick="geneMonthPlan();">确 定
				</button>
			</div>
		</div>

	</div>
</div>
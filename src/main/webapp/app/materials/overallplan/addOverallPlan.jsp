<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--<meta charset="utf-8">--%>
	<title>layui</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

	<jsp:include page="../../../inc.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/css/layui.css" media="all"/>


	<style>
		.table_style tbody tr:nth-child(even) td,
		.table_style tbody tr:nth-child(even) th {background-color:#fff;}

		.table_style tbody tr:nth-child(odd) td,
		.table_style tbody tr:nth-child(odd) th {background-color:#eee;}
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
            var trObj = document.createElement("tr");
            trObj.id = "tr_" + row.id;
            trObj.innerHTML =
                "<td style='text-align:center;'>" + document.getElementById("overPlanTable").rows.length + "</td>" +
                "<td>" + row.mc + "</td>" +
                "<td>" + row.specifications + "</td>" +
                "<td style='text-align:center;'><input type='text' class='layui-input' style='margin-bottom:0px;width: 50px'></td>" +
                "<td>" + row.dw + "</td>" +
                "<td>" + row.dw + "</td>" +
                "<td style='text-align:center; '><button class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + row.id + ")'><i class='layui-icon'></i>删除</button></td>";
            document.getElementById("mainbody").appendChild(trObj);
        }

        function del(_id) {
            document.getElementById("mainbody").removeChild(document.getElementById("tr_" + _id));
            for (var i = 1; i < document.getElementById("overPlanTable").rows.length; i++) {
                document.getElementById("overPlanTable").rows[i].cells[0].innerHTML = i;
            }
            $('#' + "gridtree_row_" + _id).removeAttr("checked");
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
		<div class="layui-col-md4">
			<blockquote class="layui-elem-quote">材料库</blockquote>
			<div data-options="region:'center',border:false">
				<table id="dataGrid"></table>
			</div>
		</div>
		<div class="layui-col-md8">
			<blockquote class="layui-elem-quote">材料总计划</blockquote>
			<table class="table_style table table-striped table-bordered table-hover table-condensed" id="overPlanTable">
				<caption>
					<blockquote>十二月 材料总计划</blockquote>
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
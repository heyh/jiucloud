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

    var dataGrid;
    $(document).ready(function () {
        if ("${proId}" == '' || "${proId}" == undefined || "${proId}" == 'undefined') {
            $('#overallPlanTableId').hide();
            $('#materialsDataGridId').show();

            var _url = '${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid';
            var _clickUrl = '${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGridChild';
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
                                    width: 150
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

                                    addNoProject(row);
                                } else {
                                    $('#' + "gridtree_row_" + row.id).removeAttr("checked");

                                    del(row.id);
                                }
                            },
                        });
            });

        } else {
			$('#overallPlanTableId').show();
            $('#materialsDataGridId').hide();

            overallPlan("${proId}");
        }

    });

    function searchMaterialsFun() {
        $.post('${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid',
            {keyword: $('#keyword').val()},
            function (data) {
                dataGrid.treegrid('loadData', data);
            },
            'json');
    }

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

    function addNoProject(row) {

        if (document.getElementById("norecord") != undefined) {
            document.getElementById("monthPlanTabBody").removeChild(document.getElementById("norecord"));
        }

        var monthPlanTableLength = document.getElementById("monthPlanTable").rows.length;
        var trObj = document.createElement("tr");
        trObj.id = "tr_monthplan_" + row.id;
        trObj.innerHTML =
            "<td style='text-align:center;'>" + document.getElementById("monthPlanTable").rows.length + "</td>" +
            "<td style='display: none;'>" + '' + "</td>" +
            "<td style='display: none;'>" + '' + "</td>" +
            "<td style='display: none;'>" + row.id + "</td>" +
            "<td>" + row.mc + "</td>" +
            "<td>" + row.specifications + "</td>" +
            "<td>" + row.dw + "</td>" +
            "<td style='text-align:center;'><input onblur='cal(" + document.getElementById("monthPlanTable").rows.length + ")' type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:center;'><input onblur='cal(" + document.getElementById("monthPlanTable").rows.length + ")' type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:center;'><input type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:center; ' onmouseover='overShow(" + monthPlanTableLength + ")' onmouseout='outHide(" + monthPlanTableLength + ")' ><span id='span_" + monthPlanTableLength + "'></span><input id='btn_" + monthPlanTableLength + "' type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='supplierPage(" + monthPlanTableLength + ")' value='选择'></input></td>" +
            "<td style='display: none;'></td>" +
            "<td style='text-align:center; '><input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + row.id + ")' value='删除'></input></td>";
        document.getElementById("monthPlanTabBody").appendChild(trObj);
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
		var monthPlanTableLength = document.getElementById("monthPlanTable").rows.length;
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
            "<td style='text-align:center;'><input onblur='cal(" + document.getElementById("monthPlanTable").rows.length + ")' type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' value=' " + count + " '></td>" +
            "<td style='text-align:center;'><input onblur='cal(" + document.getElementById("monthPlanTable").rows.length + ")' type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
            "<td style='text-align:center;'><input type='text' class='layui-input' style='text-align: right;margin-bottom:0px;width: 50px; ' ></td>" +
 			"<td style='text-align:center; ' onmouseover='overShow(" + monthPlanTableLength + ")' onmouseout='outHide(" + monthPlanTableLength + ")' ><span id='span_" + monthPlanTableLength + "'></span><input id='btn_" + monthPlanTableLength + "' type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='supplierPage(" + monthPlanTableLength + ")' value='选择'></input></td>" +
            "<td style='display: none;'></td>" +
            "<td style='text-align:center; '><input type='button' class='layui-btn  layui-btn-xs layui-btn-normal' onclick='del(" + checkedId + ")' value='删除'></input></td>";
        document.getElementById("monthPlanTabBody").appendChild(trObj);
    }
    
    function cal(_id) {

        var tableObj = document.getElementById("monthPlanTable");
		var _count = tableObj.rows[_id].cells[7].firstElementChild.value;
        var _price = tableObj.rows[_id].cells[8].firstElementChild.value;
        debugger;
        if (_count != '' && _price != '') {
            var _total = (_count * _price).toFixed(2);
            tableObj.rows[_id].cells[9].firstElementChild.value = _total;
		}
    }

    function overShow(checkedId) {
        var supplierName = document.getElementById("monthPlanTable").rows[checkedId].cells[10].firstElementChild.innerText;
		$('#' + "span_" + checkedId).hide();
		$('#' + "btn_" + checkedId).show();

    }

    function outHide(checkedId) {
        var supplierName = document.getElementById("monthPlanTable").rows[checkedId].cells[10].firstElementChild.innerText;
        if(supplierName == undefined || supplierName == 'undefined' || supplierName == '') {
            $('#' + "btn_" + checkedId).show();
        } else {
            $('#' + "span_" + checkedId).show();
            $('#' + "btn_" + checkedId).hide();
        }
    }

    function del(_id) {
        document.getElementById("monthPlanTabBody").removeChild(document.getElementById("tr_monthplan_" + _id));
        for (var i = 1; i < document.getElementById("monthPlanTable").rows.length; i++) {
            document.getElementById("monthPlanTable").rows[i].cells[0].innerHTML = i;
        }
        if ("${proId}" == '' || "${proId}" == undefined || "${proId}" == 'undefined') {
            $('#' + "gridtree_row_" + _id).removeAttr("checked");
		} else {
            $('#' + "overallPlanTable_row_" + _id).removeAttr("checked");
		}


        if (document.getElementById("monthPlanTable").rows.length == 1) {
            var trObj = document.createElement("tr");
            trObj.id = "norecord";
            trObj.innerHTML = "<td colspan='100' style='text-align:center;'>温馨提示:勾选左侧材料总体计划，添加材料采购计划!</td>";
            document.getElementById("monthPlanTabBody").appendChild(trObj);
        }
    }

    function supplierPage(_id) {

        var supplierHtml = '';
        supplierHtml += '<table class="table_style table table-striped table-bordered table-hover table-condensed" id="supplierTable">';
        supplierHtml += '	<thead>';
        supplierHtml += '   	<tr>';
        supplierHtml += '   		<th style="text-align:center;">序号</th>';
        supplierHtml += '   		<th style="text-align:center;">厂家名称</th>';
        supplierHtml += '   		<th style="text-align:center;">企业电话</th>';
        supplierHtml += '   		<th style="text-align:center;">企业地址</th>';
        supplierHtml += '   		<th style="text-align:center;">联系人</th>';
        supplierHtml += '   		<th style="text-align:center;">联系电话</th>';
        supplierHtml += '   		<th style="text-align:center;">选择</th>';
        supplierHtml += '   	</tr>';
        supplierHtml += '	</thead>';
        supplierHtml += '	<tbody id="supplierTabBody">';
        $.ajax({
            url: '${pageContext.request.contextPath}/supplierController/securi_supplierList',
            type: 'post',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (data) {
                debugger
                if (data.success) {
                    var supplierList = data.obj;
                    for (var i in supplierList) {

                        var trObj = document.createElement("tr");
                        trObj.id = "tr_supplier_" + i;
                        trObj.innerHTML =
                            '<td style="text-align:center;">' + i + 1 + '</td>' +
                            '<td style="display: none;">' + supplierList[i].id + '</td>' +
                            '<td>' + supplierList[i].name + '</td>' +
                            '<td>' + supplierList[i].tel + '</td>' +
                            '<td>' + supplierList[i].addr + '</td>' +
                            '<td>' + supplierList[i].linkman + '</td>' +
                            '<td>' + supplierList[i].linkphone + '</td>' +
                            "<td style='text-align:center; '>" +
                            "<input type='radio' name='radio' onclick='chooseSupplier(" + JSON.stringify(supplierList[i]) + "," + _id + ")' />" +
                            "</td>";
                        document.getElementById("supplierTabBody").appendChild(trObj);
                    }
                }
            }
        });

        layer.open({
            type: 1,
            title: '供应商',
            content: supplierHtml,
            btn: '确定',
            btnAlign: 'c',
            shade: 0.3,
            area: ['800px', '700px'],
            yes: function () {
				layer.closeAll();
            }
        });
    }

    function chooseSupplier(supplier, _id) {
        var tableObj = document.getElementById("monthPlanTable");
        document.getElementById("monthPlanTable").rows[_id].cells[10].firstElementChild.innerText = supplier.name;
        $('#' + "span_" + _id).show();
        $('#' + "btn_" + _id).hide();
        tableObj.rows[_id].cells[11].innerText = supplier.id;
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
            var _supplier = tableObj.rows[i].cells[11].innerText;

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
//                            overallPlan($('#projectId').val());
//                            searchFun();
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
</script>

<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<!--<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>-->
<!--<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>-->
<%--<![endif]-->--%>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span5">
			<div id="overallPlanTableId" style="display: none;">
				<blockquote class="layui-elem-quote" style="height: 25px">
					<a style="font-size:16px;">材料总体计划</a>
				</blockquote>

				<table class="table_style table table-striped table-bordered table-hover table-condensed"
					   id="overallPlanTable">
					<thead>
					<tr>
						<th style="text-align:center; width: 25px;">序号</th>
						<th style="display: none; ">项目ID</th>
						<th style="display: none;">计划ID</th>
						<th style="display: none; ">材料ID</th>
						<th style="text-align:center; ">材料名称</th>
						<th style="text-align:center; width: 30px;">规格型号</th>
						<th style="text-align:center; width: 30px;">计划</th>
						<th style="text-align:center; width: 30px;">剩余</th>
						<th style="text-align:center; width: 25px;">单位</th>
						<th style="text-align:center; width: 25px;">选择</th>
					</tr>
					</thead>
					<tbody id="overallPlanTabBody">
					</tbody>
				</table>
			</div>
			<div id="materialsDataGridId" style="display: none;">
				<blockquote class="layui-elem-quote" style="height: 25px">
					<a style="font-size:16px;">材料库</a>
					<input type="text" class="input-medium search-query" style="margin-left: 10px" id="keyword" name="keyword">
					<button class="layui-btn layui-btn-sm layui-btn-normal layui-btn-radius" onclick="searchMaterialsFun()">搜索</button>
				</blockquote>

				<div data-options="region:'center',border:false">
					<table id="dataGrid"></table>
				</div>
			</div>
		</div>

		<div class="span7">
			<form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
				<input type="hidden" id="projectId" name="projectId" value="${proId}"/>
				<blockquote class="layui-elem-quote" style="text-align: center;height: 25px">
					<a style="font-size:16px;">${proName}</a>&nbsp;&nbsp;采购计划
				</blockquote>
				<table class="table_style table table-striped table-bordered table-hover table-condensed" id="monthPlanTable">
					<thead>
					<tr>
						<th style="text-align:center; width: 25px;">序号</th>
						<th style="display: none; ">项目ID</th>
						<th style="display: none;">计划ID</th>
						<th style="display: none; ">材料ID</th>
						<th style="text-align:center; ">材料名称</th>
						<th style="text-align:center; ">规格型号</th>
						<th style="text-align:center; width: 25px;">单位</th>
						<th style="text-align:center; width: 25px;">数量</th>
						<th style="text-align:center;  width: 25px;">单价</th>
						<th style="text-align:center; ">总价</th>
						<th style="text-align:center; ">供应商</th>
						<th style="text-align:center;  width: 25px;">操作</th>
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
				<input type="button" class='layui-btn layui-btn-normal layui-btn-radius' onclick="geneMonthPlan(); " value="确 定"></input>
			</div>
		</div>
	</div>
</div>
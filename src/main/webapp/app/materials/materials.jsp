<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
//    String underlingUsers = null;
    String projectInfos = null;
    List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
	JSONArray jsonArray = new JSONArray();
	JSONArray billCostTree = new JSONArray();
	boolean hasOnlyReadRight = false;
	boolean hasReadEditRight = false;
	boolean hasOutRight = false;
	boolean hasBackFillRight = false;

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
//        underlingUsers = sessionInfo.getUnderlingUsers();
        projectInfos = sessionInfo.getProjectInfos();
		billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
		for (Map<String, Object> nodeMap : billCostInfos) {
			JSONObject nodeJson = JSONObject.fromObject(nodeMap);
			jsonArray.add(nodeJson);
		}
		billCostTree = sessionInfo.getBillCostTree();
		hasOnlyReadRight = sessionInfo.getRightList().contains("16") && 0 != sessionInfo.getParentId();
		hasReadEditRight = sessionInfo.getRightList().contains("15") || 0 == sessionInfo.getParentId();
		hasOutRight = sessionInfo.getRightList().contains("17");
		hasBackFillRight = sessionInfo.getRightList().contains("18");
	}

%>

<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../../inc.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/css/layui.css"/>

	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/bootstrap-2.3.1/css/bootstrap.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/bootstrap-2.3.1/js/bootstrap.js"></script>

	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/skin/default/layer.css" media="all"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>


	<style>
		.table_style {
			width: 100%;
			margin-bottom: 20px;
			border:1px solid #EDEDED

		}
		.table_style th{height:34px; background:#76b3ff; color:#fff; font-weight: normal}
		.table_style td{height:32px; color:#0e0e0e;padding-left:10px;color:#666}
		.table_style tbody tr{background:#eee; cursor: default}
		.table_style tbody tr.hover td{background:#e4fbfb}
		.td_title{font-weight:bold;color:#333}

		.subtotal { font-weight: bold; }/*合计单元格样式*/
	</style>
	<script type="text/javascript">
	var dataGrid;
	$(function() {

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
                        fitColumns : false,
                        columns: [[
                            {
                                title: '材料名称',
                                field: 'mc',
                                width: 500
                            },
                            {
                                title: '规格型号',
                                field: 'specifications',
                                width: 300
                            },
                            {
                                title: '单位',
                                field: 'dw',
                                width: 100
                            },
                            {
                                field : 'action',
                                title : '操作',
                                width : 190,
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
                                            ' <img style="cursor:pointer" onclick="editFun(\'{0}\');" src="{1}" title="修改"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');

                                    str += '&nbsp;';
                                    str += $
                                        .formatString(
                                            ' <img style="cursor:pointer" onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
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
                            parent.$.messager.progress('close');
                            $('#searchForm table').show();
                            $(this).treegrid('collapseAll');
                            $(this).treegrid('tooltip');
                        }
                    });
        });
    });

    // 增加根结点
	function addFun() {
        layer.open({
            type: 1,
            title: '材料库维护-增加材料类型',
            content: $('#rootDiv'),
            btn: '确定',
            btnAlign: 'r',
            shade: 0.3,
            area: ['300px', '200px'],
            yes: function () {
                if ($('#mc_add_root').val() == '') {
                    layer.alert('材料类型名称必填！', {icon: 2});
                    return;
                }
                $.ajax({
                    url: '${pageContext.request.contextPath}/materialManageController/securi_addNode',
                    type: 'post',
                    data: {
                        pid: '0',
                        mc: $('#mc_add_root').val(),
                        specifications: '',
                        dw: ''
                    },
                    dataType: 'json',
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (data) {
                        if (data.rspCode == '0') {
                            layer.closeAll();
                            layer.msg('增加成功!');
                            searchFunForNoKeyword();
                            $('#mc_add_root').val('');
                        }
                    }
                });
            }
        });
    }

    // 增加结点
    function addNodeFun(pid) {
        layer.open({
            type: 1,
            title: '材料库维护-增加材料',
            content: $('#addChildDiv'),
            btn: '确定',
            btnAlign: 'r',
            shade: 0.3,
            area: ['300px', '260px'],
            yes: function () {
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
                            searchFunForNoKeyword();
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
    function deleteFun(id) {
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
                            searchFunForNoKeyword();
                        }
                    }
                });
            },
            function () {
            });
    }

	//编辑
	function editFun(id) {
        $.getJSON('${pageContext.request.contextPath}/materialManageController/securi_getMaterialsById?id=' + id, function (data) {
            var materials = data.materials;
            $('#mc_edit').val(materials.mc.trim());
            $('#specifications_edit').val(materials.specifications.trim());
            $('#dw_edit').val(materials.dw.trim());

            layer.open({
                type: 1,
                title: '材料库维护-修改材料',
                content: $('#editChildDiv'),
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
                                searchFunForNoKeyword();
                            }
                        }
                    });
                }
            });
        });


	}

	//搜索
    function searchFun() {
        $.post('${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid',
            {keyword: $('#keyword').val()},
            function (data) {
                dataGrid.treegrid('loadData', data);
            },
            'json');
    }

    function searchFunForNoKeyword() {
        $.post('${pageContext.request.contextPath}/materialManageController/securi_materialsTreeGrid',
            {keyword: ''},
            function (data) {
                dataGrid.treegrid('loadData', data);
            },
            'json');
    }

	//清除条件
	function cleanFun() {
        $('#keyword').val('');
        searchFun();
	}

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 75px; overflow: hidden;">
			<form id="searchForm">
				<div style="margin-top: 10px">
					<input type="text" class="input-medium search-query" style="margin-left: 10px" id="keyword" name="keyword">&nbsp;&nbsp;
					<input type="button" class="layui-btn layui-btn-sm layui-btn-normal layui-btn-radius" onclick="searchFun()" value="搜索"/>
					<input type="button" class="layui-btn layui-btn-sm  layui-btn-primary layui-btn-radius" onclick="cleanFun()" value="清除"/>
				</div>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'">添加</a>
	</div>
</body>

<div style="text-align: center; margin-top: 30px" id="rootDiv">
	<div class="controls">
		<input type="text" id="mc_add_root" placeholder="材料类型名称">
	</div>
</div>

<div style="text-align: center; margin-top: 10px" id="addChildDiv">
	<div class="controls">
		<input type="text" id="mc_add" placeholder="材料名称">
	</div>
	<div class="controls">
		<input type="text" id="specifications_add" placeholder="规格型号">
	</div>
	<div class="controls">
		<input type="text" id="dw_add" placeholder="单位">
	</div>
</div>

<div style="text-align: center; margin-top: 10px" id="editChildDiv">
	<div class="controls">
		<input type="text" id="mc_edit" placeholder="材料名称">
	</div>
	<div class="controls">
		<input type="text" id="specifications_edit" placeholder="规格型号">
	</div>
	<div class="controls">
		<input type="text" id="dw_edit" placeholder="单位">
	</div>
</div>

</html>
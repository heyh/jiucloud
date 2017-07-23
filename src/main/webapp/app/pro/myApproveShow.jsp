<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    List<Map<String, Object>> dataCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray4Data = new JSONArray();
    JSONArray dataCostTree = new JSONArray();

    List<Map<String, Object>> docCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray4Doc = new JSONArray();
    JSONArray docCostTree = new JSONArray();

    List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray4Bill = new JSONArray();
    JSONArray billCostTree = new JSONArray();

    List<Map<String, Object>> materialCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray4Material = new JSONArray();
    JSONArray materialCostTree = new JSONArray();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    String userId = null;
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        dataCostTree = sessionInfo.getCostTree();
        dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        for (Map<String, Object> nodeMap : dataCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray4Data.add(nodeJson);
        }

        docCostTree = sessionInfo.getDocCostTree();
        docCostInfos = sessionInfo.getCostTypeInfos().get("docCostInfos");
        for (Map<String, Object> nodeMap : docCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray4Doc.add(nodeJson);
        }

        billCostTree = sessionInfo.getBillCostTree();
        billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
        for (Map<String, Object> nodeMap : billCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray4Bill.add(nodeJson);
        }

        materialCostTree = sessionInfo.getMaterialCostTree();
        materialCostInfos = sessionInfo.getCostTypeInfos().get("materialCostInfos");
        for (Map<String, Object> nodeMap : materialCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray4Material.add(nodeJson);
        }
        userId = sessionInfo.getId();
    }

%>
<!DOCTYPE html>
<html>
<head>
    <title>项目详情</title>
    <%--<jsp:include page="../../inc2.jsp"></jsp:include>--%>
    <link href="${pageContext.request.contextPath}/jslib/bootstrap-2.3.1/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <!-- 引入jQuery -->
    <script src="${pageContext.request.contextPath}/jslib/jquery-1.8.3.js"
            type="text/javascript" charset="utf-8"></script>
    <link id="easyuiTheme" rel="stylesheet"
          href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/themes/bootstrap/easyui.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/themes/icon.css" type="text/css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"
            charset="utf-8"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"
            charset="utf-8"></script>
    <!-- 修复EasyUI1.3.3中layout组件的BUG -->
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/plugins/jquery.layout.js"
            charset="utf-8"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jslib/extJquery.js?v=201305301341"
            charset="utf-8"></script>
    <!-- 引入Highcharts -->
    <%--<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>--%>
    <%--<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/exporting.js"></script>--%>
    <script src="https://cdn.bootcss.com/highcharts/4.1.10/highcharts.js"></script>
    <script src="https://cdn.bootcss.com/highcharts/4.1.10/modules/exporting.js"></script>


    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>


    <style>

        ul {
            margin: 0;
            padding: 0;
        }

        ul {
            margin-bottom: 2em;
        }

        ul {
            margin-left: 1em;
        }

        #nav {
            left: 20px;
            list-style: none;
            margin: 0;
            position: fixed;
            top: 20px;
        }

        #nav li {
            margin-bottom: 2px;
        }

        #nav a {
            background: #ededed;
            color: #666;
            display: block;
            font-size: 14px;
            padding: 5px 10px;
            text-decoration: none;
            text-transform: uppercase;
        }

        #nav a:hover {
            background: #dedede;
        }

        #nav .current a {
            background: #666;
            color: #ededed;
        }
        .section {
            border-bottom: 5px solid #ccc;
            min-height: 60vh;
            padding:0px 10px 0 100px;

        }

        .section p:last-child {
            margin-bottom: 0;
        }
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

        /*fieldset {*/
        /*margin-top: 20px;*/
        /*}*/

        legend {
            color: #5eade0;
            font-weight: 800;
            font-size: 14px;
        }
    </style>
</head>

<body>
<ul id="nav">
    <li class="current"><a href="#data">项目数据</a></li>
    <li><a href="#doc">项目资料</a></li>
    <li><a href="#bill">清单项量</a></li>
    <li><a href="#material">项目材料</a></li>
</ul>

<div id="container">

    <div class="section" id="data">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png" style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>项目数据</b></span>
        <table id="dataGrid4Data" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbar4Data" class="fee_detail" style="display: none;">
            <span>关键字搜索:</span>
            <input style="margin-top:9px; width: 150px; height: 17px" class="easyui-textbox"  type="text" name="keyword4Data" id="keyword4Data" data-options=""/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>费用类型:</span>
            <input class="easyui-combotree" name="costType4DataRef" id="costType4DataRef" style="width:180px;" placeholder="请选择">
            <input type="hidden" name="costType4Material" id="costType4Data">
            <input type="hidden" name="itemCode4Material" id="itemCode4Data">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>起止时间:</span>
            <input style="width: 150px" class="easyui-datebox" name="startTime4Data" id='startTime4Data' editable="false" placeholder="点击选择时间"  value='${first }' />
            - <input style="width: 150px" class="easyui-datebox"  name="endTime4Data" id='endTime4Data' editable="false" placeholder="点击选择时间"  value='${last }' />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="searchFun4Data();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="cleanFun4Data();">清空条件</a>
        </div>
    </div>

    <div class="section" id="doc">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png" style="padding-top:20px; padding-bottom:20px; vertical-align:middle" />
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>项目资料</b></span>
        <table id="dataGrid4Doc" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbar4Doc" class="fee_detail" style="display: none;">
            <span>关键字搜索:</span>
            <input style="margin-top:9px; width: 150px; height: 17px" class="easyui-textbox"  type="text" name="keyword4Doc" id="keyword4Doc" data-options=""/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>资料类型:</span>
            <input class="easyui-combotree" name="costType4DocRef" id="costType4DocRef" style="width:180px;" placeholder="请选择">
            <input type="hidden" name="costType4Material" id="costType4Doc">
            <input type="hidden" name="itemCode4Material" id="itemCode4Doc">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>起止时间:</span>
            <input style="width: 150px" class="easyui-datebox" name="startTime4Doc" id='startTime4Doc' editable="false" placeholder="点击选择时间"  value='${first }' />
            - <input style="width: 150px" class="easyui-datebox"  name="endTime4Doc" id='endTime4Doc' editable="false" placeholder="点击选择时间"  value='${last }' />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="searchFun4Doc();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="cleanFun4Doc();">清空条件</a>
        </div>
    </div>

    <div class="section" id="bill" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png" style="padding-top:20px; padding-bottom:20px; vertical-align:middle" />
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>清单项量</b></span>
        <table id="dataGrid4Bill" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbar4Bill" class="fee_detail" style="display: none;">
            <span>关键字搜索:</span>
            <input style="margin-top:9px; width: 150px; height: 17px" class="easyui-textbox"  type="text" name="keyword4Doc" id="keyword4Bill" data-options=""/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>费用类型:</span>
            <input class="easyui-combotree" name="costType4BillRef" id="costType4BillRef" style="width:180px;" placeholder="请选择">
            <input type="hidden" name="costType4Material" id="costType4Bill">
            <input type="hidden" name="itemCode4Material" id="itemCode4Bill">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>起止时间:</span>
            <input style="width: 150px" class="easyui-datebox" name="startTime4Bill" id='startTime4Bill' editable="false" placeholder="点击选择时间"  value='${first }' />
            - <input style="width: 150px" class="easyui-datebox"  name="endTime4Bill" id='endTime4Bill' editable="false" placeholder="点击选择时间"  value='${last }' />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="searchFun4Bill();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="cleanFun4Bill();">清空条件</a>
        </div>
    </div>

    <div class="section" id="material" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png" style="padding-top:20px; padding-bottom:20px; vertical-align:middle" />
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>项目材料</b></span>
        <table id="dataGrid4Material" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbar4Material" class="fee_detail" style="display: none;">
            <span>关键字搜索:</span>
            <input style="margin-top:9px; width: 150px; height: 17px" class="easyui-textbox"  type="text" name="keyword4Doc" id="keyword4Material" data-options=""/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>费用类型:</span>
            <input class="easyui-combotree" name="costType4MaterialRef" id="costType4MaterialRef" style="width:180px;" placeholder="请选择">
            <input type="hidden" name="costType4Material" id="costType4Material">
            <input type="hidden" name="itemCode4Material" id="itemCode4Material">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>起止时间:</span>
            <input style="width: 150px" class="easyui-datebox" name="startTime4Material" id='startTime4Material' editable="false" placeholder="点击选择时间"  value='${first }' />
            - <input style="width: 150px" class="easyui-datebox"  name="endTime4Material" id='endTime4Material' editable="false" placeholder="点击选择时间"  value='${last }' />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="searchFun4Material();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="cleanFun4Material();">清空条件</a>
        </div>
    </div>

</div>


<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery.nav.js"></script>

<script>

    // 获取当前日期
    function getCurrentDate() {
        var date = new Date();
        var seperator = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator + month + seperator + strDate;
        return currentdate;
    }

    // yyyy-MM-dd 日期比较
    function compareDate(dateA, dateB) {
        return new Date(dateA.replace(/-/g, "/")) - new Date(dateB.replace(/-/g, "/"));
    }

    var dataGrid4Data;
    var dataGrid4Doc;
    var dataGrid4Bill;
    var dataGrid4Material;
    $(function() {

        $('#nav').onePageNav();

        dataGrid4Data = $('#dataGrid4Data')
            .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/securi_myApproveDataGrid?source=data',
                    fit : true,
                    fitColumns : false,
                    border : true,
                    pagination : true,
                    idField : 'id',
                    pageSize : 10,
                    pageList : [ 10, 20, 30, 40, 50 ],
                    sortName : 'name',
                    sortOrder : 'asc',
                    checkOnSelect : false,
                    selectOnCheck : false,
                    nowrap : false,
                    rownumbers: true,
                    columns : [ [
                        {
                            field : 'id',
                            title : '多选框',
                            width : 100,
                            checkbox : true
                        },
                        {
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'sectionName',
                            title : '标段',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '名称',
                            width : 350
                        },
                        {
                            field : 'costType',
                            title : '类型',
                            width : 100
                        },
                        {
                            field : 'unit',
                            title : '单位',
                            width : 100
                        },
                        {
                            field : 'price',
                            title : '单价',
                            width : 100
                        },
                        {
                            field : 'count',
                            title : '数量',
                            width : 100
                        },
                        {
                            field : 'moeny',
                            title : '金额',
                            width : 100,
                            formatter : function(value, row, index) {
                                return (row.count * row.price).toFixed(2);
                            }

                        },
                        {
                            field : 'specifications',
                            title : '规格型号',
                            width : 200
                        },
                        {
                            field : 'remark',
                            title : '备注',
                            width : 150
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 100
                        },
                        {
                            field : 'creatTime',
                            title : '录入时间',
                            width : 100
                        },
                        {
                            field : 'uid',
                            title : '用户ID',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'isLock',
                            title : '工程锁定标志',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'needApproved',
                            title : '审批状态',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                if ('0' == value) {
                                    str = '不需审批'
                                } else if ('1' == value) {
                                    str = '<span style="color: #ff0000">' + '未审批' + '</span>';
                                } else if ('2' == value) {
                                    str = '审批通过';
                                } else if ('8' == value) {
                                    str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                } else if ('9' == value) {
                                    str = '<span style="color: #ff0000">' + '审批未通过' + '</span>';
                                }
                                return str;
                            }
                        },
                        {
                            field : 'approvedOption11',
                            title : '审批意见',
                            width : 200,
                        },
                        {
                            field : 'currentApprovedUser',
                            title : '当前审批人',
                            width : 100
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                // modify by heyh 当数据填报之后，在当日内23:59分内均可以修改自己填报数据
                                var userId = <%= userId %>;
                                if((compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0 || '9' == row.needApproved) && userId == row.uid) {
                                    if ('0' == row.isLock && '2' != row.needApproved) {
                                        str += $
                                            .formatString(
                                                '<img onclick="editFun(\'{0}\', dataGrid4Data);" src="{1}" title="编辑" />',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                    }
                                }
                                str += $
                                    .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');

                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbar4Data',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');

                    }
                });


        dataGrid4Doc = $('#dataGrid4Doc')
            .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/securi_myApproveDataGrid?source=doc',
                    fit : true,
                    fitColumns : false,
                    border : true,
                    pagination : true,
                    idField : 'id',
                    pageSize : 10,
                    pageList : [ 10, 20, 30, 40, 50 ],
                    sortName : 'name',
                    sortOrder : 'asc',
                    checkOnSelect : false,
                    selectOnCheck : false,
                    nowrap : false,
                    rownumbers: true,
                    showFooter: true,
                    columns : [ [
                        {
                            field : 'id',
                            title : '多选框',
                            width : 100,
                            checkbox : true
                        },
                        {
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'sectionName',
                            title : '标段',
                            width : 100
                        },
                        {
                            field : 'costType',
                            title : '资料类型',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '名称',
                            width : 350
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 100
                        },
                        {
                            field : 'creatTime',
                            title : '入库时间',
                            width : 100
                        },
                        {
                            field : 'uid',
                            title : '用户ID',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'isLock',
                            title : '工程锁定标志',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'needApproved',
                            title : '审批状态',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                if ('0' == value) {
                                    str = '不需审批'
                                } else if ('1' == value) {
                                    str = '<span style="color: #ff0000">' + '未审批' + '</span>';
                                } else if ('2' == value) {
                                    str = '审批通过';
                                } else if ('8' == value) {
                                    str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                } else if ('9' == value) {
                                    str = '<span style="color: #ff0000">' + '审批未通过' + '</span>';
                                }
                                return str;
                            }
                        },
                        {
                            field : 'approvedOption',
                            title : '审批意见',
                            width : 200,
                        },
                        {
                            field : 'currentApprovedUser',
                            title : '当前审批人',
                            width : 100
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                // modify by heyh 当数据填报之后，在当日内23:59分内均可以修改自己填报数据
                                var userId = <%= userId %>;
                                if((compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0 || '9' == row.needApproved) && userId == row.uid) {
                                    if ('0' == row.isLock && '2' != row.needApproved) {
                                        str += $
                                            .formatString(
                                                '<img onclick="editFun(\'{0}\', dataGrid4Doc);" src="{1}" title="编辑" />',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                    }
                                }
                                str += $
                                    .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');

                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbar4Doc',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
                    }
                });

        dataGrid4Bill= $('#dataGrid4Bill')
            .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/securi_myApproveDataGrid?source=bill',
                    fit : true,
                    fitColumns : false,
                    border : true,
                    pagination : true,
                    idField : 'id',
                    pageSize : 10,
                    pageList : [ 10, 20, 30, 40, 50 ],
                    sortName : 'name',
                    sortOrder : 'asc',
                    checkOnSelect : false,
                    selectOnCheck : false,
                    nowrap : false,
                    rownumbers: true,
                    showFooter: true,
                    columns : [ [
                        {
                            field : 'id',
                            title : '多选框',
                            width : 100,
                            checkbox : true
                        },
                        {
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'specifications',
                            title : '设施名称',
                            width : 200
                        },

                        {
                            field : 'costType',
                            title : '类型',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '名称',
                            width : 350
                        },
                        {
                            field : 'remark',
                            title : '项目特征',
                            width : 150
                        },
                        {
                            field : 'unit',
                            title : '单位',
                            width : 100
                        },
                        {
                            field : 'count',
                            title : '数量',
                            width : 100
                        },
                        {
                            field : 'price_ys',
                            title : '预算单价',
                            width : 100
                        },
                        {
                            field : 'moeny_ys',
                            title : '预算合计',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = (row.count * ((row.price_ys==null || row.price_ys=='') ? 0 : row.price_ys)).toFixed(2);
                                return str;
                            }
                        },
                        {
                            field : 'price_sj',
                            title : '审计单价',
                            width : 100
                        },
                        {
                            field : 'moeny_sj',
                            title : '审计合计',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = (row.count * ((row.price_sj==null || row.price_sj=='') ? 0 : row.price_sj)).toFixed(2);
                                return str;
                            }
                        },
                        {
                            field : 'sectionName',
                            title : '工程属性',
                            width : 100
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 100
                        },
                        {
                            field : 'creatTime',
                            title : '录入时间',
                            width : 100
                        },
                        {
                            field : 'uid',
                            title : '用户ID',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'isLock',
                            title : '工程锁定标志',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'needApproved',
                            title : '审批状态',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                if ('0' == value) {
                                    str = '不需审批'
                                } else if ('1' == value) {
                                    str = '<span style="color: #ff0000">' + '未审批' + '</span>';
                                } else if ('2' == value) {
                                    str = '审批通过';
                                } else if ('8' == value) {
                                    str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                } else if ('9' == value) {
                                    str = '<span style="color: #ff0000">' + '审批未通过' + '</span>';
                                }
                                return str;
                            }
                        },
                        {
                            field : 'approvedOption',
                            title : '审批意见',
                            width : 100,
                            formatter: function (value, row, index) {
                                var str = '';
                                if (row.needApproved == '0' || row.needApproved == '1') {
                                    return str;
                                } else {
                                    if (row.approvedOption != '' && row.approvedOption != undefined && row.approvedOption != 'undefined') {
//                                        var approvedOption = row.row.approvedOption;
                                        str += $.formatString('<a onclick="viewApproveDetailsFun(\' {0} \');" href="javascript:void(0);" class="easyui-linkbutton">' +
                                            '<img src="${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/viewDetails.png">' +
                                            '审批详情</a>',
                                            row.approvedOption);
                                    }
                                }

                                return str;
                            }
                        },
                        {
                            field : 'currentApprovedUser',
                            title : '当前审批人',
                            width : 100,
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                // modify by heyh 当数据填报之后，在当日内23:59分内均可以修改自己填报数据
                                var userId = <%= userId %>;
                                if((compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0 || '9' == row.needApproved) && userId == row.uid) {
                                    if ('0' == row.isLock && '2' != row.needApproved) {
                                        str += $
                                            .formatString(
                                                '<img onclick="editFun(\'{0}\', dataGrid4Bill);" src="{1}" title="编辑" />',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                    }
                                }
                                str += $
                                    .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');

                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbar4Bill',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
                    }
                });

        dataGrid4Material = $('#dataGrid4Material')
            .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/securi_myApproveDataGrid?source=material',
                    fit : true,
                    fitColumns : false,
                    border : true,
                    pagination : true,
                    idField : 'id',
                    pageSize : 10,
                    pageList : [ 10, 20, 30, 40, 50 ],
                    sortName : 'name',
                    sortOrder : 'asc',
                    checkOnSelect : false,
                    selectOnCheck : false,
                    nowrap : false,
                    rownumbers: true,
                    showFooter: true,
                    columns : [ [
                        {
                            field : 'id',
                            title : '多选框',
                            width : 100,
                            checkbox : true
                        },
                        {
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'costType',
                            title : '费用类型',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '材料名称',
                            width : 350
                        },
                        {
                            field : 'unit',
                            title : '单位',
                            width : 100
                        },
                        {
                            field : 'price',
                            title : '单价',
                            width : 100
                        },
                        {
                            field : 'count',
                            title : '数量',
                            width : 100
                        },
                        {
                            field : 'moeny',
                            title : '金额',
                            width : 100,
                            formatter : function(value, row, index) {
                                return (row.count * row.price).toFixed(2);
                            }

                        },
                        {
                            field : 'specifications',
                            title : '规格型号',
                            width : 200
                        },
                        {
                            field : 'supplier',
                            title : '供应商',
                            width: 100
                        },
                        {
                            field : 'remark',
                            title : '备注',
                            width : 150
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 100
                        },
                        {
                            field : 'creatTime',
                            title : '入库时间',
                            width : 100
                        },
                        {
                            field : 'uid',
                            title : '用户ID',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'isLock',
                            title : '工程锁定标志',
                            width : 100,
                            hidden: true
                        },
                        {
                            field : 'needApproved',
                            title : '审批状态',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                if ('0' == value) {
                                    str = '不需审批'
                                } else if ('1' == value) {
                                    str = '<span style="color: #ff0000">' + '未审批' + '</span>';
                                } else if ('2' == value) {
                                    str = '审批通过';
                                } else if ('8' == value) {
                                    str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                } else if ('9' == value) {
                                    str = '<span style="color: #ff0000">' + '审批未通过' + '</span>';
                                }
                                return str;
                            }
                        },
                        {
                            field : 'approvedOption',
                            title : '审批意见',
                            width : 100
                        },
                        {
                            field : 'currentApprovedUser',
                            title : '当前审批人',
                            width : 100
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                // modify by heyh 当数据填报之后，在当日内23:59分内均可以修改自己填报数据
                                var userId = <%= userId %>;
                                if((compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0 || '9' == row.needApproved) && userId == row.uid) {
                                    if ('0' == row.isLock && '2' != row.needApproved) {
                                        str += $
                                            .formatString(
                                                '<img onclick="editFun(\'{0}\', dataGrid4Material);" src="{1}" title="编辑" />',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                        str += '&nbsp;';
                                        str += $
                                            .formatString(
                                                '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                row.id,
                                                '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                    }
                                }
                                str += $
                                    .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');

                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbar4Material',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
                    }
                });
    });

    //过滤条件查询
    function searchFun4Data() {
        var startTime = $('#startTime4Data').datebox('getValue').substring(0, 10) + ' 00:00:00';
        var endTime = $('#endTime4Data').datebox('getValue').substring(0, 10) + ' 23:59:59';
        $('#dataGrid4Data').datagrid('reload',{keyword:$('#keyword4Data').val(),itemCode:$('#itemCode4Data').val(),
            startTime:startTime,endTime:endTime});
    }
    //清除条件
    function cleanFun4Data() {
        $('#toolbar4Data input').val('');
        $('#dataGrid4Data').datagrid('reload', {});
    }

    //过滤条件查询
    function searchFun4Doc() {
        var startTime = $('#startTime4Doc').datebox('getValue').substring(0, 10) + ' 00:00:00';
        var endTime = $('#endTime4Doc').datebox('getValue').substring(0, 10) + ' 23:59:59';
        $('#dataGrid4Doc').datagrid('reload',{keyword:$('#keyword4Doc').val(),itemCode:$('#itemCode4Doc').val(),
            startTime:startTime,endTime:endTime});
    }
    //清除条件
    function cleanFun4Doc() {
        $('#toolbar4Doc input').val('');
        $('#dataGrid4Doc').datagrid('reload', {});
    }

    //过滤条件查询
    function searchFun4Bill() {
        var startTime = $('#startTime4Bill').datebox('getValue').substring(0, 10) + ' 00:00:00';
        var endTime = $('#endTime4Bill').datebox('getValue').substring(0, 10) + ' 23:59:59';
        $('#dataGrid4Bill').datagrid('reload',{keyword:$('#keyword4Bill').val(),itemCode:$('#itemCode4Bill').val(),
            startTime:startTime,endTime:endTime});
    }
    //清除条件
    function cleanFun4Bill() {
        $('#toolbar4Bill input').val('');
        $('#dataGrid4Bill').datagrid('reload', {});
    }

    //过滤条件查询
    function searchFun4Material() {
        var startTime = $('#startTime4Material').datebox('getValue').substring(0, 10) + ' 00:00:00';
        var endTime = $('#endTime4Material').datebox('getValue').substring(0, 10) + ' 23:59:59';
        $('#dataGrid4Material').datagrid('reload',{keyword:$('#keyword4Material').val(),itemCode:$('#itemCode4Material').val(),
            startTime:startTime,endTime:endTime});
    }
    //清除条件
    function cleanFun4Material() {
        $('#toolbar4Material input').val('');
        $('#dataGrid4Material').datagrid('reload', {});
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
                                url: '${pageContext.request.contextPath}/fieldDataController/delfieldData',
                                data: {
                                    id: id
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

    //编辑
    function editFun(id, dg) {
        parent.$
            .modalDialog({
                title: '编辑',
                width: 420,
                height: 460,
                href: '${pageContext.request.contextPath}/fieldDataController/upfieldData?id='
                + id,
                buttons: [{
                    text: '下一步',
                    handler: function () {
                        parent.$.modalDialog.openner_dataGrid = dg;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                        var f = parent.$.modalDialog.handler.find('#form');
                        f.submit();
                    }
                }]
            });
    }

    //附件管理
    function FileFun(id) {
        parent.$
            .modalDialog({
                title: '附件管理',
                width: 800,
                height: 600,
                href: '${pageContext.request.contextPath}/fieldDataController/securi_fieldDataFile?id='
                + id,
                buttons: [{
                    text: '关闭',
                    handler: function () {
                        parent.$.modalDialog.handler.dialog('close');
                    }
                }]
            });
    }

    $('#costTypeDataRef').combotree({
        data: <%= dataCostTree %>,
        lines: true,
        editable:true,
        onLoadSuccess: function () {
            $('#costTypeDataRef').combotree('tree').tree("collapseAll");
        },
        //选择树节点触发事件
        onSelect : function(node) {
            var _jsonArray = <%= jsonArray4Data %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].nid == node.id) {
                    $('#itemCode4Data').val(_jsonArray[i].itemCode);
                    break;
                }
            }
        }
    });

    $('#costType4DocRef').combotree({
        data: <%= docCostTree %>,
        lines: true,
        editable:true,
        onLoadSuccess: function () {
            $('#costType4DocRef').combotree('tree').tree("collapseAll");
        },
        //选择树节点触发事件
        onSelect : function(node) {
            var _jsonArray = <%= jsonArray4Doc %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].nid == node.id) {
                    $('#itemCode4Doc').val(_jsonArray[i].itemCode);
                    break;
                }
            }
        }
    });

    $('#costType4BillRef').combotree({
        data: <%= billCostTree %>,
        lines: true,
        editable:true,
        onLoadSuccess: function () {
            $('#costType4BillRef').combotree('tree').tree("collapseAll");
        },
        //选择树节点触发事件
        onSelect : function(node) {
            var _jsonArray = <%= jsonArray4Bill %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].nid == node.id) {
                    $('#itemCode4Bill').val(_jsonArray[i].itemCode);
                    break;
                }
            }
        }
    });

    $('#costType4MaterialRef').combotree({
        data: <%= materialCostTree %>,
        lines: true,
        editable:true,
        onLoadSuccess: function () {
            $('#costType4MaterialRef').combotree('tree').tree("collapseAll");
        },
        //选择树节点触发事件
        onSelect : function(node) {
            var _jsonArray = <%= jsonArray4Material %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].nid == node.id) {
                    $('#itemCode4Material').val(_jsonArray[i].itemCode);
                    break;
                }
            }
        }
    });

    (function(){
        $.fn.combotree.defaults.editable = true;
        $.extend($.fn.combotree.defaults.keyHandler,{
            up:function(){
                console.log('up');
            },
            down:function(){
                console.log('down');
            },
            enter:function(){
                console.log('enter');
            },
            query:function(q){
                var t = $(this).combotree('tree');
                var nodes = t.tree('getChildren');
                for(var i=0; i<nodes.length; i++){
                    var node = nodes[i];
                    if (node.text.indexOf(q) >= 0){
                        $(node.target).show();
                    } else {
                        $(node.target).hide();
                    }
                }
                var opts = $(this).combotree('options');
                if (!opts.hasSetEvents){
                    opts.hasSetEvents = true;
                    var onShowPanel = opts.onShowPanel;
                    opts.onShowPanel = function(){
                        var nodes = t.tree('getChildren');
                        for(var i=0; i<nodes.length; i++){
                            $(nodes[i].target).show();
                        }
                        onShowPanel.call(this);
                    };
                    $(this).combo('options').onShowPanel = opts.onShowPanel;
                }
            }
        });
    })(jQuery);


    function viewApproveDetailsFun(approvedOption) {
        var approvedOptions = approvedOption.split('|');

        var approvedOptionsHtml =
            '<table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">' +
                '<tr>' +
                    '<td align="center">时间</td>' +
                    '<td align="center">审核人</td>' +
                    '<td align="center">审核意见</td>' +
                '</tr>';

        for (var i=0; i<approvedOptions.length; i++) {
            approvedOptionsHtml += '<tr>';
            var approvedOptionInfos = approvedOptions[i].split('::');
            for (var j=0; j<approvedOptionInfos.length; j++) {
                approvedOptionsHtml += '<td>' + approvedOptionInfos[j] + '</td>';
            }
            approvedOptionsHtml += '</tr>'
        }
        approvedOptionsHtml += '</table>';


        var approvedOptions = approvedOption.split('|');
        layer.open({
            type: 1,
            title: '审批详情',
            closeBtn: 2,
            shadeClose: true,
            maxmin: true, //开启最大化最小化按钮
            area: ['360px', '300px'],
            content: approvedOptionsHtml
        });
    }
</script>
</html>
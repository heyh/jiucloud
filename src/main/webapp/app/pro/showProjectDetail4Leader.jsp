<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    List<Map<String, Object>> dataCostInfos = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> docCostInfos = new ArrayList<Map<String, Object>>();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        docCostInfos = sessionInfo.getCostTypeInfos().get("docCostInfos");
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
    </style>
</head>

<body>
<ul id="nav">
    <li class="current"><a href="#projectInfo">项目详情</a></li>
    <li><a href="#costStat">费用汇总</a></li>
    <li><a href="#costDetail">费用明细</a></li>
    <li><a href="#docDetail">资料明细</a></li>
</ul>

<div id="container">

    <div class="section" id="projectInfo">

        <h4>项目详情</h4>
        <input name="id" type="hidden" class="span2" value="${pro.id}" readonly="readonly">
        <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
            <tr>
                <td width="15%"><span class="td_title">项目编号</span></td>
                <td>${pro.projectId}</td>
                <td width="15%"><span class="td_title">项目名称</span></td>
                <td>${pro.proName}</td>
            </tr>
            <tr>
                <td><span class="td_title">工程简称</span></td>
                <td>${pro.shortname}</td>
                <td><span class="td_title">中标通知书日期</span></td>
                <%--<td>${pro.zbtzsrq}</td>--%>
                <td><fmt:formatDate value="${pro.zbtzsrq}" pattern="yyyy年MM月dd日" /></td>
            </tr>
            <tr>
                <td><span class="td_title">工程合同价</span></td>
                <td>${pro.gchtj}</td>
                <td><span class="td_title">建筑面积或规模值</span></td>
                <td>${pro.jzmjorgm}</td>
            </tr>
            <tr>
                <td><span class="td_title">省</span></td>
                <td>${pro.provice}</td>
                <td><span class="td_title">市</span></td>
                <td>${pro.city}</td>
            </tr>
            <tr>
                <td><span class="td_title">投标项目经理</span></td>
                <td>${pro.manager2}</td>
                <td><span class="td_title">工程类型</span></td>
                <td>${pro.gclx}</td>
            </tr>
            <tr>
                <td><span class="td_title">开工日期</span></td>
                <%--<td>${pro.kgrq}</td>--%>
                <td><fmt:formatDate value="${pro.kgrq}" pattern="yyyy年MM月dd日" /></td>
                <td><span class="td_title">竣工日期</span></td>
                <%--<td>${pro.jgrq}</td>--%>
                <td><fmt:formatDate value="${pro.jgrq}" pattern="yyyy年MM月dd日" /></td>
            </tr>
            <tr>
                <td><span class="td_title">造价类型</span></td>
                <td>${pro.zjlx}</td>
                <td><span class="td_title">工程状态</span></td>
                <td>${pro.gczt}</td>
            </tr>
            <tr>
                <td><span class="td_title">工程质保约定</span></td>
                <td>${pro.gczbyd}</td>
                <td><span class="td_title">工程付款约定</span></td>
                <td>${pro.gcfkyd}</td>
            </tr>

            <tr>
                <td><span class="td_title">施工项目经理</span></td>
                <td>${pro.manager}</td>
                <td><span class="td_title">工程款支付状态</span></td>
                <td>${pro.money_state}</td>
            </tr>
            <tr>
                <td><span class="td_title">工程正式开工日期</span></td>
                <%--<td>${pro.gckgrq}</td>--%>
                <td><fmt:formatDate value="${pro.gckgrq}" pattern="yyyy年MM月dd日" /></td>
                <td><span class="td_title">工程正式竣工日期</span></td>
                <%--<td>${pro.gcjgrq}</td>--%>
                <td><fmt:formatDate value="${pro.gcjgrq}" pattern="yyyy年MM月dd日" /></td>
            </tr>
            <tr>
                <td><span class="td_title">工程到期移交情况</span></td>
                <td>${pro.gcdqyjqk}</td>
                <td><span class="td_title">工程质量获奖情况</span></td>
                <td>${pro.gczlhjqk}</td>
            </tr>
            <tr>
                <td><span class="td_title">竣工结算书</span></td>
                <td>${pro.jgjss}</td>
                <td><span class="td_title">工程安全文明情况</span></td>
                <td>${pro.gcaqwmqk}</td>
            </tr>
            <tr>
                <td><span class="td_title">竣工报告</span></td>
                <td>${pro.jgbb}</td>
                <td><span class="td_title">竣工资料</span></td>
                <td>${pro.jgzl}</td>
            </tr>
            <tr>
                <td><span class="td_title">造价员</span></td>
                <td>${pro.zjy}</td>
                <td><span class="td_title">资料员</span></td>
                <td>${pro.zly}</td>
            </tr>

            <tr>
                <td><span class="td_title">养护级别</span></td>
                <td>${pro.yhjb}</td>
                <td><span class="td_title">养护承包人</span></td>
                <td>${pro.yhcbr}</td>
            </tr>
            <tr>
                <td><span class="td_title">内部维(养)护开始时间</span></td>
                <%--<td>${pro.whkssj}</td>--%>
                <td><fmt:formatDate value="${pro.whkssj}" pattern="yyyy年MM月dd日" /></td>
                <td><span class="td_title">内部维(养)护结束时间</span></td>
                <%--<td>${pro.whjssj}</td>--%>
                <td><fmt:formatDate value="${pro.whjssj}" pattern="yyyy年MM月dd日" /></td>
            </tr>
            <tr>
                <td><span class="td_title">合同维(养)护截止日</span></td>
                <%--<td>${pro.htwhjzr}</td>--%>
                <td><fmt:formatDate value="${pro.htwhjzr}" pattern="yyyy年MM月dd日" /></td>
                <td><span class="td_title">工程移交日期</span></td>
                <%--<td>${pro.gcyjrq}</td>--%>
                <td><fmt:formatDate value="${pro.gcyjrq}" pattern="yyyy年MM月dd日" /></td>
            </tr>
            <tr>
                <td><span class="td_title">工程维(养)护期</span></td>
                <td>${pro.gcwhq}</td>
                <td></td>
                <td></td>
            </tr>
        </table>
    </div>

    <div class="section" id="costStat">
        <h4>费用汇总</h4>
        <div style="display: table" >
            <div id="feeChart" style="float:left; "></div>
            <div style="float:left;" id="feeDiv">
                <table id="feeTable" class="table_style" style="font-size: 12px; display: none; " cellpadding="0" cellspacing="0"  >
                    <thead>
                        <tr>
                            <th>费用类型</th>
                            <th>费用</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr id="cloneTr">
                            <td></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="section" id="costDetail" style="padding-bottom:20px;">
        <h4>费用明细</h4>
        <table id="dataGridCost" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbar" class="fee_detail" style="display: none;">
            <span>操作人:</span>
            <input class="easyui-textbox"  style="margin-top:9px; width: 150px;height: 17px;" type="text" name="uname" id="uname"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span>费用类型:</span>
            <%--<input class="easyui-textbox"  type="text" name="costType" id="costType" data-options=""/>--%>
            <select style="width: 150px" name="dataCostType" id="dataCostType" >
                <option></option>
                <c:forEach var="costTypeInfo" items="<%= dataCostInfos %>" varStatus="index">
                    <c:if test="${costTypeInfo.isSend == '0'}">
                        <optgroup label="${costTypeInfo.costType}"> " " </optgroup>
                    </c:if>
                    <c:if test="${costTypeInfo.isSend == '1'}">
                        <option value="${costTypeInfo.costType}">&nbsp;&nbsp;&nbsp;&nbsp;${costTypeInfo.costType}</option>
                    </c:if>
                </c:forEach>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;

            <span>起止时间:</span>
                <input style="width: 150px" class="easyui-datebox" name="startTime" id='startTime' editable="false" placeholder="点击选择时间"  value='${first }' />
                - <input style="width: 150px" class="easyui-datebox"  name="endTime" id='endTime'  editable="false" placeholder="点击选择时间"  value='${last }' />

            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="cleanFun();">清空条件</a>

        </div>

    </div>

    <div class="section" id="docDetail" style="padding-bottom:20px;">
        <h4>资料明细</h4>
        <table id="dataGridDoc" class="easyui-datagrid" width="100%">
        </table>
        <div id="toolbarDoc" class="fee_detail" style="display: none;">
            <span>操作人:</span><input style="margin-top:9px; width: 150px;height: 17px;" class="easyui-textbox"  type="text" name="unameDoc" id="unameDoc" data-options=""/>
            <span>资料类型:</span>
            <%--<input class="easyui-textbox"  type="text" name="costTypeDoc" id="costTypeDoc" data-options=""/>--%>
            <select style="width: 150px"  name="docCostType" id="docCostType">
                <option></option>
                <c:forEach var="costTypeInfo" items="<%= docCostInfos %>" varStatus="index">
                    <c:if test="${costTypeInfo.isSend == '0'}">
                        <optgroup label="${costTypeInfo.costType}"> " " </optgroup>
                    </c:if>
                    <c:if test="${costTypeInfo.isSend == '1'}">
                        <option value="${costTypeInfo.costType}">&nbsp;&nbsp;&nbsp;&nbsp;${costTypeInfo.costType}</option>
                    </c:if>
                </c:forEach>
            </select>

            <span>起止时间:</span>
            <input style="width: 150px" class="easyui-datebox" name="startTimeDoc" id='startTimeDoc' editable="false" placeholder="点击选择时间"  value='${first }' />
            - <input style="width: 150px" class="easyui-datebox"  name="endTimeDoc" id='endTimeDoc' editable="false" placeholder="点击选择时间"  value='${last }' />

            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFunDoc();">过滤条件</a>
            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="cleanFunDoc();">清空条件</a>

        </div>

    </div>

</div>





<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery.nav.js"></script>

<script>
    formatterFirstDate = function(date) {
        var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
        var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1);
        return date.getFullYear() + '-' + month + '-' + '01';
    };
    formatterCurrentDate = function(date) {
        var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
        var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1);
        return date.getFullYear() + '-' + month + '-' + day;
    };

    window.onload = function () {
        $('#startTime').datebox('setValue', formatterFirstDate(new Date()));
        $('#endTime').datebox('setValue', formatterCurrentDate(new Date()));
        $('#startTimeDoc').datebox('setValue', formatterFirstDate(new Date()));
        $('#endTimeDoc').datebox('setValue', formatterCurrentDate(new Date()));
    }

    $(function() {
        $('#dataGridCost')
                .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/dataGrid?source=data&id=${pro.id}',
                    fit : true,
                    fitColumns : true,
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
                            field : 'projectName',
                            title : '工程名称',
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
                            field : 'money',
                            title : '金额',
                            width : 100,
                            formatter : function(value, row, index) {
                                if(value != undefined) {
                                    return value.toFixed(2);
                                }
                                return (row.count * row.price)
                                        .toFixed(2);
                            }

                        },
                        {
                            field : 'specifications',
                            title : '规格类型',
                            width : 50
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 50
                        },
                        {
                            field : 'creatTime',
                            title : '入库时间',
                            width : 100
                        }
                        ,
                        {
                            field : 'action',
                            title : '操作',
                            width : 53,
                            formatter : function(value, row, index) {
                                var str = '';
                                if(value != undefined && value == true) { // footer 不需要操作
                                    return str;
                                }
                                str += $
                                        .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/book_go.png');
                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbar',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
//                        $(this).datagrid('tooltip');

                    }
                });
    });

    $(function() {
        $('#dataGridDoc')
                .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/dataGrid?source=doc&id=${pro.id}',
                    fit : true,
                    fitColumns : true,
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
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'costType',
                            title : '类型',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '名称',
                            width : 100
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
                        }
                        ,
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                str += $
                                        .formatString(
                                        ' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/book_go.png');
                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbarDoc',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
//                        $(this).datagrid('tooltip');
                    }
                });
    });

    $(document).ready(function() {

        $('#nav').onePageNav();

        //表格样式
        function tableStyle(obj){
            $(obj).children('tbody').find('tr:even').css({background:'#fff'})
            $(obj).children('tbody').on('mouseover','tr',function(){
                $(this).addClass('hover');
                $(this).siblings('tr').removeClass('hover')
            }).on('mouseout','tr',function(){
                $(this).removeClass('hover');
            });
        }

        tableStyle('.table_style');

        $("#dataCostType").select2({
            tags: "true",
            placeholder: "可以模糊查询",
            allowClear: true,
            <%--data:<%=dataTypeInfos%>--%>
        });
        $("#docCostType").select2({
            tags: "true",
            placeholder: "可以模糊查询",
            allowClear: true,
            <%--data:<%=docTypeInfos%>--%>
        });

    });

    //过滤条件查询
    function searchFun() {
        $('#startTime').val($('#startTime').val().substring(0, 10) + ' 00:00:00');
        $('#endTime').val($('#endTime').val().substring(0, 10) + ' 23:59:59');
//        $('#dataGridCost').datagrid('reload',{uname:$('#uname').val(),costType:$('#costType').val(),
//                                              startTime:$('#startTime').datebox('getValue'),endTime:$('#endTime').datebox('getValue')});
        $('#dataGridCost').datagrid('reload',{uname:$('#uname').val(),costType:$('#dataCostType').val(),
                                             startTime:$('#startTime').datebox('getValue'),endTime:$('#endTime').datebox('getValue')});
    }
    //清除条件
    function cleanFun() {
        $('#toolbar input').val('');
        $('#dataGridCost').datagrid('reload', {});
    }

    //过滤条件查询
    function searchFunDoc() {
        $('#startTimeDoc').val($('#startTimeDoc').val().substring(0, 10) + ' 00:00:00');
        $('#endTimeDoc').val($('#endTimeDoc').val().substring(0, 10) + ' 23:59:59');
        $('#dataGridDoc').datagrid('reload',{uname:$('#unameDoc').val(),costType:$('#docCostType').val(),
            startTime:$('#startTimeDoc').datebox('getValue'),endTime:$('#endTimeDoc').datebox('getValue')});
    }
    //清除条件
    function cleanFunDoc() {
        $('#toolbarDoc input').val('');
        $('#dataGridDoc').datagrid('reload', {});
    }

    //附件管理
    function FileFun(id) {
        parent.$
                .modalDialog({
                    title : '附件管理',
                    width : 800,
                    height : 600,
                    href : '${pageContext.request.contextPath}/fieldDataController/securi_fieldDataFile?id='
                    + id,
                    buttons : [ {
                        text : '关闭',
                        handler : function() {
                            parent.$.modalDialog.handler.dialog('close');
                        }
                    } ]
                });
    }

    // 饼图
    $(function () {
        var chart;
        $(document).ready(function() {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: "feeChart",
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                //图表的主标题
                title: {
                    text : "费用统计分布图"
                },
                //当鼠标经过时的提示设置
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
                },
                //每种图表类型属性设置
                plotOptions: {
                    //饼状图
                    pie: {
                        allowPointSelect: true,
                        cursor: "pointer",
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter : function () {
                                return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.y, 2, ".", ",")
                            }
                        }
                    }
                },
                //图表要展现的数据
                series: [{
                        type: "pie",
                        name: "费用占比"
                    }]
            });
        });

        //异步请求数据
        $.ajax({
            type:"GET",
            url:"${pageContext.request.contextPath}/projectStatController/securi_feeStat?id="+${pro.id},//提供数据的Servlet
            success:function(data){
                //定义一个数组
                browsers = [];
                $.each(jQuery.parseJSON(eval(data)).cata,function(key,value){
                    browsers.push([key,value]);
                });
                //设置数据
                chart.series[0].setData(browsers);

                // table begin
                var tr = $("#cloneTr");

                var clonedTr = tr.clone();
                clonedTr.children("td").each(function(inner_index) {
                    switch (inner_index) {
                        case(0):
                            $(this).html("<b>合计:</b>");
                            break;
                        case(1):
                            $(this).html("<b>"+jQuery.parseJSON(eval(data)).totalMoney + "</b>");
                            break;
                    }
                });
                clonedTr.insertAfter(tr);

                $.each(jQuery.parseJSON(eval(data)).cata, function(key, value){
                    //克隆tr，每次遍历都可以产生新的tr
                    var clonedTr = tr.clone();

                    //循环遍历cloneTr的每一个td元素，并赋值
                    clonedTr.children("td").each(function(inner_index){

                        //根据索引为每一个td赋值
                        switch(inner_index){
                            case(0):
                                $(this).html(key);
                                break;
                            case(1):
                                $(this).html(value.toFixed(2));
                                break;
                        }//end switch
                    });//end children.each
                    //把克隆好的tr追加原来的tr后面
                    clonedTr.insertAfter(tr);
                });//end $each

                $("#cloneTr").hide();//隐藏id=clone的tr，因为该tr中的td没有数据，不隐藏起来会在生成的table第一行显示一个空行
                $("#feeTable").show();

                // table end
            },
            error:function(e){
                alert(e);
            }
        });

    });

</script>
</body>

</html>
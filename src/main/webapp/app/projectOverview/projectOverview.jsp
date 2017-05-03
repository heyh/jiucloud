<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //    String underlingUsers = null;
    List<Map<String, Object>> dataCostInfos = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> docCostInfos = new ArrayList<Map<String, Object>>();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
//        underlingUsers = sessionInfo.getUnderlingUsers();
        dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        docCostInfos = sessionInfo.getCostTypeInfos().get("docCostInfos");
    }

%>
<!DOCTYPE html>
<html>
<head>
    <title>项目详情</title>
    <%--<jsp:include page="../../inc2.jsp"></jsp:include>--%>
    <link href="${pageContext.request.contextPath}/jslib/bootstrap-2.3.1/css/bootstrap.min.css" rel="stylesheet"
          media="screen">
    <!-- 引入jQuery -->
    <script src="${pageContext.request.contextPath}/jslib/jquery-1.8.3.js"
            type="text/javascript" charset="utf-8"></script>
    <link id="easyuiTheme" rel="stylesheet"
          href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/themes/bootstrap/easyui.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/themes/icon.css"
          type="text/css">
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


    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet"/>
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
            padding: 0px 10px 0 120px;

        }

        .section p:last-child {
            margin-bottom: 0;
        }

        #container {
            padding:20px;
        }

    </style>
</head>

<body>
<ul id="nav" style="padding-top: 20px">
    <li class="current"><a href="#projectDataInfos">分部分项</a></li>
    <li><a href="#cs1Infos">措施项目（一)</a></li>
    <li><a href="#cs2Infos">措施项目（二）</a></li>
    <li><a href="#qtInfos">其它项目</a></li>
    <li><a href="#gljSummaryInfos">工料机</a></li>
    <li><a href="#summaryInfos">取费</a></li>
</ul>
<div id="container">
    <div class="section" id="projectDataInfos">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>分部分项</b></span>
        <table id="projectDataTable" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th field="projectDataname1" width="50">序号</th>
                <th field="projectDataname2" width="100">定额号</th>
                <th field="projectDataname3" width="100">项目名称</th>
                <th field="projectDataname4" width="100">项目特征</th>
                <th field="projectDataname5" width="100">单位</th>
                <th field="projectDataname6" width="100">数量</th>
                <th field="projectDataname7" width="100">单价</th>
                <th field="projectDataname8" width="100">人工费</th>
                <th field="projectDataname9" width="100">材料费</th>
                <th field="projectDataname10" width="100">机械费</th>
                <th field="projectDataname11" width="100">管理费</th>
                <th field="projectDataname12" width="100">利润</th>
                <th field="projectDataname13" width="100">合计</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.projectDataInfos}" var="projectDataInfo" varStatus="projectDataInfoStatus">
                <tr onclick="getProjectMachineInfo(projectDataInfo.mdbPath, projectDataInfo.pointNo)">
                    <td>${projectDataInfoStatus.index + 1}</td>
                    <td>${projectDataInfo.deh}</td>
                    <td>${projectDataInfo.zmmc}</td>
                    <td>${projectDataInfo.mshxmtz}</td>
                    <td>${projectDataInfo.zmdw}</td>
                    <td>${projectDataInfo.gclz}</td>
                    <td align="right">${projectDataInfo.jjdj}</td>
                    <td align="right">${projectDataInfo.rgfdj}</td>
                    <td align="right">${projectDataInfo.clfdj}</td>
                    <td align="right">${projectDataInfo.jxfdj}</td>
                    <td align="right">${projectDataInfo.glf}</td>
                    <td align="right">${projectDataInfo.jhlrz}</td>
                    <td align="right">${projectDataInfo.jj}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section" id="cs1Infos">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>措施项目（一)</b></span>
        <table id="cs1Table" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th field="cs1name1" width="100">序号</th>
                <th field="cs1name2" width="200">项目名称</th>
                <th field="cs1name3" width="50">单位</th>
                <th field="cs1name4" width="200">计算公式</th>
                <th field="cs1name5" width="150">基数</th>
                <th field="cs1name6" width="100">费率(%)</th>
                <th field="cs1name7" width="100">费率范围</th>
                <th field="cs1name8" width="100">合计</th>
                <th field="cs1name9" width="100">文明费</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.cs1Infos}" var="cs1Info" varStatus="cs1InfoStatus">
                <tr>
                    <td>${cs1Info.measureXh}</td>
                    <td>${cs1Info.measureName}</td>
                    <td>${cs1Info.measureDw}</td>
                    <td>${cs1Info.measureJsgs}</td>
                    <td>${cs1Info.measureBase}</td>
                    <td>${cs1Info.measureFee}</td>
                    <td>${cs1Info.flfw}</td>
                    <td>${cs1Info.measureSum}</td>
                    <td>${cs1Info.measureWmf}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section" id="cs2Infos" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>措施项目（二）</b></span>
        <table id="cs2Table" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th field="cs2name1" width="50">序号</th>
                <th field="cs2name2" width="100">定额号</th>
                <th field="cs2name3" width="100">项目名称</th>
                <th field="cs2name4" width="100">项目特征</th>
                <th field="cs2name5" width="100">单位</th>
                <th field="cs2name6" width="100">数量</th>
                <th field="cs2name7" width="100">单价</th>
                <th field="cs2name8" width="100">人工费</th>
                <th field="cs2name9" width="100">材料费</th>
                <th field="cs2name10" width="100">机械费</th>
                <th field="cs2name11" width="100">管理费</th>
                <th field="cs2name12" width="100">利润</th>
                <th field="cs2name13" width="100">合计</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.cs2Infos}" var="cs2Info" varStatus="cs2InfoStatus">
                <tr>
                    <td>${cs2InfoStatus.index + 1}</td>
                    <td>${cs2Info.deh}</td>
                    <td>${cs2Info.zmmc}</td>
                    <td>${cs2Info.mshxmtz}</td>
                    <td>${cs2Info.zmdw}</td>
                    <td>${cs2Info.gclz}</td>
                    <td align="right">${cs2Info.jjdj}</td>
                    <td align="right">${cs2Info.rgfdj}</td>
                    <td align="right">${cs2Info.clfdj}</td>
                    <td align="right">${cs2Info.jxfdj}</td>
                    <td align="right">${cs2Info.glf}</td>
                    <td align="right">${cs2Info.jhlrz}</td>
                    <td align="right">${cs2Info.jj}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section" id="qtInfos" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>其它项目</b></span>
        <table id="qtTable" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th field="qtname1" width="50">序号</th>
                <th field="qtname2" width="100">项目名称</th>
                <th field="qtname3" width="100">单位</th>
                <th field="qtname4" width="100">计算公式</th>
                <th field="qtname5" width="100">基数</th>
                <th field="qtname6" width="100">费率(%)</th>
                <th field="qtname7" width="100">金额</th>
                <th field="qtname8" width="100">甲供</th>
                <th field="qtname9" width="100">备注</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.qtInfos}" var="qtInfo" varStatus="qtInfoStatus">
                <tr>
                    <td>${qtInfo.id}</td>
                    <td>${qtInfo.name}</td>
                    <td>${qtInfo.dw}</td>
                    <td>${qtInfo.jsgs}</td>
                    <td>${qtInfo.js}</td>
                    <td>${qtInfo.fl}</td>
                    <td>${qtInfo.je}</td>
                    <td>${qtInfo.jg}</td>
                    <td>${qtInfo.bz}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section" id="gljSummaryInfos" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>工料机</b></span>
        <table id="gljSummaryTable" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th width="100">编码</th>
                <th width="100">工料机名称</th>
                <th width="100">规格型号</th>
                <th width="100">单位</th>
                <th width="100">含税价</th>
                <th width="100">采保(%)</th>
                <th width="100">税率(%)</th>
                <th width="100">除税价</th>
                <th width="100">定额含量</th>
                <th width="100">预算含量</th>
                <th width="100">系数</th>
                <th width="100">含量</th>
                <th width="100">金额</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.gljSummaryInfos}" var="gljSummaryInfo" varStatus="gljSummaryInfoStatus">
                <tr>
                    <td>${gljSummaryInfo.clbm}</td>
                    <td>${gljSummaryInfo.clmc}</td>
                    <td>${gljSummaryInfo.ggxh}</td>
                    <td>${gljSummaryInfo.dw}</td>
                    <td>${gljSummaryInfo.hsj}</td>
                    <td>${gljSummaryInfo.cb}</td>
                    <td>${gljSummaryInfo.taxrate}</td>
                    <td>${gljSummaryInfo.csj}</td>
                    <td>${gljSummaryInfo.dehl}</td>
                    <td>${gljSummaryInfo.yshl}</td>
                    <td>${gljSummaryInfo.xs}</td>
                    <td>${gljSummaryInfo.hl}</td>
                    <td>${gljSummaryInfo.je}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section" id="summaryInfos" style="padding-bottom:20px;">
        <img src="${pageContext.request.contextPath}/images/verticalLine.png"
             style="padding-top:20px; padding-bottom:20px; vertical-align:middle"/>
        <span style="vertical-align:middle; font-family:SimSun; margin-left: 4px"><b>取费</b></span>
        <table id="summaryTable" class="easyui-datagrid" style="height: 400px">
            <thead>
            <tr>
                <th field="summaryname1" width="50">编号</th>
                <th field="summaryname2" width="150">费用名称</th>
                <th field="summaryname3" width="250">计算公式</th>
                <th field="summaryname4" width="100">基数</th>
                <th field="summaryname5" width="100">费率(%)</th>
                <th field="summaryname6" width="100">计算结果</th>
                <th field="summaryname7" width="100">是否总价</th>
                <th field="summaryname8" width="100">甲供</th>
                <th field="summaryname9" width="100">规税编号</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mdbInfo.summaryInfos}" var="summaryInfo" varStatus="summaryInfoStatus">
                <tr>
                    <td>${summaryInfo.FEEXH}</td>
                    <td>${summaryInfo.FEENAME}</td>
                    <td>${summaryInfo.FEECALC}</td>
                    <td>${summaryInfo.FEEBASE}</td>
                    <td>${summaryInfo.FEERATE}</td>
                    <td>${summaryInfo.FEERESULT}</td>
                    <td>${summaryInfo.ISSHOW}</td>
                    <td>${summaryInfo.FEEJG}</td>
                    <td>${summaryInfo.FEEREMARK}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery.nav.js"></script>

<script>

    $(document).ready(function () {

        $('#nav').onePageNav();
    });

    function getProjectMachineInfo(mdbPath, pointNo) {
        $.ajax({
            url: '${pageContext.request.contextPath }/projectOverviewController/securi_getProjectMachineInfo',
            type: 'post',
            data: {'mdbPath': mdbPath, 'pointNo': pointNo},
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (data) {

                if (data.success) {

                }

            }
        });
    }
</script>
</body>

</html>
<%--
  Created by IntelliJ IDEA.
  User: heyh
  Date: 16/2/1
  Time: 下午9:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    Integer parentId = 1;
    List<String> rightList = null;
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        parentId = sessionInfo.getParentId();
        rightList = sessionInfo.getRightList();
    }

%>

<style>
    .container-fluid {
        background: #f7f7f7;
    }

    /*fieldset {*/
        /*margin-top: 20px;*/
    /*}*/

    legend {
        color: #5eade0;
        font-weight: 800;
        font-size: 14px;
    }

    .row-fluid {
        margin-top: 20px;
    }

    .showBase {
        display: <%= parentId == 0 || rightList.contains("3") ? "block" : "none"%>;
    }

    .showAgreement {
        display: <%= parentId == 0 || rightList.contains("4") ? "block" : "none"%>;
    }

    .showBuild {
        display: <%= parentId == 0 || rightList.contains("5") ? "block" : "none"%>;
    }
    .showMaintenance {
        display: <%= parentId == 0 || rightList.contains("6") ? "block" : "none"%>;
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

</style>

<script type="text/javascript">

    $(function() {
        parent.$.messager.progress('close');
    });
    //添加
    function addFun() {
        var url = '${pageContext.request.contextPath}/fieldDataController/addfieldData?proid=${pro.id}';
        var text = "添加现场数据";
        var params = {
            url : url,
            title : text,
            iconCls : 'wrench'
        }
        window.parent.ac(params);
        parent.$.modalDialog.handler.dialog('close');
    }

</script>

<div class="container-fluid">

    <form class="form-horizontal" name="form" id="form" method="post">
        <input name="id" type="hidden" class="span2" value="${pro.id}" readonly="readonly"/>
        <input type="hidden" id="provicehid" value='${pro.provice }'/>
        <input type="hidden" id="cityhid" value='${pro.city }'/>

        <fieldset class="showBase">
            <legend>项目立项</legend>
            <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"><span class="td_title">项目编号</span></td>
                    <td>${pro.projectId}</td>
                    <td width="20%"><span class="td_title">财务编码</span></td>
                    <td>${pro.financeCode}</td>
                </tr>
                <tr>
                    <td><span class="td_title">工程合同名称</span></td>
                    <td>${pro.proName}</td>
                    <td><span class="td_title">工程简称</span></td>
                    <td>${pro.shortname}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="showAgreement">
            <legend>合同基本信息</legend>
            <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"><span class="td_title">中标通知书日期</span></td>
                    <td><fmt:formatDate value="${pro.zbtzsrq}" pattern="yyyy年MM月dd日" /></td>
                    <td width="20%"><span class="td_title">合同签订日期</span></td>
                    <td><fmt:formatDate value="${pro.htqdrq}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">合同金额</span></td>
                    <td>${pro.gchtj}</td>
                    <td><span class="td_title">投标项目经理</span></td>
                    <td>${pro.manager2}</td>
                </tr>
                <tr>
                    <td><span class="td_title">开工日期</span></td>
                    <td><fmt:formatDate value="${pro.kgrq}" pattern="yyyy年MM月dd日" /></td>
                    <td><span class="td_title">竣工日期</span></td>
                    <td><fmt:formatDate value="${pro.jgrq}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">工程质保约定</span></td>
                    <td>${pro.gczbyd}</td>
                    <td><span class="td_title">工程付款约定</span></td>
                    <td>${pro.gcfkyd}</td>
                </tr>
                <tr>
                    <td><span class="td_title">项目所在省</span></td>
                    <td>${pro.provice}</td>
                    <td><span class="td_title">项目所在市</span></td>
                    <td>${pro.city}</td>
                </tr>
                <tr>
                    <td><span class="td_title">项目所在区县</span></td>
                    <td>${pro.area}</td>
                    <td><span class="td_title"></span></td>
                    <td></td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="showBuild">
            <legend>项目施工阶段</legend>
            <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"><span class="td_title">施工项目经理</span></td>
                    <td>${pro.manager}</td>
                    <td width="20%"><span class="td_title">工程正式开工日期</span></td>
                    <td><fmt:formatDate value="${pro.gckgrq}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">工程正式竣工日期</span></td>
                    <td><fmt:formatDate value="${pro.gcjgrq}" pattern="yyyy年MM月dd日" /></td>
                    <td><span class="td_title">工程质量获奖情况</span></td>
                    <td>${pro.gczlhjqk}</td>
                </tr>
                <tr>
                    <td><span class="td_title">合同到期移交情况</span></td>
                    <td>${pro.gcdqyjqk}</td>
                    <td><span class="td_title">资料员</span></td>
                    <td>${pro.zly}</td>
                </tr>
                <tr>
                    <td><span class="td_title">工程安全文明情况</span></td>
                    <td>${pro.gcaqwmqk}</td>
                    <td><span class="td_title">竣工资料</span></td>
                    <td>${pro.jgzl}</td>
                </tr>
                <tr>
                    <td><span class="td_title">竣工报告</span></td>
                    <td>${pro.jgbb}</td>
                    <td><span class="td_title">竣工结算书</span></td>
                    <td>${pro.jgjss}</td>
                </tr>
                <tr>
                    <td><span class="td_title">造价员</span></td>
                    <td>${pro.zjy}</td>
                    <td><span class="td_title">初审额</span></td>
                    <td>${pro.cse}</td>
                </tr>
                <tr>
                    <td><span class="td_title">申报额</span></td>
                    <td>${pro.sbe}</td>
                    <td><span class="td_title">审计额</span></td>
                    <td>${pro.sje}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="showMaintenance">
            <legend>项目后期维护</legend>
            <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"><span class="td_title">养护项目经理</span></td>
                    <td>${pro.maintenanceManager}</td>
                    <td width="20%"><span class="td_title">竣工验收时间</span></td>
                    <td><fmt:formatDate value="${pro.jgysrq}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">合同养护期</span></td>
                    <td>${pro.htyhq}</td>
                    <td><span class="td_title">公司内部养护开始日期</span></td>
                    <td><fmt:formatDate value="${pro.whkssj}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">公司内部养护结束日期</span></td>
                    <td><fmt:formatDate value="${pro.whjssj}" pattern="yyyy年MM月dd日" /></td>
                    <td><span class="td_title">合同养护截止日期</span></td>
                    <td><fmt:formatDate value="${pro.htwhjzr}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">养护承包费用</span></td>
                    <td>${pro.maintenanceCost}</td>
                    <td><span class="td_title">养护级别</span></td>
                    <td>${pro.yhjb}</td>
                </tr>
                <tr>
                    <td><span class="td_title">养护承保人</span></td>
                    <td>${pro.yhcbr}</td>
                    <td><span class="td_title">工程移交日期</span></td>
                    <td><fmt:formatDate value="${pro.gcyjrq}" pattern="yyyy年MM月dd日" /></td>
                </tr>
                <tr>
                    <td><span class="td_title">项目经理确认</span></td>
                    <td>${pro.managerConfirm}</td>
                    <td><span class="td_title"></span></td>
                    <td></td>
                </tr>
            </table>
        </fieldset>

        <fieldset>
            <table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="20%"><span class="td_title"></span></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td width="20%"><span class="td_title"></span></td>
                    <td><span>操作</span>&nbsp;&nbsp;<input type="button" value="添加现场数据" onclick="addFun()" style="color: red"></td>
                </tr>
            </table>
        </fieldset>


    </form>

</div>

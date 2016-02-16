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
<%--<html>--%>
<%--<head>--%>
<%--<title>增加工程</title>--%>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<%--<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>--%>

<!-- 新 Bootstrap 核心 CSS 文件 -->
<%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.css">--%>
<%--<!-- 可选的Bootstrap主题文件（一般不用引入） -->--%>
<%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.css">--%>
<%--<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->--%>
<%--<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.js"></script>--%>

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

<%--<link rel="stylesheet"--%>
<%--href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/less/datepicker.less">--%>


<!-- 引入EasyUI -->
<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"
        charset="UTF-8"></script>

<style>
    .container-fluid {
        background: #f7f7f7;
    }

    fieldset {
        /*margin-top: 20px;*/
    }

    legend {
        color: #5eade0;
        font-weight: 800;
        background: #f7f7f7;
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

</style>

<script type="text/javascript">

    $('.input-append').datepicker({
        format: "yyyy-mm-dd",
        language: "zh-CN",
        autoclose: true,
        todayHighlight: true,
        maxViewMode: 1,
        todayBtn: true
    });

    $(function () {
        parent.$.messager.progress('close');
        $('#form').form({
            url: '${pageContext.request.contextPath}/projectController/update',
            onSubmit: function () {
                parent.$.messager.progress({
                    title: '提示',
                    text: '数据处理中，请稍后....'
                });
                var isValid = $(this).form('validate');
                if (!isValid) {
                    parent.$.messager.progress('close');
                }
                for (var i = 1; i < 12; i++) {
                    if ($('#date' + i).val() == '') {
                        continue;
                    }
                    $('#date' + i).val($('#date' + i).val() + ' 00:00:00');
                }
                return isValid;
            },
            success: function (result) {
                parent.$.messager.progress('close');
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.messager.progress('close');
//                    alert("添加成功!");
//                    jQuery.messager.alert('提示:','添加成功!','info');
                    jQuery.messager.show({
                        title:'温馨提示:',
                        msg:'修改成功!',
                        timeout:3000,
                        showType:'show'
                    });
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });

    var cfg = {
        url : '${pageContext.request.contextPath}/projectController/getCities',
        type : 'GET',
        dataType : 'json',
        success : function(dataObj) {
            $("#city").empty();
            $.each(dataObj, function(idx, item) {
                $(
                        "<option value='" + item.name + "'>"
                        + item.name + "</option>").appendTo(
                        $("#city"));
            });
            getAreas();
        }
    };

    function getCity() {
        //获取表单值，并以json的数据形式保存到data中
        cfg.data = {
            provincename : $("#provice").val()
        }
        $.ajax(cfg);
    }

    var cfg1 = {
        url : '${pageContext.request.contextPath}/projectController/securi_getAreas',
        type : 'GET',
        dataType : 'json',
        success : function(dataObj) {
            $("#area").empty();
            $.each(dataObj, function(idx, item) {
                $(
                        "<option value='" + item.name + "'>"
                        + item.name + "</option>").appendTo(
                        $("#area"));
            });
        }
    };

    function getAreas() {
        cfg1.data = {
            cityname : $("#city").val()
        }
        $.ajax(cfg1);
    }

    var provicehid = document.getElementById('provicehid').value;
    for (var i = 0; i < document.getElementById('provice').options.length; i++) {
        if (document.getElementById('provice').options[i].value == provicehid) {
            document.getElementById('provice').options[i].selected = true;
            break;
        }
    }
    var cityhid = document.getElementById('cityhid').value;
    for (var i = 0; i < document.getElementById('city').options.length; i++) {
        if (document.getElementById('city').options[i].value == cityhid) {
            document.getElementById('city').options[i].selected = true;
            break;
        }
    }
    var areahid = document.getElementById('areahid').value;
    for (var i = 0; i < document.getElementById('area').options.length; i++) {
        if (document.getElementById('area').options[i].value == areahid) {
            document.getElementById('area').options[i].selected = true;
            break;
        }
    }
</script>
<%--</head>--%>
<%--<body>--%>
<div class="container-fluid">

    <form class="form-horizontal" name="form" id="form" method="post" role="form">
        <input name="id" type="hidden" class="span2" value="${pro.id}" readonly="readonly"/>
        <input type="hidden" id="provicehid" value='${pro.provice }'/>
        <input type="hidden" id="cityhid" value='${pro.city }'/>
        <input type="hidden" id="areahid" value='${pro.area }'/>

        <fieldset class="showBase">
            <legend>项目立项</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="projectId">项目编码:</label>

                        <div class="controls">
                            <input type="text" name="projectId" id="projectId" value="${pro.projectId}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="proName">工程合同名称:</label>

                        <div class="controls">
                            <input type="text" name="proName" id="proName" value="${pro.proName}">
                        </div>
                    </div>
                </div>

                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="financeCode">财务编码:</label>

                        <div class="controls">
                            <input type="text" name="financeCode" id="financeCode" value="${pro.financeCode}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="shortname">工程简称:</label>

                        <div class="controls">
                            <input type="text" name="shortname" id="shortname" value="${pro.shortname}">
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="showAgreement">
            <legend>合同基本信息</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="date1">中标通知书日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="zbtzsrq" id="date1" readonly value="${pro.zbtzsrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gchtj">合同金额:</label>

                        <div class="controls">
                            <input type="text" name="gchtj" id="gchtj" value="${pro.gchtj}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date2">开工日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="kgrq" id="date2" readonly value="${pro.kgrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gczbyd">工程质保约定:</label>

                        <div class="controls">
                            <input type="text" name="gczbyd" id="gczbyd" value="${pro.gczbyd}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="provice">项目所在省:</label>
                        <div class="controls">
                            <select id="provice" name="provice" onchange="getCity()">
                                <c:forEach items="${provinces}" var="tem">
                                    <option value="${tem.name}">${tem.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="area">项目所在市:</label>
                        <div class="controls">
                            <select id="area" name="area">
                                <c:forEach items="${areas}" var="tem">
                                    <option value="${tem.name}">${tem.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="date3">合同签订日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="htqdrq" id="date3" readonly value="${pro.htqdrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="manager2">投标项目经理:</label>

                        <div class="controls">
                            <input type="text" name="manager2" id="manager2" value="${pro.manager2}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date4">竣工日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="jgrq" id="date4" readonly value="${pro.jgrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gcfkyd">工程付款约定:</label>

                        <div class="controls">
                            <input type="text" name="gcfkyd" id="gcfkyd" value="${pro.gcfkyd}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="city">项目所在市:</label>
                        <div class="controls">
                            <select id="city" name="city" onchange="getAreas()">
                                <c:forEach items="${cities}" var="tem">
                                    <option value="${tem.name}">${tem.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="showBuild">
            <legend>项目施工阶段</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="manager">施工项目经理:</label>

                        <div class="controls">
                            <input type="text" name="manager" id="manager" value="${pro.manager}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date5">工程正式竣工日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="gcjgrq" id="date5" readonly value="${pro.gcjgrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gcdqyjqk">合同到期移交情况:</label>

                        <div class="controls">
                            <input type="text" name="gcdqyjqk" id="gcdqyjqk" value="${pro.gcdqyjqk}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gcaqwmqk">工程安全文明情况:</label>

                        <div class="controls">
                            <input type="text" name="gcaqwmqk" id="gcaqwmqk" value="${pro.gcaqwmqk}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="jgbb">竣工报告:</label>

                        <div class="controls">
                            <input type="text" name="jgbb" id="jgbb" value="${pro.jgbb}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="zjy">造价员:</label>

                        <div class="controls">
                            <input type="text" name="zjy" id="zjy" value="${pro.zjy}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="sbe">申报额:</label>

                        <div class="controls">
                            <input type="text" name="sbe" id="sbe" value="${pro.sbe}">
                        </div>
                    </div>
                </div>

                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="date6">工程正式开工日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="gckgrq" id="date6" readonly value="${pro.gckgrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="gczlhjqk">工程质量获奖情况:</label>

                        <div class="controls">
                            <input type="text" name="gczlhjqk" id="gczlhjqk" value="${pro.gczlhjqk}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="zly">资料员:</label>

                        <div class="controls">
                            <input type="text" name="zly" id="zly" value="${pro.zly}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="jgzl">竣工资料:</label>

                        <div class="controls">
                            <input type="text" name="jgzl" id="jgzl" value="${pro.jgzl}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="jgjss">竣工结算书:</label>

                        <div class="controls">
                            <input type="text" name="jgjss" id="jgjss" value="${pro.jgjss}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="cse">初审额:</label>

                        <div class="controls">
                            <input type="text" name="cse" id="cse" value="${pro.cse}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="sje">审计额:</label>

                        <div class="controls">
                            <input type="text" name="sje" id="sje" value="${pro.sje}">
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="showMaintenance">
            <legend>项目后期维护</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="maintenanceManager">养护项目经理:</label>

                        <div class="controls">
                            <input type="text" name="maintenanceManager" id="maintenanceManager" value="${pro.maintenanceManager}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="htyhq">合同养护期:</label>

                        <div class="controls">
                            <input type="text" name="htyhq" id="htyhq" value="${pro.htyhq}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date7">公司内部养护结束日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="whjssj" id="date7" readonly value="${pro.whjssj}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="maintenanceCost">养护承包费用:</label>

                        <div class="controls">
                            <input type="text" name="maintenanceCost" id="maintenanceCost" value="${pro.maintenanceCost}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="yhcbr">养护承保人:</label>

                        <div class="controls">
                            <input type="text" name="yhcbr" id="yhcbr" value="${pro.yhcbr}" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="managerConfirm">项目经理确认:</label>

                        <div class="controls">
                            <input type="text" name="managerConfirm" id="managerConfirm" value="${pro.managerConfirm}" />
                        </div>
                    </div>
                </div>

                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="date8">竣工验收时间:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="jgysrq" id="date8" readonly value="${pro.jgysrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date9">公司内部养护开始日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="whkssj" id="date9" readonly value="${pro.whkssj}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date10">合同养护截止日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="htwhjzr" id="date10" readonly value="${pro.htwhjzr}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="yhjb">养护级别:</label>

                        <div class="controls">
                            <input type="text" name="yhjb" id="yhjb" value="${pro.yhjb}">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="date11">工程移交日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="gcyjrq" id="date11" readonly value="${pro.gcyjrq}">
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <%--<div style="text-align:center">--%>
        <%--<input type="submit" class="button" value="提  交" style="width: 250px; margin-top:20px;height: 40px;"/>--%>
        <%--</div>--%>
        <%--<div class="clear">--%>
        <%--<input type="reset" name="reset" style="display: none;"/>--%>
        <%--</div>--%>
    </form>

</div>
<%--</body>--%>
<%--</html>--%>

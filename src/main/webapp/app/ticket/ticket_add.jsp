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


%>

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

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jslib/webuploader/webuploader.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jslib/webuploader/style.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/webuploader/webuploader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/webuploader/upload-common.js"></script>

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

    .ty-summary {
        width: 100%;
    }

    .clearfix {
        zoom: 1;
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
            url: '${pageContext.request.contextPath}/ticket/add',
            onSubmit: function () {
                parent.$.messager.progress({
                    title: '提示',
                    text: '数据处理中，请稍后....'
                });

                var isValid = $(this).form('validate');
                if (!isValid) {
                    parent.$.messager.progress('close');
                }
                for (var i = 1; i < 2; i++) {
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
                    jQuery.messager.show({
                        title: '温馨提示:',
                        msg: '添加成功!',
                        timeout: 3000,
                        showType: 'show'
                    });

                    var count = 0;
                    $(".filelist").each(function () {
                        count += $(this).children('li').length;
                    });
                    if (count > 0) {
                        uploader.options.formData = {'mid': result.obj};
                        $('.uploadBtn').click();
                        uploader.on('uploadSuccess', function (file) {
                            setTimeout(function () {
                                parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                                parent.$.modalDialog.handler.dialog('close');
                            }, 1000);
                        });
                    } else {
                        parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                        parent.$.modalDialog.handler.dialog('close');
                    }
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });

    $(function () {
        var ticketType = $("#ticketType").val();
        if (ticketType == '0') {
            $("#consumerDiv").attr("style", "display:none;");
            $("#authStatusDiv").attr("style", "display:none;");
            $("#inLegend").attr("style", "display:none;");
        } else if (ticketType == '1') {
            $("#supplierDiv").attr("style", "display:none;");
            $("#ticketStatusDiv").attr("style", "display:none;");
            $("#outLegend").attr("style", "display:none;");
        }
    });
</script>

<div class="container-fluid">

    <form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
        <input type="hidden" id="ticketType" name="ticketType" value='${ticketType}'/>
        <fieldset>
            <legend id="outLegend">开票台帐</legend>
            <legend id="inLegend">收票台帐</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="ticketName">发票名称:</label>

                        <div class="controls">
                            <input type="text" name="ticketName" id="ticketName">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="contract">合同:</label>

                        <div class="controls">
                            <input type="text" name="contract" id="contract">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="taxBank">纳税账户开户银行:</label>

                        <div class="controls">
                            <input type="text" name="taxBank" id="taxBank">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="taxStatus">纳税人资格:</label>

                        <div class="controls">
                            <select id="taxStatus" name="taxStatus">
                                <option value="一般纳税人">一般纳税人</option>
                                <option value="小规模纳税人">小规模纳税人</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="count">数量:</label>

                        <div class="controls">
                            <input type="text" name="count" id="count">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="price">单价:</label>

                        <div class="controls">
                            <input type="text" name="price" id="price">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="money">金额:</label>

                        <div class="controls">
                            <input type="text" name="money" id="money">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="linkPhone">联系电话:</label>

                        <div class="controls">
                            <input type="text" name="linkPhone" id="linkPhone">
                        </div>
                    </div>

                </div>

                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="date1">发票日期:</label>

                        <div class="controls">
                            <div class="input-append date">
                                <input type="text" name="ticketDate" id="date1" readonly>
                                <span class="add-on"><i class="icon-th"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group" id="supplierDiv">
                        <label class="control-label" for="supplier">供应商名称:</label>

                        <div class="controls">
                            <input type="text" name="supplier" id="supplier">
                        </div>
                    </div>
                    <div class="control-group" id="consumerDiv">
                        <label class="control-label" for="consumer">客户名称:</label>

                        <div class="controls">
                            <input type="text" name="consumer" id="consumer">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="taxAccount">纳税账户开户账号:</label>

                        <div class="controls">
                            <input type="text" name="taxAccount" id="taxAccount">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="taxNo">纳税识别号:</label>

                        <div class="controls">
                            <input type="text" name="taxNo" id="taxNo">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="unit">单位:</label>

                        <div class="controls">
                            <input type="text" name="unit" id="unit">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="specifications">规格型号:</label>

                        <div class="controls">
                            <input type="text" name="specifications" id="specifications">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="address">地址:</label>

                        <div class="controls">
                            <input type="text" name="address" id="address">
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>发票跟踪</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group" id="ticketStatusDiv">
                        <label class="control-label" for="ticketStatus">发票状态:</label>

                        <div class="controls">
                            <select id="ticketStatus" name="ticketStatus">
                                <option/>
                                <option value="正常发票">正常发票</option>
                                <option value="在途发票">在途发票</option>
                                <option value="作废发票">作废发票</option>
                                <option value="冲红发票">冲红发票</option>
                            </select>
                        </div>
                    </div>

                    <div class="control-group" id="authStatusDiv">
                        <label class="control-label" for="authStatus">认证状态:</label>

                        <div class="controls">
                            <select id="authStatus" name="authStatus">
                                <option/>
                                <option value="已认证">已认证</option>
                                <option value="未认证">未认证</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="reciveStatus">接收状态:</label>

                        <div class="controls">
                            <select id="reciveStatus" name="reciveStatus">
                                <option/>
                                <option value="未接收">未接收</option>
                                <option value="已接收">已接收</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>上传附件</legend>
            <div id="wrapper">
                <div id="container">
                    <!--头部，相册选择和格式选择-->
                    <div id="uploader">
                        <div class="queueList">
                            <div id="dndArea" class="placeholder">
                                <div id="filePicker" style="text-align: center"></div>
                                <%--<p>或将照片拖到这里，单次最多可选300张</p>--%>
                            </div>
                        </div>
                        <div class="statusBar" style="display:none;">
                            <div class="progress">
                                <span class="text">0%</span>
                                <span class="percentage"></span>
                            </div>
                            <div class="info"></div>
                            <div class="btns">
                                <div id="filePicker2"></div>
                                <div class="uploadBtn" style="display: none">开始上传</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>

</div>

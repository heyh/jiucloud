<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="sy.model.Param" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %><%--
  Created by IntelliJ IDEA.
  User: heyh
  Date: 16/7/10
  Time: 下午11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Param> unitParams = new ArrayList<Param>();
    JSONArray costTree = new JSONArray();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        unitParams = sessionInfo.getUnitParams();
        costTree = sessionInfo.getCostTree();
    }

%>
<!DOCTYPE html>
<html>
<head>
    <title>费用数据添加</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

    <script type="text/javascript">

        $.getJSON('${pageContext.request.contextPath}/projectController/securi_getProjects', function (data) {
            $('#projectName').select2({
                placeholder: "可以模糊查询",
                data: [{id: '', text: ''}].concat(data.obj),
                allowClear: true,
            })
        });

        function getCostTemplate() {
            //获取表单值，并以json的数据形式保存到data中
            getCostTemplateCfg.data = {
                projectId: $("#projectName").val()
            }
            $.ajax(getCostTemplateCfg);
        };

        var getCostTemplateCfg = {
            url: '${pageContext.request.contextPath}/templateController/securi_getCostTemplate',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                var html = "";
                if (data.obj.length > 0) {
                    html += '<tr>';
                    html += '<td colspan="3">';
                    html += '<div style="float:left;">';
                    html += '<label><B>成本合计:</B></label>';
                    html += '</div>'
                    html += '<div style="float:left; padding-left: 10px">';
                    html += '<span>￥</span><span class="summary" style="font-family:微软雅黑; color:red; ">0.00</span>';
                    html += '</div>';
                    html += '</td>'
                    html += '</tr>';
                    $.each(data.obj, function (_index, _item) {
                        if (_index % 2 != 0) {
                            html += '<tr><td colspan="3"></td></tr>';
                        }
                        if (_item._attrtype == 'input') {
                            html += '<tr style="background: #f7f7f7">';
                            html += '<td>';
                            html += '<div>';
                            html += '<label><B>' + _item._text + '</B></label>';
                            html += '</div>';
                            html += '</td>'
                            html += '<td colspan="2">';
                            html += '<div style="float:left;">';
                            html += '<label><B>合计:</B></label>';
                            html += '</div>'
                            html += '<div style="float:left; padding-left: 10px">';
                            html += '<span>￥</span><span class="inputsummary summary_' + _index + '"  style="font-family:微软雅黑; color:red; ">0.00</span>';
                            html += '</div>';
                            html += '</td>';
                            html += '</tr>';

                            $.each(_item.childrens, function (_child_index, _child_item) {
                                html += '<tr>';
                                html += '<td rowspan=' + (_child_item.childrens.length+3) + '>';
                                html += '<div>';
                                html += '<label><B>' + _child_item._text + '</B></label>';
                                html += '</div>';
                                html += '</td>'
                                html += '<tr>'
                                $.each(_child_item.childrens, function (_child_child_index, _child_child_item) {
                                    html += '<tr>';
                                    html += '<td style="width: 120px; ">';
                                    html += '<div >';
                                    html += '<label for=' + _child_child_item._id + '>' + _child_child_item._text + '</label>';
                                    html += '</div>';
                                    html += '</td>';
                                    html += '<td>';
                                    html += '<div >';
                                    html += '<input onchange="cal(' + _index + ',' + _child_index + ' ,this.value)" type="text" name=' + _child_child_item._id + ' id=' + _child_child_item._id + ' class="' + _index + '_' + _child_index + ' easyui-numberbox" precision="2">';
                                    html += '</div>'
                                    html += '</td>';
                                    html += '</tr>';
                                });
                                html += '<tr>';
                                html += '<td>';
                                html += '<div>';
                                html += '<label><B>小计:</B></label>';
                                html += '</div>'
                                html += '</td>';
                                html += '<td>';
                                html += '<div>';
                                html += '<span>￥</span><span class="littlesummary summary_' + _index + '_' + _child_index + '"  style="font-family:微软雅黑; color:red; ">0.00</span>';
                                html += '</div>';
                                html += '</td>';
                                html += '</tr>';
                            });
                        } else if (_item._attrtype == 'cal'){
                            html += '<tr style="background: #f7f7f7">';
                            html += '<td rowspan="' + (_item.childrens.length + 3) + '">';
                            html += '<div>';
                            html += '<label><B>' + _item._text + '</B></label>';
                            html += '</div>';
                            html += '</td>'
                            html += '<td colspan="2">';
                            html += '<div style="float:left;">';
                            html += '<label><B>合计:</B></label>';
                            html += '</div>'
                            html += '<div style="float:left; padding-left: 10px">';
                            html += '<span>￥</span><span class="calsummary summary_' + _index + '"  style="font-family:微软雅黑; color:red; ">0.00</span>';
                            html += '</div>';
                            html += '</td>';
                            html += '</tr>';
                            $.each(_item.childrens, function (_child_index, _child_item) {
                                html += '<tr>';
                                html += '<td style="width: 120px;">';
                                html += '<div >';
                                html += '<label for=' + _child_item._id + '>' + _child_item._text + '</label>';
                                html += '</div>'
                                html += '</td>';
                                html += '<td>';
                                html += '<div>';
                                html += '<input type="text" name=' + _child_item._id + ' id=' + _child_item._id + ' class="cal" precision="2" _type='+ _child_item._type +' _target=' + _child_item._target + ' _ratio='+ _child_item._ratio + ' readonly>';
                                html += '</div>'
                                html += '</td>';
                                html += '</tr>';
                            });
                        }
                    });
                    $("#cost_table").html(html);
                    $('.btn').show();
                } else {
                    $("#cost_table").html('<tr><td>没有配置项目成本模板</td></tr>');
                    $('.btn').hide();
                }

                $.parser.parse($("#cost_table"));
            }
        };

        function cal(_index, _child_index, itemValue) {
            debugger;
            var _liitlesum = 0.00;
            $.each($('.' + _index + '_' + _child_index), function(_input_index, _input_item){
                if ($('#' + _input_item.id).val() == '') {
                    return;
                }
                _liitlesum = parseFloat(_liitlesum) + parseFloat($('#' + _input_item.id).val());
            });
            $('.summary_'+ _index + '_' + _child_index).text(_liitlesum.toFixed(2));

            var _sum = 0.00;
            $.each($('.littlesummary'), function (_little_index, _little_item) {
                _sum = parseFloat(_sum) + parseFloat(_little_item.innerText);
            });
            $('.summary_'+ _index).text(_sum.toFixed(2)) ;


            var _calSum = 0.00;
            $.each($('.cal'), function (_cal_index, _item) {
                var ratioValue = 0.00;
                if (_item.getAttribute("_type") == '1') {
                    ratioValue = parseFloat($('.summary_'+ _index).text()) * parseFloat(_item.getAttribute("_ratio"));

                } else if (_item.getAttribute("_type") == '2') {
                    var targetValue = $('#'+_item.getAttribute("_target")).val() == '' ? 0.00 : $('#'+_item.getAttribute("_target")).val();
                    ratioValue = parseFloat(targetValue) * parseFloat(_item.getAttribute("_ratio"));
                }
                $('#'+_item.id).val(ratioValue.toFixed(2));

                _calSum = parseFloat(_calSum) + parseFloat($('#'+_item.id).val());
            });
            $('.calsummary').text(_calSum.toFixed(2)) ;

            var _inputsummary = $('.inputsummary').text() == '' ? 0.00 : $('.inputsummary').text();
            var _calsummary = $('.calsummary').text() == '' ? 0.00 : $('.calsummary').text();
            var _summary = (parseFloat(_inputsummary) + parseFloat(_calsummary)).toFixed(2);
            $('.summary').text(_summary);
        };

        $(function() {
            parent.$.messager.progress('close');
            $('#form').form({
                url : '${pageContext.request.contextPath}/projectCostController/securi_addProjectCost',
                onSubmit : function() {
                    parent.$.messager.progress({
                        title : '提示',
                        text : '数据处理中，请稍后....'
                    });
                    var isValid = $(this).form('validate');

                    if (!isValid) {
                        parent.$.messager.progress('close');
                    }
                    return true;
                },
                success : function(result) {
                    parent.$.messager.progress('close');
                    result = $.parseJSON(result);
                    if (result.success) {
                        jQuery.messager.alert('提示:','添加成功!','info');
                        setTimeout(function () {
                            location.reload();
                        }, 1000);
                    } else {
                        parent.$.messager.alert('错误', result.msg, 'error');
                    }
                }
            });

        });
    </script>

    <style>
        .container-fluid {
            /*background: #f7f7f7;*/
            /*padding: 25px 15px 25px 10px;*/
        }

        fieldset {
            margin-top: 20px;
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

        .basic-grey {
            margin-left: auto;
            margin-right: auto;
            max-width: 1002px;
            /*background: #F7F7F7;*/
            padding: 25px 15px 60px 10px;
            /*font: 12px Georgia, "Times New Roman", Times, serif;*/
            color: #888;
            text-shadow: 1px 1px 1px #FFF;
            border: 1px solid #E4E4E4;
        }

        table {
            border-collapse:collapse;
        }

        table td {
            border:1px solid #ddd; width:50px; height:25px; valign:center;
        }

    </style>
</head>
<body>

<div class="container-fluid">
    <form class="form-horizontal basic-grey" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
        <legend>
            <div class="control-group">
                <label class="control-label" for="projectName">项目名称:</label>

                <div class="controls">
                    <select id="projectName" style="width:380px" name="projectName" onchange="getCostTemplate()">
                    </select>
                </div>
            </div>
        </legend>
            <div class="row-fluid">
                <table id="cost_table" class="table">
                    <tr>
                        <td style="font-weight:bold; color: #00a2d4; text-align: center;">请选择项目,根据模板填入对应成本金额</td>
                    </tr>
                </table>
            </div>


        <div class="span12" style="text-align:center">
            <input type="submit"  value="添加"  class="btn btn-danger btn-lg" style="width: 250px; margin-top:10px;height: 40px; display: none">
        </div>


        <div class="clear">
            <input type="reset" name="reset" style="display: none;"/>
        </div>
    </form>
</div>
</body>
</html>

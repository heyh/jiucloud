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

<%--<link rel="stylesheet"--%>
<%--href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/less/datepicker.less">--%>


<!-- 引入EasyUI -->
<script type="text/javascript"
		src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"
		charset="UTF-8"></script>

<link rel="stylesheet" type="text/css"
	  href="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/css/layui.css" media="all"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layui-v2.2.3/layui/layui.js"></script>

<link rel="stylesheet" type="text/css"
	  href="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/skin/default/layer.css" media="all"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>
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


</style>

<script type="text/javascript">


    $(function() {
        parent.$.messager.progress('close');
        $('#form')
            .form(
                {
                    url : '${pageContext.request.contextPath}/stockController/securi_saveOutStock',

                    onSubmit : function() {
                        var outProId = $("#outProId").val();
                        if (outProId == '') {
                            alert('请选择项目');
                            return false;
                        }

                        if ($('#outCount').val() == '') {
                            alert('请输入出库数量');
                            return false;
                        }

                        var dOutCount = parseFloat($('#outCount').val());
                        if (dOutCount == 0) {
                            alert('请输入出库数量');
                            return false;
                        }

                        if (parseFloat($('#outCount').val()) > parseFloat($('#stockCount').val())) {
                            alert('库存不足,请修改出库数量!');
                            return false;
                        }
                        return true;
                    },

                    success : function(data) {
                        parent.$.messager.progress('close');
                        data = JSON.parse(data);
                        if (data.success) {
                            jQuery.messager.show({
                                title: '温馨提示:',
                                msg: data.msg,
                                timeout: 3000,
                                showType: 'show'
                            });
                            parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                            parent.$.modalDialog.handler.dialog('close');
                        } else {
                            parent.$.messager.alert('错误', data.msg, 'error');
                        }
                    }
                });

    });

    $.ajax({
        url: '${pageContext.request.contextPath}/projectController/securi_getProjects',    //后台webservice里的方法名称
        type: "post",
        dataType: "json",
        contentType: "application/json",
        traditional: true,
        success: function (data) {
            debugger
            var projectInfos = data.obj;
            var optionstring = "";
            for (var i in projectInfos) {
                if ('${project.proName}' == projectInfos[i].text) {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" selected = 'selected'>" + projectInfos[i].text + "</option>";
                } else {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
                }
            }
            $("#outProId").html("<option value=''>请选择项目</option> "+optionstring);
        },
        error: function (msg) {
            alert("系统异常，请联系管理员！");
        }
    });

</script>

<div class="container-fluid">

	<form class="form-horizontal" name="form" id="form" method="post" role="form">
		<input type="hidden" id="id" name="id" value="${stockBean.id}" />

		<fieldset class="display:block">
			<legend>库存信息</legend>
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="mc">材料名称:</label>

						<div class="controls">
							<input type="text" name="mc" id="mc" value="${stockBean.mc}" style="text-align: left ;" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="specifications">规格型号:</label>

						<div class="controls">
							<input type="text" name="specifications" id="specifications" value="${stockBean.specifications}" style="text-align: left ;" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="stockCount">库存数量:</label>

						<div class="controls">
							<input type="text" value="${stockBean.stockCount}" name="stockCount" id="stockCount" required="true" class="easyui-numberbox" precision="2" style="text-align: right ;" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="dw">单位:</label>

						<div class="controls">
							<input type="text" name="dw" id="dw" value="${stockBean.dw}"  style="text-align: left ;" readonly="true">
						</div>
					</div>
				</div>
			</div>
		</fieldset>

		<fieldset class="display:block">
			<legend>操作</legend>
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="outProId">项目名称:</label>
						<div class="controls">
							<select id="outProId" name="outProId" required="true">
							</select>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label" for="outCount">出库数量:</label>

						<div class="controls">
							<input type="text" name="outCount" id="outCount" required="true" class="easyui-numberbox" precision="2" style="text-align: right ;">
						</div>
					</div>
				</div>
			</div>
		</fieldset>

	</form>

</div>

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
                    url : '${pageContext.request.contextPath}/fieldDataController/securi_saveOutFieldData',

                    onSubmit : function() {
                        var outProId = $("#outProId").val();
                        debugger;
                        if (outProId == '') {
                            alert('请选择工程');
                            return false;
                        }

                        var outCount = $('#outCount').val();
                        if (outCount == '') {
                            alert('请输入出库数量');
                            return false;
                        }

                        var storageCount = $('#storageCount').val();
                        var dOutCount = parseFloat(outCount).toFixed(4);
                        if (dOutCount == 0) {
                            alert('请输入出库数量');
                            return false;
                        }
                        var dStorageCount = parseFloat(storageCount).toFixed(4);
                        if (dOutCount > dStorageCount) {
                            alert('库存不足,请修改出库数量!');
                            return false;
                        }
                        return true;
                    },

                    success : function(result) {
                        parent.$.messager.progress('close');
                        debugger;
                        result = $.parseJSON(result);
                        if (result.rspCode == 0) {
                            alert("出库成功!");
                            parent.$.modalDialog.openner_dataGrid
                                .datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                            parent.$.modalDialog.handler
                                .dialog('close');
                        } else {
                            parent.$.messager.alert('错误', result.msg,
                                'error');
                        }
                    }
                });

    });

//    var sectionId = document.getElementById('sectionId').value;
//    for (var i = 0; i < document.getElementById('section').options.length; i++) {
//        if (document.getElementById('section').options[i].value == sectionId) {
//            document.getElementById('section').options[i].selected = true;
//            break;
//        }
//    }

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
                $("#currentApprovedUser").html(optionstring);
            }
        }
    });
</script>

<div class="container-fluid">

	<form class="form-horizontal" name="form" id="form" method="post" role="form">
		<input type="hidden" id="id" name="id" value="${tfielddata.id}" />
		<input type="hidden" id="itemCode" name="itemCode" value="${tfielddata.itemCode}" />
		<input type="hidden" id="creatTime" name="creatTime" value="${tfielddata.creatTime}" />
		<input type="hidden" id="cid" name="cid" value="${tfielddata.cid}" />
		<input type="hidden" id="uid" name="uid" value="${tfielddata.uid}" />
		<input type="hidden" id="uname" name="uname" value="${tfielddata.uname}" />
		<input type="hidden" id="company" name="company" value="${tfielddata.company}" />
		<input type="hidden" id="costType" name="costType" value="${tfielddata.costType}" />
		<input type="hidden" id="projectName" name="projectName" value="${tfielddata.projectName}" />
		<input type="hidden" id="sectionId" value="${tfielddata.section}"/>

		<fieldset class="display:block">
			<legend>库存信息</legend>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="proName">工程名称:</label>

						<div class="controls">
							<input type="text" name="proName" id="proName" value="${project.proName}" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="dataName">规格型号:</label>

						<div class="controls">
							<input type="text" name="specifications" id="specifications" value="${tfielddata.specifications}" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="costTypeName">库存数量:</label>

						<div class="controls">
							<%--<input type="text" name="storageCount" id="storageCount" value="${storageCount}" readonly="true">--%>
							<span><input type="text" name="storageCount" id="storageCount" value="${storageCount}" readonly="true"><span style="color: #00a0e9">${tfielddata.unit}</span></span>
						</div>
					</div>
				</div>

				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="costTypeName">费用类型:</label>

						<div class="controls">
							<input type="text" name="costTypeName" id="costTypeName" value="${cost.costType}" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="dataName">名称:</label>

						<div class="controls">
							<input type="text" name="dataName" id="dataName" value="${tfielddata.dataName}" readonly="true">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="costTypeName">数量:</label>

						<div class="controls">
							<%--<input type="text" name="count" id="count" value="${tfielddata.count}" readonly="true">--%>
							<span><input type="text" name="count" id="count" value="${tfielddata.count}" readonly="true"><span style="color: #00a0e9">${tfielddata.unit}</span></span>
						</div>
					</div>

				</div>


			</div>
		</fieldset>

		<fieldset class="display:block">
			<legend>操作</legend>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="outProId">工程名称:</label>
						<div class="controls">
							<select id="outProId" name="outProId" required="true">
							</select>
						</div>
					</div>
				</div>

				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="dataName">出库数量:</label>

						<div class="controls">
							<input type="text" name="outCount" id="outCount" required="true" class="easyui-numberbox" precision="4">
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="dataName">审批人选择:</label>

						<div class="controls">
							<select id="currentApprovedUser" name="currentApprovedUser">

							</select>
						</div>
					</div>
				</div>
			</div>
		</fieldset>

	</form>

</div>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	JSONArray docCostTree = new JSONArray();

	SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	if (sessionInfo == null) {
		response.sendRedirect(request.getContextPath());
	} else {
		docCostTree = sessionInfo.getDocCostTree();
	}

%>
<!DOCTYPE html>
<html>
<head>
	<title>资料数据添加</title>
	<jsp:include page="../../../inc.jsp"></jsp:include>
	<%--<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet"/>--%>
	<%--<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>--%>

	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/select2/dist/css/select2.min.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/select2/dist/js/select2.min.js"></script>

	<link rel="stylesheet" type="text/css"
		  href="${pageContext.request.contextPath }/jslib/webuploader/webuploader.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jslib/webuploader/style.css"/>

	<%--<link rel="stylesheet"--%>
	<%--href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.css">--%>
	<%--<link rel="stylesheet"--%>
	<%--href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.standalone.css">--%>

	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/webuploader/webuploader.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/webuploader/upload.js"></script>
	<%--<script type="text/javascript"--%>
	<%--src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/bootstrap-datepicker.js"--%>
	<%--charset="UTF-8"></script>--%>
	<%--<script type="text/javascript"--%>
	<%--src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"--%>
	<%--charset="UTF-8"></script>--%>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>


	<script type="text/javascript">
		var flag = 0;


		var cfg = {
			url: '${pageContext.request.contextPath}/fieldDataController/savefieldData',
			type: 'post',
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success: function (data) {
				alert(data.msg);
				if (data.success) {

					var count = 0;
					$(".filelist").each(function () {
						count += $(this).children('li').length;
					});
					if (count <= 0) {
						location.reload();
					} else {
						uploader.options.formData = {'mid': data.obj, 'updateType': 'webuploader'};
						$('.uploadBtn').click();
						uploader.on('uploadFinished', function (file) {
							setTimeout(function () {
								location.reload();
							}, 500);
						});
					}
				}
			}
		};

		function aaa() {

			if (flag == 1) {
				var a = confirm("检测到该数据可能已经添加，确定要重复添加吗");
				if (!a) {
					return;
				}
			}
			var projectName = document.getElementById("projectName").value;
			var remark = document.getElementById("remark").value;
            var currentApprovedUser = document.getElementById("currentApprovedUser").value;
			cfg.data = {
				'projectName': projectName,
				'costType': 480,
				'dataName': $("#projectName").find("option:selected").text() + ' - 总材料计划',
				'remark': remark,
				'needApproved': '1',
                'currentApprovedUser': currentApprovedUser
			}

			$.ajax(cfg);

		}

		window.onload = function () {
			$('#form input').click(function () {
				flag = 0;
			})
		}

		$(document).ready(function () {
			$("#chooseApprove").select2({
				placeholder: "请选择",
				allowClear: true
			});
            $("#projectName").select2({
                placeholder: "请选择项目",
                allowClear: true
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
                            if (users[i].id == $('#firstLevelParentDepartment').val()) {
                                optionstring += "<option value=\"" + users[i].id + "\" selected = 'selected'>" + users[i].username + "</option>";
                            } else {
                                optionstring += "<option value=\"" + users[i].id + "\" >" + users[i].username + "</option>";
                            }
                        }
                        $("#currentApprovedUser").html(optionstring);
                    }
                }
            });
		});

        $.getJSON('${pageContext.request.contextPath}/projectController/securi_getProjects', function (data) {
            var optionstring = "";
            var projectInfos = data.obj;
            for (var i in projectInfos) {
                if ($('#maxProjectId').val() == projectInfos[i].id) {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" selected = 'selected'>" + projectInfos[i].text + "</option>";
                } else {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
                }

            }
            $("#projectName").html("<option value=''>请选择项目</option> "+optionstring);
        });



	</script>

	<style>
		.container-fluid {
			/*background: #f7f7f7;*/
			/*padding: 25px 15px 25px 10px;*/
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

		.basic-grey {
			margin-left: auto;
			margin-right: auto;
			max-width: 1002px;
			background: #F7F7F7;
			padding: 25px 15px 60px 10px;
			font: 12px Georgia, "Times New Roman", Times, serif;
			color: #888;
			text-shadow: 1px 1px 1px #FFF;
			border: 1px solid #E4E4E4;
		}
	</style>
</head>
<body>

<div class="container-fluid">
	<form class="form-horizontal basic-grey" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
		<input type="hidden" id = "maxProjectId" name="maxProjectId" value="${maxProjectId}"/>
		<input type="hidden" id = "firstLevelParentDepartment" name="firstLevelParentDepartment" value="${firstLevelParentDepartment}">

		<fieldset>
			<legend>添加数据</legend>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="projectName">工程名称:</label>

						<div class="controls">
							<select style="width:250px;" id="projectName" name="projectName">
							</select>
						</div>
					</div>
				</div>

				<div class="span6">
					<div class="control-group" id="chooseApproveDiv">
						<label class="control-label" for="currentApprovedUser">审批人选择:</label>

						<div class="controls">
							<select style="width:250px;margin-bottom: 20px" id="currentApprovedUser" name="currentApprovedUser">

							</select>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label" for="remark">备注:</label>

						<div class="controls">
							<input type="text" name="remark" id="remark" class="easyui-textbox" style="width:236px">
						</div>
					</div>

				</div>

				<div class="span12" style="text-align:center">
					<div style="width:10%;float:left;padding-left:35px;text-align:right">工程总材料计划:</div>
					<div style="float:left;">
						<div id="uploader" style="width: 751px">
							<div class="queueList">
								<div id="dndArea" class="placeholder">
									<div id="filePicker"></div>
									<%--<p>或将照片拖到这里，单次最多可选300张</p>--%>
								</div>
							</div>
							<div class="statusBar" style="display:none;">
								<div class="progress">
									<span class="text">0%</span>
									<span class="percentage"></span>
								</div><div class="info"></div>
								<div class="btns">
									<div id="filePicker2"></div>
									<div class="uploadBtn" style="display:none">开始上传</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</fieldset>

		<div class="span12" style="text-align:center">
			<input type="button" class="btn btn-danger btn-lg" value="添加" onclick="aaa()" style="width: 250px; margin-top:10px;height: 40px;" />
		</div>


		<div class="clear">
			<input type="reset" name="reset" style="display: none;" />
		</div>
	</form>
</div>
</body>
</html>

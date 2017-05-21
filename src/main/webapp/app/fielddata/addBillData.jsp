<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="sy.model.Param" %>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%--
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
    List<Map<String, Object>> dataCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = new JSONArray();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        unitParams = sessionInfo.getUnitParams();
        costTree = sessionInfo.getCostTree();
        dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        for (Map<String, Object> nodeMap : dataCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray.add(nodeJson);
        }
    }

%>
<!DOCTYPE html>
<html>
<head>
    <title>费用数据添加</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
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
        function selectp() {
            parent.$
                    .modalDialog({
                        title: '选择项目工程',
                        width: 550,
                        height: 550,
                        href: '${pageContext.request.contextPath }/fieldDataController/securi_selectp',
                        buttons: [{
                            text: '确认',
                            handler: function () {
                                var id = parent.$.modalDialog.handler.find(
                                        "#proidh").val();
                                var name = parent.$.modalDialog.handler.find(
                                        "#proNameh").val();
                                document.getElementById("projectName").value = id;
                                document.getElementById("pName").value = name;
                                parent.$.modalDialog.handler.dialog('close');
                            }
                        }]
                    });
        }

        function selectc() {
            parent.$
                    .modalDialog({
                        title: '选择费用类型',
                        width: 450,
                        height: 500,
                        href: '${pageContext.request.contextPath }/fieldDataController/securi_selectc',
                        buttons: [{
                            text: '确认',
                            handler: function () {
                                var id = parent.$.modalDialog.handler.find(
                                        "#proidh").val();
                                var name = parent.$.modalDialog.handler.find(
                                        "#proNameh").val();
                                var itemCode = parent.$.modalDialog.handler.find(
                                        "#code").val();
                                document.getElementById("costType").value = id;
                                document.getElementById("costTypeName").value = name;
                                document.getElementById("itemCode").value = itemCode;
                                parent.$.modalDialog.handler.dialog('close');
                                if (name == '纯附件') {
                                    $('.will_hide').hide();
                                } else {
                                    $('.will_hide').show();
                                }
                            }
                        }]
                    });
        }

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
            var costType = $("#costType").combotree("getValue")
            var dataName = document.getElementById("dataName").value;
            var price = document.getElementById("price").value;
            price = (price * 1).toFixed(2);
            var count = document.getElementById("count").value;
            var specifications = document.getElementById("specifications").value;
            var remark = document.getElementById("remark").value;
            var unit = document.getElementById("unit").value;
//            var itemCode = $("#itemCode").val();
            var needApproved = document.getElementById("needApproved").value;
//            var approvedUser = document.getElementById("approvedUser").value;
            var currentApprovedUser = document.getElementById("currentApprovedUser").value;
            var section = $('#section').val();
            var supplier = $('#supplier').val();

            if (projectName == '') {
                alert("项目名称不能为空");
                return;
            }
            if (costType == '') {
                alert("费用类型不能为空");
                return;
            }
            if (dataName == '') {
                alert("现场数据名称不能为空");
                return;
            }
            if (section == '') {
                alert("标段不能为空");
                return;
            }
            if (price == '') {
                alert("价格不能为空");
                return;
            }
            if (count == '' || count == '0') {
                alert("数量不能为空");
                return;
            }

            var _jsonArray =  <%= jsonArray %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].itemCode.substring(0, 3) == '700' && _jsonArray[i].nid == costType) {
                    if (unit == '') {
                        alert("单位不能为空");
                        return;
                    }
                    if (specifications == '') {
                        alert("规格（设施名称）不能为空");
                        return;
                    }
                }

            }

            cfg.data = {
                'projectName': projectName,
                'costType': costType,
                'dataName': dataName,
                'price': price,
                'count': count,
                'specifications': specifications,
                'remark': remark,
                'unit': unit,
//                'itemCode': itemCode,
                'needApproved': needApproved,
                'currentApprovedUser': currentApprovedUser,
                'section': section,
                'supplier': supplier
            }

            $.ajax(cfg);

        }

        function cal() {
            var price = document.getElementById("price").value;
            var count = document.getElementById("count").value;
            if (price != '' && count != '') {
                document.getElementById("sumprice").value = (price * count)
                        .toFixed(2);
            }
        }

        window.onload = function () {
//		if (document.getElementById("costTypeName").value == '纯附件') {
//			$('.will_hide').hide();
//		}
            $('#form input').click(function () {
                flag = 0;
            })
        }

        function isNeedApprove(isNeedApprove) {
            $('#approvedUser').value = '';
            $('#chooseApproveName').value = '';
            if (isNeedApprove == '0') {
                $('#chooseApproveDiv').hide();
                $('#approvedUserLabel').hide();
            } else if (isNeedApprove == '1') {
                $('#chooseApproveDiv').show();
                chooseApprove();
            }
        }

        function chooseApprove() {
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


        }
        <%--function isChooseApprove(isChooseApprove) {--%>
            <%--$('#approvedUser').value = '';--%>
            <%--$('#chooseApproveName').value = '';--%>
            <%--if (isChooseApprove == '1') {--%>
                <%--parent.$--%>
                        <%--.modalDialog({--%>
                            <%--title: '选择审批人',--%>
                            <%--width: 450,--%>
                            <%--height: 500,--%>
                            <%--href: '${pageContext.request.contextPath }/fieldDataController/securi_chooseApprove',--%>
                            <%--buttons: [{--%>
                                <%--text: '确认',--%>
                                <%--handler: function () {--%>
                                    <%--var chooseNodes = parent.$.modalDialog.handler.find(".chooseNode");--%>
                                    <%--console.log(chooseNodes);--%>
                                    <%--var approveUids = [];--%>
                                    <%--var approveNames = [];--%>
                                    <%--$.each(chooseNodes, function (index, chooseNode) {--%>
                                        <%--if (chooseNode.firstChild.id != '-1' && $.inArray(chooseNode.firstChild.id, approveUids) == '-1') {--%>
                                            <%--approveUids.push(chooseNode.firstChild.id);--%>
                                            <%--approveNames.push(chooseNode.innerText);--%>
                                        <%--}--%>
                                    <%--});--%>
                                    <%--document.getElementById("chooseApproveName").value = approveNames.reverse().join(',');--%>
                                    <%--document.getElementById("approvedUser").value = approveUids.reverse().join(',');--%>
                                    <%--parent.$.modalDialog.handler.dialog('close');--%>

                                    <%--$('#approvedUserLabel').show();--%>
                                <%--}--%>
                            <%--}]--%>
                        <%--});--%>
            <%--} else {--%>
                <%--$('#approvedUserLabel').hide();--%>
            <%--}--%>
        <%--}--%>

        $(document).ready(function () {
            $("#unit").select2({
                tags: "true",
                placeholder: "可以模糊查询",
                allowClear: true
            });

            $("#needApproved").select2({
                placeholder: "请选择",
                allowClear: true
            });
            $("#chooseApprove").select2({
                placeholder: "请选择",
                allowClear: true
            });
            $("#projectName").select2({
                placeholder: "请选择项目",
                allowClear: true
            });
            $("#section").select2({
                placeholder: "请选择标段",
                allowClear: true
            });

            $('.easyui-combotree').combotree({
                data: <%= costTree %>,
                lines: true,
                valueField: 'id',
                textField: 'text',
                onLoadSuccess: function () {
                    $('.easyui-combotree').combotree('tree').tree("collapseAll");
                },
                //选择树节点触发事件
                onSelect : function(node) {
                    //返回树对象
                    var tree = $(this).tree;
                    //选中的节点是否为叶子节点,如果不是叶子节点,清除选中
                    var isLeaf = tree('isLeaf', node.target);
                    if (!isLeaf) {
                        //清除选中
                        $('.easyui-combotree').treegrid("unselect");
                    } else {
                        var _jsonArray =  <%= jsonArray %>;
                        for (var i=0; i<_jsonArray.length; i++) {
                            if (_jsonArray[i].itemCode.substring(0, 3) == '700' && _jsonArray[i].nid == node.id) {
                                $('#dataName').val(node.text);
                                break;
                            } else {
                                $('#dataName').val('');
                            }
                        }
                    }
                },
                onChange: function (node) {
                    var _nid = node;

                    $.ajax({
                        url: '${pageContext.request.contextPath}/fieldDataController/securi_getCostInfo',
                        data: {nid: _nid},
                        type: 'post',
                        dataType: 'json',
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        success: function (data) {
                            if (data.success) {
                                if (data.obj != null && data.obj != '' && data.obj.itemCode.substring(0, 3) == '800') {
                                    $("#supplierDiv").show();
                                } else {
                                    $("#supplierDiv").hide();
                                }
                            }
                        }
                    });
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
            changeProject();
        });

        function changeProject() {
            $.ajax({
                url: '${pageContext.request.contextPath }/itemController/securi_getSelectItems',
                type: 'post',
                data: {'projectId': $('#projectName').val()},
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {

                    if (data.success) {
                        var _section = data.obj.section;
                        var sectionInfos = data.obj.itemList;
                        var optionstring = "";
                        for (var i in sectionInfos) {
                            if (_section == sectionInfos[i].id) {
                                optionstring += "<option value=\"" + sectionInfos[i].id + "\" selected = 'selected'>" + sectionInfos[i].text + "</option>";

                            } else {
                                optionstring += "<option value=\"" + sectionInfos[i].id + "\" >" + sectionInfos[i].text + "</option>";
                            }

                        }
                        $("#section").html("<option value=''>请选择标段</option> "+optionstring);

                        changeSection();
                    }

                }
            });
        };

        function changeSection() {
            if ($('#projectName').val() == '' || $('#section').val() == '') {
                $("#supInfos").hide();
                return;
            }

            $.ajax({
                url: '${pageContext.request.contextPath}/itemController/securi_getSupInfo',
                data: {projectId: $('#projectName').val(), section: $('#section').val()},
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.success) {
                        if (data.obj == null || data.obj == '') {
                            $("#supInfos").hide();
                            return;
                        }
                        $("#supInfos").show();
                        var htmlStr = '';
                        var optionstring = "";
                        var supInfos = data.obj;
                        for (var i in supInfos) {
                            optionstring += "<option value=\"" + supInfos[i] + "\" >" + supInfos[i] + "</option>";
                        }
                        $("#supInfoSel").html("<option value=''>请选择</option> "+optionstring);
                    }
                }
            });
        }

        function chooseSupInfos() {
            layer.open({
                type: 1,
                title: '附加信息',
                closeBtn: 0,
                shadeClose: true,
                content: $('#supInfosDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });
        }
        
        function selectSupInfo() {
            $('#dataName').val($("#supInfoSel").val());

        }
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
        <fieldset>
            <legend>添加数据</legend>
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="projectName">工程名称:</label>

                        <div class="controls">
                            <select style="width:250px;" id="projectName" name="projectName" onchange="changeProject()">
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="specifications">规格型号(设施名称):</label>

                        <div class="controls">
                            <input type="text" name="specifications" id="specifications" class="easyui-textbox" style="width:236px" onblur="cal()">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="unit">单位:</label>

                        <div class="controls">
                            <select style="width:250px" name="unit" id="unit">
                                <option></option>
                                <c:forEach var="unitParam" items="<%= unitParams %>" varStatus="index">
                                    <c:if test="${unitParam.parentCode == ''}">
                                        <optgroup label="${unitParam.paramValue}"></optgroup>
                                    </c:if>
                                    <c:if test="${unitParam.parentCode != ''}">
                                        <option value="${unitParam.paramValue}">&nbsp;&nbsp;&nbsp;&nbsp;${unitParam.paramValue}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="price">单价:</label>

                        <div class="controls">
                            <input type="text" name="price" id="price" class="easyui-numberbox" precision="2" style="width:236px" onblur="cal()">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="remark">备注(特征):</label>

                        <div class="controls">
                            <textarea type="text" name="remark" id="remark" style="width:236px"></textarea>
                            <%--<input type="text" name="remark" id="remark" class="easyui-textbox" style="width:236px">--%>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="section">标段(属性):</label>

                        <div class="controls">
                            <select style="width:250px;" id="section" name="section" onchange="changeSection()">
                            </select>
                        </div>
                    </div>

                </div>

                <div class="span6">

                    <div class="control-group">
                        <label class="control-label" for="costType">费用类型:</label>

                        <div class="controls">
                            <input class="easyui-combotree" name="costType" id="costType" style="width:250px;">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="dataName">名称:</label>

                        <div class="controls">
                            <input type="text" name="dataName" id="dataName" class="easyui-textbox" style="width:236px">
                            <a style="display: none;" id="supInfos" onclick="chooseSupInfos()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'"></a>
                        </div>

                    </div>
                    <div class="control-group">
                        <label class="control-label" for="count">数量:</label>

                        <div class="controls">
                            <input type="text" name="count" id="count" class="easyui-numberbox" precision="4" style="width:236px" onblur="cal()">
                        </div>
                    </div>
                    <div class="control-group" style="display: none">
                        <label class="control-label" for="sumprice">金额:</label>

                        <div class="controls">
                            <input type="text" name="sumprice" id="sumprice" class="easyui-numberbox" precision="2" style="width:236px" onblur="cal()">
                        </div>
                    </div>
                    <div class="control-group" id="supplierDiv" style="display: none;">
                        <label class="control-label" for="supplier">供应商:</label>

                        <div class="controls">
                            <input type="text" name="supplier" id="supplier" class="easyui-textbox" style="width:236px">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="needApproved">需要审批:</label>

                        <div class="controls">
                            <select onchange="isNeedApprove(this.options[this.options.selectedIndex].value)" style="width:250px;" id="needApproved" name="needApproved">
                                <option value="0" selected = "selected">不需要</option>
                                <option value="1">需要</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group" style="display:none" id="chooseApproveDiv">
                        <label class="control-label" for="currentApprovedUser">审批人选择:</label>

                        <div class="controls">
                            <select style="width:250px;margin-bottom: 20px" id="currentApprovedUser" name="currentApprovedUser">

                            </select>
                        </div>
                    </div>

                </div>

                <div class="span12" style="text-align:center">
                    <div style="width:10%;float:left;padding-left:35px;text-align:right">上传附件 :</div>
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
<div id="supInfosDiv" style="display:none; width: 300px;height:150px;">
    <select style="margin: 30px" id="supInfoSel" onchange="selectSupInfo()">
    </select>
</div>

<div id="approveDiv" style="display:none; width: 300px;height:150px;">
    <div id="approveRadioDiv" style="margin: 20px">

    </div>
</div>

</html>

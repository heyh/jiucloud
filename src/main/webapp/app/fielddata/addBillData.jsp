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
    JSONArray billCostTree = new JSONArray();
    List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = new JSONArray();

    String cid = "";
    String uid = "";
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        unitParams = sessionInfo.getUnitParams();
        billCostTree = sessionInfo.getBillCostTree();
        billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
        for (Map<String, Object> nodeMap : billCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray.add(nodeJson);
        }

        cid = sessionInfo.getCompid();
        uid = sessionInfo.getId();
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

    <%--&lt;%&ndash;&lt;%&ndash;<!-- Latest compiled and minified CSS -->&ndash;%&gt;&ndash;%&gt;--%>
    <%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jslib/bootstrap-table/src/bootstrap-table.css"/>--%>
    <%--&lt;%&ndash;&lt;%&ndash;<!-- Latest compiled and minified JavaScript -->&ndash;%&gt;&ndash;%&gt;--%>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/bootstrap-table/src/bootstrap-table.js"></script>--%>

    <%--&lt;%&ndash;&lt;%&ndash;<!-- Latest compiled and minified Locales -->&ndash;%&gt;&ndash;%&gt;--%>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/bootstrap-table/src/locale/bootstrap-table-zh-CN.js"></script>--%>

    <style>
        .table_style {
            width: 100%;
            margin-bottom: 20px;
            border:1px solid #EDEDED

        }

        .table_style th{height:34px; background:#76b3ff; color:#fff; font-weight: normal;text-align:center;vertical-align:middle;}
        .table_style td{height:32px; color:#0e0e0e;padding-left:10px;color:#666}
        .table_style tbody tr{cursor: default}
        .table_style tbody tr.hover td{background:#e4fbfb}
        .td_title{font-weight:bold;color:#333}

        .table_style tbody tr:nth-child(even) td,
        .table_style tbody tr:nth-child(even) th {background-color:#fff;}

        .table_style tbody tr:nth-child(odd) td,
        .table_style tbody tr:nth-child(odd) th {background-color:#eee;}
    </style>

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
            var costType = $("#costType").combotree("getValue");
            var dataName = document.getElementById("dataName").value;
//            var price = $('#price').numberbox('getValue');
            var count = $('#count').numberbox('getValue');

            var specificationArr = new Array();
            for (var i=0; i<$('#specifications').select2('data').length; i++) {
                specificationArr.push($('#specifications').select2('data')[i].text);
            }
            var specifications = specificationArr.join(',');

            var remark = document.getElementById("remark").value;
            var unit = document.getElementById("unit").value;
//            var itemCode = $("#itemCode").val();
            var needApproved = document.getElementById("needApproved").value;
//            var approvedUser = document.getElementById("approvedUser").value;
            var currentApprovedUser = document.getElementById("currentApprovedUser").value;
            var section = $('#section').val();
            var supplier = $('#supplier').val();
//            var payAmount = $('#payAmount').numberbox('getValue');


            if (projectName == '') {
                alert("项目名称不能为空");
                return;
            }
            if (costType == '') {
                alert("费用类型不能为空");
                return;
            }
            if (dataName == '') {
                alert("名称不能为空");
                return;
            }
//            if (section == '') {
//                alert("工程属性不能为空");
//                return;
//            }
//            if (price == '') {
//                alert("价格不能为空");
//                return;
//            }
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
                        alert("设施名称不能为空");
                        return;
                    }
                }

            }

            if (needApproved == '1' && currentApprovedUser == '') {
                alert('请选择审批人');
            }

            cfg.data = {
                'projectName': projectName,
                'costType': costType,
                'dataName': dataName,
//                'price': price,
                'count': count,
                'specifications': specifications,
                'remark': remark,
                'unit': unit,
//                'itemCode': itemCode,
                'needApproved': needApproved,
                'currentApprovedUser': currentApprovedUser,
                'section': section,
                'supplier': supplier
//                'payAmount': payAmount
            }

            $.ajax(cfg);

        }

//        function cal() {
//            var price = $('#price').numberbox('getValue');
//            var count = $('#count').numberbox('getValue');
//            var total = (price * count).toFixed(2);
//            if (price != '' && count != '') {
//                $('#sumprice').numberbox('setValue', total);
////                $('#payAmount').numberbox('setValue', total);
//            }
//        }

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


        }

        $(document).ready(function () {

            $("#specifications").select2({
                placeholder: "可多选",
                tags: true,
                multiple: true,
                allowClear: true
            });

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
//            $("#section").select2({
//                placeholder: "请选择标段",
//                allowClear: true
//            });

            $('.easyui-combotree').combotree({
                data: <%= billCostTree %>,
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
                        var bigItemCode = '';
                        for (var i=0; i<_jsonArray.length; i++) {
                            if (_jsonArray[i].itemCode.substring(0, 3) == '700' && _jsonArray[i].nid == node.id) {
                                $('#dataName').val(node.text);
                                var costType = node.text;
                                if ((costType.indexOf("(") != -1 || costType.indexOf("（") != -1)
                                    && (costType.indexOf(")") != -1 || costType.indexOf("）") != -1)) {
                                    var strUnit = costType.substring(costType.indexOf("（")+1,costType.indexOf("）"));
                                    $("#unit").val(strUnit).trigger("change");

                                    bigItemCode = _jsonArray[i].itemCode.substring(0, _jsonArray[i].itemCode.length-3);
                                }

                                break;
                            } else {
                                $('#dataName').val('');
                            }
                        }

                        for (var i=0; i<_jsonArray.length; i++) {
                            if (_jsonArray[i].itemCode == bigItemCode ) {
                                $('#section').val(_jsonArray[i].costType);
                                break;
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

                                $('#hideItemCode').val(data.obj.itemCode);

                                $.ajax({
                                    url: '${pageContext.request.contextPath}/featureController/securi_getFeatures',
                                    data: {keyword: $('#keyword').val(), cid: <%= cid %>, uid: <%= uid %>, itemCode: data.obj.itemCode},
                                    type: 'post',
                                    dataType: 'json',
                                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                                    success: function (data) {
                                        if (data.rspCode == '0') {
                                            var htmlStr = '';
                                            var features = data.features;
                                            $.each(data.features,function(index,item){
                                                htmlStr +=
                                                    '<tr class="featureRow">'+
                                                    '<td >' + (index + 1) + '</td>'+
                                                    '<td class="featureCol">' + item.mc + '</td>'+
                                                    '<td class="featureCol">'+
                                                    '<input type="text" align="right" placeholder="数量" style="margin-bottom:0px;width: 80px" value="'+ item.count + '">'+
                                                    '</td>'+
                                                    '<td class="featureCol">' + item.dw + '</td>'+
                                                    '<td class="featureCol" style="display: none">9</td>'+
                                                    '<td><button class="btn btn-xs btn-link" onclick="delFeature(' + item.id + ')">删除</button>  </td>'+
                                                    '</tr>'
                                            });
                                            $('.featuresBody').html(htmlStr);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });

            var optionstring = '';
            if ($('#firstLevelParentDepartment').val() != '') {
                optionstring += "<option value='0'>不需要</option>";
                optionstring += "<option value='1' selected = 'selected'>需要</option>";
                isNeedApprove(1);
            } else {
                optionstring += "<option value='0' selected = 'selected'>不需要</option>";
                optionstring += "<option value='1' >需要</option>";
                isNeedApprove(0);
            }

            $('#needApproved').html(optionstring);
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

//                    if (data.success) {
//                        var _section = data.obj.section;
//                        var sectionInfos = data.obj.itemList;
//                        var optionstring = "";
//                        for (var i in sectionInfos) {
//                            if (_section == sectionInfos[i].id) {
//                                optionstring += "<option value=\"" + sectionInfos[i].id + "\" selected = 'selected'>" + sectionInfos[i].text + "</option>";
//
//                            } else {
//                                optionstring += "<option value=\"" + sectionInfos[i].id + "\" >" + sectionInfos[i].text + "</option>";
//                            }
//
//                        }
//                        $("#section").html("<option value=''>请选择标段</option> "+optionstring);
//
//                        changeSection();
//                    }

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
                closeBtn: 2,
                shadeClose: true,
                content: $('#supInfosDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });
        }
        
        function selectSupInfo() {
            $('#dataName').val($("#supInfoSel").val());

        }

        function openFeatureModal() {
            layer.open({
                type: 1,
                title: '项目特征',
                closeBtn: 2,
                btn:['确定','增加'],
                yes: function(index, layero){
                    merageFeatures();
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                },
                btn2: function(index, layero){
                    addLine();
                    return false // 开启该代码可禁止点击该按钮关闭
                },
                shadeClose: true,
                area: ['600px', '400px'],
                content: $('#featuresDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            });
        }

        function merageFeatures() {
            var feature = '';
            $(".featureRow").each(function (index, row) {
                var features = row.getElementsByClassName("featureCol");
                var mc;
                var dw;
                var modifyTag = features[3].innerHTML;
                if (modifyTag == '0') { // 新增
                    mc = features[0].firstElementChild.value;
                    dw = features[2].firstElementChild.value;
                } else if (modifyTag == '9') {
                    mc = features[0].innerHTML;
                    dw = features[2].innerHTML;
                }
                var count = eval(features[1].firstElementChild.value);

                if (mc != '' && count != '' && dw != '') {
                    feature += mc + ":" + count + ' ' + dw + ";  ";
                    if (modifyTag == '0') {
                        addFeature(mc, count, dw);
                    }
                }

                $('#remark').val(feature);
            });
        }

        function addFeature(mc, count, dw) {
            $.ajax({
                url: '${pageContext.request.contextPath}/featureController/securi_addFeature',
                data: {mc: mc, count: count, dw: dw, cid: <%= cid %>, uid: <%= uid %>, itemCode: $('#hideItemCode').val()},
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.rspCode == '0') {
                        console.log("ok")
                    }
                }
            });
        }

        function delFeature(id) {
            $.ajax({
                url: '${pageContext.request.contextPath}/featureController/securi_delFeature',
                data: {id: id, cid: <%= cid %>, uid: <%= uid %>, itemCode: $('#hideItemCode').val()},
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.rspCode == '0') {
                        var htmlStr = '';
                        var features = data.features;
                        $.each(data.features,function(index,item){
                            htmlStr +=
                                '<tr class="featureRow">'+
                                    '<td >' + (index + 1) + '</td>'+
                                    '<td class="featureCol">' + item.mc + '</td>'+
                                    '<td class="featureCol">'+
                                        '<input type="text" align="right" placeholder="数量" style="margin-bottom:0px;width: 80px">'+
                                    '</td>'+
                                    '<td class="featureCol">' + item.dw + '</td>'+
                                    '<td class="featureCol" style="display: none">9</td>'+
                                    '<td><button class="btn btn-xs btn-warning" onclick="delFeature(' + item.id + ')">删除</button>  </td>'+
                                '</tr>'
                        });
                        $('.featuresBody').html(htmlStr);
                    }
                }
            });
        }

        function searchFeatures() {
            $.ajax({
                url: '${pageContext.request.contextPath}/featureController/securi_getFeatures',
                data: {keyword: $('#keyword').val(), cid: <%= cid %>, uid: <%= uid %>, itemCode: $('#hideItemCode').val()},
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.rspCode == '0') {
                        var htmlStr = '';
                        var features = data.features;
                        $.each(data.features,function(index,item){
                            htmlStr +=
                                '<tr class="featureRow">'+
                                '<td >' + (index + 1) + '</td>'+
                                '<td class="featureCol">' + item.mc + '</td>'+
                                '<td class="featureCol">'+
                                '<input type="text" align="right" placeholder="数量" style="margin-bottom:0px;width: 80px">'+
                                '</td>'+
                                '<td class="featureCol">' + item.dw + '</td>'+
                                '<td class="featureCol" style="display: none">9</td>'+
                                '<td><button class="btn btn-xs btn-link" onclick="delFeature(' + item.id + ')">删除</button>  </td>'+
                                '</tr>'
                        });
                        $('.featuresBody').html(htmlStr);
                    }
                }
            });
        }

        function addLine() {
            var rowHtml = '';
            rowHtml = '<tr class="featureRow">'+
                '<td ></td>'+
                '<td class="featureCol">' +
                    '<input type="text" align="right" placeholder="名称" style="margin-bottom:0px;width: 100px">'+
                '</td>'+
                '<td class="featureCol">'+
                    '<input type="text" align="right" placeholder="数量" style="margin-bottom:0px;width: 80px">'+
                '</td>'+
                '<td class="featureCol">' +
                    '<input type="text" align="right" placeholder="单位" style="margin-bottom:0px;width: 50px">'+
                '</td>'+
                '<td class="featureCol" style="display: none">0</td>'+
                '<td><button class="btn btn-xs btn-link" onclick="this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode)">删除</button>  </td>'+
            '</tr>';
            $('.featuresBody').append(rowHtml);

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
        <input type="hidden" id = "firstLevelParentDepartment" name="firstLevelParentDepartment" value="${firstLevelParentDepartment}">
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
                        <label class="control-label" for="specifications">设施(分部分项)名称:</label>

                        <div class="controls">
                            <select style="width:250px" name="specifications" id="specifications">
                                <option></option>
                                <c:forEach var="location" items="${locations}" varStatus="index">
                                    <option value="${location.mc}">${location.mc}</option>
                                </c:forEach>
                            </select>

                            <%--<input type="text" name="specifications" id="specifications" class="easyui-textbox" style="width:236px">--%>
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
                                        <option value="${unitParam.paramValue}">${unitParam.paramValue}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <%--<div class="control-group">--%>
                        <%--<label class="control-label" for="price">单价:</label>--%>

                        <%--<div class="controls">--%>
                            <%--<input type="text" name="price" id="price" class="easyui-numberbox" precision="2" style="width:236px" onblur="cal()">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="control-group">--%>
                        <%--<label class="control-label" for="payAmount">实付金额:</label>--%>

                        <%--<div class="controls">--%>
                            <%--<input type="text" name="payAmount" id="payAmount" class="easyui-numberbox" precision="2" style="width:236px">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="control-group">
                        <label class="control-label" for="remark">项目特征:</label>

                        <div class="controls">
                            <textarea type="text" name="remark" id="remark" style="width:236px"></textarea>
                            <a onclick="openFeatureModal()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'"></a>

                        </div>
                    </div>


                </div>

                <div class="span6">

                    <div class="control-group">
                        <label class="control-label" for="costType">费用类型:</label>

                        <div class="controls">
                            <input class="easyui-combotree" name="costType" id="costType" style="width:250px;">
                        </div>

                        <input type="hidden" id="hideItemCode" name="hideItemCode">
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="dataName">名称(工序名称):</label>

                        <div class="controls">
                            <input type="text" name="dataName" id="dataName" class="easyui-textbox" style="width:236px">
                            <a style="display: none;" id="supInfos" onclick="chooseSupInfos()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'add_new'"></a>
                        </div>

                    </div>
                    <div class="control-group">
                        <label class="control-label" for="count">数量:</label>

                        <div class="controls">
                            <input type="text" name="count" id="count" class="easyui-numberbox" precision="4" style="width:236px" >
                        </div>
                    </div>
                    <%--<div class="control-group">--%>
                        <%--<label class="control-label" for="sumprice">金额:</label>--%>

                        <%--<div class="controls">--%>
                            <%--<input type="text" name="sumprice" id="sumprice" class="easyui-numberbox" precision="2" style="width:236px" onblur="cal()" readonly>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="control-group" id="supplierDiv" style="display: none;">
                        <label class="control-label" for="supplier">供应商:</label>

                        <div class="controls">
                            <input type="text" name="supplier" id="supplier" class="easyui-textbox" style="width:236px">
                        </div>
                    </div>
                    <div class="control-group" style="display: none;">
                        <label class="control-label" for="section">工程属性:</label>

                        <div class="controls">
                            <input type="text" name="section" id="section" class="easyui-textbox" style="width:236px">
                            <%--<select style="width:250px;" id="section" name="section" onchange="changeSection()">--%>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="needApproved">需要审批:</label>

                        <div class="controls">
                            <select onchange="isNeedApprove(this.options[this.options.selectedIndex].value)" style="width:250px;" id="needApproved" name="needApproved">
                                <%--<option value="0">不需要</option>--%>
                                <%--<option value="1">需要</option>--%>
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

<div id="featuresDiv" style="display:none; ">
    <div>
        <input type="text" placeholder="关键字搜索" id="keyword" style="margin-top: 10px" oninput="searchFeatures()">
    </div>
    <table class="table_style featuresTable">
        <thead>
        <%--<tr>--%>
            <%--<th></th>--%>
            <%--<th style="padding-top: 5px;padding-bottom: 5px;"><input type="text" placeholder="关键字搜索" id="keyword" style="margin-bottom:0px;" oninput="searchFeatures()"></th>--%>
            <%--<th></th>--%>
            <%--<th></th>--%>
            <%--<th></th>--%>
        <%--</tr>--%>
        <tr style="height:34px; background:#76b3ff; color:#fff; font-weight: normal">
            <th>序号</th>
            <th>名称</th>
            <th>数量</th>
            <th>单位</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="featuresBody" style="overflow: scroll;">
        <c:forEach items="${features}" var="item" varStatus="status">
            <tr class="featureRow">
                <td >${status.index+1}</td>
                <td style="text-align:center;" class="featureCol">${item.mc}</td>
                <td class="featureCol" style="text-align:center;">
                    <input type="text" placeholder="数量" style="margin-bottom:0px;width: 80px">
                </td>
                <td style="text-align:center;" class="featureCol">${item.dw}</td>
                <td class="featureCol" style="display: none">9</td>
                <td style="text-align:center;"><button class="btn btn-xs btn-link" onclick="delFeature(${item.id})">删除</button>  </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</html>

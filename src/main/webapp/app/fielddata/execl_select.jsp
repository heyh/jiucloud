<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String userId = null;
    List<Map<String, Object>> largeCostTypeInfos = new ArrayList<Map<String, Object>>();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
        List<Map<String, Object>> costInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        for (Map<String, Object> costInfo : costInfos) {
            if (costInfo.get("pid").equals("-1")) {
                largeCostTypeInfos.add(costInfo);
            }
        }
    }

%>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<style>
    /*a  upload */
    .a-upload {
        padding: 4px 10px;
        height: 20px;
        line-height: 20px;
        position: relative;
        cursor: pointer;
        color: #888;
        background: #fafafa;
        border: 1px solid #ddd;
        border-radius: 4px;
        overflow: hidden;
        display: inline-block;
        *display: inline;
        *zoom: 1
    }

    .a-upload  input {
        position: absolute;
        font-size: 100px;
        right: 0;
        top: 0;
        opacity: 0;
        filter: alpha(opacity=0);
        cursor: pointer
    }

    .a-upload:hover {
        color: #444;
        background: #eee;
        border-color: #ccc;
        text-decoration: none
    }

    .table_style {
        /*width: 90%;*/
        /*margin-bottom: 20px;*/
        /*border:1px solid #EDEDED*/

    }
    .table_style td{height:32px; color:#0e0e0e;padding-top:10px;padding-left:15px;color:#666}

</style>
<script type="text/javascript">

parent.$.messager.progress('close');

    $(function () {
        $('#form')
                .form(
                        {
                            url: '${pageContext.request.contextPath}/fieldDataController/securi_execlImport',

                            onSubmit: function () {
                                if (document.getElementById("projectName").value == '') {
                                    alert('请选择项目');
                                    return false;
                                }
                                if (document.getElementById("largeCostType").value == '') {
                                    alert('请选择费用类别');
                                    return false;
                                }
                                if (document.getElementById("section").value == '') {
                                    alert('请选择标段');
                                    return false;
                                }
                                if($("#excel").val()=="" || document.getElementById("excel").files[0] =='请选择xls格式的文件'){
                                    alert('请选择xls文件');
                                    return false;
                                }
                                parent.$.messager.progress({
                                    title : '提示',
                                    text : '导入中，请稍后....'
                                });
                                return true;
                            },
                            success: function (result) {
                                parent.$.messager.progress('close');
                                result = $.parseJSON(result);
                                if (result.success) {
                                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                                    parent.$.modalDialog.handler.dialog('close');
                                    parent.$.messager.alert('提示', '导入成功', 'success');
                                } else {
                                    parent.$.messager.alert('错误', result.msg, 'error');
                                }
                            }
                        });

    });

$(".a-upload").on("change","input[type='file']",function(){
    debugger;
    var filePath=$(this).val();
    if(filePath.indexOf("xlsx")!=-1 || filePath.indexOf("xls")!=-1){
        $(".fileerrorTip").html("").hide();
        var arr=filePath.split('\\');
        var fileName=arr[arr.length-1];
        $(".showFileName").val(fileName);

        var largeCostTypeName = $('#largeCostType  option:selected').text();
        $('#dataName').val(fileName.substring(0, fileName.length-4) + '-' + largeCostTypeName);

    }else{
        $(".showFileName").val("");
        $(".fileerrorTip").html("您未上传文件，或者您上传文件类型有误！").show();
        return false
    }
})

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
            if ($('#maxProjectId').val() == projectInfos[i].id) {
                optionstring += "<option value=\"" + projectInfos[i].id + "\" selected = 'selected'>" + projectInfos[i].text + "</option>";
            } else {
                optionstring += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
            }
        }
        $("#projectName").html("<option value=''>请选择项目</option> "+optionstring);
        changeProject();
    },
    error: function (msg) {
        alert("系统异常，请联系管理员！");
    }
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
                    }
                    optionstring += "<option value=\"" + sectionInfos[i].id + "\" >" + sectionInfos[i].text + "</option>";
                }
                $("#section").html("<option value=''>请选择标段</option> "+optionstring);
            }

        }
    });
};

function changeLargeCostType() {
    var largeCostTypeName = $('#largeCostType  option:selected').text();
    var projectName = $(".showFileName").val();
    $('#dataName').val(projectName.substring(0, projectName.length-4) + '-' + largeCostTypeName);
};


</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <form class="form-inline" role="form" id="form" method="post" enctype="multipart/form-data">
        <input type="hidden" id = "maxProjectId" name="maxProjectId" value="${maxProjectId}"/>
        <table class="table_style">
            <tr>
                <td>
                    <span>项目</span>
                </td>
                <td>
                    <select style="width:220px;" id="projectName" name="projectName" onchange="changeProject()">
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <span>费用类别</span>
                </td>
                <td>
                    <select style="width:220px;" id="largeCostType" name="largeCostType" onchange="changeLargeCostType()">
                        <option value="">请选择费用类别</option>
                        <c:forEach var="largeCostTypeInfo" items="<%= largeCostTypeInfos %>" varStatus="index">
                            <option value="${largeCostTypeInfo.itemCode}">${largeCostTypeInfo.costType}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <span>标段</span>
                </td>
                <td>
                    <select style="width:220px;" id="section" name="section">
                        <option>请选择标段</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <a href="javascript:;" class="a-upload">
                        <input type="file" name="excel" id="excel">选择文件
                    </a>
                </td>
                <td>
                    <input type='text' name='showFileName' id='showFileName' class='showFileName' readonly="readonly"/>
                </td>

            </tr>
            <tr>
                <td>
                    <a class="btn btn-mini btn-success" onclick="window.location.href='http://gcgl.9393915.com:8080/fileserver/template/<%= userId %>/projectTemplate.xls'">下载模版</a>
                </td>
            </tr>
            <tr>
                <td>
                    <span>名称</span>
                </td>
                <td>
                    <input type="text" id="dataName" name="dataName" value=""/>
                </td>
            </tr>
        </table>
    </form>
</div>

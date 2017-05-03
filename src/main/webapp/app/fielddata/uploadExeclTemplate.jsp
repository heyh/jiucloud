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
                            url: '${pageContext.request.contextPath}/paramTransController/securi_uploadExeclTemplate',

                            onSubmit: function () {
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
                                    parent.$.messager.alert('提示', '上传成功', 'success');
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
    }else{
        $(".showFileName").val("");
        $(".fileerrorTip").html("您未上传文件，或者您上传文件类型有误！").show();
        return false
    }
})



</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <form class="form-inline" role="form" id="form" method="post" enctype="multipart/form-data">
        <table class="table_style">
            <tr>
                <td>
                    <input type='text' name='showFileName' id='showFileName' class='showFileName' readonly="readonly"/>
                </td>
                <td>
                    <a href="javascript:;" class="a-upload">
                        <input type="file" name="excel" id="excel">选择文件
                    </a>
                </td>
            </tr>
        </table>
    </form>
</div>

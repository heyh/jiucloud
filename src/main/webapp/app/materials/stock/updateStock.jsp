
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%

%>
<script type="text/javascript">
    $(function () {
        parent.$.messager.progress('close');
        $('#form')
            .form(
                {
                    url: '${pageContext.request.contextPath}/stockController/securi_updateStock',
                    onSubmit: function () {
                        var count = $('#count').val();
                        if (count == '') {
                            alert('请填写材料数量');
                            return false;
                        }
                        return true;
                    },
                    success: function (result) {
                        parent.$.messager.progress('close');
                        result = $.parseJSON(result);
                        if (result.success) {
                            alert("修改成功!");
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

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;">
        <form class="form-horizontal" name="form" id="form" method="post" role="form" style="padding-right: 100px">

            <input type="hidden" id="id" name="id" value="${stockBean.id}" />
            <input type="hidden" id="materialsId" name="materialsId" value="${stockBean.materialsId}" />

            <div class="control-group" style="padding-top: 20px; ">
                <label class="control-label" for="mc">材料名称:</label>
                <div class="controls">
                    <input type="text" id="mc" name="mc" value="${stockBean.mc}" disabled='true'>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="specifications">规格型号:</label>
                <div class="controls">
                    <input type="text" id="specifications" name="specifications" value="${stockBean.specifications}" disabled='true'>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="count">数量:</label>

                <div class="controls">
                    <input type="number" id="count" name="count" value="${stockBean.count}" style="text-align: right">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="dw">单位:</label>

                <div class="controls">
                    <input type="text" id="dw" name="dw" value="${stockBean.dw}" disabled='true'>
                </div>
            </div>
        </form>
    </div>
</div>
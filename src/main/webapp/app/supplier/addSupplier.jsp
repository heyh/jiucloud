
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
                    url: '${pageContext.request.contextPath}/supplierController/securi_add',
                    onSubmit: function () {
                        if ($('#name').val() == '') {
                            alert('请填写供应商名称');
                            return false;
                        }
                        if ($('#tel').val() == '') {
                            alert('请填写企业电话');
                            return false;
                        }
                        if ($('#linkman').val() == '') {
                            alert('请填写联系人');
                            return false;
                        }
                        if ($('#linkphone').val() == '') {
                            alert('请填写联系电话');
                            return false;
                        }
                        return true;
                    },
                    success: function (result) {
                        parent.$.messager.progress('close');
                        result = $.parseJSON(result);
                        if (result.success) {
                            alert("增加成功!");
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

            <div class="control-group" style="padding-top: 20px; ">
                <label class="control-label" for="name">供应商名称:</label>
                <div class="controls">
                    <input type="text" id="name" name="name" required>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="tel">企业电话:</label>
                <div class="controls">
                    <input type="text" id="tel" name="tel" required>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="addr">企业地址:</label>
                <div class="controls">
                    <input type="text" id="addr" name="addr">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="linkman">联系人:</label>
                <div class="controls">
                    <input type="text" id="linkman" name="linkman" required>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="linkphone">联系电话:</label>
                <div class="controls">
                    <input type="text" id="linkphone" name="linkphone" required>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="remark">备注:</label>
                <div class="controls">
                    <textarea type="text" name="remark" id="remark" style="width: 220px"></textarea>
                </div>
            </div>
        </form>
    </div>
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/My97DatePicker4.8b3/My97DatePicker/WdatePicker.js"
        charset="utf-8"></script>
<script type="text/javascript">
    $(function () {
        parent.$.messager.progress('close');
        $('#form')
            .form(
                {
                    url: '${pageContext.request.contextPath}/clockinginTimeController/securi_updateClockinginTime',

                    onSubmit: function () {
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
                            parent.$.messager.alert('错误', result.msg, 'error');
                        }
                    }
                });

    });

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;">
        <form class="form-horizontal" name="form" id="form" method="post" role="form">

            <input type="hidden" id="id" name="id" value="${clockinginTime.id}"/>

            <div class="control-group" style="padding-top: 20px; ">
                <label class="control-label" >上午:&nbsp;&nbsp;</label>

                <div style="float: left">
                    <%--<div class="input-append date">--%>
                    <%--<input type="text" name="clockinginStartTime" id="clockinginStartTime" readonly value="${clockinginTime.clockinginStartTime}">--%>
                    <%--<span class="add-on"><i class="icon-th"></i></span>--%>
                    <%--</div>--%>
                    <input style="" class="Wdate span2" name="clockinginStartTime" id='clockinginStartTime'
                           placeholder="上班时间" value="${clockinginTime.clockinginStartTime}"
                           onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
                    <span>至</span>
                    <input style="" class="Wdate span2" name="clockinginStartTime2" id='clockinginStartTime2'
                           placeholder="上班时间" value="${clockinginTime.clockinginStartTime2}"
                           onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>

                </div>
            </div>

            <div class="control-group" style="padding-top: 20px; ">
                <label class="control-label" >下午:&nbsp;&nbsp;</label>

                <div style="float: left">
                    <%--<div class="input-append date">--%>
                    <%--<input type="text" name="clockinginEndTime" id="clockinginEndTime" readonly value="${clockinginTime.clockinginEndTime}">--%>
                    <%--<span class="add-on"><i class="icon-th"></i></span>--%>
                    <%--</div>--%>
                    <input style="" class="Wdate span2" name="clockinginEndTime2" id='clockinginEndTime2'
                           placeholder="上班时间" value="${clockinginTime.clockinginEndTime2}"
                           onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
                    <span>至</span>
                    <input style="" class="Wdate span2" name="clockinginEndTime" id='clockinginEndTime'
                           placeholder="上班时间" value="${clockinginTime.clockinginEndTime}"
                           onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>

                    <%--<input style="" class="Wdate span2" name="clockinginEndTime" id='clockinginEndTime'--%>
                           <%--placeholder="上班时间" value="${clockinginTime.clockinginEndTime}"--%>
                           <%--onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>--%>
                </div>
            </div>
        </form>
    </div>
</div>
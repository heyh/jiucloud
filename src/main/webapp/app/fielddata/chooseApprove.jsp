<%--
  Created by IntelliJ IDEA.
  User: heyh
  Date: 16/4/16
  Time: 下午2:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jOrgChart/bootstrap.min.css">--%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jOrgChart/jquery.jOrgChart.css"/>
<%--<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/jOrgChart/jquery-1.10.2.min.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jOrgChart/jquery.jOrgChart.js"></script>

<script type="text/javascript">

    $(function () {
        $.ajax({
            url: '${pageContext.request.contextPath }/fieldDataController/securi_getParentNodes',
            type: 'post',
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (data) {
                if (data.success) {
                    console.log((data.obj))
                    var strHtml = $("<ul id='org' style='display:none'></ul>");
                    showOrg(data.obj, strHtml);
                    $("#jOrgChart").append(strHtml);
                    $("#org").jOrgChart({
                        chartElement: '#jOrgChart',//指定在某个dom生成jorgchart
                        dragAndDrop: false //设置是否可拖动
                    });
                    parent.$.messager.progress('close');
                }

            }
        });
    });

    function showOrg(nodeList, parent) {
        $.each(nodeList, function (index, item) {
            if (item.childrens != null && item.childrens.length > 0) {
                var li = $("<li></li>");
                li.append("<a href='javascript:void(0)' id= "  + item.userId + " onclick=getOrgId(" + item.userId + ");>" + item.name + "</a>").append("<ul></ul>").appendTo(parent);
                //递归显示
                showOrg(item.childrens, $(li).children().eq(1));
            } else {
                $("<li></li>").append("<a href='javascript:void(0)' id= "  + item.userId + " onclick=getOrgId(" + item.userId + ");>" + item.name + "</a>").appendTo(parent);
            }
        });

    }

</script>

    <!--显示组织架构图-->
    <div id='jOrgChart'>
    </div>

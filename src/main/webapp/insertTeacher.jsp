<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<jsp:include page="inc.jsp"></jsp:include>
</head>
<body>
   <form action="insertTeacher" id="aa">
   <input type="text" id="teacherid" name="teacherid"/><input type="hidden" value="${sessionScope.teacherid}" id="mm">
   <button type="button" id="tj">提交</button>
   </form>
</body>
</html>
<script type="text/javascript">
$('#tj').click(function(){
	var teacherid=$('#teacherid').val();
	var mm=$('#mm').val();
	
	if(mm!=teacherid)
		{
		alert('你输入的教职工号不存在');
		}else{
			$('#aa').submit();
		}
	});
	
	
	



</script> 
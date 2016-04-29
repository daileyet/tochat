<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome ToChat</title>
</head>
<body>
<%


if(session.getAttribute("LOGIN_NAME")!=null){
	out.print("Hello "+session.getAttribute("LOGIN_NAME"));
	out.print("<BR>");
}else{
	session.setAttribute("LOGIN_NAME","dailey");
}
%>

<jsp:forward page="/room/index.htm"></jsp:forward>

</body>
</html>
<%-- 
    Document   : Home
    Created on : 5 Dec, 2016, 12:27:03 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>News & Events:: HRMS</title>
        <link href="https://fonts.googleapis.com/css?family=Cabin" rel="stylesheet">
        <style type="text/css">
            body{margin:0px;background:#EAEAEA;font-family:'Cabin', 'Sans Serif'}
            #menu_container{background:#0D395D;margin:0px auto;height:40px;}
            #menu_wrap{width:1000px;margin:0px auto;}
            #menu_wrap ul{margin:0px;padding:0px;list-style-type:none;}
            #menu_wrap ul li a{float:left;padding:0px 15px;line-height:40px;color:#FFFFFF;text-decoration:none;}
            #menu_wrap ul li a:hover{color:#a3a183}
        </style>
    </head>
    <body>
                        <jsp:include page="HeaderFront.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
            <h1 style="margin:0px;font-size:18pt;">News & Events</h1>
                        <jsp:include page="FooterFront.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
    </body>
</html>

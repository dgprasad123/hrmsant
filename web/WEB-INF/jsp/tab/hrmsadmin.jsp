
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
       
    </head>
  <c:choose>
    <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
            <jsp:include page="../tab/AlfaMenu.jsp"/>  
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                <div class="container-fluid" style="min-height:450px;background:url('images/logo_fade.png') no-repeat top center">
    </c:when>
       
                    
      <c:when test = "${LoginUserBean.loginuserid eq 'agpension'}">
            <body style="margin-top:0px;background: rgb(220, 234, 232);">
            <jsp:include page="../tab/agOdishaMenu.jsp"/>  
            
    </c:when>              
                    
                    
    <c:otherwise>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper" style="min-height:903px;background-color: #dbe8ed;">
                <div class="container-fluid">
                                    <img src="images/HRMSbanner.jpg" width="100%" height="90px"  >
            </c:otherwise>
                    
                    
    </c:choose>
            </div>
        </div>
    </body>
</html>

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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>

        <script language="JavaScript" type="text/javascript">
            var randomText = randomString(32);
            $("#randomTextValue").val(randomText);
            function randomString(length) {
                var text = "";
                var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                for (var i = 0; i < length; i++) {
                    text += possible.charAt(Math.floor(Math.random() * possible.length));
                }
                return text;
            }

            function encrtyptPassword() {
                var userPassword = $("#userPassword").val();
                var randomValue = randomString(32);
                var randmPassword = randomValue + userPassword;
                $("#userPassword").val(randmPassword);
                var newpassword = $("#newpassword").val();
                randomValue = randomString(32);
                randmPassword = randomValue + newpassword;
                $("#newpassword").val(randmPassword);
                var confirmpassword = $("#confirmpassword").val();
                randmPassword = randomValue + confirmpassword;
                $("#confirmpassword").val(randmPassword);
            }

        </script>
        <style>
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
        </style>

    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
        </c:when>
        
        <c:when test = "${fn:contains(LoginUserBean.loginusername, 'paradmin')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/ParMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
        </c:when>
        <c:when test = "${fn:contains(LoginUserBean.loginusertype, 'B')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/hrmsadminmenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
        </c:when>                
        <c:otherwise>
            <body>
                <div id="wrapper">
                    <jsp:include page="../tab/agOdishaMenu.jsp"/>
                    <div id="page-wrapper">
        </c:otherwise>
    </c:choose>
                                                 
                <div class="container-fluid" style="padding-top: 125px;padding-bottom: 125px;">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-lg-12">
                                    Change Password
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <form:form class="form-inline" action="showpasswordchange.htm" commandName="loginForm">

                                <div class="row">
                                    <label class="control-label col-sm-3">Current Password:</label>
                                    <div class="col-sm-3" style="width:60%;">
                                        <form:password class="form-control" path="userPassword"/>                                    
                                    </div>
                                </div>

                                <div class="row">
                                    <label class="control-label col-sm-3">New Password:</label>
                                    <div class="col-sm-3" style="width:60%;">
                                        <form:password class="form-control" path="newpassword"/>                                    
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="control-label col-sm-3">Confirm Password:</label>
                                    <div class="col-sm-3" style="width:60%;">
                                        <form:password class="form-control" path="confirmpassword"/>                                    
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-3" style="width:60%;">
                                        <input type="submit" class="btn btn-primary"  name="submit" value="Change" onclick="encrtyptPassword()" / >
                                    </div>
                                </div>
                                <div class ="row">
                                    <div class ="col-sm-12">
                                        <span class="help-block" style="color: red;">Password policy to match 8 characters with alphabets in combination with numbers and special characters. e.g Welcome@12</span>
                                    </div>
                                </div>

                                <div class="row">

                                    <div class="col-sm-12" style="width:60%;">
                                        <span class="help-block" style="color: #FF4500;">${msg}</span>                                   
                                    </div>
                                </div>                    
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
    </body>
    </html>

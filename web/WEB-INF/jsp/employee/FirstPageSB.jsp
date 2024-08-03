<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Welcome to HRMS Odisha ::</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>  
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script>
            function callNoImage() {
                var userPhoto = document.getElementById('employeesb');
                userPhoto.src = "images/SB1stPage.png";

            }

            function UploadImage() {
                var url = 'fileUploadSBForm.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }
            function refreshImage() {
                $("#employeesb").attr('src', 'displayfistpagesb.htm?' + 'date=' + (new Date()).getTime());
            }
        </script>
    </head>
    <body style="padding-top: 10px;">        
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="FIRSTPAGESB" />
        </jsp:include>
        <div id="profile_container">
            <c:if test = "${ifProfileCompleted == 'Y'}">
                <div id="notification_blk" style="height:30px;background:#008900;color:#FFFFFF;line-height:30px;text-align:center;font-weight:bold;">Congratulations! Your Profile is complete.</div>
            </c:if>
            <c:if test = "${ifProfileCompleted == 'N'}">
                <div id="notification_blk" style="height:30px;background:#F00;color:#FFFFFF;line-height:30px;text-align:center;font-weight:bold;">Your Profile is not complete. <a href="javascript:void(0)" onclick="javascript: showInformationWindow()" style="color:#EAEAEA;text-decoration:underline;"> Click here to know more...</a></div>
            </c:if> 
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">

                <div class="col-6">
                    <img id="employeesb" src="displayfistpagesb.htm" onerror="callNoImage()" height="722" style="padding: 5px;"/>
                </div>                    

            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <legend align="left">
                    <c:if test="${empty ifProfileVerified || ifProfileVerified eq 'N'}">
                        <a href="javascript:UploadImage(0)" class="atag"> <input type="submit" name="action" value="Edit" class="btn btn-default"/></a><br />
                    </c:if>
                    <c:if test="${not empty ifProfileVerified && ifProfileVerified eq 'Y'}">
                        <img src="images/Lock.png" width="20" height="20"/>
                    </c:if>
                </legend>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $("#notification_blk").slideUp();
            }, 5000);
        });
    </script> 
</html>

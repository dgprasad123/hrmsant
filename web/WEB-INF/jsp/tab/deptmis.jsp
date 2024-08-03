<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
        <style>
            .placeholder img{
                display: inline-block;
                border-radius: 50%;                
            }
            .heading_dashboard{
                background:#0067A2;color:#FFFFFF;font-weight:bold;text-decoration:none;height:150px;border-radius:50px;float:left;padding:10px;padding-top:0px;line-height:150px;text-align:center;font-size:16pt;
            }
            .heading_dashboard:hover{
                background:#087ED3;color:#FFFFFF;text-decoration:none;
            }
        </style>    

    </head>
    <body>
        <jsp:include page="deptmismenu.jsp"/>        
        <div id="page-wrapper">

            <div class="container-fluid" style='min-height: 400px'>
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <h1 class="alert alert-success">Administrator Dashboard Panel</h1>

                    <div class="row placeholders">
                        <div class="col-xs-6 col-sm-3 placeholder">             
                            <a class="heading_dashboard" href="misDeptVacantPost.htm">Base Level Vacant Post</a>  
                        </div>
                        <div class="col-xs-6 col-sm-3 placeholder">              
                            <a class="heading_dashboard" href="misRaschemeList.htm">Rehabilitation Scheme</a>              
                        </div>

                        <div class="col-xs-6 col-sm-3 placeholder">
                            <a class="heading_dashboard" href="misScstRecruitmentList.htm">Special Recruitment</a>   
                        </div>
                        <div class="col-xs-6 col-sm-3 placeholder">
                            <a class="heading_dashboard" href="misRecruitmentDriveList.htm">Recruitment Drive</a>
                        </div>

                        <div class="col-xs-6 col-sm-3 placeholder" style="margin-top:50px">             
                            <a class="heading_dashboard" href="misTrainingList.htm">Training Details</a>             
                        </div>
                        <div class="col-xs-6 col-sm-3 placeholder" style="margin-top:50px">
                            <a class="heading_dashboard" href="misBleoList.htm">BLEO Recruitment</a>  
                        </div>
                        <div class="col-xs-6 col-sm-3 placeholder" style="margin-top:50px">
                            <a class="heading_dashboard" href="changePasswordmis.htm">Change Password</a>  
                        </div>


                    </div>
                    <div class="alert alert-success" style='margin-top:50px'>&nbsp;</div>

                </div>

            </div>
    </body>
</html>
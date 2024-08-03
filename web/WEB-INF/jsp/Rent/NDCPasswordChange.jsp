<%-- 
    Document   : Increment Proposal List
    Created on : 20 Jun, 2016, 12:14:12 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>        
        <script type="text/javascript">
 

        </script>


<style type="text/css">
            body{margin:0px;font-family: 'Roboto', sans-serif;background:#F7F7F7}
           .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
            .tblres td{padding:5px;}
        </style>

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
    </head>

    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#777777;border-bottom:1px solid #DADADA;padding-bottom:5px;">Change Password</h1>
        <form:form action="SaveNDCChangePassword.htm" method="post" commandName="QuarterBean">
            <div class="container-fluid" style="padding-top: 25px;padding-bottom: 125px;">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            Change Password
                                                        </div>
                                                    </div>
                                                </div>
                                                                                                        <div class="row">

                                                            <div class="col-sm-12" style="width:60%;">
                                                                <span class="help-block" style="color: red;">${msg}</span>                                   
                                                            </div>
                                                        </div>   
                                                <div class="panel-body">
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
                                                                <input type="submit" class="btn btn-primary"  name="submit" value="Save New Password" onclick="encrtyptPassword()" />
                                                            </div>
                                                        </div>
                                                        <div class ="row">
                                                            <div class ="col-sm-12">
                                                                <span class="help-block" style="color:#008900;font-size:13pt;font-style:italic;">Password Policy to match 8 characters with alphabets in combination with numbers and special characters. e.g Welcome@12</span>
                                                            </div>
                                                        </div>

                 
                                                </div>
                                            </div>
                                        </div>
        </form:form>
            </div>
        </div>                
    </body>
</html>




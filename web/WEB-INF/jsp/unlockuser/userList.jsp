<%-- 
    Document   : userList
    Created on : Jan 7, 2019, 12:19:45 PM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

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
        <script type="text/javascript">
            function validation() {
                if ($("#userName").val() == "") {
                    alert("please Enter User Id");
                    $("#userName").focus();
                    return false;
                }
                if ($("#userPassword").val() == "") {
                    alert("please Enter Password");
                    $("#userPassword").focus();
                    return false;
                }
                if ($("#usertype").val() == "") {
                    alert("please Enter User Type");
                    $("#usertype").focus();
                    return false;
                }
                if ($("#empId").val() == "") {
                    alert("please Enter LinkId");
                    $("#empId").focus();
                    return false;
                }
            }

        </script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> User List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> New User
                                </li>
                            </ol>
                        </div>
                    </div>
                    <form:form action="userlistview.htm" commandName="users" method="post">
                        <div class="container-fluid">
                            <div class="panel panel-default">
                                <div class="panel-body">

                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >User Id:</label>
                                        <div class="col-sm-4" > 
                                            <form:input class="form-control" path="userName" />
                                        </div>
                                    </div><br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >Password:</label>
                                        <div class="col-sm-4" > 

                                            <form:input class="form-control" path="userPassword" />                                    
                                        </div>
                                    </div><br><br>

                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >User Type:</label>
                                        <div class="col-sm-4" > 
                                            <form:input class="form-control" path="usertype" />
                                        </div>
                                    </div><br><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" >Link Id:</label>
                                        <div class="col-sm-4" > 
                                            <form:input class="form-control" path="empId" />
                                        </div>
                                    </div><br><br>
                                </div>
                                <div class="panel-footer">
                                    <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validation()"/>  
                                    <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
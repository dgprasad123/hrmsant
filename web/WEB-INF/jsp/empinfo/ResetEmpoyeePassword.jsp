<%-- 
    Document   : EmployeeBasicProfile
    Created on : Dec 27, 2017, 12:17:13 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
        </style>
    </head>
    <body  style="background-color:white">
        <div class="container">
            <div class="form-group">
                <c:if test="${not empty ResetEmpoyeePassword.username}">
                    <h4 style="color:green">Password reset Successfully!!!
                        <br/> 
                        User Name is :<u><i>${ResetEmpoyeePassword.username}</i></u><br/>
                        Password is :<u><i>${ResetEmpoyeePassword.password}</i></u>
                    </h4>
                </c:if>
                <c:if test="${ empty ResetEmpoyeePassword.username}">
                    <h4 style="color:red">HRMS ID is not available Please contact to State Team                           
                    </h4>    
                </c:if>    
            </div> 

        </div>
    </body>
</html>

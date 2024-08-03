<%-- 
    Document   : EmployeeBasicProfile
    Created on : Dec 27, 2017, 12:17:13 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <body>
        <div class="container">
            <div class="form-group">
                <label>Hrms Id:</label>
                <label>${EmployeeProfile.empid} </label>
            </div>
            <div class="form-group">
                <label>Employee Name:</label>
                <label>${EmployeeProfile.fname} ${EmployeeProfile.mname} ${EmployeeProfile.lname}</label>
            </div>
            <div class="form-group">
                <label>Account Type:</label>
                <label>${EmployeeProfile.accttype} </label>
            </div>
            <div class="form-group">
                <label>Account No:</label>
                <label>${EmployeeProfile.gpfno} </label>
            </div>
            <div class="form-group">
                <label>Date Of Birth:</label>
                <label>${EmployeeProfile.dob} </label>
            </div> 
            <div class="form-group">
                <label>Current Office:</label>
                <label>${EmployeeProfile.office} </label>
            </div>
            <div class="form-group">
                <label>District:</label>
                <label>${EmployeeProfile.district} </label>
            </div>            
            <div class="form-group">
                <label>Current Post:</label>
                <label>${EmployeeProfile.post} </label>
            </div>
            <div class="form-group">
                <label>Mobile / Phone No:</label>
                <label>${EmployeeProfile.mobile} </label>
            </div>
            <div class="form-group">
                <label>Email Id:</label>
                <label>${EmployeeProfile.email} </label>
            </div>
            <div class="form-group">
                <label>Cadre Name:</label>
                <label>${EmployeeProfile.empCadre} </label>
            </div>
        </div>
    </body>
</html>

<%-- 
    Document   : EmployeeBasicProfileLMS
    Created on : Feb 24, 2020, 3:38:15 PM
    Author     : Madhusmita
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
                <label>Account No:</label>
                <label>${EmployeeProfile.gpfno} </label>
            </div>
            <div class="form-group">
                <label>Employee Name:</label>
                <label>${EmployeeProfile.fname} ${EmployeeProfile.mname} ${EmployeeProfile.lname}</label>
            </div>
            <div class="form-group">
                <label>Office Code:</label>
                <label>${EmployeeProfile.officecode} </label>
            </div>
            <div class="form-group">
                <label>Current Office:</label>
                <label>${EmployeeProfile.office} </label>
            </div>           
            <div class="form-group">
                <label>Current Post:</label>
                <label>${EmployeeProfile.post} </label>
            </div>
            <div class="form-group">
                <label>Substantive Post Code:</label>
                <label>${EmployeeProfile.spc} </label>
            </div>
            <div class="form-group">
                <label>Mobile / Phone No:</label>
                <label>${EmployeeProfile.mobile} </label>
            </div>
            <div class="form-group">
                <label>Current Basic:</label>
                <label>${EmployeeProfile.basic} </label>
            </div>
            <div class="form-group">
                <label>Cell:</label>
                <label>${EmployeeProfile.cell} </label>
            </div>
            <div class="form-group">
                <label>Level:</label>
                <label>${EmployeeProfile.level} </label>
            </div>
            <div class="form-group">
                <label>Quarter Address:</label>
                <label>${EmployeeProfile.quarterAddress} </label>
            </div>
            <div class="form-group">
                <label>Cadre Name:</label>
                <label>${EmployeeProfile.empCadre} </label>
            </div>
        </div>
    </body>
</html>

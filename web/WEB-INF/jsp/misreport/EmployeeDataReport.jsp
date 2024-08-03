<%-- 
    Document   : EmployeeDataReport
    Created on : Aug 9, 2018, 2:38:31 PM
    Author     : Manas
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            .table thead tr th {
                background-color: #e9e9ea;
                text-align: center;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
        </style>
    </head>
    <body>
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h2 align="center">EMPLOYEE DATA REPORT</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="10%">
                                            Employee Id<br>(If Temporary)<br>Entitlement of<br>Pension<br>[GPF No]
                                        </th>
                                        <th width="23%">
                                            Full Name of Employee<br>Father's Name<br>[Gender]<br>DDO Code
                                        </th>
                                        <th width="8%">
                                            Date of Birth<br>(Date of <br>Appointment)
                                        </th>
                                        <th width="17%">
                                            Current Post<br>(Post at First Appointment)
                                        </th>
                                        <th width="14%">
                                            Scale of Pay<br>(Date of <br>Increment)
                                        </th>
                                        <th width="5%">
                                            Basic<br>Gr.Pay<br>Per. Pay<br>Spl. Pay<br>Oth.Pay<br>
                                        </th>
                                        <th width="5%">
                                            DA<br>DP<br>HRA<br>Oth.Allw<br>NPA
                                        </th>
                                        <th width="5%">
                                            Gross<br>Salary
                                        </th>
                                        <th width="13%">
                                            Name of Bank <br>Branch<br>(Bank A/c No)
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${employeeList}" var="employee">
                                        <tr>
                                            <td>${employee.empid}</td>
                                            <td>${employee.fullname}</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

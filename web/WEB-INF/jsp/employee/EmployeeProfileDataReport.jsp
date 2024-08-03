<%-- 
    Document   : EmployeeProfileDataReport
    Created on : 27 Mar, 2019, 10:56:10 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">
                            Employee Profile Verification Report
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>Sl No</th>
                                            <th>HRMS ID</th>
                                            <th>ACC TYPE/ACC NO</th>
                                            <th>Employee Name/Designation</th>

                                            <th> DOB </th>
                                            <th> Profile Completed </th>
                                            <th> Verified </th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <c:forEach items="${EmpList}" var="emp" varStatus="ctr">
                                        <tr>
                                            <td> ${ctr.index+1} </td>
                                            <td> ${emp.empid} </td>
                                            <td ><b>${emp.accttype} </b></br>${emp.gpfno} </td>
                                            <td> <b>${emp.empName}</b> </br> ${emp.curDesg}</td>

                                            <td> ${emp.dob} </td>
                                            <td> 
                                                <c:if test="${emp.ifprofileCompleted eq 'N'}">
                                                    <b>Not Completed</b>
                                                </c:if>
                                                <c:if test="${emp.ifprofileCompleted eq 'Y'}">
                                                    Completed
                                                </c:if>
                                            </td>
                                            <td> 

                                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                                    <b>Verified</b>
                                                </c:if>

                                            <td> 
                                                <c:if test="${emp.ifprofileCompleted eq 'Y' && emp.ifprofileVerified eq 'N'}">
                                                    <a href="viewProfileVerificationController.htm?empId=${emp.empid}" class="btn btn-primary">Action</a> 
                                                </c:if>
                                                <c:if test="${emp.ifprofileVerified eq 'Y' && emp.ifprofileCompleted eq 'Y'}">
                                                    <a href="viewProfileVerificationController.htm?empId=${emp.empid}" class="btn btn-primary">View</a> 
                                                </c:if>

                                            </td>
                                        </tr>

                                    </c:forEach>







                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>












</html>

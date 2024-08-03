<%-- 
    Document   : OfficeWiseEmployee
    Created on : Feb 9, 2017, 4:42:58 PM
    Author     : Manas Jena
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
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Department Wise Employee Profile Updation  Report</div>
                        <div class="panel-body">
                            <div class="table-responsive">

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Department</th>
                                                <th>Total Employee</th>
                                                <th>Total Employee Completed</th>
                                                <th>Total Employee Verified</th>

                                            </tr>
                                        </thead>
                                        <c:set var="GtotalEmp" value="${0}" />
                                        <c:set var="GtotalComp" value="${0}" />
                                        <c:set var="GtotalVerified" value="${0}" />    
                                        <c:forEach items="${ProfileDetails}" var="bgroup" varStatus="count">
                                            <c:set var="GtotalEmp" value="${GtotalEmp + bgroup.totalEmp}" />
                                            <c:set var="GtotalComp" value="${GtotalComp + bgroup.totalCompleted}" />
                                            <c:set var="GtotalVerified" value="${GtotalVerified + bgroup.totalVerified}" />
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td><a href="DDOWiseprofileUpdate.htm?dcode=${bgroup.deptCode}" target='blank'>${bgroup.departmentname}</a></td>
                                                <td>${bgroup.totalEmp}</td>
                                                <td>${bgroup.totalCompleted}</td>
                                                <td>${bgroup.totalVerified}</td>
                                            </tr>
                                        </c:forEach>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>${GtotalEmp}</th>
                                        <th>${GtotalComp}</th>
                                        <th>${GtotalVerified}</th>
                                       
                                    </tr>      
                                    </table>
                                </div>                
                            </div>
                        </div>
                    </div>
                </div>
            </div>

    </body>
</html>

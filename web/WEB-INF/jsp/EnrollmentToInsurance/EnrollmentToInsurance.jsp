<%-- 
    Document   : EnrollmentToInsurance
    Created on : 8 Dec, 2021, 1:16:24 PM
    Author     : Devikrushna
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
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <form:form action="EnrollmentToInsurance.htm" method="post" commandName="enrollmentForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Insurance 
                    </div>
                    <div class="panel-body">
                         <form:hidden path="schemetype" id="schemetype"/>
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Sl No</th>
                                    <th>Name of Scheme</th>
                                    <th>Insurance Account Number</th>                                 
                                    <th>With Effect Date</th>
                                    <th>Monthly Subscription Amount</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${EnrollmentList}" varStatus="count">
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>${item.schemename}</td>
                                        <td>${item.insaccountno}</td>
                                        <td>${item.witheffectdate}</td>
                                        <td>${item.subamount}</td>
                                        <td>
                                            <c:if test="${item.isValidated eq 'N'}">
                                                <a href="editEnrollment.htm?notId=${item.notId}">Edit</a>
                                            </c:if>
                                            <c:if test="${item.isValidated eq 'Y'}">    
                                                <a href="viewEnrollment.htm?notId=${item.notId}">View</a>
                                            </c:if> 
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                       <form:hidden class="form-control" path="empid" id="empid"/>
                         <a href="addallNewEnrollment.htm">
                             <button type="button" class="btn btn-info">Add New Insurance</button>  </a>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

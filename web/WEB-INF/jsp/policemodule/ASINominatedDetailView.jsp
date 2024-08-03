<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <h3 style="text-align:center"> List of eligible and willing candidates</h3>
                    <div class="panel-heading">
                        <a href="asiNominationList.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                    </div>
                    <div class="panel-body">
                        <c:if test="${nominateddetailviewlist.size() gt 0}">
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>HRMS ID</th>
                                                <th>Employee Name</th>
                                                <th>DOB/DOS</th>
                                                <th>Date of Joining</th>
                                                <th>Current Rank</th>
                                                <th style="text-align: center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${nominateddetailviewlist}" var="emp" varStatus="counter">
                                                <tr>
                                                    <td width="5%" >${counter.count}</td>
                                                    <td width="10%">${emp.empId}</td>
                                                    <td width="25%">${emp.empName}</td>
                                                    <td width="10%">
                                                        ${emp.dob} </br> <span style="color:red">${emp.dos}</span>
                                                    </td>
                                                    <td width="10%" style="text-align: center">${emp.doj}</td>
                                                    <td width="15%" style="text-align: center">${emp.sltpostName}</td>
                                                    <td width="10%" style="text-align: center">
                                                        <a href="ASINominatedEmployeeDetailView.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}">View</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="panel-footer">
                        <a href="asiNominationList.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
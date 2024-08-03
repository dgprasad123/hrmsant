<%-- 
    Document   : AnnualEstablishmentReportList
    Created on : 20 Feb, 2018, 11:16:29 AM
    Author     : Surendra
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <head>
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
    </head>
</head>
<body>
    <form:form action="displayEstablishmentReport.htm" >
        <div class="container">
            <h1 align="center"> ${OffName}</h1>
            <h2 align="center">PROFORMA</h2>
            
            <table class="table table-hover" style="border:1px solid">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Financial Year</th>
                        <th>Submitted To</th>
                        <th>Submitted On</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="establish" items="${MasterList}">
                        <tr>
                            <td> ${establish.serialno} </td>
                            <td> ${establish.fy} </td>
                            <td> ${establish.controllingSpc} </td>
                            <td> ${establish.submittedDate} </td>
                            <td> ${establish.status} </td>
                            <td> 
                                <c:if test="${establish.status eq 'COMPLETED'}">
                                    <a href="downloadEstablishmentReport.htm" target="_blank">Download</a>
                                </c:if>
                                <c:if test="${establish.status eq 'REVERTED'}">
                                    <a href="displayEstablishmentReport.htm?fy=<c:out value="${establish.fy}"/>">Resubmit</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    
                <div> <%--<c:if test="${visible eq 'NO'}"> <input type="submit" value="AddNew" name="btnAer"/> </c:if>--%> </div>
                </tbody>
            </table>
        </div>
    </form:form>
</body>
</html>
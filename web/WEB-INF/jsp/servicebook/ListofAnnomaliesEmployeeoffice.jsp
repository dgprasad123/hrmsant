<%-- 
    Document   : ListofAnnomaliesEmployee
    Created on : Nov 24, 2020, 6:39:46 PM
    Author     : Manas
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"> 
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <h2>Anomalies List Submitted</h2>
                    <div class="table-responsive">
                        <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                            <thead>
                                <tr style="font-weight:bold;font-size:12pt;">
                                    <td align="center">Sl No.</td>
                                    <td align="center">Emp Id</td>
                                    <td align="center">Emp Name</td>
                                    <td align="center">Service Period From - To</td>
                                    <td align="center">Posting Details</td>
                                    <td align="center">Anomaly Type</td>
                                    <td align="center">Description</td>
                                    <td align="center">Attachment</td>
                                    <td align="center">Status</td>
                                    <td align="center">Action</td>
                                </tr>

                            </thead>
                            <tbody>                                        
                                <c:forEach items="${anomaliesList}" var="anomaly" varStatus="count">
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>${anomaly.empId}</td>
                                        <td>${anomaly.empname}</td>
                                        <td>${anomaly.servicePeriodFrom} to ${anomaly.servicePeriodTo}</td>
                                        <td>${anomaly.postingdeails}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test = "${anomaly.anomaliType eq 'P'}">Correctness of Personal Profile data</c:when>
                                                <c:when test = "${anomaly.anomaliType eq 'O'}">Correctness of Posting Detail</c:when>
                                                <c:when test = "${anomaly.anomaliType eq 'T'}">Correctness of Transfer Detail</c:when>
                                                <c:when test = "${anomaly.anomaliType eq 'M'}">Correctness of Promotion Detail</c:when>
                                                <c:when test = "${anomaly.anomaliType eq 'I'}">Correctness of Increment Detail</c:when>
                                                <c:otherwise>&nbsp;</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${anomaly.furtherDesc}</td>
                                        <td>
                                            &nbsp;
                                            <c:if test="${not empty anomaly.originalFileName}">
                                                <a href="downloadSupportingFile.htm?anomaliId=${anomaly.anomaliId}">${anomaly.originalFileName}</a>
                                            </c:if>

                                        </td>
                                        <td>
                                            <c:if test="${anomaly.status eq 'P'}">
                                                Pending
                                            </c:if>
                                            <c:if test="${anomaly.status eq 'S'}">
                                                Solved
                                            </c:if>
                                        </td>
                                        <td><a href="javascript:void(0)">Reply</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div> 
        </div>
    </body>
</html>

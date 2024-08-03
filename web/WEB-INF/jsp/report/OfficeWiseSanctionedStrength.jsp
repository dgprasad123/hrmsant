<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <form action="addOfficeWiseSanctionedStrength.htm" commandName="officebean">
                <div class="panel panel-default">
                    <div class="panel-header">
                        <div align="center" style="font-weight: bold;margin-top:20px;">
                            <c:out value="${offName}"/>
                        </div>
                        <div align="center" style="font-weight: bold;margin-top:20px;">
                            SANCTIONED STRENGTH
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-striped table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th width="5%">SL</th>
                                    <th width="15%">Financial Year</th>
                                    <th width="10%">Group A</th>
                                    <th width="10%">Group B</th>
                                    <th width="10%">Group C</th>
                                    <th width="10%">Group D</th>
                                    <th width="15%">Grant-in-Aid</th>
                                    <th width="10%">Total</th>
                                    <th width="10%" align="center">Action</th>
                                </tr>
                            </thead>
                            <hr />
                            <c:if test="${not empty data}">
                                <tbody>
                                    <c:forEach var="list" items="${data}" varStatus="count">
                                        <tr>
                                            <td>
                                                ${count.index + 1}
                                            </td>
                                            <td>
                                                <c:out value="${list.financialYear}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.groupAData}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.groupBData}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.groupCData}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.groupDData}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.grantInAid}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.total}"/>
                                            </td>
                                            <td>
                                                <c:if test="${list.status ne 'YES' or list.status ne 'COMPLETED'}">
                                                    <a href="addOfficeWiseSanctionedStrength.htm?aerId=<c:out value="${list.aerId}"/>">Edit</a> / 
                                                    <a href="GPCWiseSPCList.htm?aerId=<c:out value="${list.aerId}"/>">Update</a> /
                                                    <a href="displayReportList.htm?fy=<c:out value="${list.financialYear}"/>&aerId=<c:out value="${list.aerId}"/>">Report</a> /
                                                </c:if>
                                                <a href="scheduleIIReport.htm?aerId=<c:out value="${list.aerId}"/>" target="_blank">Schedule-II</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="submit" value="Add New" class="btn btn-primary"/>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>

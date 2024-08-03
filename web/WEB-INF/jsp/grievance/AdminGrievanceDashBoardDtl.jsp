<%-- 
    Document   : EmployeeGrievanceList
    Created on : Jan 6, 2018, 2:56:34 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>        
        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                            <td>Sl No</td>
                            <td>Category</td>
                            <td>Full Name</td>
                            <td>HRMS ID</td>
                            <td>Mobile</td>
                            <td>Grievance</td>
                            <td>Time</td>
                            <td>Pending At</td>
                            <td>Status</td>
                            <td colspan="2">Actions</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${grievncelist}" var="grievnce" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>${grievnce.category}-${grievnce.gid}</td>
                                <td>${grievnce.fullname}</td>
                                <td>${grievnce.hrmsid}</td>
                                <td>${grievnce.appmobile}</td>
                                <td>${grievnce.grievanceDetail}</td>
                                <td>${grievnce.grievanceTime}</td>
                                <td>${grievnce.pendingat}</td>
                                <td>
                                    <c:if test="${grievnce.isdisposed == 'Y'}">
                                        Disposed
                                    </c:if>
                                    <c:if test="${grievnce.isrejected == 'Y'}">
                                        Rejected
                                    </c:if>
                                    <c:if test="${grievnce.isforwarded == 'Y'}">
                                        Forwarded
                                    </c:if>
                                </td>
                                <td>
                                    <a href="adminGrievanceDetail.htm?gid=${grievnce.gid}"><img src="images/details.gif"></a>
                                </td>
                                <td>
                                    <a href="grievancecommunications.htm?gid=${grievnce.gid}"><img height="20" title="View Grievance History" src="images/comm.png"></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>

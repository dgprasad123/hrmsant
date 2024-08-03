<%-- 
    Document   : AdminGrievanceReport
    Created on : Feb 15, 2018, 3:43:37 PM
    Author     : Manas
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                            <td>Battalion Name</td>
                            <td>Received</td>
                            <td>Disposed</td>
                            <td>Rejected</td>
                            <td>Pending</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${BattalionwiseDashBoardDetail}" var="BattalionwiseDashBoard" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>${BattalionwiseDashBoard.battalionName}</td>
                                <td>${BattalionwiseDashBoard.totalGrievance}</td>
                                <td>${BattalionwiseDashBoard.disposedGrievance}</td>
                                <td>${BattalionwiseDashBoard.rejectedGrievance}</td>
                                <td>${BattalionwiseDashBoard.pendingGrievance}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>

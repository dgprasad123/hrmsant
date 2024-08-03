<%-- 
    Document   : EmployeeSuspensionList
    Created on : 4 Jul, 2018, 11:16:15 AM
    Author     : Surendra
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
            $(document).ready(function () {

            });
            function getMonth(monthStr) {
                var d = Date.parse(monthStr + "1, 2012");
                if (!isNaN(d)) {
                    return new Date(d).getMonth();
                }
                return -1;
            }
        </script>
    </head>
    <body>
        <form:form action="addSuspensionController.htm" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        HRMS ID: ${command.empid}
                    </div>

                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%">Notification Type</th>
                                <th width="10%">Date of Entry</th>
                                <th width="10%">Suspension Order No</th>
                                <th width="10%">Suspension Order Date</th>
                                <th width="7%">Suspension <br> Allowance in Rs. </th>
                                <th width="10%">Edit</th>
                                <th width="10%"> Action </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${suspensionList}" var="list">
                                <tr>
                                    <td>${list.nottficationType}</td>
                                    <td>${list.doe}</td>
                                    <td>${list.ordno}</td>
                                    <td>${list.ordDate}</td>
                                    <td>${list.txtallowance}</td>
                                    <td>
                                        <a href="editSuspension.htm?susId=${list.suspensionId}">Edit</a>
                                    </td>
                                    <td>
                                        <c:if test="${not empty list.suspensionId }">
                                            <a href="reinstatementEntry.htm?spId=${list.suspensionId}&empid=${command.empid}">Reinstatement</a>
                                        </c:if>
                                    </td>
                                     <td>
                                        
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:hidden class="form-control" path="empid" id="empid"/>
                    <input type="submit" name="action" value="Create Suspension" class="btn btn-default"/>
                    
                </div>
            </div>
        </div>
    </form:form>
</body>
</html>

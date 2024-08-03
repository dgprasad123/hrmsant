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
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="20%">Date of Entry</th>
                                <th width="30%">Notification Order No</th>
                                <th width="25%">Notification Order Date</th>
                                <th width="20%" align="center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${reappointmentlist}" var="reapp">
                                <tr>
                                    <td>${reapp.doe}</td>
                                    <td>${reapp.ordno}</td>
                                    <td>${reapp.ordDate}</td>
                                    <td>
                                        <a href="editReappointment.htm?notId=${reapp.notId}"/>Edit</a> | <a href="relieve.htm?notId=${reapp.notId}"/>Relieve</a>
                                    </td> 
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="NewReAppointment.htm" method="POST" commandName="reAppointmentForm">
                        <button type="submit" class="btn btn-default">New ReAppointment</button>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>

















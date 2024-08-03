<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">

        
            
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                    <div class="panel-heading">
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <h2 align="center" style="color:#008900;font-weight:bold;">Excel File has been imported successfully.</h2>
                            </div>
                        </div>
                    </div>
                    <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>HRMS ID</th>
                                            <th>Emp Name</th>
                                            <th>Days Worked</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${attendanceList}" var="aList" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${aList.empId}</td>
                                                <td>${aList.empName}</td>
                                                <td>${aList.daysWorked}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                </div>
        </div>
    </body>
</html>

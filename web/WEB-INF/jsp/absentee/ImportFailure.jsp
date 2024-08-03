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
            function confirmReset(month, year)
            {
                if(confirm("Are you sure you want to delete the below list?"))
                {
                    $.ajax({
                    url: 'DeleteAttendanceList.htm',
                    data: 'month='+month+'&year='+year,
                    type: 'get',
                    success: function(retVal) {
                        self.location = 'importAbsenteeExcel.htm';
                    }
                
                    }); 
                }
            }
            </script>
                
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                    <div class="panel-heading">
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <c:if test="${isExisting == 'true'}">
                            <div class="col-lg-12">
                                <h2 align="center" style="color:#FF0000;font-weight:bold;font-size:14pt;">
                                    You had already imported the Excel for this Month. Given below the List.<br /><br />
                                    <a href="javascript:void(0)" onclick="javascript: confirmReset(${month}, ${year})" style="color:#008900;">If you want to delete the below list and re upload click here.</a></h2>
                            </div>
                            </c:if>
                            <c:if test="${isExisting == 'false'}">
                            <div class="col-lg-12">
                                <h2 align="center" style="color:#FF0000;font-weight:bold;font-size:14pt;">
                                    No Excel have been uploaded for ${strMonth}, ${year}<br /><br />
                                    <a href="importAbsenteeExcel.htm" style="color:#008900;">Click here to Upload Excel.</a></h2>
                            </div>
                            </c:if>
                        </div>
                    </div>
                    
                </div>
            <c:if test="${isExisting == 'true'}">
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
            </c:if>
        </div>
    </body>
</html>

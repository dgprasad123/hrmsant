<%-- 
    Document   : viewEmployeeAttendance
    Created on : Nov 14, 2022, 11:59:15 AM
    Author     : BIBHUTI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getAttendance() {
                var periodfrom = $('#txtperiodFrom').val();
                var periodto = $('#txtperiodTo').val();
                if (periodfrom == "")
                {
                    alert("Please enter From Date");
                    $('#txtperiodFrom').focus();
                    return false;
                }
                if (periodto == "")
                {
                    alert("Please enter To Date");
                    $('#txtperiodTo').focus();
                    return false;
                }

            }
        </script>
    </head>
    <body>
        <form:form class="form-inline" action="viewEmployeeAttendance.htm" method="POST" commandName="empAttendanceBean">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" >
                        <div class="align-center" >
                            <div class="align-center" >
                                <h2 class="align-center" >
                                    Attendance Report of ${empAttendanceBean.empName}
                                </h2>
                            </div>
                        </div>
                    </div>
                    
                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="10%">Date</th>                                    
                                    <th width="20%">In Punch</th>
                                    <th width="10%">Out Punch</th> 
                                    
                                </tr>
                            </thead>
                            <tbody>
                        <c:forEach items="${empList}" var="empDetails" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>
                                    ${empDetails.attDate}
                                </td>                               
                                <td>
                                    ${empDetails.inTime}
                                </td>  
                                <td>
                                    ${empDetails.outTime}
                                </td>
                                 
                            </tr>
                        </c:forEach>
                    </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>



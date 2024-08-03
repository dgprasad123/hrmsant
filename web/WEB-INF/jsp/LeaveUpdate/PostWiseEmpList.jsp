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
            $(function () {
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
            function getLeave() {
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
        <form:form class="form-inline" action="postWiseEmpList.htm" method="POST" commandName="clupdateform">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="align-center">
                            <div class="align-center">
                                <h2 class="align-center">
                                    Employee Wise Leave Status
                                    <form:hidden path="offCode"/>
                                    <form:hidden path="postCode"/>
                                </h2>
                            </div>
                        </div>
                    </div>

                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="10%">Sl No</th>
                                    <th width="10%">HRMS Id</th>
                                    <th width="20%">Employee Name</th>
                                    <th width="20%" colspan="2">EL</th>
                                    <th width="20%"  colspan="2">CL</th>
                                </tr>
                                <tr>
                                    <th width="10%"></th>
                                    <th width="10%"></th>
                                    <th width="20%"></th>
                                    <th width="10%">Total Balance</th>
                                    <th width="10%">Available</th>
                                    <th width="10%">Total Balance</th>
                                    <th width="10%">Available</th>
                                    <th width="10%">Update Leave</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty empList}">
                                    <c:forEach items="${empList}" var="empList" varStatus="count">
                                        <tr>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>${empList.empid}</td>
                                            <td>${empList.empName}</td>
                                            <td>${empList.totalEl}</td>
                                            <td>${empList.totalElAvail}</td>
                                            <td>${empList.totalCl}</td>
                                            <td>${empList.totalClAvail}</td>
                                            <td>
                                                <a class="btn btn-primary" href="getLeaveList.htm?empId=${empList.empid}&postCode=${clupdateform.postCode}&offCode=${clupdateform.offCode}">Update Leave </a>  
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" value="Back" name="btn" class="btn btn-danger" />
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

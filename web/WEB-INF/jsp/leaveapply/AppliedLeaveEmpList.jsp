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
        <form:form class="form-inline" action="AppliedLeaveEmpList.htm" method="POST" commandName="leaveForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" >
                        <div class="align-center" >
                            <div class="align-center" >
                                <h2 class="align-center" >
                                    Leave Statement Report
                                </h2>
                            </div>
                        </div>
                    </div>
                    <div class="row" >
                        <div class="col-lg-1"  style="left:50px;">From Date:</div>

                        <div class="col-lg-2" >

                            <div style="left:50px;" class='input-group date' id='processDate'>

                                <form:input class="form-control" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </div>
                        <div class="col-lg-1" style="left:50px;" >To Date:</div>
                        <div class="col-lg-2" >
                            <div class='input-group date' id='processDate'>
                                <form:input class="form-control" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </div>
                        <div class="col-lg-2" >
                            <input type="submit" name="action" value="Ok" class="btn btn-success" onclick="return getLeave()"/>
                        </div>
                    </div>
                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="10%">HRMS ID</th>
                                    <th width="15%">Name</th>
                                    <th width="20%">Designation</th>
                                    <th width="10%">Leave Type</th>
                                    <th width="10%">From Date</th>
                                    <th width="10%">To Date</th>
                                    <th width="5%">Status</th>
                                    <th width="5%">No Of<br>Days</th>
                                     <th width="10%">Initiated<br>On</th>
                                    <th width="10%">Leave<br>Balance<br>As on Date</th>
                                    <th width="10%">Download <br>Sanction Order</th>
                                    <th width="5%">View</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty emplist}">
                                    <c:forEach items="${emplist}" var="emplist" varStatus="count">
                                        <tr>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>
                                                <c:out value="${emplist.empId}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.applicantName}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.post}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.sltleaveType}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.txtperiodFrom}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.txtperiodTo}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.status}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.daysdiff}"/>
                                            </td>
                                             <td>
                                                <c:out value="${emplist.initiatedOn}"/>
                                            </td>
                                            <td>
                                                <c:out value="${emplist.leaveBalance}"/>
                                            </td>
                                            <td>
                                                <c:if test = "${emplist.statusId == '4' || emplist.statusId == '5'}"> 
                                                    <a href="viewSanctionLeaveOrder.htm?empId=${emplist.empId}&taskId=${emplist.taskId}&spc=${emplist.hidSpcCode}" target="_blank"><img border="0" alt="PDF" src="images/pdf.png" height="20">Download</a> 
                                                    </c:if>    
                                            </td>
                                            <td>
                                                <a href="leaveViewDataOE.htm?&taskId=${emplist.taskId}">View</a> 
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
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

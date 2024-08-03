

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <link rel="stylesheet" type="text/css" href="css/select.bootstrap.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" src="js/dataTables.select.min.js" type="text/javascript"></script>


        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#leavePeriodFrom').datetimepicker({
                    format: 'DD-MM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#leavePeriodTo').datetimepicker({
                    format: 'DD-MM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                //$('#example').DataTable();
                var table = $('#example').DataTable({
                    scrollY: "500px",
                    scrollCollapse: true,
                    paging: true,
                    responsive: true,
                    select: true

                });
                $('#example tbody').on('click', 'tr', function () {
                    var rowData = table.row(this).data();
                    $("#hidempid").val(rowData[0]);
                });

            });
            function leaveStatus() {
               var fromdate = document.getElementById("leavePeriodFrom");
                if (fromdate.value == "")
                {
                    alert("Please enter from date.");
                    fromdate.focus();
                    return false;
                }
                var todate = document.getElementById("leavePeriodTo");
                if (todate.value == "")
                {
                    alert("Please enter to date.");
                    todate.focus();
                    return false;
                }
            }


        </script>
    </head>
    <body class="boxed-layout pt-40 pb-40 pt-sm-0">
        <form:form action="leavestatus.htm" method="POST" commandName="leaveForm">
            <div class="main-content">
                <input type="hidden" id="hidempid" name="hidempid"/>
            </div>
            <div data-spy="scroll" data-target="#list-example" data-offset="0" class="scrollspy-example">
                <table   id="example" class="table table-striped" width="100%" cellspacing="0">
                    <tr height="40px">
                        <td>From date:</td>
                        <td>
                            <div class='input-group date' style="width:40%;" id='processDate'>
                                <form:input class="form-control"  id="leavePeriodFrom" path="leavePeriodFrom" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </td>
                        <td>To date:</td>
                        <td>
                            <div class='input-group date' style="width:40%;" id='processDate'>
                                <form:input class="form-control"  id="leavePeriodTo" path="leavePeriodTo" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>
                        </td>
                        <td style="alignment-adjust: left">
                            <div class="btn-group btn-group-sm" style="margin-bottom:2px;width:40%;">
                                <input type="submit" name="Status" value="Status" class="btn btn-primary" onclick="return leaveStatus();" /> 

                            </div>

                        </td>
                    </tr>

                </table>


            </div>
            <div data-spy="scroll" data-target="#list-example" data-offset="0" class="scrollspy-example">
                <table id="example" class="table table-striped" width="100%" cellspacing="0">
                    <thead class="table-success">
                        <tr>
                            <th>HRMS ID</th>
                            <th>Employee Name</th>
                            <th>Post</th>
                            <th>From Date</th>
                            <th>To Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach  var="emplist" items="${empLeaveStatusList}">
                            <tr>
                                <td>${emplist.empId}</td>
                                <td>${emplist.intitals}&nbsp;${emplist.fname}&nbsp;${emplist.mname}&nbsp;${emplist.lname}</td>
                                <td>${emplist.post}</td>
                                <td>${emplist.leavePeriodFrom}</td>
                                <td>${emplist.leavePeriodTo}</td>
                            </tr>  
                        </c:forEach>
                </table>
            </div>
        </form:form>
    </body>
</html>

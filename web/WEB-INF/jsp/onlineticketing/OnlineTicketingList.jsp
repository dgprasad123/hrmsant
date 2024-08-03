<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $('#example').DataTable({
                    "order": [[0, "desc"]]
                });

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
        </script>
    </head>
    <body>
        <form:form action="onlineticketlist.htm" commandName="onlineticketing"  method="GET">
            <div class="main-content">
                <div >
                    <div class="float-left">
                        <input type="submit" value="New Ticket" name="newticket" class="btn btn-primary"/>
                        <c:if test="${isDDO eq 'Y' || isEstablishmentSection eq 'Y'}">
                            <a href="onlineticketlistbyemployee.htm"> <strong style="color:red"><input type="button" value="Ticket raised by Me" class="btn btn-warning"/></strong></a> 
                            <a href="onlineticketlistbyoffice.htm"> <strong style="color:red"> <input type="button" value="Ticket raised by Office" class="btn btn-warning"/></strong></a>
                        </c:if>
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
                    <input type="submit" name="action" value="Search" class="btn btn-success"/>
                </div>
            </div> 
            <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                <thead>
                    <tr>
                        <th style='width:10%'>Ticket Id</th>
                        <th style='width:15%'>Create Date</th>
                        <th style='width:5%'>Category</th>
                        <th style='width:40%'>Message</th>
                        <th style='width:20%'>Office Name</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                        <tr>
                            <td><c:out value="${ticketlist.fticketid}"/></td>
                            <td><c:out value="${ticketlist.createdDateTimeString}"/></td>
                            <td><c:out value="${ticketlist.topicName}"/></td>
                            <td><c:out value="${ticketlist.message}"/></td>
                            <td><c:out value="${ticketlist.offname}"/></td>
                            <c:if test="${not empty ticketlist.status && (ticketlist.status eq 'Open' || ticketlist.status eq 'Data Insufficient')}">
                                <td>
                                    <strong style="color:red"><c:out value="${ticketlist.status}"/></strong>
                                </td>
                                <td>                                   
                                    <a href="ticketEmpActionDc.htm?ticketId=${ticketlist.ticketId}"> <strong style="color:red"> Action Required </strong></a>

                                </td>
                            </c:if>
                            <c:if test="${empty ticketlist.status || ticketlist.status ne 'Open' && ticketlist.status ne 'Data Insufficient'}">
                                <td>
                                    <c:out value="${ticketlist.status}"/>
                                </td>
                                <td>
                                    <a href="ticketEmpActionDc.htm?ticketId=${ticketlist.ticketId}">View</a>

                                </td>
                            </c:if>

                        </tr>
                    </c:forEach>                    
            </table>

        </form:form>
    </body>
</html>

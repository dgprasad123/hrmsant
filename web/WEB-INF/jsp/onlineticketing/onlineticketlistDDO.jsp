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
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>

        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#example').DataTable( {
                    "pageLength": 50,
                   // "order": [[ 5, "desc" ]]
                  // "ordering": false;
                  "order": []
                } );
            });
            function archieve_report(){
                window.location="onlineticketlistResolved.htm";
            }
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
        </script>

    </head>
    <body>
        <div id="wrapper">


                      <form:form autocomplete="off" role="form" action="#" commandName="onlineticketing" method="GET">
                                  
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


                                </form:form>  

                                <hr/>    
                    <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th style='width:10%'>Ticket Id</th>
                                <th  style='width:10%'>Created Date</th>
                                <th  style='width:10%'>Category</th>
                                <th  style='width:40%'>Message</th>                                
                                <th  style='width:15%'>Office Name</th>
                                 <th style='width:5%'>DDO Code</th>
                                <th style='width:5%'>Status</th>
                                 <th style='width:5%'>Assigned Id</th>
                                <th style='width:5%'>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                                <tr>
                                    <td nowrap>
                                        <c:out value="${ticketlist.fticketid}"/>
                                        <c:if test = "${empty ticketlist.iread && ticketlist.iread ne 'Y' }">
                                            <img src="images/unread.png" width="18px"/>
                                        </c:if>
                                    
                                    </td>
                                    <td><c:out value="${ticketlist.createdDateTimeString}"/></td>
                                    <td><c:out value="${ticketlist.topicName}"/></td>
                                    <td><c:out value="${ticketlist.message}"/></td>
                                  
                                    <td><c:out value="${ticketlist.offname}"/></td>
                                     <td><strong style='color:blue'><c:out value="${ticketlist.ddoCode}"/></strong></td>
                                    <td><c:out value="${ticketlist.status}"/></td>
                                     <td><strong style='color:red'><c:out value="${ticketlist.assignedToUserId}"/></strong></td>
                                    <td><a href="ticketActionDc.htm?ticketId=${ticketlist.ticketId}&encticketid=${ticketlist.encticketid}">Action</a></td>
                                </tr>
                            </c:forEach>                    
                    </table>




                </div>
            </div>


    </body>
</html>

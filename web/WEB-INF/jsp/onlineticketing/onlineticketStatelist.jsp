<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
                   <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           


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
                    "order": [[ 0, "desc" ]]
                } );
            });
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
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Online Ticket System 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <br/>
                    <br/>
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
                    <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th style='width:5%'>Ticket Id</th>
                                 <th style='width:10%'>Created Date</th>
                                <th style='width:5%'>Category</th>
                                <th style='width:40%'>Message</th>
                                <th style='width:5%'>District</th>
                                <th style='width:15%'>Office Name</th>
                                 <th style='width:5%'>DDO Code</th>
                                <th style='width:5%'>Status</th>
                                 <th style='width:5%'>Assigned Id</th>
                                <th style='width:5%'>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                                <tr>
                                    <td><c:out value="${ticketlist.fticketid}"/></td>
                                    <td><c:out value="${ticketlist.createdDateTimeString}"/></td>
                                    <td><c:out value="${ticketlist.topicName}"/></td>
                                    <td><c:out value="${ticketlist.message}"/></td>
                                    <td><strong style='color:green'>
                                             <c:if test="${not empty ticketlist.distCode}">
                                              <c:out value="${ticketlist.distCode}"/>
                                             </c:if>
                                         <c:if test="${ empty ticketlist.distCode}">
                                              Secretariat 
                                             </c:if>
                                    </strong>
                                    </td>
                                    <td><c:out value="${ticketlist.offname}"/></td>
                                    <td><strong style='color:blue'><c:out value="${ticketlist.ddoCode}"/></strong></td>
                                    <td><c:out value="${ticketlist.status}"/></td>
                                    <td><strong style='color:red'><c:out value="${ticketlist.assignedToUserId}"/></strong></td>
                                    <td><a href="ticketActionState.htm?ticketId=${ticketlist.ticketId}">Action</a></td>
                                </tr>
                            </c:forEach>                    
                    </table>




                </div>
            </div>
        </div>

    </body>
</html>

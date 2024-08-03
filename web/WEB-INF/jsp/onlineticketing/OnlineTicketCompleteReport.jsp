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
            $(document).ready(function() {
                $('#example').DataTable({
                    "pageLength": 50,
                    // "order": [[ 5, "desc" ]]
                    // "ordering": false;
                    "order": []
                });
            });
            function archieve_report() {
                window.location = "onlineticketlistResolved.htm";
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
                                    <i class="fa fa-file"></i> Resolved & Closed Ticket List
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <br/>
                    <br/>
                    <form:form action="GetOnlineTicketReport.htm" commandName="onlineticketing">

                        <div class="row" >
                            <div class="col-lg-1"  style="left:50px;">Select Month:</div>

                            <div class="col-lg-2" >

                                <div style="left:50px;" class='input-group date' id='processDate'>
                                    <form:select path="month" class="form-control">
                                        <form:option value="1">JANUARY</form:option>
                                        <form:option value="2">FEBRUARY</form:option>
                                        <form:option value="3">MARCH</form:option>
                                        <form:option value="4">APRIL</form:option>
                                        <form:option value="5">MAY</form:option>
                                        <form:option value="6">JUNE</form:option>
                                        <form:option value="7">JULY</form:option>
                                        <form:option value="8">AUGUST</form:option>
                                        <form:option value="9">SEPTEMBER</form:option>
                                        <form:option value="10">OCTOBER</form:option>
                                        <form:option value="11">NOVEMBER</form:option>
                                        <form:option value="12">DECEMBER</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="col-lg-1" style="left:50px;" >Select Year:</div>
                            <div class="col-lg-2" >
                                <div class='input-group date' id='processDate'>
                                    <form:select path="year" class="form-control">

                                        <form:option value="2020" >  2020 </form:option>
                                        <form:option value="2021"> 2021 </form:option>
                                        <form:option value="2022" > 2022 </form:option>
                                        <form:option value="2023"> 2023 </form:option>
                                        <form:option value="2024"> 2024 </form:option>
                                    </form:select>
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
                                <th style="width:7%">Ticket Id</th>
                                <th style="width:13%">Create Date</th>
                                <th style="width:10%">Category</th>
                                <th style="width:40%">Message</th>
                                <th style="width:15%">Office Name</th>
                                <th style="width:5%">DDO Code</th>
                                <th style="width:5%">Status</th>
                                <th style="width:5%">&nbsp;</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:if test="${not empty onlineticketreportlist}">
                                <c:forEach items="${onlineticketreportlist}" var="list">
                                    <tr>
                                        <td>
                                            <c:out value="${list.ticketId}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.createdDateTimeString}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.topicName}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.message}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.offname}"/>
                                        </td>
                                        <td><strong style='color:blue'><c:out value="${list.ddoCode}"/></strong></td>
                                        <td>
                                            <c:out value="${list.status}"/>
                                        </td>
                                        <td><a href="viewActionDetails.htm?ticketId=${list.ticketId}" class="btn btn-info">View Action Details</a></td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>




                </div>
            </div>
        </div>

    </body>
</html>

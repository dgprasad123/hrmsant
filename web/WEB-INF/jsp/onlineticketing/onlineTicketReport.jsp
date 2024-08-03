<%-- 
    Document   : onlineTicketReport
    Created on : Apr 12, 2019, 1:19:21 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $('#example').DataTable({
                    "order": [[0, "desc"]]
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
                                    <i class="fa fa-file"></i> Report of Online Tickets
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form:form action="onlineTicketReport.htm" commandName="OnlineTicketing" method="POST">                      

                        <table class="table table-striped table-bordered" width="100%" border="5" cellspacing="0">

                            <tr>
                            <label class="control-label col-sm-1">District:</label>
                            <div class="col-sm-3">
                                <div class="form-group" >
                                    <form:select class="form-control" id="sltdistName" path="sltdistName">
                                            <form:option value="" >---Select District---</form:option>
                                            <form:options items="${distList}" itemLabel="distName" itemValue="distCode"/>
                                    </form:select>
                                </div>
                            </div>
                            <label class="control-label col-sm-1">Month:</label>
                            <div class="col-sm-2">
                                <form:select class="form-control" path="month">
                                    <form:option value="">Select</form:option>
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
                            <label class="control-label col-sm-1">Year:</label>
                            <div class="col-sm-2">
                                <form:select class="form-control" path="year">
                                    <form:option value="">Select</form:option>                                                
                                    <form:option value="2018">2018</form:option>
                                    <form:option value="2019">2019</form:option>
                                     <form:option value="2020">2020</form:option>
                                      <form:option value="2021">2021</form:option>
                                      <form:option value="2022">2022</form:option>
                                      <form:option value="2023">2023</form:option>
                                       <form:option value="2024">2024</form:option>
                                </form:select>
                            </div>

                            <div class="col-sm-2">
                                <button type="submit" class="btn btn-primary"  style="width:130px" name="OK">OK</button>
                            </div>
                            </tr>
                        </table>
                        <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th style='width:5%'>Ticket Id</th>
                                    <th style='width:10%'>Created Date</th>
                                    <th style='width:5%'>Category</th>
                                    <th style='width:40%'>Message</th>
                                    <th style='width:5%'>District</th>
                                    <th style='width:20%'>Office Name</th>
                                    <th style='width:5%'>Status</th>
                                    <th style='width:5%'>Assigned Id</th>
                                    <th style='width:5%'>Pending From</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                                    <tr>
                                        <td><c:out value="${ticketlist.fticketid}"/></td>
                                        <td><c:out value="${ticketlist.createdDateTime}"/></td>
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
                                        <td><c:out value="${ticketlist.status}"/></td>
                                        <td><strong style='color:red'><c:out value="${ticketlist.assignedToUserId}"/></strong></td>
                                        <td><c:out value="${ticketlist.pendingdays}"/></td>
                                        <%--<td><a href="ticketActionState.htm?ticketId=${ticketlist.ticketId}">Action</a></td>--%>
                                    </tr>
                                </c:forEach>                    
                        </table>


                    </form:form>

                </div>
            </div>
        </div>

    </body>
</html>

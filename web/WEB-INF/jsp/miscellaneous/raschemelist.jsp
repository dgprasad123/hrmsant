<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<script type='text/javascript'>
    function delete_data(ids) {
        var con = confirm("Do you want to delete this information");
        if (con) {
            window.location = "deleteAppointmentStatus.htm?taskId=" + ids;

        }
    }
</script>  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Rehabilitation List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newRascheme.htm">New Rehabilitation</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Rehabilitation List</h2>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-striped">
                                    <thead style='background-color:black;color:white;font-size:14px'>
                                        <tr>
                                            <th>Month</th>
                                            <th>Year</th>
                                            <th> No of RA Cases till last month</th>
                                            <th>No of cases instituted during the month</th>
                                            <th>Total No.of cases under R.A Scheme </th>
                                            <th>No. of cases considered for appointment during the month</th>
                                            <th>No. of cases considered and rejected during the month</th>
                                            <th>Total no of cases disposed during the month</th>
                                            <th>Total no of cases considered for appointment till last month</th>
                                              <th>Total no of cases considered and rejected till last month</th>
                                            <th>Total no of cases disposed till last month</th>
                                            <th>Cumulative no of cases disposed</th>
                                            <th>Balance no of cases pending</th>
                                            <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${raschemeList}" var="rascheme">
                                            <tr>
                                                <td>${rascheme.month}</td>
                                                <td>${rascheme.year}</td>
                                                <td>${rascheme.lastmonthPending}</td>
                                                <td>${rascheme.institutedCase}</td>
                                                <td>${rascheme.totalCase}</td>
                                                <td>${rascheme.totalAppointment}</td>
                                                <td>${rascheme.totalRejected}</td>				
                                                <td>${rascheme.disposedcase}</td>
                                                 <td>${rascheme.totalAppoLastmonth}</td>				
                                                <td>${rascheme.totalRejLastmonth}</td>
                                                <td>${rascheme.disposedlastMonth}</td>
                                                <td>${rascheme.totalCleared}</td>
                                                <td>${rascheme.totalPending}</td>				

                                                <td nowrap><a href='editAppointmentStatus.htm?Taskid=${rascheme.raSchemeId}'><img src="images/action.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                    <a href="#" onclick="delete_data(${rascheme.raSchemeId})"><img src="images/delete_icon.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>	
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
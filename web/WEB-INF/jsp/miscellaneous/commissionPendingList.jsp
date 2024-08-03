<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
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
            <jsp:include page="../tab/commissionmenu.jsp"/>                
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
                                    <i class="fa fa-file"></i>Monthly Activity Report </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="CommisionPending.htm">Add New Pending List</a>
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Monthly Activity Report</h2>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                            <th>Month</th>
                                            <th>Year</th>
                                            <th>Department</th>
                                            <th>Post</th>
                                            <th>Number<br/>Of<br/>Vacancy</th>
                                            <th>Date Of <br/>Receipt of <br/>Requisition</th>
                                            <th>Advertisement <br/>No <br/>Date</th>
                                            <th>Date of <br/>Preliminary<br/> Examination</th>
                                            <th>Date of <br/>Written <br/>Examination</th>     
                                            <th>Date of <br/>Viva-Voce/Skill<br/> Test</th>
                                            <th>Date Of <br/>Publication Of<br/> Final Result</th>
                                            <th>Date Of <br/>Sponsoring of <br/>Candidates</th>
                                            <th>Court Cases If Any</th>
                                            <th>Remarks</th>
                                            <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cpendingList}" var="cpendingList">
                                            <tr scope="row">

                                                <td>${cpendingList.month}</td>                                                
                                                <td>${cpendingList.year}</td>                                                
                                                <td>${cpendingList.departmentId}</td>                                                
                                                <td>${cpendingList.postId}</td>
                                                <td>${cpendingList.noofVan}</td>
                                                <td>${cpendingList.reqDate}</td>
                                                <td>${cpendingList.advNo}<br/> ${cpendingList.advDate}</td>  
                                                 <td>${cpendingList.examdate}</td>  
                                                  <td>${cpendingList.writtendate}</td>  
                                                  <td>${cpendingList.vivavoceDate}</td>  
                                                  <td>${cpendingList.finalResultDate}</td> 
                                                   <td>${cpendingList.sponsoringDate}</td>  
                                                  <td>${cpendingList.coursecaseDetails}</td>  
                                                  <td>${cpendingList.remarks}</td>
                                                <td><a href='editcommissionpending.htm?pendingId=${cpendingList.pendingId}&deptid=${cpendingList.deptId}'><img src="images/user_plus_icon.png" alt="Saction Order" width="24px" title="Add Candidate List" /></a></td>
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
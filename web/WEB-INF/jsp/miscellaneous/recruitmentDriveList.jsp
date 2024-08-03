<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<script type='text/javascript'>
    function delete_data(ids){
     var con=confirm("Do you want to delete this information");
     if(con){
         window.location="deleteRecruitDriveData.htm?taskId="+ids;
         
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
                                    <i class="fa fa-file"></i> Recruitment List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newRecruitment.htm">New Recruitment</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Recruitment List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead style='background-color:black;color:white;font-size:14px'>
                                        <tr>
                                            <th>Month & Year</th>
                                           
                                            <th>Post</th>
                                            <th>Number of vacancy </th>
                                            <th>Whether Requisition Sent to the recruiting Agency</th>                  
                                            <th>Letter No</th>
                                            <th>Letter Date</th>
                                            <th>Number of posts for which Requisition sent</th>
                                            <th>Name of the Recruiting Agency</th>
                                            <th>Advertisement Status</th>
                                            <th>Examination Conducted</th>
                                            <th>Result Published</th>
                                            <th>Remarks</th>
                                             <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${recruitmentList}" var="recruitment">
                                        <tr>
                                            <td>${recruitment.year}</td>
                                          
                                            <td>${recruitment.postName}</td>
                                            <td>${recruitment.totalVacanct}</td>
                                            <td>${recruitment.reqSentAgentStatus}</td>
                                            <td>${recruitment.sentLetterNo}</td>				
                                            <td>${recruitment.sentDate}</td>
                                            <td>${recruitment.postReqSent}</td>
                                            <td>${recruitment.recruitAgency}</td>
                                            <td>${recruitment.advDetails}</td>
                                            <td>${recruitment.examDetails}</td>
                                            <td>${recruitment.resultPublished}</td>				
                                            <td>${recruitment.remarks}</td>
                                             <td><a href='editRecruitDrive.htm?Taskid=${recruitment.recruitDriveId}'><img src="images/action.png" alt="Saction Order" height="20px" width="20px" /></a>
                                              <a href="#" onclick="delete_data(${recruitment.recruitDriveId})"><img src="images/delete_icon.png" alt="Saction Order" height="20px" width="20px" /></a>
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
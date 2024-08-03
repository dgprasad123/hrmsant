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
         window.location="deleteEmpTrainingData.htm?taskId="+ids;
         
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
                                    <i class="fa fa-file"></i> Module List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newTraining.htm">New Training</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Training List</h2>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                          <th>Month</th>
                                          <th>Year</th>
                                          
                                            <th>Post</th>
                                            <th>Total Number of Candidates appointed  </th>
                                            <th>Date of Appointment</th>
                                            <th>Number of Employee completed induction training</th>
                                            <th>Place of training for induction training</th>
                                            <th>Duration for induction training</th>
                                            <th>Number of Employee completed job training</th>
                                            <th>Place of training for job training</th>
                                            <th>Duration for job training</th>
                                            <th>Total Number of Employee Training out of Candidates Appointed</th>
                                            <th>Remarks</th>
                                            <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${trainingList}" var="training">
                                        <tr>
                                           <td>${training.month}</td>
                                            <td>${training.year}</td>
                                           
                                            <td>${training.postName}</td>
                                            <td>${training.totalAppointment}</td>
                                            <td>${training.appointmentDate}</td>
                                            <td>${training.empIndvTrained}</td>				
                                            <td>${training.trainingPlace}</td>
                                            <td>${training.trainingDuration}</td>
                                            <td>${training.empJobTrained}</td>
                                            <td>${training.jobPlaceTraining}</td>
                                            <td>${training.jobDuration}</td>
                                            <td>${training.totalEmpTrained}</td>				
                                            <td>${training.remarks}</td>
                                             <td><a href='editEmpTraining.htm?Taskid=${training.trainingId}'><img src="images/action.png" alt="Saction Order" height="20px" width="20px" /></a>
                                               <a href="#" onclick="delete_data(${training.trainingId})"><img src="images/delete_icon.png" alt="Saction Order" height="20px" width="20px" /></a>
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
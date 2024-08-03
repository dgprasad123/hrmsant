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
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

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
                                    <i class="fa fa-file"></i> Cadre 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Employee ER SHEET 
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <h3 class="alert alert-success">Complete Biodata</h3>
                            <div class="panel panel-primary">
                                <div class="panel-heading">Profile Information</div>
                                <div class="panel-body">

                                    <div class=" col-md-6 col-lg-6 " > 
                                      
                                        <table class="table table-user-information" align="center">
                                            <tbody>
                                                <tr>
                                                    <td>Name: </td>
                                                    <td>${EmployeeProfile.empName}</td>
                                                </tr>
                                                <tr>
                                                    <td>DOB:</td>
                                                    <td>${EmployeeProfile.dob}</td>
                                                </tr>
                                                <tr>
                                                    <td>Gender:</td>
                                                    <td>${EmployeeProfile.gender}</td>
                                                </tr>
                                                <tr>
                                                    <td>GPF NO:</td>
                                                    <td>${EmployeeProfile.gpfno}</td>
                                                </tr>
                                                <tr>
                                                    <td>Source of Recruitment:</td>
                                                    <td>${EmployeeProfile.recsource}</td>
                                                </tr>
                                                <tr>
                                                    <td>Service/ Cadre/ Allotment Year:</td>
                                                    <td>${EmployeeProfile.cadreId}/${EmployeeProfile.cardeallotmentyear}</td>
                                                </tr>
                                                <tr>
                                                    <td>Home Town:</td>
                                                    <td>${EmployeeProfile.homeTown}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                         <table>
                                        <tr>
                                            <td><a href="profile.htm" target="_blank" class="btn btn-primary active" >Edit New Profile</a></td>
                                        </tr>    
                                    </table>        
                                    </div>    

                                </div>
                            </div>

                            <div class="panel panel-primary">
                                <div class="panel-heading">Educational Qualifications</div>
                                <div class="panel-body">

                                    <table class="table table-bordered">
                                        <thead>
                                            <tr class="bg-primary text-white">
                                                <th>#</th>
                                                <th>Qualification</th>
                                                <th>Faculty</th>
                                                <th>Year of Pass</th>
                                                <th>Subject</th>
                                                <th>Institute</th>
                                                <th>Board</th>
                                                <th>&nbsp;</th>

                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${educations}" var="education" varStatus="cnt">
                                                <tr>
                                                    <th scope="row">${cnt.index+1}</th>
                                                    <td>${education.qualification}</td>
                                                    <td>${education.faculty}</td>
                                                    <td>${education.yearofpass}</td>
                                                    <td>${education.subject}</td>
                                                    <td>${education.institute}</td>
                                                    <td>${education.board}</td>
                                                    <td><a href="education.htm" target='_blank'>Edit</a></td>

                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                     <table>
                                        <tr>
                                            <td><a href="education.htm" target="_blank" class="btn btn-primary active" >Add New Education</a></td>
                                        </tr>    
                                    </table>
                                </div>
                            </div> 

                            <!--------------------- Education Details End ---------------------->                      
                            <div class="panel panel-primary">
                                <div class="panel-heading">Training Details</div>
                                <div class="panel-body">

                                    <table class="table table-bordered">
                                        <thead>
                                            <tr class="bg-primary text-white">
                                                <th>#</th>
                                                <th>Title</th>
                                                <th>Place</th>                                                
                                                <th>Note</th>
                                                <th>&nbsp;</th>


                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${trainings}" var="training" varStatus="cnt">
                                                <tr>
                                                    <th scope="row">${cnt.index+1}</th>
                                                    <td>${training.title}</td>
                                                    <td>${training.place}</td>
                                                    <td>${training.note}</td>
                                                    <td><a href="gettrainingdata.htm?trainId=${training.trainid}&empid=${EmpId}" target='_blank'>Edit</a></td>


                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <table>
                                        <tr>
                                            <td><a href="trainingschedulelist.htm" target="_blank" class="btn btn-primary active" >Add New Training</a></td>
                                        </tr>    
                                    </table>
                                </div>
                            </div> 
                            <!------------------------- Training Details -------------------------------->
                            <!--------------------- Experience Details ---------------------->                      
                            <div class="panel panel-primary">
                                <div class="panel-heading">Experience Details</div>
                                <div class="panel-body">

                                    <table class="table table-bordered">
                                        <thead>
                                            <tr class="bg-primary text-white">
                                                <th>#</th>
                                                <th>Order No</th>
                                                <th>Order Date</th>                                                
                                                <th>Designation</th>
                                                <th>Department</th>                                              
                                                <th>Period From </th>
                                                <th>Period To </th>
                                              

                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${transfer}" var="transfer" varStatus="cnt">
                                                <tr>
                                                    <th scope="row">${cnt.index+1}</th>
                                                    <td>${transfer.orderno}</td>
                                                    <td>${transfer.orderdate}</td>
                                                    <td>${transfer.joinspn}</td>
                                                    <td>${transfer.departmentName}</td>

                                                    <td>${transfer.joindate}</td>
                                                    <td>${transfer.relievedate}</td>
                                                    
                                                    


                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <table>
                                        <tr>
                                            <td><a href="TransferList.htm" target="_blank" class="btn btn-primary active" >Add New Joining</a></td>
                                        </tr>    
                                    </table>  

                                </div>
                            </div> 
                            <!------------------------- Training Details -------------------------------->


                            <!--------------------- Deputation Details ---------------------->                      
                            <div class="panel panel-primary">
                                <div class="panel-heading">Deputation Details</div>
                                <div class="panel-body">

                                    <table class="table table-bordered">
                                        <thead>
                                            <tr class="bg-primary text-white">
                                                <th>#</th>
                                                <th >Posting Office</th>
                                                <th>Posting Designation</th>
                                                <th >Period From</th>
                                                <th >Period To</th>
                                                <th>&nbsp;</th>


                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${depuList}" var="dlist">
                                                <tr>
                                                    <th scope="row">${cnt.index+1}</th>    
                                                    <td>${dlist.postingOffice}</td>
                                                    <td>${dlist.postingPost}</td>
                                                    <td>${dlist.periodFrom}</td>
                                                    <td>${dlist.periodTo}</td>
                                                    <td><a href="editDeputation.htm?notId=${dlist.notId}" target="_blank">Edit</a></td>

                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                   <table>
                                        <tr>
                                            <td><a href="DeputationList.htm" target="_blank" class="btn btn-primary active" >Add New Deputation</a></td>
                                        </tr>    
                                    </table>      
                                </div>
                            </div> 
                            <!------------------------- Training Details -------------------------------->
                    <div style="text-align:center;padding:5px">
                        <a href="getRollWiseLinkDC.htm?nodeID=${EmpId}" class="btn btn-primary active" target="_blank"><strong>Edit ERSheet</strong></a>
                       

                    </div> 
                        </div>
                    </div>




                </div>
            </div>
        </div>

        <!-- Modal -->


    </body>
</html>

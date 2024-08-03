<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ER Sheet</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script> 

    </head>
    <body>
        <div class="container-fluid" style="padding-top: 5px;">
           <form action="commentERSheet.htm" method="POST" commandName="commentERSheet" >
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
                                        <tr>
                                           <td>Comments:</td>
                                           <td><textarea name="profile_comment" class="form-control"  >${Ersheet.profile_comment}</textarea></td>
                                        </tr>
                                    </tbody>
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

                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <table class="table table-bordered">
                            <tr>
                                 <td>Comments:</td>
                                 <td><textarea name="education_comment" class="form-control"  >${Ersheet.education_comment}</textarea></td>
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


                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${trainings}" var="training" varStatus="cnt">
                                        <tr>
                                            <th scope="row">${cnt.index+1}</th>
                                            <td>${training.title}</td>
                                            <td>${training.place}</td>
                                            <td>${training.note}</td>


                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                             <table class="table table-bordered">
                            <tr>
                                 <td>Comments:</td>
                                 <td><textarea name="training_comment" class="form-control"  >${Ersheet.training_comment}</textarea></td>
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
                                            <td>${transfer.joinspn}</td>
                                            <td>${transfer.departmentName}</td>

                                            <td>${transfer.joindate}</td>
                                            <td>${transfer.relievedate}</td>


                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <table class="table table-bordered">
                            <tr>
                                 <td>Comments:</td>
                                 <td><textarea name="experience_comment" class="form-control"  >${Ersheet.experience_comment}</textarea></td>
                              </tr>
                            </table> 
                        </div>
                    </div> 
                      <!--------------------- Education Details End ---------------------->                      
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


                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${depuList}" var="dlist">
                                    <tr>
                                       
                                        <td>${dlist.postingOffice}</td>
                                        <td>${dlist.postingPost}</td>
                                        <td>${dlist.periodFrom}</td>
                                        <td>${dlist.periodTo}</td>
                                   
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                             <table class="table table-bordered">
                            <tr>
                                 <td>Comments:</td>
                                 <td><textarea name="deputation_comment" class="form-control"  >${Ersheet.deputation_comment}</textarea></td>
                              </tr>
                            </table> 
                        </div>
                    </div> 
                    <!------------------------- Training Details -------------------------------->         
                    <!------------------------- Training Details -------------------------------->
                    <div style="text-align:center;padding:5px">
                        <input class="easyui-linkbutton" type="submit" name="Save" value="Save" />
                        <input class="easyui-linkbutton" type="reset" name="reset" value="Clear" />

                    </div>   
                    <form>   
                </div>
                </body>
                </html>

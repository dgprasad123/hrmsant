<%-- 
    Document   : EmployeeProfileDetailPage
    Created on : 30 Mar, 2019, 11:05:31 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
            @media (min-width: 800px) {
                .modal-dialog {
                    width: 600;
                    margin: 30px auto;
                }
                .modal-content {
                    -webkit-box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                    box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                }
                .modal-sm {
                    width: 300px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;"></div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <%--<jsp:include page="../tab/hrmsadminmenu.jsp"/> --%>       
                        <%--<div id="page-wrapper">--%> 


                        <div class="container-fluid" >
                            <div style="text-align:center;border:2px;font-weight:bold;font-size: 20px ">
                                EMPLOYEE DETAIL PROFILE DATA 
                            </div>
                            <div class="row bg-primary" style="padding: 5px;">Personal Information</div>


                            <div class="row">
                                <label class="control-label col-sm-4">&emsp; Employee Name:  </label> 
                                <div class="col-sm-4" >
                                    ${EmpData.fname}&nbsp;${EmpData.mname}&nbsp;${EmpData.lname}
                                </div>
                                <div class="col-sm-4" ><img src="https://hrmsws.hrmsodisha.gov.in/restService/employeeRestService/empprofilephoto/${EmpData.empid}" height="120" width="100"/></div>
                            </div> 
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp; Current Office:  </label> 
                                <div class="col-sm-4" >
                                    ${EmpData.office}
                                </div>
                            </div>     
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp; Current Post:  </label> 
                                <div class="col-sm-4" >
                                    ${EmpData.post}
                                </div>
                            </div>      
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp;  Mobile Number:  </label> 
                                <div class="col-sm-4" >
                                    ${EmpData.mobile}
                                </div>
                            </div>  

                            <div class="row">
                                <label class="control-label col-sm-4">&emsp; Date of Birth:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.dob}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp;Date of entry into Govt. service:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.joindategoo}
                                </div>
                            </div> 
                                <div class="row">
                                <label class="control-label col-sm-4">&emsp;Date from which in continuous service with GoO:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.doeGov}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp; Gender:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.gender}

                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4">&emsp;Marital Status:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.maritalStatus}

                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4">&emsp;Group Post:  </label> 
                                <div class="col-sm-6">
                                    ${EmpData.postGrpType}

                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp;Home town:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.homeTown}

                                </div>
                            </div>

                                 


                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Bank Name:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.bankName}
                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Branch Name:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.branchName}
                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Bank Account No: </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.bankaccno}
                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; PAN No:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.panno}
                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; AADHAAR No:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.aadhaarno}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Driving Licence No:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.drivingLicence}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Present Address:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.presentaddr}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Permanent Address:  </label> 
                                <div class="col-sm-6" >
                                    ${EmpData.permanentaddr}
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-4"> &emsp; Family Details:  </label> 
                                <div class="col-sm-6" >
                                    <table width="100%" class="table table-bordered">
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Relation</th>
                                            <th>Full Name</th>
                                        </tr>
                                        <c:forEach var="eachMember" items="${FamilyData}" varStatus="slnoCnt">
                                            <tr>
                                                <td>${slnoCnt.index+1}</td>
                                                <td>${eachMember.relation}</td>
                                                <td>${eachMember.fname} ${eachMember.mname} ${eachMember.lname}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-6"> </label> 
                                <div class="col-sm-6" >
                                        <a href="downloadEmployeeDetails.htm?empId=${EmpData.empid}" class="btn btn-primary" target="_blank">Download PDF</a>
                                        
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
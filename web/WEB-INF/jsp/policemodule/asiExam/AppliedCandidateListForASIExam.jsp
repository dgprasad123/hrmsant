<%-- 
    Document   : AppliedCandidateListForASIExam
    Created on : 28 Nov, 2022, 4:43:54 PM
    Author     : Manisha
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>

        <style type="text/css">
            .table > tbody > tr > td{
                font-size: 12px;
            }
        </style>
    </head>



    <body>
        <jsp:include page="../../tab/hrmsadminmenu.jsp"/> 
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                <a href="backToCenterAllotmentPage.htm"><button type="button" class="btn btn-primary">Back</button></a>
                <div class="row" id="showCadreDetail">
                    <div class="col-lg-12">
                        <h2>Candidate List</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="1%">Sl No</th> 
                                        <th>Employee Name</th>
                                        <th>Category</th>
                                        <th>Fathers Name</th>
                                        <th>DOB</th>
                                        <th>Date Of Appointment</th>
                                        <th>Office Name</th>
                                        <th>Date Of Course Completion</th>
                                        <th>Year Service Length</th>
                                        <th>Year redeployment Length</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${candidateList}" var="candidate" varStatus="count">
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td>${candidate.empId}</td>
                                            <td>${candidate.category}</td>
                                            <td>${candidate.fathersname}</td>
                                            <td>${candidate.dob}</td>
                                            <td>${candidate.doeGov}</td>
                                            <td>${candidate.officeName}</td>
                                            <td>${candidate.txtTrainingCompletedDate}</td>
                                            <td>${candidate.yearinServiceLength}</td>
                                            <td>${candidate.yearinRedeploymentServiceLength}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="generateQualifiedList.htm"><input type="button" name="action" value="Generate" class="btn btn-primary"/></a>
                </div>

            </div>
    </body>
</html>



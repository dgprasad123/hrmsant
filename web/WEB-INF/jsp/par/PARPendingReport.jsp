<%-- 
    Document   : PARPendingReport
    Created on : 27 Jun, 2022, 3:13:07 PM
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
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
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
        <script type="text/javascript">
            $(document).ready(function() {
               /* $("#showdata").hide();
                $("#showdata1").hide();
                $("#showdata2").hide();*/
            });
            function saveCheck() {
                if ($('#fiscalyear').val() == "") {
                    alert("Please select Financial Year");
                    $('#fiscalyear').focus();
                    return false;
                }

                if ($('#searchCriteria').val() == "") {
                    alert("Please select any Search Criteria");
                    $('#searchCriteria').focus();
                    return false;

                }
               /* if (($('#fiscalyear').val()) !== ""  && ($('#searchCriteria').val() !== "")) {
                    $("#showdata").show();
                    $("#showdata1").show();
                    $("#showdata2").show();
                } */
            }
        </script>
    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">
                <div class="row">
                    <div class="col-lg-12">                            
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-file"></i> Pending PAR Report 
                            </li>                                
                        </ol>
                    </div>
                </div>
                <h3 style="text-align:center"><b> Pending PAR Report</b></h3>
                <form:form action="pendingPARReport.htm" method="post" commandName="parMessageCommunication">
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-1">
                            <label for="lastdate">Fiscal Year</label>
                        </div>
                        <div class="col-lg-2">
                            <form:select path="fiscalyear"  class="form-control">
                                <form:option value="" label="Select" cssStyle="width:30%"/>
                                <c:forEach items="${fiscyear}" var="fiscyear">
                                    <form:option value="${fiscyear.fy}" label="${fiscyear.fy}"/>
                                </c:forEach>                                 
                            </form:select>
                        </div>
                        <div class="col-lg-1"><label>Search Criteria</label></div>
                        <div class="col-lg-2">
                            <select name="searchCriteria" id="searchCriteria" class="form-control">
                                <option value="">ALL</option>
                                <option value="empid">Employee Id</option>
                                <option value="empname">First Name</option>  
                                <option value="gpfno">GPF/PRAN NO</option>
                                <option value="dob">DOB</option>
                                <option value="mobileno">Mobile No</option>
                            </select>
                        </div>
                        <div class="col-lg-2"><input type="text" name="searchString" id="searchString" class="form-control"> </div>
                        <div class="col-lg-2">
                            <input type="submit" name="action" value="Search" class="btn-primary" onclick="return saveCheck()">                           
                        </div>
                    </div>
                    <div class="row" align="center"><h3><b>Pending PAR As Reporting Authority</b></h3></div>
                    <div class="row" id="showdata">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Appraise Name</th>
                                    <th>Appraise Designation</th>
                                    <th>Pending As</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${pendingPARReportList}" var="pendingPARReport" varStatus="cnt">
                                    <c:if test="${pendingPARReport.parStatus == 6}">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${pendingPARReport.appraiseName}</td>
                                            <td>${pendingPARReport.appraisePost}</td>
                                            <td>${pendingPARReport.statusName}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><h3><b>Pending PAR As Reviewing Authority</b></h3></div>
                    <div class="row" id="showdata1">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Appraise Name</th>
                                    <th>Appraise Designation</th>
                                    <th>Pending As</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${pendingPARReportList}" var="pendingPARReport" varStatus="cnt">
                                    <c:if test="${pendingPARReport.parStatus == 7}">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${pendingPARReport.appraiseName}</td>
                                            <td>${pendingPARReport.appraisePost}</td>
                                            <td>${pendingPARReport.statusName}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><h3><b>Pending PAR As Accepting Authority</b></h3></div>
                    <div class="row" id="showdata2">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Appraise Name</th>
                                    <th>Appraise Designation</th>
                                    <th>Pending As</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${pendingPARReportList}" var="pendingPARReport" varStatus="cnt">
                                    <c:if test="${pendingPARReport.parStatus == 8}">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${pendingPARReport.appraiseName}</td>
                                            <td>${pendingPARReport.appraisePost}</td>
                                            <td>${pendingPARReport.statusName}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>



                </form:form>
            </div>
        </div>

    </body>
</html>





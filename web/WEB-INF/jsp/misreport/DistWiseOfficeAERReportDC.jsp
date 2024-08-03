<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">
        
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">DDO Wise AER Updation Report</div>
                        <div class="panel-body">
                            <div style="margin-bottom: 10px;">
                                <form:form action="DistWiseOfficeAERReportDC.htm" method="POST" commandName="command">
                                    <div class="row">
                                        <div class="col-lg-5"></div>
                                        <div class="col-lg-2">
                                            <form:select path="financialYear" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="2023-24">2023-24</form:option>
                                                <form:option value="2022-23">2022-23</form:option>
                                                <form:option value="2021-22">2021-22</form:option>
                                                <form:option value="2020-21">2020-21</form:option>
                                                <form:option value="2019-20">2019-20</form:option>
                                            </form:select>
                                        </div>
                                        <div class="col-lg-2">
                                            <input type="submit" value="Search" class="btn btn-danger"/>
                                        </div>
                                        <div class="col-lg-4"></div>
                                    </div>
                                </form:form>
                            </div>
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>&nbsp;</th>
                                                <th>&nbsp;</th>                                               
                                                <th colspan="2">Head of Offices</th>                                               
                                                <th colspan="2">Heads of Department</th>  
                                                <th>Administration</th> 
                                            </tr>   
                                            <tr style="color: #777777;">
                                                <th>Sl No</th>
                                                <th>Office Name</th>                                           
                                                <th>Operator Submitted</th>
                                                <th>Approver Submitted</th>
                                                <th>Reviewer Submitted</th>
                                                <th>Verifier Submitted</th>
                                                <th>Acceptor Submitted</th>
                                            </tr>
                                        </thead>

                                        <c:set var="GoperatorSubmitted" value="${0}" />
                                        <c:set var="GapproverSubmitted" value="${0}" />

                                        <c:set var="GreviewerSubmitted" value="${0}" />
                                        <c:set var="GverifierSubmitted" value="${0}" />
                                        <c:set var="GacceptorSubmitted" value="${0}" /> 
                                        <c:forEach items="${AERDetails}" var="bgroup" varStatus="count">

                                            <c:set var="GoperatorSubmitted" value="${GoperatorSubmitted + bgroup.operatorSubmitted}" />
                                            <c:set var="GapproverSubmitted" value="${GapproverSubmitted + bgroup.approverSubmitted}" />

                                            <c:set var="GreviewerSubmitted" value="${GreviewerSubmitted + bgroup.reviewerSubmitted}" />
                                            <c:set var="GverifierSubmitted" value="${GverifierSubmitted + bgroup.verifierSubmitted}" />
                                            <c:set var="GacceptorSubmitted" value="${GacceptorSubmitted + bgroup.acceptorSubmitted}" />
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>
                                                    <a href="#" target='blank'>${bgroup.departmentname}(${bgroup.offCode})</a>&nbsp;&nbsp;
                                                    <a href="DownloadPDFReportDC.htm?offcode=${bgroup.offCode}&aerId=${bgroup.aerId}" target="_blank">
                                                        <span class="fa fa-file-pdf-o" style="color:red"></span> Schedule-I
                                                    </a>
                                                </td>
                                                <td>${bgroup.operatorSubmitted}</td>
                                                <td>${bgroup.approverSubmitted}</td>
                                                <td>${bgroup.reviewerSubmitted}</td>
                                                <td>${bgroup.verifierSubmitted}</td>
                                                <td>${bgroup.acceptorSubmitted}</td>
                                            </tr>
                                        </c:forEach>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>&nbsp;</th>
                                            <th>&nbsp;</th>
                                            <th>${GoperatorSubmitted}</th>
                                            <th>${GapproverSubmitted}</th>
                                            <th>${GreviewerSubmitted}</th>
                                            <th>${GverifierSubmitted}</th>
                                            <th>${GacceptorSubmitted}</th>
                                        </tr>      
                                    </table>
                                </div>                
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

<%-- 
    Document   : CustodianFinalReportForFitForPromotionandNotFitForPromotion
    Created on : 3 Dec, 2020, 12:40:53 PM
    Author     : Manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>

    </head>
    <body>
        <form:form action="groupCCustdianReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
            <form:hidden path="fiscalyear"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;"> Group C Employee List For Fit For Promotion and Not Fit For Promotion</div>
                    <div class="panel-body" style="height: 550px;overflow: auto;">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th width="6%">SI NO</th>
                                        <th width="6%">Employee Name</th>                                    
                                        <th width="15%">Employee Post</th>
                                        <th width="6%">Remarks Of Reporting</th>
                                        <th width="6%">Remarks Of Reviewing</th>
                                        <th width="6%">Remarks Of Accepting</th>
                                    </tr>                            
                                </thead>
                                <tbody>
                                    <c:forEach items="${groupCEmpList}" var="remarkdetail" varStatus="count">
                                        <tr>   
                                            <td>${count.index + 1}</td>
                                            <td>${remarkdetail.reviewedempname}</td>                                        
                                            <td>${remarkdetail.reviewedpost}</td>
                                            <td>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityReporting eq 'Y'}">
                                                    <span class="label label-success">Fit For Promotion</span> 
                                                </c:if>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityReporting eq 'N'}">
                                                    <span class="label label-danger">Not Fit For Promotion</span> 
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityReviewing eq 'Y'}">
                                                    <span class="label label-success">Fit For Promotion</span> 
                                                </c:if>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityReviewing eq 'N'}">
                                                    <span class="label label-danger">Not Fit For Promotion</span> 
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityAccepting eq 'Y'}">
                                                    <span class="label label-success">Fit For Promotion</span> 
                                                </c:if>
                                                <c:if test="${remarkdetail.isfitforShoulderingResponsibilityAccepting eq 'N'}">
                                                    <span class="label label-danger">Not Fit For Promotion</span> 
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-default" name="action" value="Download">                        
                        <input type="submit" class="btn btn-default" name="action" value="Back">
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
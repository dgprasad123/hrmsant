<%-- 
    Document   : PARStatusChangeList
    Created on : 18 Apr, 2022, 3:09:55 PM
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

    </head>



    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <form:form action="parStatusManageByPARAdmin.htm" commandName="parStatusBean" method="post" class="form-horizontal" enctype="multipart/form-data">
            <div id="wrapper"> 
                <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">
                    <div class="panel-heading"><center><b>PAR Status Change List</b></center></div>
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="3%">#</th>
                                <th width="10%">Financial Year</th>
                                <th width="10%">PAR Status For Appraise</th>
                                <th width="10%">PAR Status For Authority</th>
                                <th width="10%">PAR Status Change On</th>
                                <th width="15%">Period For Appraise</th>
                                <th width="15%">Period For Reporting</th>
                                <th width="15%">Period For Reviewing</th>
                                <th width="15%">Period For Accepting</th>
                                <th width="15%">Attachment</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${parStatusDetail}" var="parStatus" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index + 1}</td>
                                    <td>${parStatus.fiscalyear}</td>
                                    <td>${parStatus.isClosedForAppraisee}</td>
                                    <td>${parStatus.isClosedForAuthority}</td>
                                    <td>${parStatus.parStatusChangeOnDate}</td>
                                    <td>${parStatus.parPeriodForAppraisee}</td>
                                    <td>${parStatus.parPeriodForReporting}</td>
                                    <td>${parStatus.parPeriodForReviewing}</td>
                                    <td>${parStatus.parPeriodForAccepting}</td>
                                    <td>
                                        <c:if test="${not empty parStatus.originalFileNameForparStatus}">
                                            <a href="downloadFileForChangePARStatus.htm?logId=${parStatus.logId}" class="btn btn-default">
                                                <span class="glyphicon glyphicon-paperclip"></span> ${parStatus.originalFileNameForparStatus}</a>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>



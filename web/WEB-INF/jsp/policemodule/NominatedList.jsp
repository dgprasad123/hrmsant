<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
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
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <h3 style="text-align:center">Nomination List</h3>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Submitted On</th>
                                            <th>Submitted By</th>
                                            <th>Nomination For</th>
                                            <th colspan="3" style="text-align: center">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${nominatedlist}" var="obj" varStatus="counter">
                                            <tr>
                                                <td width="10%">${counter.count}</td>
                                                <td width="10%">${obj.submittedOn}</td>
                                                <td width="30%">${obj.submittedByOffice}</td>
                                                <td width="20%">${obj.sltNominationForPost}</td>
                                                <td width="10%">
                                                    
                                                    <c:if test="${obj.sltpostName eq '140090'}">
                                                        <a href="viewNominationrollForDGOffice.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Recommend"><i class="fa fa-pencil-square-o fa-2x"></i>
                                                            </a>
                                                    </c:if>
                                                    <c:if test="${obj.sltpostName eq '140858'}">
                                                        <a href="NominatedListDetailView.htm?nominationMasterId=${obj.nominationMasterId}" title="View"><i class="fa fa-check-square-o fa-2x"></i></a>
                                                    </c:if>
                                                </td>
                                                <c:if test="${obj.sltpostName eq '140090'}">
                                                    <td width="10%" style="text-align: center">
                                                        
                                                    </td>
                                                    <td width="10%" style="text-align: center">
                                                        <a href="downloadAnnextureAForDSPRankController.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Download"><i class="fa fa-file-pdf-o text-red fa-2x"/></i></a>
                                                    </td>
                                                </c:if>
                                                <c:if test="${obj.sltpostName eq '140858'}">
                                                    <td width="10%" style="text-align: center">
                                                        <a href="downloadASINominationAnnexureAExcel.htm?nominationMasterId=${obj.nominationMasterId}" title="Annexure-A">
                                                            <i class="fa fa-file-excel-o fa-2x" style="color:green;" aria-hidden="true"></i> Annexure-A
                                                        </a>
                                                    </td>
                                                    <td width="10%" style="text-align: center">
                                                        <a href="downloadASINominationAnnexureBExcel.htm?nominationMasterId=${obj.nominationMasterId}" title="Annexure-B"><i class="fa fa-file-excel-o fa-2x" style="color:green;" aria-hidden="true"></i> Annexure-B</a>
                                                    </td>
                                                </c:if>
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

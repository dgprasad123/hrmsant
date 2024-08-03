<%-- 
    Document   : DPPendingReportCadreWise
    Created on : 4 Jul, 2022, 11:01:06 AM
    Author     : Manisha
--%>


<<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<c:set var="sum_totaldpSubmitted" value="0"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Department wise Disciplinary Proceeding Report </title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="pendingDepartmentProceedingReport.htm" commandName="dPPendingReportBean" method="post">
                    <div class="container-fluid">
                        <div class="panel panel-primary">
                            <div class="panel-heading">Cadre wise Disciplinary Proceeding Report</div>
                            <div class="panel-body" style="margin-top:20px;">

                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="10%">#</th>
                                            <th width="40%">Cadre Name</th>
                                            <th width="50%">No of DP Submitted</th>
                                        </tr>

                                    </thead>
                                    <tbody>  
                                        <c:forEach items="${dpStatusReportCadreWise}" var="dpStatusReport" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index + 1}</td>
                                                <td>${dpStatusReport.cadreName}</td>
                                                <td>${dpStatusReport.totalNumberInitiated}</td>

                                            </tr>

                                            <c:set var="sum_totaldpSubmitted" value="${sum_totaldpSubmitted + dpStatusReport.totalNumberInitiated}"/>
                                        </c:forEach>
                                        <tr>
                                            <td>Total</td>
                                            <td>&nbsp;</td>
                                            <td>${sum_totaldpSubmitted}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <input type="submit" name="action" value="Back" class="btn btn-default"/>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>


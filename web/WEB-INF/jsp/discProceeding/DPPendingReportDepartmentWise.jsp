<%-- 
    Document   : DPPendingReportDepartmentWise
    Created on : 2 Jul, 2022, 4:20:33 PM
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
    <script type="text/javascript">

        function  show_div_details(ids) {
            $("#" + ids).toggle();
        }
    </script>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="pendingDepartmentProceedingReport.htm" commandName="dPPendingReportBean" method="post">
                    <div class="container-fluid">
                        <div class="panel panel-primary">
                            <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Departmental Proceeding Submission Report</div>
                            
                            <h3 style="text-align: right"> <input type="button" name="action" value="Download As Excel" class="btn btn-primary" /></h3>
                                
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Department Name</th>
                                                <th>No of DP Submitted</th>
                                            </tr>
                                        </thead>
                                        <c:forEach items="${dpStatusReport}" var="dpStatus" varStatus="count">
                                            <tr onclick="show_div_details(${count.index + 1})">
                                                <td>${count.index + 1}</td>
                                                <td>${dpStatus.departmentName}</td>
                                                <td style='color:green'>${dpStatus.totalNumberInitiated}</td>
                                                <c:set var="sum_totaldpSubmitted" value="${sum_totaldpSubmitted + dpStatus.totalNumberInitiated}"/>
                                            </tr>
                                            <tr style='display:none' id="${count.index + 1}">
                                                <td colspan="3">
                                                    <table class="table table-bordered table-hover table-striped">
                                                        <thead>
                                                            <tr style="background-color: #CCCCCC;color: #ffffff;">
                                                                <th>Sl No</th>
                                                                <th>Office Code</th>
                                                                <th>Office Name</th>
                                                                <th>Total Number Submitted</th>
                                                            </tr>
                                                        </thead>  
                                                        <c:forEach items="${dpStatus.deptWiseDP}" var="dpStatusReportdeptwise" varStatus="count">
                                                            <tr>
                                                                <td>${count.index + 1}</td>
                                                                <td>${dpStatusReportdeptwise.officeCode}</td>
                                                                <td>${dpStatusReportdeptwise.officeName}</td>
                                                                <td>${dpStatusReportdeptwise.totalNumberInitiated}</td>
                                                            </tr>

                                                        </c:forEach>
                                                    </table> 	

                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>Total</th>
                                            <th>&nbsp;</th>                                           
                                            <th>${sum_totaldpSubmitted}</th> 
                                        </tr> 
                                    </table>

                                    <div class="panel-footer">
                                        <input type="submit" name="action" value="Back" class="btn btn-default"/>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
    </body>
</html>


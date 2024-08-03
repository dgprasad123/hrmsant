<%-- 
    Document   : GroupWiseParReportForAuthority
    Created on : 11 Apr, 2022, 11:22:15 AM
    Author     : Manisha
--%>

<<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<c:set var="sum_totalNumberEmployee" value="0"/>
<c:set var="sum_totalParSubmitted" value="0"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Par Statement Report</title>

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
                <form:form action="groupwiseParReportForAuthority.htm" commandName="parAdminProperties" method="post">
                    <div class="container-fluid">
                        <div class="panel panel-primary">
                            <div class="panel-heading">PAR STATEMENT STATUS REPORT</div>
                            <div class="panel-body" style="margin-top:20px;">

                                <h2 align="center">PAR STATEMENT STATUS REPORT (${fiscalyear})</h2>
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="10%">#</th>
                                            <th width="40%">Post Group Type</th>
                                            <th width="40%">Total Number Of Employee</th>
                                            <th width="50%">No of Par Statement Submitted</th>
                                        </tr>

                                    </thead>
                                    <tbody>  
                                        <c:forEach items="${parStatusList}" var="parStatus" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index + 1}</td>
                                                <td>${parStatus.totalNopostgrouptype}</td>
                                                <td>${parStatus.totalNumberEmployee}</td>
                                                <td>${parStatus.totalParSubmitted}</td>

                                            </tr>
                                           
                                            <c:set var="sum_totalNumberEmployee" value="${sum_totalNumberEmployee + parStatus.totalNumberEmployee}"/>
                                            <c:set var="sum_totalParSubmitted" value="${sum_totalParSubmitted + parStatus.totalParSubmitted}"/>
                                        </c:forEach>
                                        <tr>
                                            <td>Total Count</td>
                                            <td>&nbsp;</td>
                                            <td>${sum_totalNumberEmployee}</td>
                                            <td>${sum_totalParSubmitted}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <form:hidden path="fiscalyear"/>
                                <input type="submit" name="action" value="Back" class="btn btn-default"/>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>







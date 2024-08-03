<%-- 
    Document   : GroupWiseParReport
    Created on : 21 Oct, 2021, 11:38:43 AM
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
        <div id="page-wrapper">
            <div class="container-fluid">

                <div class="panel-heading" style="text-align: center">
                    <b>PAR CREATION AND SUBMISSION STATUS REPORT </b>
                </div>



                <div class="table-responsive">
                    <form:form action="groupwiseParReport.htm" commandName="parAdminProperties" method="post">
                        <div class="panel panel-default">
                            <div class="panel-body">  
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
                                            <td><b>Total Count</b></td>
                                            <td>&nbsp;</td>
                                            <td><b>${sum_totalNumberEmployee}</b></td>
                                            <td><b>${sum_totalParSubmitted}</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <form:hidden path="fiscalyear"/>
                                <input type="submit" name="action" value="Back" class="btn btn-default"/>
                            </div>

                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>


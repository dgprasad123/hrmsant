<%-- 
    Document   : GroupWisePropertyReport
    Created on : 8 Oct, 2021, 10:37:53 AM
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
                    <b>PROPERTY STATEMENT STATUS REPORT </b>
                </div>
                                
                       
           
                <div class="table-responsive">
                    <form:form action="groupwisePropertyStatementReport.htm" commandName="propertyStatementAdminBean" method="post">
                        <div class="panel panel-default">
                            <div class="panel-body">  
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="10%">#</th>
                                            <th width="40%">Post Group Type</th>
                                            <th width="40%">Total Number Of Employee</th>
                                            <th width="50%">No of Property Statement Submitted</th>
                                            <th width="50%">Total Number Of Regular Employee</th>
                                            <th width="50%">Property Statement Submitted By Regular Employee</th>
                                        </tr>
                                    </thead>
                                    <tbody>  
                                        <c:forEach items="${propertyStatusList}" var="propertyStatus" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index + 1}</td>
                                                <td>${propertyStatus.totalNopostgrouptype}</td>
                                                <td>${propertyStatus.totalNumberEmployee}</td>
                                                <td>${propertyStatus.totalPropertySubmitted}</td>
                                                <td>${propertyStatus.totalNumberRegularEmployee}</td>
                                                <td>${propertyStatus.totalRegularempPropertySubmitted}</td>
                                            </tr>
                                        </c:forEach>
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


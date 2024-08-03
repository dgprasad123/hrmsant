<%-- 
    Document   : NrcStatementDetailReport
    Created on : Feb 19, 2020, 12:07:11 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }


        </style>
    </head>
    <body style="background-color:#FFFFFF">
        <form:form action="createdpcStatementDetailReport.htm" method="POST" commandName="departmentPromotionBean" class="form-inline">      
            <div class="panel-body table-responsive">
                <table class="table table-bordered" >
                    <thead>
                        <tr>
                            <th width="5%">Fiscal year From</th>
                            <th width="10%">Fiscal year To</th>
                            <th width="10%">Cadre</th>
                            <th width="10%">DPC Name</th>
                            <th width="10%">Created On</th>
                            <th width="10%">Detail</th>
                            <th width="10%">Action</th>
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${dpcDataList}" var="dpcdata">
                            <tr>
                                <td>${dpcdata.fiscalYearFrom}</td>  
                                <td>${dpcdata.fiscalYearTo}</td>
                                <td>${dpcdata.cadrename}</td>
                                <td>${dpcdata.dpcName}</td>
                                <td>${dpcdata.createdOn}</td>
                                
                                <td><a href="CadrewiseDpcStatementDetailReport.htm?dpcId=${dpcdata.dpcId}">Detail</a></td>
                                <td>
                                    <a href="editdpcStatementDetailReport.htm?dpcId=${dpcdata.dpcId}" class="btn btn-default"><span class="glyphicon glyphicon-pencil"> Edit</span></a>

                                    <a href="viewdpcStatementDetailReport.htm?dpcId=${dpcdata.dpcId}"  class="btn btn-default">View</a>
                                    <a href="deletedpcStatementDetailReport.htm?dpcId=${dpcdata.dpcId}" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-remove"> Delete</span></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div align="center" style="margin-top: 5px;">
                <input type="submit" class="btn btn-primary" name="action" value="Create New"/>

            </div>
        </form:form>
    </body>
</html>

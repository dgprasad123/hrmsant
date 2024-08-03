<%-- 
    Document   : EmployeeGrievanceList
    Created on : Jan 6, 2018, 2:56:34 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="col-md-12 col-sm-12" align="right" style="margin:10px;">
            <div style="margin-bottom:8px;" class="row">
                <div class="col-md-8 col-sm-8">
                    <form:form commandName="EmployeeGrievanceForm" method="post" action="adminGrievanceList.htm">
                        <input type="hidden" value="44556677" id="my_id" name="my_id">
                        <b>Browse By:</b>
                        <form:select path="categoryCode" cssStyle="height:38px;border:2px solid #C5C5C5;width:190px;">
                            <form:option label="All" value="0"/>
                            <form:options items="${categorylist}" itemValue="categoryCode" itemLabel="category"/>                                
                        </form:select>                        
                        <form:select path="grievanceStatus"  style="height:38px;border:2px solid #C5C5C5;width:220px;">                            
                            <form:option value="">All</form:option>
                            <form:option value="P">Pending</form:option>
                            <form:option value="F">Forwarded</form:option>
                            <form:option value="R">Rejected</form:option>
                            <form:option value="D">Disposed</form:option>
                        </form:select>
                        <form:input path="grievanceFromDate" class="datePick" placeholder="From Date" style="height:38px;border:2px solid #C5C5C5;" size="10" />
                        <form:input path="grievanceToDate" class="datePick" placeholder="To Date" style="height:38px;border:2px solid #C5C5C5;" readonly="readonly" size="10" />
                        <input type="submit" style="height:38px;border:1px solid #034303;width:100px;" class="btn-success" value="Search">                        
                    </form:form>
                </div>
                <div align="right" class="col-md-2 col-sm-2">
                    <form method="post" action="adminGrievanceEntry.htm">
                        <input type="submit" value="New Grievance Entry" name="ADD" class="btn btn-success">
                    </form>                    
                </div>
                <div align="left" class="col-md-2 col-sm-2">
                    <a href="adminGrievanceDashBoard.htm" class="btn btn-success">Dashboard</a>
                    <a href="adminGrievanceReport.htm" class="btn btn-success">Detail Report</a>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                            <td>Sl No</td>
                            <td>Category</td>
                            <td>Full Name</td>
                            <td>HRMS ID</td>
                            <td>Mobile</td>
                            <td>Grievance</td>
                            <td>Time</td>                            
                            <td>Status</td>
                            <td colspan="2">Actions</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${grievncelist}" var="grievnce" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>${grievnce.category}</td>
                                <td>${grievnce.fullname}</td>
                                <td>${grievnce.hrmsid}</td>
                                <td>${grievnce.appmobile}</td>
                                <td>${grievnce.grievanceDetail}</td>
                                <td>${grievnce.grievanceTime}</td>
                                <td>
                                    <c:if test="${grievnce.isdisposed == 'Y'}">
                                        Disposed
                                    </c:if>
                                    <c:if test="${grievnce.isrejected == 'Y'}">
                                        Rejected
                                    </c:if>
                                    <c:if test="${grievnce.isforwarded == 'Y'}">
                                        Forwarded
                                    </c:if>
                                </td>
                                <td>
                                    <a href="adminGrievanceDetail.htm?gid=${grievnce.gid}"><img src="images/details.gif"></a>
                                </td>
                                <td>
                                    <a href="grievancecommunications.htm?gid=${grievnce.gid}"><img height="20" title="View Grievance History" src="images/comm.png"></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>

<%-- 
    Document   : OfficeWiseEmployee
    Created on : Feb 9, 2017, 4:42:58 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Office Wise PayBill Reports</div>
                        <div class="panel-body">
                            
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Office Name</th>
                                                <th>DDO Code</th>
                                                <th>Total DDO</th>
                                                <th>DDO Prepared</th>
                                                <th>Bill Prepared</th>
                                                <th>DDO Submitted</th>
                                                <th>Bill Submitted</th>
                                                <th>Token Generated</th>
                                                <th>Employee Details</th>
                                                <th>Bill Details</th>
                                            </tr>
                                        </thead>
                                        <c:set var="GtotalDDO" value="${0}" />
                                        <c:set var="GddoPrepared" value="${0}" />
                                         <c:set var="GbillPrepared" value="${0}" />
                                        <c:set var="GddoSubmitted" value="${0}" />
                                         <c:set var="GbillSubmitted" value="${0}" />
                                        <c:set var="GtokenPrepared" value="${0}" />
                                    <c:forEach items="${billDetails}" var="bgroup" varStatus="count">
                                         <c:set var="GtotalDDO" value="${GtotalDDO + bgroup.totalDDO}" />
                                          <c:set var="GddoPrepared" value="${GddoPrepared + bgroup.ddoPrepared}" />
                                          <c:set var="GbillPrepared" value="${GbillPrepared + bgroup.billPrepared}" />
                                          <c:set var="GddoSubmitted" value="${GddoSubmitted + bgroup.ddoSubmitted}" />
                                           <c:set var="GbillSubmitted" value="${GbillSubmitted + bgroup.billSubmitted}" />
                                          <c:set var="GtokenPrepared" value="${GtokenPrepared + bgroup.tokenPrepared}" />
                                        <tr>
                                        <td>${count.index + 1}</td>
                                        <td>${bgroup.departmentname}</td>
                                        <td>${bgroup.ddoCode}</td>
                                        <td>${bgroup.totalDDO}</td>
                                        <td>${bgroup.ddoPrepared}</td>
                                        <td>${bgroup.billPrepared}</td>
                                        <td>${bgroup.ddoSubmitted}</td>
                                        <td>${bgroup.billSubmitted}</td>
                                        <td>${bgroup.tokenPrepared}</td>
                                        <td><a href="DownlaodOfficeWiseEmployee.htm?ocode=${bgroup.deptCode}&month=${bgroup.strMonth}&year=${bgroup.strYear}" class="btn btn-primary" target="_blank">Details</a></td>
                                        <td><a href="DownloadOfficeWiseBillDetails.htm?ocode=${bgroup.deptCode}&month=${bgroup.strMonth}&year=${bgroup.strYear}" class="btn btn-primary" target="_blank">Details</a></td>
                                        
                                        </tr>
                                    </c:forEach>
                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>&nbsp;</th>
                                                <th>&nbsp;</th>
                                                <th>${GtotalDDO}</th>
                                                <th>${GddoPrepared}</th>
                                                <th>${GbillPrepared}</th>
                                                <th>${GddoSubmitted}</th>
                                                <th>${GbillSubmitted}</th>
                                                <th>${GtokenPrepared}</th>
                                                <th>&nbsp;</th>
                                                <th>&nbsp;</th>
                                            </tr>    
                                </table>
                            </div>                
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>

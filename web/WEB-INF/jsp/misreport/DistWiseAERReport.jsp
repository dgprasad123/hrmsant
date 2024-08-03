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
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">District Wise AER  Report(Fy:<c:out value="${fiscalyear}"/>)</div>
                        <div align='right' style='margin:10px'><a href="TreasuryWiseAERStatus.htm"><input type='button' value="Treasury Wise AER Report " class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="COWiseDeptAERStatus.htm"><input type='button' value="CO Wise AER Status" class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="DeptWiseAERStatus.htm"><input type='button' value="Department Wise AER Status" class="btn btn-primary"  name='btn1'  /></a>&nbsp;&nbsp;<a href="DistWiseAERReport.htm"><input type='button' class="btn btn-primary"  name='btn2' value="District Wise AER Status"/></a></div>
                        <div class="panel-body">
                            <div style="margin-bottom: 10px;">
                                <form:form action="DistWiseAERReport.htm" method="POST" commandName="command">
                                    <div class="row">
                                        <div class="col-lg-5"></div>
                                        <div class="col-lg-2">
                                            <form:select path="financialYear" class="form-control">
                                                <form:option value="">--Select--</form:option>
                                                <form:option value="2023-24">2023-24</form:option>
                                                <form:option value="2022-23">2022-23</form:option>
                                                <form:option value="2021-22">2021-22</form:option>
                                                <form:option value="2020-21">2020-21</form:option>
                                                <form:option value="2019-20">2019-20</form:option>
                                            </form:select>
                                        </div>
                                        <div class="col-lg-2">
                                            <input type="submit" value="Search" class="btn btn-danger"/>
                                        </div>
                                        <div class="col-lg-4"></div>
                                    </div>
                                </form:form>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>&nbsp;</th>
                                            <th>&nbsp;</th>
                                            <th>&nbsp;</th>
                                            <th colspan="2" align='right'>Head of Offices</th>                                               
                                            <th colspan="2" align='center'>Heads of Department</th>  
                                            <th align='center'>Administration</th> 
                                        </tr>
                                        <tr style="color: #777777;">
                                            <th>Sl Nos</th>
                                            <th>Department</th>
                                            <th>Total No Head of Offices</th>
                                            <th>Operator Submitted</th>
                                            <th>Approver Approved</th>
                                            <th>Reviewer Approved</th>
                                            <th>Verifier Approved</th>
                                            <th>Acceptor Approved</th>

                                        </tr>
                                    </thead>
                                    <c:set var="GtotalEmp" value="${0}" />
                                    <c:set var="GoperatorSubmitted" value="${0}" />
                                    <c:set var="GapproverSubmitted" value="${0}" />

                                    <c:set var="GreviewerSubmitted" value="${0}" />
                                    <c:set var="GverifierSubmitted" value="${0}" />
                                    <c:set var="GacceptorSubmitted" value="${0}" /> 
                                    <c:forEach items="${AERDetails}" var="bgroup" varStatus="count">
                                        <c:set var="GtotalEmp" value="${GtotalEmp + bgroup.totalDDO}" />
                                        <c:set var="GoperatorSubmitted" value="${GoperatorSubmitted + bgroup.operatorSubmitted}" />
                                        <c:set var="GapproverSubmitted" value="${GapproverSubmitted + bgroup.approverSubmitted}" />

                                        <c:set var="GreviewerSubmitted" value="${GreviewerSubmitted + bgroup.reviewerSubmitted}" />
                                        <c:set var="GverifierSubmitted" value="${GverifierSubmitted + bgroup.verifierSubmitted}" />
                                        <c:set var="GacceptorSubmitted" value="${GacceptorSubmitted + bgroup.acceptorSubmitted}" />
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td><a href="DistWiseOfficeAERReport.htm?distcode=${bgroup.deptCode}" target='blank'>${bgroup.departmentname}</a></td>
                                            <td>${bgroup.totalDDO}</td>
                                            <td>${bgroup.operatorSubmitted}</td>
                                            <td>${bgroup.approverSubmitted}</td>
                                            <td>${bgroup.reviewerSubmitted}</td>
                                            <td>${bgroup.verifierSubmitted}</td>
                                            <td>${bgroup.acceptorSubmitted}</td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>${GtotalEmp}</th>
                                        <th>${GoperatorSubmitted}</th>
                                        <th>${GapproverSubmitted}</th>
                                        <th>${GreviewerSubmitted}</th>
                                        <th>${GverifierSubmitted}</th>
                                        <th>${GacceptorSubmitted}</th>

                                    </tr>      
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

    </body>
</html>

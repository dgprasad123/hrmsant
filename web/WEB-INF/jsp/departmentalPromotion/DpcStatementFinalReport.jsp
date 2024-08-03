<%-- 
    Document   : NrcStatementFinalReport
    Created on : Feb 25, 2020, 11:49:37 AM
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
    <body style="background-color:#FFFFFF;margin-top:0px;">
        <form:form action="viewdpcStatementDetailReport.htm" method="POST" commandName="departmentPromotionBean" class="form-inline">
            <form:hidden path="dpcId"/>
            <div class="panel-body table-responsive">
                <table class="table table-bordered" width="1000">
                    <thead>
                        <tr>
                            <th width="46">#</th>
                            <th width="330">Employee Name / designation</th>
                            <th width="150">Date of birth</th>                      
                            <th width="120">Year</th>
                            <th width="130">Period From</th>
                            <th width="130">Period To</th>
                            <th width="80">Post Group</th>
                            <th width="130">Status</th>
                            <th width="130">Grade</th>
                            <th>Remarks</th>
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${cadrewisenrcReport}" var="nrcreport" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${nrcreport.empName} / ${nrcreport.empPost}</td>

                                <td>${nrcreport.dob}</td>
                                <td style="padding: 0px;" colspan="7">
                                    <table class="table table-bordered" style="border: 0px;margin-bottom:0px;">
                                        <c:forEach items="${nrcreport.fiscalYearList}" var="fiscalYear">
                                            <tr>
                                                <td width="120" style="border-left: 0px;">${fiscalYear.fy}</td>
                                                <td style="padding: 0px;">
                                                    <table  class="table table-bordered" style="border: 0px;margin-bottom:0px;">                                                        
                                                        <c:forEach items="${fiscalYear.yearwisedata}" var="yearwisepardata">
                                                            <tr>
                                                                <td width="130" style="border-left: 0px;">${yearwisepardata.periodfrom}</td>
                                                                <td width="130">${yearwisepardata.periodto}</td>
                                                                <td width="80">${yearwisepardata.postGroup}</td>
                                                                <td width="130">
                                                                    <!-- if the post group type is 'A' AND isreviewed equal to  'Y' -->
                                                                    <c:if test="${yearwisepardata.postGroup eq 'A' && yearwisepardata.isreviewed eq 'Y'}">
                                                                        <c:if test="${yearwisepardata.parstatus eq 17}">
                                                                            <a target="_blank" href="getNRCdetail.htm?parId=${yearwisepardata.parid}">NRC</a>
                                                                        </c:if>
                                                                        <c:if test="${yearwisepardata.parstatus ne 17 && yearwisepardata.parid ne 0}">
                                                                            <a target="_blank" href="getviewPARAdmindetail.htm?parId=${yearwisepardata.parid}&taskId=${yearwisepardata.taskId}">PAR</a>
                                                                        </c:if> 
                                                                    </c:if>
                                                                    <c:if test="${yearwisepardata.postGroup eq 'A' && yearwisepardata.isreviewed eq 'N' && yearwisepardata.isadversed  ne 'Y' && yearwisepardata.parstatus ne 0 && yearwisepardata.parstatus ne 6}">
                                                                        Not Reviewed
                                                                    </c:if>

                                                                    <c:if test="${yearwisepardata.postGroup eq 'A' && yearwisepardata.isadversed  eq 'Y'}">
                                                                        Adverse
                                                                    </c:if>

                                                                    <!-- if the post group type is 'B'-->
                                                                    <c:if test="${yearwisepardata.postGroup eq 'B'}">
                                                                        <c:if test="${yearwisepardata.parstatus eq 17}">
                                                                            <a target="_blank" href="getNRCdetail.htm?parId=${yearwisepardata.parid}">NRC</a>
                                                                        </c:if>
                                                                        <c:if test="${yearwisepardata.parstatus ne 17 && yearwisepardata.parid ne 0}">
                                                                            <a target="_blank" href="getviewPARAdmindetail.htm?parId=${yearwisepardata.parid}&taskId=${yearwisepardata.taskId}">PAR</a>
                                                                        </c:if> 
                                                                    </c:if>
                                                                    <c:if test="${yearwisepardata.parid eq 0 || yearwisepardata.parstatus eq 0}">
                                                                        ${yearwisepardata.grade}                                                                        
                                                                    </c:if> 
                                                                </td>
                                                                <td width="130">
                                                                    <c:if test="${(yearwisepardata.postGroup eq 'A' && yearwisepardata.isreviewed eq 'Y') or yearwisepardata.postGroup eq 'B' && (yearwisepardata.parstatus ne 17)}">
                                                                        ${yearwisepardata.gradeName}
                                                                    </c:if>
                                                                </td>

                                                                <td>
                                                                    <c:if test="${yearwisepardata.parstatus eq 17}">
                                                                        ${yearwisepardata.remark}
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="panel-footer">
                <input type="submit" class="btn btn-primary" name="action" value="Back"/>  
                <input type="submit" class="btn btn-primary" name="action" value="Download As PDF"/>
                <input type="submit" class="btn btn-primary" name="action" value="Download As Excel"/>
            </div>
        </form:form>
    </body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<%
    int listCount = 0;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">        



        <script type="text/javascript">
            $(document).ready(function() {
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {                        
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));                        
                    }
                    $('#fiscalyear').val($('#hidfiscalyear').val());
                });                
            });
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form action="ParReport.htm" method="POST">
                <input type="hidden" id="hidfiscalyear" value="${fiscalyear}"/>
                <div align="center" style="margin-top:5px;margin-bottom:10px;">
                    <div class="empInfoDiv" align="center">
                        <table border="0" width="100%" cellspacing="0" style="font-size:16px; font-family:verdana;font-weight:bold;">
                            <tr style="height:40px;">
                                <td align="center">
                                    DEPARTMENT WISE PERFORMANCE APPRAISAL REPORT FOR GROUP A AND B EMPLOYEES
                                </td>
                            </tr>
                            <tr>
                                <td align="center">
                                    Select Financial Year:
                                    <select name="fiscalyear" id="fiscalyear" ></select>

                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <button type="submit">Ok</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div align="center" style="width:100%;">
                    <div class="table-responsive"> 
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th width="40%">Department Name</th>
                                    <th width="11%">No of Employees<br />who submitted PAR</th>
                                    <th width="13%">Pending at Reporting Authority</th>
                                    <th width="13%">Pending at Reviewing Authority</th>
                                    <th width="13%">Pending at Accepting Authority</th>
                                    <th width="13%">Completed PAR</th>
                                </tr>
                            </thead>
                            <c:if test="${not empty PARReportList}">
                                <c:forEach var="plist" items="${PARReportList}" varStatus="count">                                  
                                    <tr>
                                        <td>${count.count}</td>
                                        <td align="left" style="padding-left:40px;">
                                            <c:out value="${plist.deptname}"/>
                                        </td>
                                        <td align="center">
                                            <c:out value="${plist.parapplied}"/>
                                        </td>
                                        <td align="center">
                                            <c:out value="${plist.reportingpending}"/>
                                        </td>
                                        <td align="center">
                                            <c:out value="${plist.reviewingpending}"/>
                                        </td>
                                        <td align="center">
                                            <c:out value="${plist.acceptingpending}"/>
                                        </td>
                                        <td align="center">
                                            <c:out value="${plist.completed}"/>
                                        </td>
                                    </tr>                                
                                </c:forEach>
                            </c:if>
                            <tr>
                                <td>&nbsp;</td>
                                <td align="center">Total</td>
                                <td align="center"><c:out value="${NOOFPAR}"/></td>
                                <td align="center"><c:out value="${REPORTINGPENDING}"/></td>
                                <td align="center"><c:out value="${REVIEWINGPENDING}"/></td>
                                <td align="center"><c:out value="${ACCEPTINGPENDING}"/></td>
                                <td align="center"><c:out value="${COMPLETED}"/></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>

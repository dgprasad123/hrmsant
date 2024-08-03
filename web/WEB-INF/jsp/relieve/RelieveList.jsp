<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {

            });
            function getMonth(monthStr) {
                var d = Date.parse(monthStr + "1, 2012");
                if (!isNaN(d)) {
                    return new Date(d).getMonth();
                }
                return -1;
            }
        </script>
    </head>
    <body>

        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;layout: fixed;color:#000000;font-weight:bold;">
                                <thead> </thead>
                                <tr>
                                    <td width="20%" align="right">
                                        Employee Name:                    
                                    </td>
                                    <td width="38%" style="text-transform:uppercase;" align="left">
                                        <b> ${SelectedEmpObj.fullName} </b>
                                    </td>
                                    <td width="16%" align="right">
                                        HRMS ID:                    
                                    </td>
                                    <td width="26%">
                                        ${SelectedEmpObj.empId} 
                                    </td>
                                </tr>

                                <tr>
                                    <td align="right">Current Post: </td>
                                    <td >
                                        &nbsp; ${SelectedEmpObj.postname} 
                                    </td>
                                    <td align="right">GPF/ PPAN No:</td>
                                    <td><b style="text-transform:uppercase;">${SelectedEmpObj.gpfno}&nbsp;</b></td>
                                </tr>
                                <tr>
                                    <td align="right">Current Cadre: </td>
                                    <td align="left"><b> ${SelectedEmpObj.cadrename}   &nbsp;</b></td>
                                    <td align="right">Current Status:</td>
                                    <td><b> ${SelectedEmpObj.depstatus}&nbsp;</b></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="10%">Notification Type</th>
                                <th width="8%">Date of Entry</th>
                                <th width="8%">Notification Order No</th>
                                <th width="8%">Notification Order Date</th>
                                <th width="7%">Relieve Order No</th>
                                <th width="8%">Relieve Order Date</th>
                                <th width="8%">Relieved on<br />Date</th>
                                <th width="8%">Relieved on Time</th>
                                <th width="8%">Due Date of<br />Joining</th>
                                <th width="8%">Due Time of<br />Joining</th>
                                <th width="8%">Print in<br />Service Book</th>
                                <th width="8%">Modified By</th>
                                <th width="20%" colspan="2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${relievelist}" var="rlist">
                                <c:if test="${rlist.printInServiceBook eq 'N'}">
                                    <tr style="background: #FFB9B9;">
                                </c:if>
                                <c:if test="${empty rlist.printInServiceBook || rlist.printInServiceBook eq 'Y'}">
                                    <tr>
                                </c:if>
                                    <td>${rlist.notType}</td>
                                    <td>${rlist.notdoe}</td>
                                    <td>${rlist.notordno}</td>
                                    <td>${rlist.notorddt}</td>
                                    <td>${rlist.rlvordno}</td>
                                    <td>${rlist.rlvorddt}</td>
                                    <td>${rlist.rlvondt}</td>
                                    <c:if test="${not empty rlist.rlvontime && rlist.rlvontime == 'FN'}">
                                        <td>FORE NOON</td>
                                    </c:if>
                                    <c:if test="${not empty rlist.rlvontime && rlist.rlvontime == 'AN'}">
                                        <td>AFTER NOON</td>
                                    </c:if>
                                    <c:if test="${empty rlist.rlvontime}">
                                        <td>&nbsp;</td>
                                    </c:if>
                                    <td>${rlist.rlvduedt}</td>
                                    <c:if test="${not empty rlist.rlvduetime && rlist.rlvduetime == 'FN'}">
                                        <td>FORE NOON</td>
                                    </c:if>
                                    <c:if test="${not empty rlist.rlvduetime && rlist.rlvduetime == 'AN'}">
                                        <td>AFTER NOON</td>
                                    </c:if>
                                    <c:if test="${empty rlist.rlvduetime}">
                                        <td>&nbsp;</td>
                                    </c:if>
                                    <td>
                                        <c:if test="${rlist.printInServiceBook eq 'N'}">
                                            No
                                        </c:if>
                                        <c:if test="${empty rlist.printInServiceBook || rlist.printInServiceBook eq 'Y'}">
                                            Yes
                                        </c:if>
                                    </td>
                                    <td>${rlist.modifiedby}</td>
                                    <td width="10%">
                                        <c:if test="${rlist.notType ne 'TRANSFER' && rlist.notType ne 'PROMOTION' && rlist.notType ne 'DEPUTATION'}">
                                            <c:if test="${empty rlist.rlvid}">
                                                <a href="entryRelieve.htm?notId=${rlist.notid}&rlvId=${rlist.rlvid}">Relieve from Post</a>
                                            </c:if>
                                            <c:if test="${not empty rlist.rlvid}">
                                                <a href="entryRelieve.htm?notId=${rlist.notid}&rlvId=${rlist.rlvid}"> Edit </a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td width="10%">
                                        <c:if test="${not empty rlist.rlvid && rlist.rlvduetime != '' && rlist.notType != 'RETIREMENT'}">
                                            <c:if test="${empty rlist.lpCFilePath }">
                                                <a href="generateLPC.htm?rlvId=${rlist.rlvid}" class="btn btn-danger">Generate LPC</a>
                                            </c:if>
                                            <c:if test="${ not empty rlist.lpCFilePath }">
                                                <a href="downlaodLPCFile.htm?rlvId=${rlist.rlvid}" class="btn btn-info">Download LPC</a>
                                            </c:if>        
                                        </c:if>
                                    </td> 
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <span style="color: #F00000; font-weight: bold; font-size: 15px;">Red Color row indicates not to display in Service Book</span>
                </div>
            </div>
        </div>
    </body>
</html>

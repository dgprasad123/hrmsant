<%-- 
    Document   : GrievanceCommunications
    Created on : Jan 15, 2018, 12:39:01 PM
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
        <div class="inner-block" style="padding: 5px 15px 5px 15px;">
            <div class="blank" align="center">
                <h2>Grievance Communications</h2>
                <table class="table table-bordered table-hover">
                    <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                        <td colspan="2">Details of ${grievanceDetail.ackno}</td>
                    </tr>
                    <tr>
                        <td><b>Acknowledgement Number<b>:</b></b></td>
                        <td>${grievanceDetail.ackno}</td>
                    </tr>
                    <tr>
                        <td><b>Date of Grievance:</b></td>
                        <td>${grievanceDetail.grievanceTime}</td>
                    </tr>
                    <tr>
                        <td><b>Category:</b></td>
                        <td>${grievanceDetail.category}</td>
                    </tr>
                    <tr>
                        <td><b>Office Name:</b></td>
                        <td>${grievanceDetail.offname}</td>
                    </tr>
                    <tr>
                        <td><b>Grievance:</b> </td>
                        <td>${grievanceDetail.grievanceDetail}</td>
                    </tr>
                    <tr>
                        <td><b>Attachment(if any?):</b> </td>
                        <td>
                            <c:if test="${not empty grievanceDetail.attachementName}">
                                <a target="_blank" href="downloadAttchment.htm?attachementId=${grievanceDetail.attachementId}">${grievanceDetail.attachementName}</a>
                            </c:if>

                        </td>
                    </tr>
                    <tr>
                        <td><b>Source:</b> </td>
                        <td>${grievanceDetail.source}</td>
                    </tr>
                </table>
                <table class="table table-bordered table-hover">
                    <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                        <td colspan="2">Status</td>
                    </tr>
                    <tr>
                        <td><b>Status:</b> </td>
                        <td>
                            <c:if test="${grievanceDetail.isdisposed == 'Y'}">
                                Disposed
                            </c:if>
                            <c:if test="${grievanceDetail.isrejected == 'Y'}">
                                Rejected
                            </c:if>
                            <c:if test="${grievanceDetail.isforwarded == 'Y'}">
                                Forwarded
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Remark By Authority:</b> </td>
                        <td>${grievanceDetail.source}</td>
                    </tr>
                </table>
                <table width="600" align="center" style="width:1000px" class="table table-bordered table-hover">
                    <thead>
                        <tr bgcolor="#3E6A00" style="color:#FFFFFF;font-weight:bold;">
                            <td>Sl No</td>
                            <td>From</td>
                            <td>To</td>
                            <td>Remarks</td>
                            <td>Time</td>
                            <td>Attachment</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${communicationList}" var="communication" varStatus="cnt">
                            <tr bgcolor="#FFFFFF">
                                <td>${cnt.index+1}</td>
                                <td>${communication.fromEmployee}</td>
                                <td>${communication.toEmployee}</td>
                                <td><p>${communication.remark}</p></td>
                                <td>${communication.commTime}</td>
                                <td align="center"></td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>

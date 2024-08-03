<%-- 
    Document   : EmployeeGrievanceDtls
    Created on : Jan 10, 2018, 3:09:53 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .blank h2 {
                color: #68ae00;
                font-family: "Carrois Gothic",sans-serif;
                font-size: 2em;
                margin-bottom: 1em;
            }
        </style>
    </head>
    <body>
        <div class="inner-block" style="padding: 5px 15px 5px 15px;">
            <div class="blank">
                <h2>Grievance Details</h2>
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
                <c:if test="${(grievanceDetail.isdisposed == 'Y')}">
                    <table class="table table-bordered table-hover">
                        <tbody><tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                                <td colspan="2">Current Status: Disposed</td>  
                            </tr>
                            <tr>
                                <td>Final Order:</td>
                                <td>
                                    ${grievanceDetail.finalRemark}
                                    <c:if test="${not empty grievanceDetail.attachementName}">
                                        <a target="_blank" href="downloadAttchment.htm?attachementId=${grievanceDetail.attachementId}">${grievanceDetail.attachementName}</a>
                                    </c:if>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="(grievanceDetail.isrejected == 'Y')}">
                    <table class="table table-bordered table-hover">
                        <tbody><tr style="font-weight:bold;background:#C12121;color:#FFFFFF;">
                                <td colspan="2">Current Status: Rejected</td>  
                            </tr>
                            <tr>
                                <td>Final Order:</td>
                                <td>
                                    ${grievanceDetail.finalRemark}
                                    <c:if test="${not empty grievanceDetail.attachementName}">
                                        <a target="_blank" href="downloadAttchment.htm?attachementId=${grievanceDetail.attachementId}">${grievanceDetail.attachementName}</a>
                                    </c:if>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </c:if>
                <form:form action="saveEmployeeGrievance.htm" commandName="EmployeeGrievanceForm" method="post">
                    <table class="table table-bordered table-hover">
                        <tr bgcolor="#FFFFFF">
                            <td></td>
                            <td>                                
                                <input type="submit" name="action" value="Cancel" class="btn btn-success"/>                                                        
                            </td>	
                        </tr> 
                    </table>
                </form:form>
            </div>
        </div>
    </body>
</html>

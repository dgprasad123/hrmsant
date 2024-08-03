<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="100%" cellspacing="0" style="font-size:16px; font-family:verdana;font-weight:bold;">
                        <tr style="height:40px;">
                            <td align="center">
                                HRMS ID - <c:out value="${EMPID}"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" style="border-bottom: 1px solid black;">
                                NAME - <c:out value="${EMPNAME}"/>
                            </td>
                        </tr>
                    </table>
                </div>	                                 
            </div>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="100%" cellspacing="0" style="font-size:16px; font-family:verdana;font-weight:bold;">
                        <tr style="height:40px;">
                            <td align="center">
                                COMPLETED TASKS LIST
                            </td>
                        </tr>
                    </table>
                </div>	                                 
            </div>
            <div align="center" style="width:100%;">
                <div style="background-color:#FFFFFF;margin-left:5px;margin-right:10px;"> 
                    <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0" style="border:1px solid black;font-family:Verdana;">
                        <thead>
                            <tr style="height:50px;font-family:Verdana;font-size:12px;">
                                <th width="10%" style="border:1px solid black;">TASK NAME</th>
                                <th width="50%" style="border:1px solid black;text-align:center;">INITIATED BY</th>
                                <th width="10%" style="border:1px solid black;text-align:center;">INITIATED ON</th>
                                <th width="10%" style="border:1px solid black;text-align:center;">SUBMITTED ON</th>
                                <th width="20%" style="border:1px solid black;text-align:center;">STATUS</th>
                            </tr>
                        </thead>
                        <c:if test="${not empty COMPLETEDTASKS}">
                            <c:forEach var="list" items="${COMPLETEDTASKS}">
                                <tr style="font-size:12px;">
                                    <td style="padding-left:40px;">
                                        <c:out value="${list.processname}"/>
                                    </td>
                                    <td style="padding-left:20px;">
                                        <c:out value="${list.applicant}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${list.dateOfInitiationAsString}"/>
                                    </td>
                                    <td align="center">
                                        <c:out value="${list.submitted_on}"/>
                                    </td>
                                    <td style="padding-left:20px;">
                                        <c:out value="${list.status}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>

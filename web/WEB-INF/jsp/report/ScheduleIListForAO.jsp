<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>



        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading"></div>
                <div class="panel-body" style="height:400px; overflow: auto;">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Sl No</th>
                                <th>AER Submitted Office</th>
                                <th>DDO Code</th>
                                <th>Submitted On</th>
                                <th>Status</th>
                                <th align="center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${EstablishmentList}" var="establish">
                                <tr>
                                    <td> ${establish.serialno} </td>
                                    <td> ${establish.operatoroffName} </td>
                                    <td> ${establish.ddoCode} </td>
                                    <td> ${establish.submittedDate} </td>
                                    <td> 
                                        ${establish.status}
                                    </td>
                                    <td> 
                                        <c:if test="${not empty establish.aerId}">
                                            <a href="DownloadaerPDFReport.htm?aerId=${establish.aerId}&financialYear=${establish.fy}" target="_blank">
                                                <span class="fa fa-file-pdf-o" style="color:red"> </span>Schedule-I
                                            </a>
                                        </c:if>    
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </body>
</html>

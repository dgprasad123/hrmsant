<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <table class="table table-hover" width="90%">
                    <thead>
                        <tr>
                            <th>Sl No</th>
                            <th>Office Name</th>
                            <th>DDO Code</th>
                            <th>No of Employee</th>
                            <th>Download</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${officeList}" var="office" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>${office.offName} <span class="label label-primary">${office.offCode}</span></td>
                                <td>${office.ddoCode}</td>
                                <td>${office.noofemployee}</td>
                                <th><a href="downloadEmployeeExcel.htm?offCode=${office.offCode}"> <span class="fa fa-file-excel-o"></span> Download</a></th>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
    </body>
</html>
<%-- 
    Document   : DistrictOfficeList
    Created on : Jul 16, 2018, 2:41:01 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>District Employee Report</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">      
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <h2>List of Offices in this District</h2>           
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

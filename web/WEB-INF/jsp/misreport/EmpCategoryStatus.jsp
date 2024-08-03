<%-- 
    Document   : EmpIndvStatus
    Created on : Jul 18, 2018, 7:21:40 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="container">
            <div align="center" style="font-size:10pt;font-weight:bold;">EMPLOYEE CATEGORY DATA</div>
            <div align="center" style="font-size:10pt;font-weight:bold;">OF</div>
            <div align="center" style="font-size:10pt;font-weight:bold;">
                Office Name
            </div>
            <div align="right" style="font-size:12pt;">                
                <a href="">Export To Excel</a>                
            </div>
            <div style="margin-left: 10px;">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" id="reportgrid">
                    <tr style="height: 30px;">
                        <th style="text-align:left;font-size:8pt;" colspan="5" class="lastcolumn">
                            <a>ALL</a> -> <b>CATEGORY :</b><span style="color:#FF3333;margin-right:20px;">SC</span> -> <b>GENDER :</b><span style="color:#FF3333;margin-right:20px;">Male</span>
                        </th>
                    </tr>
                </table>
            </div>
            <div class="table-responsive" style="padding: 5px;">
                <table class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="5%">SL NO.</th>
                            <th width="15%">HRMS ID</th>
                            <th width="15%">GPF NO</th>
                            <th width="35%">NAME</th>
                            <th width="30%">POST</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${empList}" var="empobj" varStatus="cnt">
                            <tr>
                                <td>${cnt.index + 1}</td>
                                <td>${empobj.empid}</td>
                                <td>${empobj.gpfno}</td>
                                <td>${empobj.name}</td>
                                <td>${empobj.post}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>

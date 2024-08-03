<%-- 
    Document   : gpcWiseEmployeeList
    Created on : Dec 14, 2022, 4:24:46 PM
    Author     : Madhusmita
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script src="js/jquery.freezeheader.js"></script>
        <style>
            table th {
                padding-top: 12px;
                padding-bottom: 12px;
                //text-align: left;
                background-color: #a2d4ed;
                //color: white;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#dataTabl").freezeHeader();
            });
        </script>


    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">

                <div class="panel-body">
                    <table class="table table-bordered" id="dataTabl">
                        <thead>
                            <tr  style="background-color: #E1D7D5">
                                <th style="text-align: center; color: #000">Sl No</th>
                                <th style="text-align: center; color: #000">Employee Name</th>
                                <th style="text-align: center; color: #000">Hrms ID</th>
                                <th style="text-align: center; color: #000">Designation</th>
                                <th style="text-align: center; color: #000">Section Name</th>
                                <th style="text-align: center; color: #000">Bill Name</th>
                                <th style="text-align: center; color: #000">Office Name</th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${genericEmpList}" var="genPost" varStatus="cnt">
                                <tr> 
                                    <td style="text-align: center">${cnt.index+1}</td>
                                    <td>${genPost.empName}</td>
                                    <td style="text-align: center">${genPost.empId}</td>
                                    <td>${genPost.empPost}</td>
                                    <td>${genPost.sectionName}</td>
                                    <td>${genPost.billGrpName}</td>
                                    <td>${genPost.offName}</td>
                                </tr>
                            </c:forEach>                     

                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </body>
</html>

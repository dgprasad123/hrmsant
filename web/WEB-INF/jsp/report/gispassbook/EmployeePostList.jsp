<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
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
        <style>
                table th {
                    padding-top: 12px;
                    padding-bottom: 12px;
                    text-align: left;
                    background-color: #4CAF50;
                    color: white;
                }
        </style>    

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3 class="alert alert-info">This report is generated comparing pay bill of ${monthString}-${yearString} with current status </h3>    
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr  class="thead-dark">
                                <th >SL NO</th>
                                <th>Emp Id</th>
                                <th>Emp Name</th>
                                <th>GPF NO</th>
                                <th>Current Status </th>
                                <th>Current Salary</th>
                                <th>Previous Salary </th>
                                <th>Current Designation</th>
                                <th>Previous Designation</th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${empdetails}" var="emodet" varStatus="cnt">
                                <tr>    
                                    <td>${cnt.index+1}</td>
                                    <td>${emodet.empId}</td>
                                    <td>${emodet.empName}</td>
                                      <td>${emodet.gfpNo}</td>
                                    <td>${emodet.currentStatus}</td>                                   
                                    <td>${emodet.currentSal}</td>
                                    <td>${emodet.prevSal}</td>
                                    <td>${emodet.curPost}</td>
                                    <td>${emodet.prevPost}</td>
                                </tr>

                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </body>
</html>

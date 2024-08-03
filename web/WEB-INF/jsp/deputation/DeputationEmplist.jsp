<%-- 
    Document   : DeputationEmplist
    Created on : 13 Dec, 2023, 10:51:45 AM
    Author     : Adarsh
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script>

            function addTab(title,url) {
                if ($('#tt').tabs('exists', title)) {
                    $('#tt').tabs('select', title);
                } else {
                    var content = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"><\/iframe>';
                    $('#tt').tabs('add', {
                        title: title,
                        content: content,
                        closable: true
                    });
                }
            }
        </script>
    </head>
    <body style="background-color: #FFFFFF;">
    <form:form action="EmployeeOnDeputationList.htm" method="POST" commandName="deputationForm" class="form-inline">
        <form:hidden path="pendingAtempId"/>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center"
                     style="background-color: #0071c5;color: #ffffff;font-size: xx-large;">Deputation Employee List
                </div>

                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="8%">Hrms Id</th>
                                <th width="8%"> GPF NO</th>
                                <th width="12%">Employee Name</th>
                                <th width="25%">Deputed From Office</th>
                                <th width="25%">Designation</th>
                                <th width="10%">Action</th>
                                <th width="20%">View</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach var="deputation" items="${deptList}">
                                <tr>
                                    <td>${deputation.empid}</td>
                                    <td>${deputation.gpfNo}</td>
                                    <td>${deputation.empName}</td>
                                    <td>${deputation.notifyingSpc}</td>
                                    <td>${deputation.postedSpc}</td>
                                    <td><a target="_blank" href="getRollWiseLinkDC.htm?nodeID=${deputation.empid}" class="label label-danger">Administration</a></td>
                                    <td><a href="DeputationEmployeePaySlipList.htm?empid=${deputation.empid}" target="_blank" >Pay Slip</a></td>
                                    <td><a href="DeputationPaySlipContribution.htm?empid=${deputation.empid}" target="_blank">Contribution Details</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </form:form>
</body>
</html>

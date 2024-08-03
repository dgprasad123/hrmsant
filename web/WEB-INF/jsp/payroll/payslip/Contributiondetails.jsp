<%-- 
    Document   : Contributiondetails.jsp
    Created on : 11 Jan, 2024, 11:23:43 AM
    Author     : Adarsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Contribution Details</title>
        <!--        <link href="css/bootstrap.min.css" rel="stylesheet">
                <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">-->
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style>

            .container-fluid {
                padding-right: 15px;
                padding-left: 15px;
                margin-right: auto;
                margin-left: auto;
            }
            body {
                background-color:  #ffffff;
                color: #ecf0f1; 
                font-family: 'Arial', sans-serif;
                margin: 0;
                padding: 0;
            }

            .container-fluid {
                margin-top: 20px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
                border: 1px solid #34495e; 
            }

            th, td {
                border: 1px solid #34495e; 
                padding: 15px;
                text-align: center;
            }

            th {
                background-color: #2c3e50;
                color: #ecf0f1; 
            }

            .btn-danger {
                background-color: #e74c3c; 
                border-color: #e74c3c;
                color: #fff;
                padding: 10px 15px;
                border-radius: 5px;
                cursor: pointer;
            }

            .form-control {
                width: 100%;
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #34495e; 
                border-radius: 5px;
                box-sizing: border-box;
                color: #2c3e50; 
                font-size: 15px; font-family: 'Roboto', sans-serif;
            }
            .dark-text {
                color: #2c3e50;
            }

            .employee-details {
                display: flex;
                justify-content: space-around;

            }

            .employee-details h5 {
                margin: 6px 0;
                color: #2c3e50;
            }
            .custom-button{
                font-size: 12px; background-color: #4CAF50; color: #fff; border: 2px solid #4CAF50; padding: 10px 20px; border-radius: 8px;
            }

        </style>

        <script type="text/javascript">
            function showPaylist() {
                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();

                if (year == "") {
                    alert("Please select Year");
                    return false;
                }

                if (month == "") {
                    alert("Please select Month");
                    return false;
                }



            }
        </script>
    </head>
    <body>
        <form:form action="PayslipContribution.htm" method="POST" commandName="contribution">

            <form:hidden path="empId" id="empid" value="${employeeDetails.empid}"/>
            <div class="container-fluid">
                <div align="center" class="dark-text" style="border: 2px solid #000000; background-color: #f0f0f0; margin-bottom: 20px;">
                    <h1>Contribution Details</h1>
                </div>

                <div class="employee-details">
                    <h5><b>Employee Name:</b><span><b>${employeeDetails.empName}</b></span></h5>
                    <h5><b>HrmsId:</b><span><b>${employeeDetails.empid}</b></span></h5>
                    <h5><b>Gpf No:</b><span><b>${employeeDetails.gpfNo}</b></span></h5>
                </div>

                <div class="panel panel-default">

                    <div class="panel-heading">
                        <table class="table table-bordered">

                            <tr>
                                <td width="20%" align="center" class="dark-text">
                                    Select Year:
                                </td>
                                <td width="20%">
                                    <form:select path="sltYear" id="sltYear" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="2022">2022</form:option>
                                        <form:option value="2023">2023</form:option>
                                        <form:option value="2024">2024</form:option>
                                    </form:select>
                                </td>

                                <td width="20%" align="center" class="dark-text">
                                    Select Month:
                                </td>
                                <td width="20%">
                                    <form:select path="sltMonth" id="sltMonth" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="0">Jan</form:option>
                                        <form:option value="1">Feb</form:option>
                                        <form:option value="2">Mar</form:option>
                                        <form:option value="3">Apr</form:option>
                                        <form:option value="4">May</form:option>
                                        <form:option value="5">Jun</form:option>
                                        <form:option value="6">Jul</form:option>
                                        <form:option value="7">Aug</form:option>
                                        <form:option value="8">Sep</form:option>
                                        <form:option value="9">Oct</form:option>
                                        <form:option value="10">Nov</form:option>
                                        <form:option value="11">Dec</form:option>
                                    </form:select>
                                </td>
                                <td width="20%" align="center">
                                    <button type="submit" class="btn btn-danger" onclick="showPaylist()">Ok</button>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="15%">Month-Year</th>
                                    <th width="15%">Basic Pay</th>
                                    <th width="15%">Leave Contribution</th>
                                    <th width="15%">Pension Contribution</th>
                                    <th width="15%">Action</th>

                                </tr> 
                            </thead>
                            <tbody>
                                <c:if test="${not empty contributionDetails.totalContribution}">
                                    <tr class="dark-text">
                                        <td>  
                                            <c:out value="${contributionDetails.month_year}"/>
                                        </td>
                                        <td>
                                            <c:out value="${contributionDetails.curBasic}"/>
                                        </td>
                                        <td>
                                            <c:out value="${contributionDetails.leaveContribution}"/>
                                        </td>
                                        <td>
                                            <c:out value="${contributionDetails.totalPensionContribution}"/>
                                        </td>                                          
                                        <td>
                                            <c:if test="${not empty contributionDetails}">
                                                <button type="button" onclick="processChallan()" class="custom-button" style="font-weight:bold;" >Process Challan</button>
                                            </c:if>

                                        </td>
                                    </tr>
                                </c:if>
                                    <c:if test="${empty contributionDetails.totalContribution}">
                                        <tr>
                                            <td class="dark-text" colspan="5"><h3>DATA NOT AVAILABLE</h3></td>
                                        </tr>
                                    </c:if>

                            </tbody>

                        </table>
                    </div>
                    <div class="panel-footer">
                        <span style="color:red"><strong>Contribution Details of only Token Generated Bills are shown in the list.</strong></span>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

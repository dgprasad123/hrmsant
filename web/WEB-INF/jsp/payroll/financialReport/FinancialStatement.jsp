<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.css">
        <style type="text/css">
            @media print {
                tr {
                    page-break-after: always;
                    display: block;
                }
            }
        </style>
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#financialTable').DataTable();
            });
        </script>
        <style>
            .edit-icon {
                text-decoration: none; /* Remove underline */
                color: #007bff; /* Set the color to blue (you can change it to your desired color) */
                font-size: 25px; /* Set the font size */
            }

            .edit-icon:hover {
                color: #0056b3; /* Change color on hover */
            }
        </style>

    </head>
    <body style="margin:0px" class="Background-Color:#FFFFFF;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2 style="text-align:center; margin-bottom: 20px;"><strong>FINANCIAL STATEMENT REPORT</strong></h2>
                    <div align="center" style="width:100%;">
                        <div style="height:30px;">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:16px;margin-top: 15px;">
                                <tr>
                                    <td align="center" width="10%" align="center" style="font-family:Verdana;font-size:16px;">
                                        <span style="font-weight:bold">Employee Name</span>:&nbsp;&nbsp;&nbsp;
                                        <span>${employee.fname}</span> <span>${employee.mname}</span> <span>${employee.lname}</span>
                                    </td>
                                    <td align="center" width="10%" style="font-family:Verdana;font-size:16px;">
                                        <span style="font-weight:bold">D.O.J</span>:&nbsp;&nbsp;&nbsp;&nbsp;
                                        <span>${employee.dateofjoining}</span>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div style="height: 20px;"></div>
                    <table id="financialTable" class="table table-bordered">
                        <thead style="background-color: #f2f2f2;">  
                            <tr style="height:45px;">
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="10%">SL NO</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="10%">WFF</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="10%">Basic Pay</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="10%">Grade Pay</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="30%">Remarks</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;" width="30%">Drawn</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% int serialNumber = 1;%>
                            <c:forEach var="finance" items="${empFinanceStateMent}">
                                <tr style="height:45px;">
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="10%"><%= serialNumber++%></td>
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="10%">${finance.ordDate}</td>
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="10%">${finance.pay}</td>
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="10%">${finance.gp}</td>
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="30%">${finance.entryType} 
                                        ${finance.incrementType == 'A' ? '(Annual Increment)' : finance.incrementType == 'T' ? '(Antedated)'
                                          : finance.incrementType == 'S' ? '(Stagnation Increment)': finance.incrementType == 'D' ? '(Advance Increment)'
                                          : finance.incrementType == 'P' ? '(Previous)': ''}</td>
                                    <td class="printLabel" style="text-align:center;font-size: 14;" width="30%">
                                        <a href="#" class="edit-icon">&#9998;</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>     
    </body>
</html>

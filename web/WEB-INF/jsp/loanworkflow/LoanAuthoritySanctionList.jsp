<%-- 
    Document   : Increment Proposal List
    Created on : 20 Jun, 2016, 12:14:12 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loan Authority Sanction List</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>        
        <script type="text/javascript">
 

        </script>


    </head>

    <body>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="font-size:15pt;">Loan Authority Sanction List</strong>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr bgcolor="#FAFAFA">
                                    <th>Loan Id</th>
                                    <th>Loan Type</th>
                                    <th>Loanee Id</th>
                                    <th>Name</th>
                                    <th>Designation</th>
                                    <th>GPF No.</th>
                                    <th>IFMS Loan ID</th>
                                    <th>Status</th>
                                    <th>Saction Order</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${sanctionList}" var="sList">
                                    <tr>
                                        
                                        <td>${sList.loanId}</td>
                                        <td>${sList.loanType}</td>
                                        <td>${sList.loaneeId}</td>
                                        <td>${sList.name}</td>
                                        <td>${sList.designation}</td>
                                        <td>${sList.gpfNo}</td>
                                        <td>${sList.ifmsLoanId}</td>
                                        <td>${sList.status}</td>
                                        <td align="center"><c:if test="${sList.btnName == 'S'}"></c:if><a href="DownloadPDF.htm?taskId=${sList.taskId}" target="_blank"><img src="images/pdf_icon.png" /></a></td>
                                        <td><c:if test="${sList.btnName == 'B'}"><input type="button" class="btn btn-success" value="Submit to IFMS" /></c:if></td>
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL WINDOW FOR FORWARD MPR WORK STARTS -->    
                    
                </div>
            </div>
    </body>
</html>




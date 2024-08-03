<%-- 
    Document   : AddIncrementMasterData
    Created on : 28 Jun, 2016, 3:46:09 PM
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
        <title>Edit Increment Proposal List</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script> 


       
        
    </head>
    <body>
        <form:form action="monthlyretirement.htm" commandName="IncrementProposal" method="post" id="monthlyretirement">

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;">
                        RETIREMENT LIST FOR THE MONTH OF : ${IncrementProposal.monthasString}
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No.</th>
                                    <th width="5%"> HRMS ID </th>
                                     <th width="5%"> GPF No </th>
                                    <th width="20%">Employee Name</th>
                                    <th width="20%">Post</th>
                                    <th width="20%">Date Of <br> Retirement</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EmpList}" var="list" varStatus="counter">
                                    <tr>
                                        <td>${counter.count}</td>
                                        <td>${list.empId}</td>
                                        <td>${list.gpfno}</td>
                                        <td>${list.empname}</td>
                                        <td>${list.post}</td>
                                        <td>${list.dateOfRetirement}</td>
                                    </tr>
                                </c:forEach>
                            <input type="hidden" id="main_counter" value="${EmpList.size()}" />
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                      
                    </div>
                </div>
            </div>
        </form:form>


    </body>
</html>

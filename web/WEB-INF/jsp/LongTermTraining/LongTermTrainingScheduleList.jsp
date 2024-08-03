<%-- 
    Document   : LongTermTrainingScheduleList
    Created on : Sep 4, 2021, 2:05:16 PM
    Author     : Madhusmita
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Long Term Training</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
    </head>
    <body>
        <form:form id="fm" action="longTermTrainingList.htm" method="post" name="myForm" commandName="lttraining">
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Date Of Entry<br> in the<br> Service Book</th>
                                <th>Order No.</th>
                                <th>Training Name</th>
                                <th>From Date</th>
                                <th>To Date</th>
                                <th>Total No Of Days</th>
                                <th>Coordinator</th>
                                <th>Training <br> Place</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${lttrainingList}" var="traininglist" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>${traininglist.txtservice}</td>
                                    <td>${traininglist.txtOrdNo}</td>
                                    <td>${traininglist.trainingName}</td>
                                    <td>${traininglist.startDate}</td>
                                    <td>${traininglist.complDate}</td>
                                    <td>${traininglist.txttotday}</td>
                                    <td>${traininglist.txtcoordinator}</td>
                                    <td>${traininglist.txttrplace}</td>
                                    <td>
                                        <c:if test="${traininglist.isValidated eq 'N'}">
                                            <a href="getLtTrainingData.htm?notId=${traininglist.notId}">Edit</a> 
                                            <%--<a href="deletetrainingdata.htm?trainId=${traininglist.trainId}" onclick="return confirm('Are you sure to Delete Training Data?')">Delete</a>--%>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>     
                    </table>
                    <table class="table table-bordered">
                        <tr>
                        <input type="submit" name="new" value="New" class="btn btn-primary" /> 
                        </tr>
                    </table>
                </div>
            </div>

        </form:form>
    </body>
</html>


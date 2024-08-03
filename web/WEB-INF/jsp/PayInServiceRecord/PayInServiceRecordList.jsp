<%-- 
    Document   : PayInServiceRecordList
    Created on : Apr 18, 2022, 12:25:39 PM
    Author     : Madhusmita
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
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
        <form:form action="payInServiceRecord.htm" method="post"  commandName="pisrForm">
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>Sl No.</th>
                                <th>Date Of Entry</th>
                                <th>With Effect date</th>
                                <th>Scale of pay</th>
                                <th>Pay</th>
                                <th>DA</th>
                                <th>Additional DA</th>
                                 <th>Leave Salary</th> 
                                <th>Total Pay</th>                                                             
                                <th>Edit</th>
                                <th>Delete</th> 
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${serviceList}" var="servicePayList" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${servicePayList.doe}</td>
                                    <td>${servicePayList.wefdate}</td>
                                    <td>${servicePayList.payScale}</td>
                                    <td>${servicePayList.pay}</td>
                                    <td>${servicePayList.da}</td>
                                    <td>${servicePayList.ada}</td>                                    
                                    <td>${servicePayList.leaveSal}</td>
                                    <td>${servicePayList.totPay}</td>
                                    <td><a href="getPisrData.htm?srpId=${servicePayList.srpId}">Edit</a></td> 
                                    <td><a href="deletePisrdata.htm?srpId=${servicePayList.srpId}" onclick="return confirm('Are you sure to Delete ?')">Delete</a></td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <%--<button type="button" name="btnSave" id="btnSave" value="Add New" class="btn btn-primary" >Add New</button>--%>
                    <input type="submit" name="btnSave" id="btnSave" value="Add New" class="btn btn-primary" />
                </div>
            </div>
        </form:form>
    </body>
</html>

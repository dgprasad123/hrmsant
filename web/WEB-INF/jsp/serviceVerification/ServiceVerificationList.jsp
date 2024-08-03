<%-- 
    Document   : ServiceVerificationList
    Created on : 20 Jul, 2018, 3:52:03 PM
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
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {

            });
            function getMonth(monthStr) {
                var d = Date.parse(monthStr + "1, 2012");
                if (!isNaN(d)) {
                    return new Date(d).getMonth();
                }
                return -1;
            }
        </script>
    </head>
    <body>
        <c:set var="totalqualifying" value="${0}"/>
        <c:forEach items="${ServiceVerifyList}" var="list">
            <c:set var="totalqualifying" value="${totalqualifying + list.qDays}" />
        </c:forEach>
        <form:form action="serviceVerifyEntryController.htm" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" style="font-size:20pt;font-weight:bold;">
                        <strong>HRMS ID: </strong>${command.txtempid} <br><strong>Date of Joining: </strong>${doj}
                    </div><br />

                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%">Date of Verification</th>
                                <th width="10%">Verified  From Date  </th> 
                                <th width="10%"> From Time</th>
                                <th width="10%">Verified Till Date </th> 
                                <th width="10%">To Time</th>
                                <th width="10%">Qualifying Service in Days</th>
                                <th width="10%">Edit</th>                               
                            </tr>
                        </thead>
                        <tbody>                            
                            <c:forEach items="${ServiceVerifyList}" var="list">                                
                                <tr>
                                    <td>${list.txtdoe}</td>
                                    <td>${list.txtfdate}</td>
                                    <td>${list.sltftime}</td>
                                    <td>${list.txttdate}</td>
                                    <td>${list.sltttime}</td>
                                    <td>${list.qDays}</td>
                                    <td>
                                        <c:if test="${list.isValidated eq 'N'}">
                                            <a href="serviceVerifyEditController.htm?txtsvid=${list.txtsvid}&txtempid=${list.txtempid}">Edit</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr style="font-weight:bold;font-size:14pt;background:#EAEAEA;">
                                <td colspan="5" align="right">Total</td>
                                
                                <td>${totalqualifying}</td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <div class="row">
                    <form:hidden class="form-control" path="txtempid" id="txtempid"/>
                    <input type="submit" name="action" value="Add New Entry" class="btn btn-success"/>
</div>
                    <div class="row" style="font-size:20pt;font-weight:bold;">
                        <div class="col-md-4">
                            <b>Total Service Days till ${currentdate2} - ${noofdaysofserviceperiod}</b>
                        </div>
                        <div class="col-md-4">
                            <span style="color: green;">
                                Qualifying Service Days - ${totalqualifying}
                            </span>
                        </div>
                        <div class="col-md-4">
                            <span style="color:red;">
                            Non-Qualifying Service Days - ${nonqualifyingdays}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>



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

        </script>
    </head>
    <body>
        <form:form action="newDeputation.htm" method="post" commandName="deputationForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Deputation 
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="9%">Notification Order No</th>
                                    <th width="10%">Notification Order Date</th>
                                    <th width="20%">Posting Office</th>
                                    <th width="10%">Period From</th>
                                    <th width="10%">Period To</th>
                                    <th colspan="2" width="10%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${DepuList}" var="dlist">
                                    <tr>
                                        <td>${dlist.notOrdNo}</td>
                                        <td>${dlist.notOrdDt}</td>
                                        <td>${dlist.postingOffice}</td>
                                        <td>${dlist.periodFrom}</td>
                                        <td>${dlist.periodTo}</td>
                                        <td>
                                            <c:if test="${dlist.isValidated eq 'N'}">
                                                <a href="editDeputation.htm?notId=${dlist.notId}">Edit</a>
                                            </c:if>
                                            <c:if test="${dlist.isValidated eq 'Y'}">
                                                <a href="viewDeputation.htm?notId=${dlist.notId}">View</a>
                                            </c:if>
                                        </td>
                                        <td><a href="entryRelieve.htm?notId=${dlist.notId}&rlvId=${dlist.relieveId}">Relieve</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">New Deputation</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

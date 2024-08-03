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
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
    </head>
    <body>
        <form:form action="newLeaveSurrender.htm" method="POST" commandName="leaveSanctionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="20%">Date of Entry</th>
                                    <th width="15%">From Date</th>
                                    <th width="15%">To Date</th>
                                    <th width="15%">Surrendered in<br />Block Period Year</th>
                                    <th width="15%">To Year</th>
                                    <th width="10%">Surrender Days</th>
                                    <th width="5%" align="center">Action</th>
                                    <th width="5%" align="center">SB Language</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${LeaveSanctionList}" var="leavelist">
                                    <tr>
                                        <td>${leavelist.doe}</td>
                                        <td>${leavelist.frmDate}</td>
                                        <td>${leavelist.toDate}</td>
                                        <td>${leavelist.sltFromYear}</td>
                                        <td>${leavelist.sltToYear}</td>
                                        <td>${leavelist.txtDays}</td>
                                        <td>
                                            <c:if test="${leavelist.isValidated eq 'N'}">
                                                <a href="editLeaveSurrender.htm?hnotid=${leavelist.notId}">Edit</a> &nbsp;
                                            </c:if>
                                            <c:if test="${leavelist.isValidated eq 'Y'}">
                                                <a href="viewLeaveSurrender.htm?hnotid=${leavelist.notId}">View</a> &nbsp;
                                            </c:if>
                                        </td>
                                        <td><a href="#" data-toggle="tooltip" title="${leavelist.sblanguage}"><span class="glyphicon glyphicon-info-sign"></span></a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="ordType" id="ordType" value="02"/>
                        <button type="submit" class="btn btn-default">Add New</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

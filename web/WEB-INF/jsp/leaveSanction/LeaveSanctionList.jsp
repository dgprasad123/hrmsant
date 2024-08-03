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
    </head>
    <body>
        <form:form action="newLeaveSanction.htm" method="POST" commandName="leaveSanctionForm">
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
                                    <th width="20%">Type of Leave</th>
                                    <th width="15%">From Date</th>
                                    <th width="15%">To Date</th>
                                    <th width="15%">SuffixFrom<br />Date</th>
                                    <th width="15%">SuffixTo<br />Date</th>
                                    <th width="15%">PrefixFrom<br />Date</th>
                                    <th width="15%">PrefixTo<br />Date</th>
                                    <th width="15%">If Long Leave</th>
                                    <th width="15%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${LeaveSanctionList}" var="leavelist">
                                    <tr>
                                        <td>${leavelist.doe}</td>
                                        <td>${leavelist.leaveType}</td>
                                        <td>${leavelist.frmDate}</td>
                                        <td>${leavelist.toDate}</td>
                                        <td>${leavelist.suffixFrmDate}</td>
                                        <td>${leavelist.suffixToDate}</td>
                                        <td>${leavelist.prefixFrmDate}</td>
                                        <td>${leavelist.prefixToDate}</td>
                                        <td>${leavelist.ifLongLeave}</td>
                                        <td><a href="editLeaveSanction.htm?notId=${leavelist.notId}">Edit</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="ordType" id="ordType" value="01"/>
                        <button type="submit" class="btn btn-default">Add New</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

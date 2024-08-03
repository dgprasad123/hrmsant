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
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12"></div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%" rowspan="2">Date of Entry in the Service Book</th>
                                <th width="20%" rowspan="2">Name of the Exam</th>
                                <th width="30%" colspan="2">Period of Examination</th>
                                <th width="20%" rowspan="2">Result of Examination</th>
                                <th width="15%" rowspan="2" align="center">Action</th>
                            </tr>
                            <tr>
                                <th width="15%">From</th>
                                <th width="15%">To</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${deptexamlist}" var="dexamlist">
                                <tr>
                                    <td>${dexamlist.dateofentry}</td>
                                    <td>${dexamlist.examinationName}</td>
                                    <td>${dexamlist.fromDate}</td>
                                    <td>${dexamlist.toDate}</td>
                                    <td>
                                        <c:if test="${dexamlist.examinationResult eq 'Y'}">
                                            Cleared
                                        </c:if>
                                        <c:if test="${dexamlist.examinationResult eq 'N'}">
                                            Not Cleared
                                        </c:if>
                                    </td>
                                    <td><a href="EditDepartmentalExam.htm?empid=${dexamlist.empid}&examid=${dexamlist.examId}">Edit</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="NewDepartmentalExam.htm" method="POST" commandName="deptExamForm">
                        <button type="submit" class="btn btn-default">Add New</button>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>

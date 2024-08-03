<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 align="center">
                                Employee Wise Superannuation Projection Report 
                            </h2>
                            <h3 align="center">
                                <u>${officeName}</u>
                            </h3>

                        </div>
                    </div>
                    <hr />

                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="5%">Sl No</th>
                                <th width="10%">HRMS ID</th>
                                <th width="10%">GPF No</th>
                                <th width="20%">Name</th>
                                <th width="20%">Designation</th>
                                <th width="10%">Date of Joining</th>
                                <th width="10%">Date of Superannuation</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty emplist}">
                                <c:set var="GtotalPost" value="${0}" />
                                <c:forEach items="${emplist}" var="list" varStatus="count">

                                    <tr>
                                        <td><c:out value="${count.index + 1}"/></td>
                                        <td>
                                            <c:out value="${list.empid}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.gpfNo}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.designation}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.dateOfJoining}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.dateOfSuperannuation}"/>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </c:if>
                            <c:if test="${empty emplist}">
                                <tr>
                                    <td colspan="7" align="center">
                                        <h3>
                                            NO RECORDS
                                        </h3>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>

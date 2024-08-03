<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-header"></div>
                <div class="panel-body">
                    <table class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr style="background-color: #d4d4d4;">
                                <th colspan="5">Persons in Position : Group-A</th>
                            </tr>
                            <tr style="background-color: #d4d4d4;">
                                <th width="5%">SL No</th>
                                <th width="20%">Name of the Employee</th>
                                <th width="20%">Post Held</th>
                                <th width="20%">Level(12-17)</th>
                                <th width="20%">Consolidated Remuneration</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty groupAEmpList}">
                                <c:forEach items="${groupAEmpList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.post}"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>
                                            <c:out value="${list.basicsalary}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty groupAEmpList}">
                                <td colspan="5" align="center">No Records</td>
                            </c:if>
                        </tbody>
                    </table><br />
                    <table class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr style="background-color: #d4d4d4;">
                                <th colspan="5">Persons in Position : Group-B</th>
                            </tr>
                            <tr style="background-color: #d4d4d4;">
                                <th width="5%">SL No</th>
                                <th width="20%">Name of the Employee</th>
                                <th width="20%">Post Held</th>
                                <th width="20%">Level(12-17)</th>
                                <th width="20%">Consolidated Remuneration</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty groupBEmpList}">
                                <c:forEach items="${groupBEmpList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.post}"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>
                                            <c:out value="${list.basicsalary}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty groupBEmpList}">
                                <td colspan="5" align="center">No Records</td>
                            </c:if>
                        </tbody>
                    </table><br />
                    <table class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr style="background-color: #d4d4d4;">
                                <th colspan="5">Persons in Position : Group-C</th>
                            </tr>
                            <tr style="background-color: #d4d4d4;">
                                <th width="5%">SL No</th>
                                <th width="20%">Name of the Employee</th>
                                <th width="20%">Post Held</th>
                                <th width="20%">Level(12-17)</th>
                                <th width="20%">Consolidated Remuneration</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty groupCEmpList}">
                                <c:forEach items="${groupCEmpList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.post}"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>
                                            <c:out value="${list.basicsalary}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty groupCEmpList}">
                                <td colspan="5" align="center">No Records</td>
                            </c:if>
                        </tbody>
                    </table><br />
                    <table class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr style="background-color: #d4d4d4;">
                                <th colspan="5">Persons in Position : Group-D</th>
                            </tr>
                            <tr style="background-color: #d4d4d4;">
                                <th width="5%">SL No</th>
                                <th width="20%">Name of the Employee</th>
                                <th width="20%">Post Held</th>
                                <th width="20%">Level(12-17)</th>
                                <th width="20%">Consolidated Remuneration</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty groupDEmpList}">
                                <c:forEach items="${groupDEmpList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.post}"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>
                                            <c:out value="${list.basicsalary}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty groupDEmpList}">
                                <td colspan="5" align="center">No Records</td>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </body>
</html>

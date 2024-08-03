<%-- 
    Document   : NominationCompletedReport
    Created on : 8 Dec, 2020, 12:24:45 PM
    Author     : Surendra
--%>

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
                                Nomination Completed Report
                            </h2>
                            <h3 align="center">
                                <u> Nomination for ${nominationPost}</u>
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
                                <th width="10%">Dist/Establishment</th>
                                <th width="20%">Full Name</th>
                                <th width="10%">Category</th>
                                <th width="10%">DOB</th>
                                <th width="20%"> Date of enlistment with rank </th>
                                    <%-- <c:if test="${nominationPost eq '140858'}">--%>
                                <th width="20%"> Date of joining as SUB INSPECTOR </th>
                                <th width="20%"> Action </th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty listData}">

                                <c:forEach items="${listData}" var="list" varStatus="count">
                                    <c:set var="empcnt" value="0" scope="page"/>
                                    <c:forEach items="${list.value}" var="listemp">
                                        <c:set var="empcnt" value="${empcnt+1}" scope="page"/>
                                        <tr>
                                            <c:if test="${empcnt == 1}">
                                                <td rowspan="${list.value.size()}">${count.index + 1}</td>
                                                <td rowspan="${list.value.size()}"><c:out value="${list.key}"/></td>
                                            </c:if>
                                            <td>
                                                <c:out value="${listemp.fullname}"/>
                                            </td>
                                            <td>
                                                <c:out value="${listemp.category}"/>
                                            </td>
                                            <td>
                                                <c:out value="${listemp.dob}"/>
                                            </td>

                                            <td>
                                                <c:out value="${listemp.doeGov}"/>
                                            </td>
                                            <td>
                                                <c:out value="${listemp.jodInspector}"/>
                                            </td>

                                            <c:if test="${empcnt == 1}">
                                                <td rowspan="${list.value.size()}"><a href="RevertNominationFromRange.htm?nominationMasterId=${listemp.nominationMasterId}" onclick="return confirm('Are you sure to Revert?');">Revert</a></td>                                                
                                            </c:if>

                                        </tr>
                                    </c:forEach>
                                </c:forEach>

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



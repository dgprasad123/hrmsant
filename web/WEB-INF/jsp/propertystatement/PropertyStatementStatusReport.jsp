<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Property Statement Report</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
    </head>
    <body>
        <div id="wrapper">
            <div id="page-wrapper">

                <div class="container-fluid">
                    <div class="panel panel-primary">
                        <div class="panel-heading">PROPERTY STATEMENT STATUS REPORT</div>
                        <div class="panel-body" style="margin-top:20px;">

                            <h2 align="center">PROPERTY STATEMENT STATUS REPORT</h2>
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="10%">#</th>
                                        <th width="40%" style="text-align: center">Calendar Year</th>
                                        <th width="50%" style="text-align: center">No of Property Statement Submitted</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty propertstatementstatus}">
                                        <c:forEach var="list" items="${propertstatementstatus}" varStatus="count">
                                            <tr>
                                                <td>${count.count}</td>
                                                <td align="center" style="padding-left:40px;">
                                                    <c:if test="${list.value eq '2016-17'}">
                                                        <span>2016</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2017-18'}">
                                                        <span>2017</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2018-19'}">
                                                        <span>2018</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2019-20'}">
                                                        <span>2019</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2020'}">
                                                        <span>2020</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2021'}">
                                                        <span>2021</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2022'}">
                                                        <span>2022</span>
                                                    </c:if>
                                                    <c:if test="${list.value eq '2023'}">
                                                        <span>2023</span>
                                                    </c:if>
                                                </td>
                                                <td align="center" style="padding-left:40px;">
                                                    <c:out value="${list.label}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

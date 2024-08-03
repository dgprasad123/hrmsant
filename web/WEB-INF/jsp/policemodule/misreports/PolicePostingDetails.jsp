<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Posting Details</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <span style="display:block; text-align: center;"><h2>Posting Details</h2></span>
        <hr />
        <div class="row">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <c:if test="${not empty empSearchResult.employeeList}">
                    <c:forEach items="${empSearchResult.employeeList}" var="emp">
                        <div class="row">
                            <div class="col-sm-12" style="text-align: center;font-size:20px; font-weight: bold;">
                                <c:out value="${emp.fname}"/> <c:out value="${emp.mname}"/> <c:out value="${emp.lname}"/>
                            </div>
                        </div>
                        <div class="row" style="margin-top:30px;">
                            <div class="col-sm-4">
                                HRMS ID/GPF No
                            </div>
                            <div class="col-sm-8">
                                <c:out value="${emp.empid}"/> / <c:out value="${emp.gpfno}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Date of Birth
                            </div>
                            <div class="col-sm-8">
                                <c:out value="${emp.dob}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Father's Name
                            </div>
                            <div class="col-sm-8">
                                <c:out value="${emp.fatherName}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Current Post
                            </div>
                            <div class="col-sm-8">
                                <c:out value="${emp.post}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Date of Joining in Government
                            </div>
                            <div class="col-sm-8">
                                <c:out value="${emp.doeGov}"/>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
            <div class="col-sm-2"></div>
        </div>
        <div class="table-responsive" style="margin-top:20px;">
            <table class="table table-bordered table-hover table-striped" width="80%">
                <thead>
                    <tr>
                        <th width="5%">SL No</th>
                        <th width="20%">Post</th>
                        <th width="15%">District/Establishment</th>
                        <th width="13%">From Date</th>
                        <th width="13%">To Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${PostingDetails}" var="postinglist" varStatus="counter">
                        <tr>
                            <td>${counter.count}</td>
                            <td>${postinglist.postingDesignation}</td>
                            <td>${postinglist.postingDistrict}</td>
                            <td>${postinglist.postingfromDate}</td>
                            <td>${postinglist.postingtoDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>

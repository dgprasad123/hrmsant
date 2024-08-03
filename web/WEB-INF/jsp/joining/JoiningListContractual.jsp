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
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="9%">Order No</th>
                                <th width="9%">Order<br /> Date</th>
                                <th width="20%">Transfer from Post</th>
                                <th width="15%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${joininglist}" var="jlist">
                                <tr>
                                    <td>${jlist.notOrdNo}</td>
                                    <td>${jlist.notOrdDt}</td>
                                    <td>${jlist.transferFromPost}</td>
                                    <td>
                                        <c:if test="${empty jlist.hidForeignTransferId}">
                                            <a href="enterJoiningContractualData.htm?jId=&primaryTrId=${jlist.hidPrimaryTransferId}">Join Post</a>
                                        </c:if>
                                        <c:if test="${not empty jlist.joinid}">
                                            <a href="enterJoiningContractualData.htm?primaryTrId=&jId=${jlist.joinid}">Edit</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
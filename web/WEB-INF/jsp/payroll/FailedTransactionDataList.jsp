<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
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
                                <th width="5%">Sl No</th>
                                <th width="15%">HRMS ID<br/>NAME</th>
                                <th width="15%">Bill No<br/>Bill Date</th>
                                <th width="14%">Token No<br/>Date</th>
                                <th width="10%">Benf Amount</th>
                                <th width="15%">Benf Acc No<br/>IFS Code</th>
                                <th width="20%">Remarks</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty ftlist}">
                                <c:forEach items="${ftlist}" var="ftattr" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.empid}"/><br /><c:out value="${ftattr.empname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.billdesc}"/><br /><c:out value="${ftattr.billdate}"/>
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.tokenNo}"/><br /><c:out value="${ftattr.tokenDate}"/>
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.benfAmount}"/>
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.benfAcctNo}"/><br /><c:out value="${ftattr.ifsCode}"/>
                                        </td>
                                        <td>
                                            <c:out value="${ftattr.remarks}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </body>
</html>

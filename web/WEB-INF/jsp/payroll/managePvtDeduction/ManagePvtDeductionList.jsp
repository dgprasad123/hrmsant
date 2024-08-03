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
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="AddPvtDeductionAccount.htm" method="POST" commandName="command">
            <form:hidden path="billNo"/>
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
                                    <th width="30%">DDO</th>
                                    <th width="15%">Bill Name</th>
                                    <th width="10%">Month Year</th>
                                    <th width="10%">Amount</th>
                                    <th width="10%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${pvtdednlist}" var="pvtlist">
                                    <tr>
                                        <td>${pvtlist.ddoName}</td>
                                        <td>${pvtlist.billName}</td>
                                        <td>${pvtlist.month}</td>
                                        <td>${pvtlist.amount}</td>
                                        <td>
                                            <c:if test="${pvtlist.isDdo eq 'N'}">
                                                <a href="editManagePvtDednData.htm?pvtdednid=${pvtlist.pvtdednId}">Edit</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnListAccount" value="Add More Account" class="btn btn-default"/>
                        <input type="submit" name="btnListAccount" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

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
                                <th width="15%">Instrument No</th>
                                <th width="10%">Date of Deposit</th>
                                <th width="15%">Voucher No</th>
                                <th width="15%">Treasury Name</th>
                                <th width="15%">Amount in Rs</th>
                                <th width="15%" align="center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${gislist}" var="gislist">
                                <tr>
                                    <td>${gislist.instrumentNo}</td>
                                    <td>${gislist.dateOfDeposit}</td>
                                    <td>${gislist.voucherNo}</td>
                                    <td>${gislist.treasuryName}</td>
                                    <td>${gislist.amount}</td>
                                    <td><a href="editGISData.htm?gisid=${gislist.gisid}">Edit</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="GISNewPage.htm">
                        <button type="submit" class="btn btn-default">New</button>  
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>

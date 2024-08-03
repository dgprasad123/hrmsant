<%-- 
    Document   : GetUploadBillStatus
    Created on : 16 Mar, 2018, 12:35:08 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
    </head>
    <body>

        <div class="container">
            <div class="row row-centered pos">
                <div class="col-lg-2 col-xs-12 col-centered">
                    BILL NO 
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    ${command.billno}
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    BILL DATE 
                </div>
                <div class="col-lg-4 col-xs-12 col-centered">
                    ${command.billdate}
                </div>
            </div>
        </div>
        <hr />
        <div class="container">
            <div class="row row-centered pos">
                <div class="col-lg-12 col-xs-12 col-centered">
                    <span>ON </span> <span class="valuelbl">ACCEPTED BY TREASURY</span>
                </div>
            </div>
        </div>
        <br />
        <div class="container">
            <div class="row row-centered pos">
                <div class="col-lg-2 col-xs-12 col-centered">
                    TOKEN NO
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    ${command.tokenno}
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    TOKEN DATE
                </div>
                <div class="col-lg-4 col-xs-12 col-centered">
                    ${command.tokendate}
                </div>
            </div>
        </div>
        <hr />
        <div class="container">
            <div class="row row-centered pos">
                <div class="col-lg-12 col-xs-12 col-centered">
                    <span>ON  </span> <span class="valuelbl">VOUCHER PREPARED BY TREASURY</span>
                </div>
            </div>
        </div>
        <br />
        <div class="container">
            <div class="row row-centered pos">
                <div class="col-lg-2 col-xs-12 col-centered">
                    VOUCHER NO
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    ${command.voucherno}
                </div>
                <div class="col-lg-2 col-xs-12 col-centered">
                    VOUCHER DATE
                </div>
                <div class="col-lg-4 col-xs-12 col-centered">
                    ${command.voucherdate}
                </div>
            </div>
        </div>
        <hr />
        <div class="container" style="width:100%">
            <div class="row">
                <div class="col-lg-6 col-xs-12 col-centered" style="font-weight: bold;">
                    Bill Status
                </div>
                <div class="col-lg-6 col-xs-12 col-centered" style="font-weight: bold;">
                    Message
                </div>
            </div>
            <hr />
            <c:if test="${not empty command.errMsg}">
                <c:forEach var="list" items="${command.errMsg}">
                    <div class="row">
                        <div class="col-lg-5 col-xs-12 col-centered">
                            <c:out value="${list.billStatus}"/>
                        </div>
                        <div class="col-lg-7 col-xs-12" style="border-left: 1px solid black;">
                            <c:if test="${not empty list.message}">
                                <c:forEach var="listErr" items="${list.message}">
                                    <c:out value="${listErr}"/>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </c:if>

        </div>
    </body>
</html>

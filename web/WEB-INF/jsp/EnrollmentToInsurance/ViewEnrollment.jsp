<%-- 
    Document   : ViewEnrollment
    Created on : 21 Dec, 2021, 1:38:37 PM
    Author     : Surendra
--%>

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
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body style="font-family: Helvetica;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Employee Enrollment
                </div>
                <div class="panel-body">
                    <form:hidden path="schemetype" id="schemetype"/>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtNotOrdDt}"/>
                            </strong>                               
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Details of Notifying Authority
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOI'}">
                                <strong>
                                    Government of India
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>           

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${notideptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radnotifyingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${notioffice}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${dptnform.notifyingSpc}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                            
                     <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Name of Scheme
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.schemetype}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Insurance Account Number
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.insaccountno}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                       <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            With Effect Date
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.witheffectdate}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>     
                            <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                           Monthly Subscription Amount
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.subamount}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
      
                    
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Note
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${dptnform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="EnrollmentToInsurance.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>


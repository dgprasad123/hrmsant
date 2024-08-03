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
                    Employee Deputation
                </div>
                <div class="panel-body">
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
                            Cadre Status
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.sltCadreStatus eq 'CDR'}">
                                <strong>
                                    CENTRAL DEPUTATION RESERVATION
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.sltCadreStatus eq 'SDR'}">
                                <strong>
                                    STATE DEPUTATION RESERVATION
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2">
                            Sub Cadre Status
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.sltCadreStatus eq 'CDR'}">
                                <c:if test="${dptnform.sltSubCadreStatus eq 'EX-CADRE'}">
                                    <strong>
                                        Ex-Cadre
                                    </strong>
                                </c:if>
                                <c:if test="${dptnform.sltSubCadreStatus eq 'ORG'}">
                                    <strong>
                                        Organisation
                                    </strong>
                                </c:if>
                            </c:if>
                            <c:if test="${dptnform.sltCadreStatus eq 'SDR'}">
                                <c:if test="${dptnform.sltSubCadreStatus eq 'EX-CADRE'}">
                                    <strong>
                                        Ex-Cadre
                                    </strong>
                                </c:if>
                                <c:if test="${dptnform.sltSubCadreStatus eq 'FS'}">
                                    <strong>
                                        Foreign Service
                                    </strong>
                                </c:if>
                                <c:if test="${dptnform.sltSubCadreStatus eq 'ISD'}">
                                    <strong>
                                        Inter State Deputation
                                    </strong>
                                </c:if>
                            </c:if>
                        </div>
                    </div>       

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Details of Posting
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.radpostingauthtype eq 'GOI'}">
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
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${posteddeptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${dptnform.radpostingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${postedoffice}"/>
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
                                <c:out value="${dptnform.postedSpc}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;" id="fieldOfficeDiv">
                        <div class="col-lg-2">
                            Field Office
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${fieldoffice}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-12">
                            Deputation Period Details
                        </div>
                    </div>  

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            If Extension of Deputation Period
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${dptnform.chkExtnDptnPrd eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${dptnform.chkExtnDptnPrd ne 'Y'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            With Effect From
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtWEFrmDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.sltWEFrmTime}"/>
                            </strong>
                        </div>
                        <div class="col-lg-4"></div>
                    </div>  
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            Till Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.txtTillDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${dptnform.sltTillTime}"/>
                            </strong>
                        </div>
                        <div class="col-lg-4"></div>
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
                    <a href="DeputationList.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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
                    Employee Posting
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Notification Order No
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${postingform.txtNotOrdNo}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Notification Order Date
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${postingform.txtNotOrdDt}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Notifying Authority
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${postingform.radnotifyingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${postingform.radnotifyingauthtype eq 'GOI'}">
                                <strong>
                                    Government of India
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${postingform.radnotifyingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${notideptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${postingform.radnotifyingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${notioffice}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${postingform.authPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Details of Posting
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${postingform.radpostingauthtype eq 'GOO'}">
                                <strong>
                                    Government of Orissa
                                </strong>
                            </c:if>
                            <c:if test="${postingform.radpostingauthtype eq 'GOI'}">
                                <strong>
                                    Government of India
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-2"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${postingform.radpostingauthtype eq 'GOO'}">
                                Department: 
                                <strong>
                                    <c:out value="${posteddeptname}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <c:if test="${postingform.radpostingauthtype eq 'GOO'}">
                                Office: 
                                <strong>
                                    <c:out value="${postedoffice}"/>
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            Post: 
                            <strong>
                                <c:out value="${postingform.postedPostName}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (a) Field Office:
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${fieldoffice}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>Details of Pay</label>
                        </div>
                        <div class="col-lg-10"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (a) Scale of Pay/Pay Band
                        </div>
                        <div class="col-lg-5">
                            <strong>
                                <c:out value="${postingform.sltPayScale}"/>
                            </strong>
                        </div>
                        <div class="col-lg-5"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (b) Grade Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${postingform.txtGP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (c) Pay in Substantive Post
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${postingform.txtBasic}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (d) Special Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${postingform.txtSP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (e) Personal Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${postingform.txtPP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (f) Other Pay
                        </div>
                        <div class="col-lg-3">
                            <strong>
                                <c:out value="${postingform.txtOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-7"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            (g) Description of Other Pay
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${postingform.txtDescOP}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Date of Effect of Pay
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${postingform.txtWEFDt}"/>
                            </strong>
                        </div>
                        <div class="col-lg-2">
                            Time
                        </div>
                        <div class="col-lg-2">
                            <strong>
                                <c:out value="${postingform.sltWEFTime}"/>
                            </strong>
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Update Cadre Status(JPR)
                        </div>
                        <div class="col-lg-2">
                            <c:if test="${postingform.chkCdrSts eq 'Y'}">
                                <strong>
                                    Yes
                                </strong>
                            </c:if>
                            <c:if test="${postingform.chkCdrSts ne 'Y'}">
                                <strong>
                                    No
                                </strong>
                            </c:if>
                        </div>
                        <div class="col-lg-8"></div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            Note(if any)
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${postingform.note}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="PostingList.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>
